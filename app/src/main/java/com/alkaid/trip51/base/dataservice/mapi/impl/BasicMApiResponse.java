package com.alkaid.trip51.base.dataservice.mapi.impl;

import android.util.SparseArray;

import com.alkaid.trip51.base.dataservice.http.impl.BasicHttpResponse;
import com.alkaid.trip51.base.dataservice.mapi.MApiResponse;
import com.alkaid.trip51.model.SimpleMsg;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;

import java.net.UnknownHostException;
import java.util.List;

public class BasicMApiResponse<E, R> extends BasicHttpResponse<E, R>
        implements MApiResponse<E, R> {

    public static final String ERROR_MALFORMED = "malformed content";
    public static final String ERROR_STATUS = "server status error";
    static SparseArray statusCodeArray;
    private byte rawData[];

    public BasicMApiResponse(int statusCode, byte[] rawData, R result, List<NameValuePair> headers, E error) {
        super(statusCode, result, headers, error);
        this.rawData = rawData;
    }

    private String getErrorMessage(int i) {
        return (String) statusCodeArray.get(i, "无小忧去吃满汉全席了");
    }

    public SimpleMsg message() {
        Object obj = error();
        SimpleMsg simplemsg;
        if (obj instanceof SimpleMsg)
            simplemsg = (SimpleMsg) obj;
        else if (ERROR_MALFORMED.equals(obj))
            simplemsg = new SimpleMsg("无小忧醉了", getErrorMessage(statusCode()), 0, 0);
        else if (ERROR_STATUS.equals(obj))
            simplemsg = new SimpleMsg("出错了", getErrorMessage(statusCode()), 0, 0);
        else if (obj instanceof Exception) {
            if ((obj instanceof UnknownHostException) || (obj instanceof HttpHostConnectException))
                simplemsg = new SimpleMsg("错误", "网络不给力哦", 0, 0);
            else
                simplemsg = new SimpleMsg("无小忧晕了", getErrorMessage(statusCode()), 0, 0);
        } else {
            simplemsg = new SimpleMsg("错误", getErrorMessage(statusCode()), 0, 0);
        }
        return simplemsg;
    }

    public byte[] rawData() {
        return rawData;
    }

    static {
        statusCodeArray = new SparseArray();
        statusCodeArray.put(-103, "无小忧去吃糖醋排骨了");
        statusCodeArray.put(-100, "无小忧去吃香辣五花肉了");
        statusCodeArray.put(-104, "无小忧去吃烧子鹅了");
        statusCodeArray.put(-102, "无小忧去吃烧花鸭了");
        statusCodeArray.put(-105, "无小忧去吃松花小肚了");
        statusCodeArray.put(-106, "无小忧去吃烩鸭条了");
        statusCodeArray.put(502, "无小忧去吃香酥鸡了");
        statusCodeArray.put(-109, "无小忧去吃熘蟹肉了");
        statusCodeArray.put(450, "无小忧去吃炒腰花儿了");
        statusCodeArray.put(-108, "无小忧去吃锅烧白菜了");
        statusCodeArray.put(403, "无小忧去吃水晶肘子了");
        statusCodeArray.put(-107, "无小忧去吃焖黄鳝了");
        statusCodeArray.put(504, "无小忧去吃什锦豆腐了");
        statusCodeArray.put(500, "无小忧去吃清蒸鸡了");
        statusCodeArray.put(404, "无小忧去吃小肚儿了");
        statusCodeArray.put(-111, "无小忧去吃烩蟹肉了");
        statusCodeArray.put(408, "无小忧去吃蜜蜡肘子了");
        statusCodeArray.put(401, "无小忧去吃炸子蟹了");
        statusCodeArray.put(503, "无小忧去吃软炸里脊了");
    }
}
