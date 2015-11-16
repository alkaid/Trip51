// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 

package com.alkaid.trip51.base.dataservice.http;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.alkaid.base.common.LogUtil;

import org.apache.http.HttpHost;

public class NetworkInfoHelper {

    public static final int NETWORK_TYPE_2G = 2;
    public static final int NETWORK_TYPE_3G = 3;
    public static final int NETWORK_TYPE_4G = 4;
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    public static final int NETWORK_TYPE_WIFI = 1;
    private ConnectivityManager connectivityManager;
    private Context context;
    private TelephonyManager teleManager;

    public NetworkInfoHelper(Context context1) {
        context = context1;
    }

    public static boolean isNetworkConnected(Context context1) {
        if (context1 == null)
            return false;
        NetworkInfo networkinfo = ((ConnectivityManager) context1.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkinfo == null)
            return false;
        return networkinfo.isAvailable();
    }

    protected ConnectivityManager connectivityManager() {
        if (connectivityManager == null)
            try {
                connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            } catch (Exception exception) {
                Log.w("network", "cannot get connectivity manager, maybe the permission is missing in AndroidManifest.xml?", exception);
            }
        return connectivityManager;
    }

    public String getDetailNetworkType() {
        String s;
        if (getNetworkType() == NETWORK_TYPE_UNKNOWN)
            s = "unknown";
        else if (getNetworkType() == NETWORK_TYPE_WIFI)
            s = "wifi";
        else
            s = String.valueOf(telephonyManager().getNetworkType());
        return s;
    }

    public String getNetworkInfo() {
        String s = "unknown";
        ConnectivityManager connectivitymanager = connectivityManager();
        if (null == connectivitymanager)
            return s;
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        if (networkinfo == null)
            return s;
        switch (networkinfo.getType()) {
            default:
                s = networkinfo.getTypeName();
                break;
            case ConnectivityManager.TYPE_WIFI: // '\001'
                s = "wifi";
                break;
            case ConnectivityManager.TYPE_MOBILE: // '\0'
                s = (new StringBuilder()).append("mobile(").append(networkinfo.getSubtypeName()).append(",").append(networkinfo.getExtraInfo()).append(")").toString();
                break;
        }
        return s;
    }

    public int getNetworkType() {
        int i = NETWORK_TYPE_UNKNOWN;
        ConnectivityManager connectivitymanager = connectivityManager();
        if (connectivitymanager == null) {
            return i;
        }
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        if (networkinfo == null)
            return i;
        switch (networkinfo.getType()) {
            case ConnectivityManager.TYPE_MOBILE: // '\0'
                switch (telephonyManager().getNetworkType()) {
                    case TelephonyManager.NETWORK_TYPE_GPRS: // '\001'
                    case TelephonyManager.NETWORK_TYPE_EDGE: // '\002'
                    case TelephonyManager.NETWORK_TYPE_CDMA: // '\004'
                    case TelephonyManager.NETWORK_TYPE_1xRTT: // '\007'
                    case TelephonyManager.NETWORK_TYPE_IDEN: // '\013'
                        i = NETWORK_TYPE_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS: // '\003'
                    case TelephonyManager.NETWORK_TYPE_EVDO_0: // '\005'
                    case TelephonyManager.NETWORK_TYPE_EVDO_A: // '\006'
                    case TelephonyManager.NETWORK_TYPE_HSDPA: // '\b'
                    case TelephonyManager.NETWORK_TYPE_HSUPA: // '\t'
                    case TelephonyManager.NETWORK_TYPE_HSPA: // '\n'
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: // '\f'
                    case TelephonyManager.NETWORK_TYPE_EHRPD: // '\016'
                    case TelephonyManager.NETWORK_TYPE_HSPAP: // '\017'
                        i = NETWORK_TYPE_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE: // '\r'
                        i = NETWORK_TYPE_4G;
                        break;
                }
                break;

            case ConnectivityManager.TYPE_WIFI:// '\001'
                i = NETWORK_TYPE_WIFI;
                break;
        }
        return i;
    }

    public HttpHost getProxy() {
        HttpHost httphost = null;
        ConnectivityManager connectivitymanager = connectivityManager();
        if (connectivitymanager == null)
            return httphost;
        do {
            try {
                NetworkInfo networkInfo = connectivitymanager.getActiveNetworkInfo();
                if (networkInfo == null) {
                    httphost = null;
                    continue;
                } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    httphost = null;
                    continue;
                } else if (networkInfo.getType() == 0) {
                    String info = networkInfo.getExtraInfo();
                    if (info == null) {
                        httphost = null;
                        continue;
                    }
                    String infoLower = info.toLowerCase();
                    if (infoLower.contains("cmnet")) {
                        httphost = null;
                        continue;
                    }
                    if (infoLower.contains("cmwap")) {
                        httphost = new HttpHost("10.0.0.172");
                        continue;
                    }
                    if (infoLower.contains("3gnet")) {
                        httphost = null;
                        continue;
                    }
                    if (infoLower.contains("3gwap")) {
                        httphost = new HttpHost("10.0.0.172");
                        continue;
                    }
                    if (infoLower.contains("uninet")) {
                        httphost = null;
                        continue;
                    }
                    if (infoLower.contains("uniwap")) {
                        httphost = new HttpHost("10.0.0.172");
                        continue;
                    }
                    if (infoLower.contains("ctnet")) {
                        httphost = null;
                        continue;
                    }
                    if (infoLower.contains("ctwap")) {
                        httphost = new HttpHost("10.0.0.200");
                        continue;
                    }
                    if (infoLower.contains("#777")) {
                        ContentResolver contentResolver = this.context.getContentResolver();
                        Uri uri = Uri.parse("content://telephony/carriers/preferapn");
                        String[] qs = new String[2];
                        qs[0] = "proxy";
                        qs[1] = "port";
                        Cursor cursor = contentResolver.query(uri, qs, null, null, null);
                        String hostname;
                        int j;
                        if (cursor.moveToFirst()) {
                            hostname = cursor.getString(0);
                            int i = hostname.length();
                            if (i > 3) {
                                j = 0;
                            }
                            try {
                                int k = Integer.parseInt(cursor.getString(1));
                                j = k;
                            } catch (NumberFormatException localNumberFormatException) {
                                break;
                            }
                            if (j <= 0) {
                                j = 80;
                            }
                            httphost = new HttpHost(hostname, j);
                        }
                    }
                }
            } catch (Exception e) {
                LogUtil.w(e);
            }
        } while (false);
        return httphost;
    }

    protected TelephonyManager telephonyManager() {
        if (teleManager == null)
            try {
                teleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            } catch (Exception exception) {
                Log.w("network", "cannot get telephony manager, maybe the permission is missing in AndroidManifest.xml?", exception);
            }
        return teleManager;
    }
}
