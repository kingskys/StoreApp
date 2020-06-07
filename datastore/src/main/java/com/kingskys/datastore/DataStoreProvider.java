package com.kingskys.datastore;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;

public class DataStoreProvider extends ContentProvider {
    public DataStoreProvider() {
    }

    private static final UriMatcher mMatcher;

    static {
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mMatcher.addURI(Const.authorities, Const.tableName, Const.tableCode);
    }

    private String getTableName(Uri uri) {
        if (mMatcher.match(uri) == Const.tableCode) {
            return Const.tableName;
        }
        return null;
    }

    private SQLiteDatabase mDB = null;
    private Context mContext = null;

    @Override
    public boolean onCreate() {
        mContext = getContext();

        DBHelper helper = new DBHelper(mContext);
        mDB = helper.getWritableDatabase();

        return true;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        String table = getTableName(uri);
        int ret = mDB.delete(table, selection, selectionArgs);
        mContext.getContentResolver().notifyChange(uri, null);
        return ret;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // 在update做添加操作，不用这个
//        String table = getTableName(uri);
//        mDB.insert(table, null, values);
//        mContext.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String table = getTableName(uri);
        return mDB.query(table, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String table = getTableName(uri);
        int ret = mDB.update(table, values, selection, selectionArgs);
        if (ret == 0) {
            mDB.insert(table, null, values);
        }
        mContext.getContentResolver().notifyChange(uri, null);
        return 1;
    }

}
