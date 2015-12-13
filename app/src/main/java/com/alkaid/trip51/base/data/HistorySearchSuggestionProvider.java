package com.alkaid.trip51.base.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class HistorySearchSuggestionProvider extends ContentProvider {
    private static class DatabaseHelper extends SQLiteOpenHelper {

        public void onCreate(SQLiteDatabase sqlitedatabase) {
            sqlitedatabase.execSQL("CREATE TABLE historys (_ID INTEGER PRIMARY KEY autoincrement, keyword TEXT, channel TEXT, date Long);");
        }

        public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j) {
            sqlitedatabase.execSQL("DROP TABLE IF EXISTS historys");
            onCreate(sqlitedatabase);
        }

        public DatabaseHelper(Context context, int version) {
            super(context, "searchhistory.db", null, version);
        }
    }


    public static final String AUTHORITY = "com.alkaid.trip51.HistorySearchSuggestionProvider";
    public static final String CHANNEL_COLUMN = "channel";
    private static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.searchhistory.historys";
    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/historys");
    private static final String DATABASE_NAME = "searchhistory.db";
    public static final String DATE_COLUMN = "date";
    public static final String ID_COLUMN = "_ID";
    public static final String KEYWORD_COLUMN = "keyword";
    private static final String ORDER_BY = "date DESC";
    private static final String TABLE_NAME = "historys";
    private static final int URI_MATCH_SUGGEST = 1;
    private static final int VERSION = 1;
    private static final UriMatcher mUriMatcher;
    private SQLiteOpenHelper mDBHelper;

    static {
        mUriMatcher = new UriMatcher(-1);
        mUriMatcher.addURI(AUTHORITY, TABLE_NAME, URI_MATCH_SUGGEST);
    }

    public HistorySearchSuggestionProvider() {
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new DatabaseHelper(getContext(), VERSION);
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (mUriMatcher.match(uri) != URI_MATCH_SUGGEST) {
            throw new IllegalArgumentException((new StringBuilder()).append("Unknow URI ").append(uri).toString());
        } else {
            int i = mDBHelper.getWritableDatabase().delete(TABLE_NAME, selection, selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
            return i;
        }
    }

    @Override
    public String getType(Uri uri) {
        if (mUriMatcher.match(uri) == URI_MATCH_SUGGEST)
            return CONTENT_TYPE;
        else
            throw new IllegalArgumentException((new StringBuilder()).append("Unknow URI ").append(uri).toString());
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentvalues) {
        if (mUriMatcher.match(uri) != URI_MATCH_SUGGEST)
            throw new IllegalArgumentException((new StringBuilder()).append("Unknow URI ").append(uri).toString());
        if (!contentvalues.containsKey("date"))
            contentvalues.put("date", Long.valueOf(System.currentTimeMillis()));
        long l = mDBHelper.getWritableDatabase().insert(TABLE_NAME, null, contentvalues);
        Uri uri1 = null;
        if (l > 0L)
            uri1 = Uri.withAppendedPath(CONTENT_URI, String.valueOf(l));
        getContext().getContentResolver().notifyChange(uri, null);
        return uri1;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (mUriMatcher.match(uri) != URI_MATCH_SUGGEST) {
            throw new IllegalArgumentException((new StringBuilder()).append("Unknow URI ").append(uri).toString());
        } else {
            SQLiteQueryBuilder sqlitequerybuilder = new SQLiteQueryBuilder();
            sqlitequerybuilder.setTables(TABLE_NAME);
            return sqlitequerybuilder.query(mDBHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, ORDER_BY);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (mUriMatcher.match(uri) != URI_MATCH_SUGGEST) {
            throw new IllegalArgumentException((new StringBuilder()).append("Unknow URI ").append(uri).toString());
        } else {
            SQLiteDatabase sqlitedatabase = mDBHelper.getWritableDatabase();
            values.put("date", Long.valueOf(System.currentTimeMillis()));
            int i = sqlitedatabase.update(TABLE_NAME, values, selection, selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
            return i;
        }
    }


}
