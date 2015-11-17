package com.alkaid.trip51.base.dataservice.mapi.impl;

import android.util.SparseArray;

import com.alkaid.trip51.base.dataservice.http.impl.BasicHttpResponse;
import com.alkaid.trip51.base.dataservice.mapi.MApiResponse;
import com.alkaid.trip51.model.SimpleMsg;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;

import java.net.UnknownHostException;
import java.util.List;

public class BasicMApiResponse<E,R> extends BasicHttpResponse<E,R>
    implements MApiResponse<E,R>
{

    public static final Object ERROR_MALFORMED = "malformed content";
    public static final Object ERROR_STATUS = "server status error";
    static SparseArray statusCodeArray;
    private byte rawData[];

    public BasicMApiResponse(int statusCode, byte[] rawData, R result, List<NameValuePair> headers, E error)
    {
        super(statusCode, result, headers, error);
        this.rawData = rawData;
    }

    private String getErrorMessage(int i)
    {
        return (String)statusCodeArray.get(i, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u6EE1\u6C49\u5168\u5E2D\u4E86");
    }

    public SimpleMsg message()
    {
        Object obj = error();
        SimpleMsg simplemsg;
        if(obj instanceof SimpleMsg)
            simplemsg = (SimpleMsg)obj;
        else
        if(obj == ERROR_MALFORMED)
            simplemsg = new SimpleMsg("\u70B9\u5C0F\u8BC4\u9189\u4E86", getErrorMessage(statusCode()), 0, 0);
        else
        if(obj == ERROR_STATUS)
            simplemsg = new SimpleMsg("\u51FA\u9519\u4E86", getErrorMessage(statusCode()), 0, 0);
        else
        if(obj instanceof Exception)
        {
            if((obj instanceof UnknownHostException) || (obj instanceof HttpHostConnectException))
                simplemsg = new SimpleMsg("\u9519\u8BEF", "\u7F51\u7EDC\u4E0D\u7ED9\u529B\u54E6", 0, 0);
            else
                simplemsg = new SimpleMsg("\u70B9\u5C0F\u8BC4\u6655\u4E86", getErrorMessage(statusCode()), 0, 0);
        } else
        {
            simplemsg = new SimpleMsg("\u9519\u8BEF", getErrorMessage(statusCode()), 0, 0);
        }
        return simplemsg;
    }

    public byte[] rawData()
    {
        return rawData;
    }

    static 
    {
        statusCodeArray = new SparseArray();
        statusCodeArray.put(-103, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u7CD6\u918B\u6392\u9AA8\u4E86");
        statusCodeArray.put(-100, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u9999\u8FA3\u4E94\u82B1\u8089\u4E86");
        statusCodeArray.put(-104, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u70E7\u5B50\u9E45\u4E86");
        statusCodeArray.put(-102, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u70E7\u82B1\u9E2D\u4E86");
        statusCodeArray.put(-105, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u677E\u82B1\u5C0F\u809A\u4E86");
        statusCodeArray.put(-106, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u70E9\u9E2D\u6761\u4E86");
        statusCodeArray.put(502, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u9999\u9165\u9E21\u4E86");
        statusCodeArray.put(-109, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u7198\u87F9\u8089\u4E86");
        statusCodeArray.put(450, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u7092\u8170\u82B1\u513F\u4E86");
        statusCodeArray.put(-108, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u9505\u70E7\u767D\u83DC\u4E86");
        statusCodeArray.put(403, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u6C34\u6676\u8098\u5B50\u4E86");
        statusCodeArray.put(-107, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u7116\u9EC4\u9CDD\u4E86");
        statusCodeArray.put(504, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u4EC0\u9526\u8C46\u8150\u4E86");
        statusCodeArray.put(500, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u6E05\u84B8\u9E21\u4E86");
        statusCodeArray.put(404, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u5C0F\u809A\u513F\u4E86");
        statusCodeArray.put(-111, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u70E9\u87F9\u8089\u4E86");
        statusCodeArray.put(408, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u871C\u8721\u8098\u5B50\u4E86");
        statusCodeArray.put(401, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u70B8\u5B50\u87F9\u4E86");
        statusCodeArray.put(503, "\u70B9\u5C0F\u8BC4\u53BB\u5403\u8F6F\u70B8\u91CC\u810A\u4E86");
    }
}
