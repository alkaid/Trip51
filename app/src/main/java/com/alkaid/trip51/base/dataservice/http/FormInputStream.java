package com.alkaid.trip51.base.dataservice.http;


import com.alkaid.trip51.util.WrapInputStream;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FormInputStream extends WrapInputStream {

    public static final String ISO_8859_1 = "ISO-8859-1";
    public static final String DEFAULT_CHARSET = ISO_8859_1;
    private String charsetName = DEFAULT_CHARSET;
    private List<BasicNameValuePair> form;

    public FormInputStream(List<BasicNameValuePair> list) {
        this(list, DEFAULT_CHARSET);
    }

    public FormInputStream(List<BasicNameValuePair> form, String charsetName) {
        this.form = form;
        this.charsetName = charsetName;
    }

    public FormInputStream(String as[], String charsetName) {
        int i = as.length / 2;
        ArrayList<BasicNameValuePair> arraylist = new ArrayList<BasicNameValuePair>(i);
        for (int j = 0; j < i; j++)
            arraylist.add(new BasicNameValuePair(as[j * 2], as[1 + j * 2]));
        form = arraylist;
        this.charsetName=charsetName;
    }

    private String encode()
            throws UnsupportedEncodingException {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator iterator = form.iterator();
        do {
            if (!iterator.hasNext())
                break;
            NameValuePair namevaluepair = (NameValuePair) iterator.next();
            if (stringbuilder.length() > 0)
                stringbuilder.append('&');
            stringbuilder.append(namevaluepair.getName());
            stringbuilder.append('=');
            if (namevaluepair.getValue() != null)
                stringbuilder.append(URLEncoder.encode(namevaluepair.getValue(), charsetName));
        } while (true);
        return stringbuilder.toString();
    }

    public String charsetName() {
        return charsetName;
    }

    public List<BasicNameValuePair> form() {
        return form;
    }

    public String toString() {
        String s = "";
        try {
            s = encode();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    protected InputStream wrappedInputStream()
            throws IOException {
        ByteArrayInputStream bytearrayinputstream;
        try {
            bytearrayinputstream = new ByteArrayInputStream(encode().getBytes(charsetName));
        } catch (UnsupportedCharsetException unsupportedcharsetexception) {
            throw new IOException(unsupportedcharsetexception.getMessage());
        }
        return bytearrayinputstream;
    }
}
