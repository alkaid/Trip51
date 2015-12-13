package com.alkaid.trip51.base.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.alkaid.base.common.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HistorySearchSuggestionHelper {

    public static final int MAX_LIMIT = 10;

    public HistorySearchSuggestionHelper() {
    }

    private static String createJsonString(String keyword, String value) {
        JSONObject jsonobject = new JSONObject();
        try {
            jsonobject.put("keyword", keyword);
            jsonobject.put("value", value);
        } catch (JSONException jsonexception) {
        }
        return jsonobject.toString();
    }

    public static void deleteChannel(ContentResolver contentresolver, String channels) {
        String as[] = channels.split(",");
        StringBuilder stringbuilder = new StringBuilder();
        for (int i = 0; i < as.length; i++) {
            if (i > 0)
                stringbuilder.append(" or ");
            stringbuilder.append("channel=?");
        }

        contentresolver.delete(HistorySearchSuggestionProvider.CONTENT_URI, stringbuilder.toString(), as);
    }

    public static boolean exists(ContentResolver contentresolver, String channel, String keyword) {
        android.net.Uri uri = HistorySearchSuggestionProvider.CONTENT_URI;
        String projection[] = new String[1];
        projection[0] = "keyword";
        String selectionArgs[] = new String[2];
        selectionArgs[0] = channel;
        selectionArgs[1] = createJsonString(keyword,null);
        Cursor cursor = contentresolver.query(uri, projection, "channel=? and keyword=?", selectionArgs, null);
        if(null==cursor) {
            return false;
        }
        boolean flag = cursor.moveToFirst();
        if(null!=cursor)
            cursor.close();
        return flag;
    }

    public static void insert(ContentResolver contentresolver, String keyword, String value, String channel) {
        if (exists(contentresolver, channel, keyword)) {
            update(contentresolver, channel, keyword, value);
        } else {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put("keyword", createJsonString(keyword, value));
            contentvalues.put("channel", channel);
            contentresolver.insert(HistorySearchSuggestionProvider.CONTENT_URI, contentvalues);
            truncate(contentresolver, channel);
        }
    }

    public static List<String> queryForChannel(ContentResolver contentresolver, String channels) {
        List<String> results;
        HashSet hashset;
        Cursor cursor;
        results = new ArrayList();
        hashset = new HashSet();
        String selectionArgs[] = channels.split(",");
        StringBuilder stringbuilder = new StringBuilder();
        for (int i = 0; i < selectionArgs.length; i++) {
            if (i > 0)
                stringbuilder.append(" or ");
            stringbuilder.append("channel=?");
        }

        android.net.Uri uri = HistorySearchSuggestionProvider.CONTENT_URI;
        String projection[] = new String[1];
        projection[0] = "keyword";
        cursor = contentresolver.query(uri, projection, stringbuilder.toString(), selectionArgs, null);
        if(null!=cursor) {
            try {
                while (cursor.moveToNext() && results.size() < MAX_LIMIT) {
                    String keywordJson = cursor.getString(cursor.getColumnIndex("keyword"));
                    JSONObject jsonobject = new JSONObject(keywordJson);
                    String keyword = jsonobject.getString("keyword");
                    String value = jsonobject.optString("value");
                    results.add(keyword);
                }
            } catch (JSONException e) {
                LogUtil.w(e);
            } finally {
                if (null != cursor)
                    cursor.close();
            }
        }
        return results;
    }

    public static void truncate(ContentResolver contentresolver, String channel) {
        android.net.Uri uri = HistorySearchSuggestionProvider.CONTENT_URI;
        String as[] = new String[1];
        as[0] = "_ID";
        String as1[] = new String[1];
        as1[0] = channel;
        Cursor cursor = contentresolver.query(uri, as, "channel = ?", as1, null);
        if (cursor != null && cursor.getCount() > MAX_LIMIT) {
            cursor.moveToPosition(9);
            android.net.Uri uri1;
            String as2[];
            for (; cursor.moveToNext(); contentresolver.delete(uri1, "_ID=?", as2)) {
                uri1 = HistorySearchSuggestionProvider.CONTENT_URI;
                as2 = new String[1];
                as2[0] = (new StringBuilder()).append(cursor.getInt(cursor.getColumnIndex("_ID"))).append("").toString();
            }

        }
        if (cursor != null)
            cursor.close();
    }

    public static void update(ContentResolver contentresolver, String channel, String keyword, String value) {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("keyword", createJsonString(keyword, value));
        contentvalues.put("channel", channel);
        android.net.Uri uri = HistorySearchSuggestionProvider.CONTENT_URI;
        String selectionArgs[] = new String[2];
        selectionArgs[0] = channel;
        selectionArgs[1] = keyword;
        contentresolver.update(uri, contentvalues, "channel=? and keyword=?", selectionArgs);
    }
}
