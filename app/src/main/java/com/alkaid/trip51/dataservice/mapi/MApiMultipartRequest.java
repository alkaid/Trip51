package com.alkaid.trip51.dataservice.mapi;

import com.alkaid.base.common.LogUtil;
import com.alkaid.trip51.model.response.ResponseData;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
//OST / HTTP/1.1
//        Host: localhost:8000
//        User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:29.0) Gecko/20100101 Firefox/29.0
//        Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
//Accept-Language: en-US,en;q=0.5
//Accept-Encoding: gzip, deflate
//Cookie: __atuvc=34%7C7; permanent=0; _gitlab_session=226ad8a0be43681acf38c2fab9497240; __profilin=p%3Dt; request_method=GET
//Connection: keep-alive
//Content-Type: multipart/form-data; boundary=---------------------------9051914041544843365972754266
//Content-Length: 554
//
//-----------------------------9051914041544843365972754266
//Content-Disposition: form-data; name="text"
//
//text default
//-----------------------------9051914041544843365972754266
//Content-Disposition: form-data; name="file1"; filename="a.txt"
//Content-Type: text/plain
//
//Content of a.txt.
//
//-----------------------------9051914041544843365972754266
//Content-Disposition: form-data; name="file2"; filename="a.html"
//Content-Type: text/html
//
//<!DOCTYPE html><title>Content of a.html.</title>
//
//-----------------------------9051914041544843365972754266--
//        Content-Disposition: form-data; name="datafile1"; filename="r.gif"
//        Content-Type: image/gif
//
//        GIF87a.............,...........D..;
//        -----------------------------287032381131322
//        Content-Disposition: form-data; name="datafile2"; filename="g.gif"
//        Content-Type: image/gif
//
//        GIF87a.............,...........D..;
/**
 * Created by df on 2015/11/24.<br/>
 * 用于multipart/form-data; 上传二进制文件等
 */
public class MApiMultipartRequest<T extends ResponseData> extends MApiRequest<T> {
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;
//    private byte[] multipartBody;
    private ByteArrayOutputStream bos = new ByteArrayOutputStream();
    private DataOutputStream dos = new DataOutputStream(bos);

    public MApiMultipartRequest(CacheType cacheType,boolean shouldRefreshCache,Class<T> responseClss,String url, Map<String,String> beSignForm, Map<String,String> unBeSignform,Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(cacheType,shouldRefreshCache,responseClss,url,beSignForm,unBeSignform,listener,errorListener);
    }

//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//        Map<String,String> params = new HashMap<String, String>();
//
//        params.put("Content-Type", "multipart/form-data; boundary=" + BOUNDARY + "; charset=utf-8");
//        return params;
//    }

    @Override
    public String getBodyContentType() {
        return mimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        Map<String,String> params=getParams();
        for(String key:params.keySet()){
            buildPart(key,params.get(key));
        }
        //这里要注意添加最后一条form-data的boundary
        try {
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
        } catch (IOException e) {
            LogUtil.e(e);
        }
        byte[] multipartBody = bos.toByteArray();
        LogUtil.v(new String(multipartBody));
        return multipartBody;
    }

    private void buildPart(String name,String value)  {
        try {
            buildPart(name,value.getBytes("utf-8"),null);
        } catch (UnsupportedEncodingException e) {
            LogUtil.e(e);
        }
    }

    public void buildPart(String name, byte[] data, String fileName)  {
        String contentType="application/octet-stream";
        try {
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\""+name+"\""
                    + (null==fileName?"" : ("; filename=\"" + fileName + "\""))
                    + lineEnd
                    + (null==fileName?"":("Content-Type:"+contentType+";charset=utf-8"+lineEnd))
                    );
            dos.writeBytes(lineEnd);

            ByteArrayInputStream fileInputStream = new ByteArrayInputStream(data);
            int bytesAvailable = fileInputStream.available();

            int maxBufferSize = 1024 * 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];

            // read file and write it into form...
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
        } catch (IOException e) {
            LogUtil.e(e);
        }
    }

//    public byte[] getFileDataFromDrawable(Context context, int id) {
//        Drawable drawable = ContextCompat.getDrawable(context, id);
//        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
//        return byteArrayOutputStream.toByteArray();
//    }

}
