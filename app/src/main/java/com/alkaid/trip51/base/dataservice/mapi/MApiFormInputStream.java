package com.alkaid.trip51.base.dataservice.mapi;

import com.alkaid.trip51.base.dataservice.http.FormInputStream;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class MApiFormInputStream extends FormInputStream
{

    public static final String UTF_8 = "UTF-8";
    public static final String DEFAULT_CHARSET = UTF_8;

    public MApiFormInputStream(List<BasicNameValuePair> list)
    {
        super(list, DEFAULT_CHARSET);
    }

    public MApiFormInputStream(String as[])
    {
        super(form(as), DEFAULT_CHARSET);
    }

    private static List<BasicNameValuePair> form(String as[])
    {
        int i = as.length / 2;
        ArrayList<BasicNameValuePair> arraylist = new ArrayList<BasicNameValuePair>(i);
        for(int j = 0; j < i; j++)
            arraylist.add(new BasicNameValuePair(as[j * 2], as[1 + j * 2]));

        return arraylist;
    }

//    public static final String UTF_8 = "UTF-8";
//    public static final String DEFAULT_CHARSET = UTF_8;
//    private String charsetName = DEFAULT_CHARSET;
//    private List<BasicNameValuePair> form;
//    private List<BasicNameValuePair> formForSign;
//
//    public MApiFormInputStream(List<BasicNameValuePair> list) {
//        this(list, DEFAULT_CHARSET);
//    }
//
//    public MApiFormInputStream(List<BasicNameValuePair>,List<BasicNameValuePair> form, String charsetName) {
//        this.form = form;
//        this.charsetName = charsetName;
//        URLEncodedUtils.
//    }
//
//    public MApiFormInputStream(String as[], String charsetName) {
//        int i = as.length / 2;
//        ArrayList<BasicNameValuePair> arraylist = new ArrayList<BasicNameValuePair>(i);
//        for (int j = 0; j < i; j++)
//            arraylist.add(new BasicNameValuePair(as[j * 2], as[1 + j * 2]));
//        form = arraylist;
//        this.charsetName=charsetName;
//    }
//
//    private String encode()
//            throws UnsupportedEncodingException {
//        StringBuilder stringbuilder = new StringBuilder();
//        Iterator iterator = form.iterator();
//        do {
//            if (!iterator.hasNext())
//                break;
//            NameValuePair namevaluepair = (NameValuePair) iterator.next();
//            if (stringbuilder.length() > 0)
//                stringbuilder.append('&');
//            stringbuilder.append(namevaluepair.getName());
//            stringbuilder.append('=');
//            if (namevaluepair.getValue() != null)
//                stringbuilder.append(URLEncoder.encode(namevaluepair.getValue(), charsetName));
//        } while (true);
//        return stringbuilder.toString();
//    }
//
//    public String charsetName() {
//        return charsetName;
//    }
//
//    public List<BasicNameValuePair> form() {
//        return form;
//    }
//
//    public String toString() {
//        String s = "";
//        try {
//            s = encode();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return s;
//    }
//
//    protected InputStream wrappedInputStream()
//            throws IOException {
//        ByteArrayInputStream bytearrayinputstream;
//        try {
//            bytearrayinputstream = new ByteArrayInputStream(encode().getBytes(charsetName));
//        } catch (UnsupportedCharsetException unsupportedcharsetexception) {
//            throw new IOException(unsupportedcharsetexception.getMessage());
//        }
//        return bytearrayinputstream;
//    }

}
