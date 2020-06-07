package com.kingskys.datastore;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

class DataStoreImp {
    private static final Uri mUri = Uri.parse("content://" + Const.authorities + "/" + Const.tableName);

    static void update(Context context, String uid, String value) {
        String where = "uid = ?";
        String[] args = new String[]{uid};
        ContentValues values = new ContentValues();
        values.put("uid", uid);
        values.put("value", value);
        ContentResolver resolver = context.getContentResolver();
        resolver.update(mUri, values, where, args);
    }

    /**
     * 获取一条数据
     * @param uid 数据键
     */
    static String query(Context context, String uid) throws ExecutionException {
        ContentResolver resolver = context.getContentResolver();
        String where = "uid = ?";
        String[] args = new String[]{uid};
        Cursor cursor = resolver.query(mUri, new String[]{"uid", "value"}, where, args, null);
        if (cursor == null) {
            throw new ExecutionException(new Throwable("query cursor is null"));
        }
        if (cursor.moveToFirst()) {
            String value = cursor.getString(1);
            cursor.close();
            return value;
        }
        cursor.close();
        return null;
    }

    /**
     * 获取所有数据
     */
    static List<DataValue> query(Context context) throws ExecutionException {
        ContentResolver resolver = context.getContentResolver();
        String where = null;
        String[] args = null;
        Cursor cursor = resolver.query(mUri, new String[]{"uid", "value"}, where, args, null);
        if (cursor == null) {
            throw new ExecutionException(new Throwable("query cursor is null"));
        }

        List<DataValue> list = new ArrayList<>();
        if (!cursor.moveToFirst()) {
            cursor.close();
            return list;
        }

        do {
            DataValue value = new DataValue();
            value.key = cursor.getString(0);
            value.value = cursor.getString(1);
            list.add(value);
        } while (cursor.moveToNext());
        cursor.close();
        return list;
    }

    /**
     * 获取当前共有多少组数据
     */
    static int getCount(Context context) throws ExecutionException {
        ContentResolver resolver = context.getContentResolver();
        String where = null;
        Cursor cursor = resolver.query(mUri, new String[]{"uid"}, where, null, null);
        if (cursor == null) {
            throw new ExecutionException(new Throwable("query cursor is null"));
        }
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /**
     * 删除所有
     */
    static void delete(Context context) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(mUri, null, null);
    }

    /**
     * 删除指定数据
     * @param uid 数据键
     */
    static void delete(Context context, String uid) {
        ContentResolver resolver = context.getContentResolver();
        String where = "uid = ?";
        String[] args = new String[]{uid};
        resolver.delete(mUri, where, args);
    }
}
