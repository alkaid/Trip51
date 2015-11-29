package com.alkaid.trip51.base.dataservice.mapi.impl;

import com.alkaid.trip51.base.dataservice.http.impl.BasicHttpRequest;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.base.dataservice.mapi.MApiFormInputStream;
import com.alkaid.trip51.base.dataservice.mapi.MApiRequest;

import org.apache.http.NameValuePair;

import java.io.InputStream;
import java.util.List;



public class BasicMApiRequest extends BasicHttpRequest
    implements MApiRequest
{

    private CacheType defaultCacheType;
    private boolean disableStatistics;

    public BasicMApiRequest(String s, String s1, InputStream inputstream, CacheType cachetype, boolean flag, List<NameValuePair> list)
    {
        this(s, s1, inputstream, cachetype, flag, list, 0L);
    }

    public BasicMApiRequest(String s, String s1, InputStream inputstream, CacheType cachetype, boolean flag, List<NameValuePair> list, long l)
    {
        super(s, s1, inputstream, list, l);
        defaultCacheType = cachetype;
        disableStatistics = flag;
    }

    /*public static InputStream compress(String s)
        throws IOException
    {
        byte abyte0[] = s.getBytes("UTF-8");
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        GZIPOutputStream gzipoutputstream = new GZIPOutputStream(bytearrayoutputstream);
        gzipoutputstream.write(abyte0);
        gzipoutputstream.finish();
        gzipoutputstream.close();
        byte abyte1[] = bytearrayoutputstream.toByteArray();
        bytearrayoutputstream.close();
        return new MApiFileInputStream(new ByteArrayInputStream(abyte1));
    }*/

    public static MApiRequest mapiGet(String s, CacheType cachetype)
    {
        return new BasicMApiRequest(s, "GET", null, cachetype, false, null);
    }

    public static MApiRequest mapiPost(String s, String as[])
    {
        return new BasicMApiRequest(s, "POST", new MApiFormInputStream(as), CacheType.DISABLED, false, null);
    }

//    public static MApiRequest mapiPostGzipString(String s, String s1, boolean flag, List<NameValuePair> list)
//    {
//        InputStream inputstream = null;
//        try
//        {
//            inputstream = compress(s1);
//            if(list == null)
//                list = new ArrayList<NameValuePair>(1);
//            list.add(new BasicNameValuePair("Content-Encoding", "gzip"));
//        }
//        catch(IOException ioexception)
//        {
//            Log.w("BasicMApiRequest compress failed");
//        }
//        return new BasicMApiRequest(s, "POST", inputstream, CacheType.DISABLED, flag, list);
//    }

    @Override
    public CacheType defaultCacheType()
    {
        return defaultCacheType;
    }

//    public boolean disableStatistics()
//    {
//        return disableStatistics;
//    }
}
