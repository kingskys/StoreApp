package com.kingskys.logstore;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

class LogStoreImp {
    static final Uri mUri = Uri.parse("content://" + Const.authorities + "/" + Const.logTableName);

    static void insert(Context context, long time, String value) {
        ContentValues values = new ContentValues();
        values.put("time", time);
        values.put("txt", value);
        ContentResolver resolver = context.getContentResolver();
        resolver.insert(mUri, values);
    }

    /**
     * 获取第一条
     */
    static LogValue query(Context context) throws ExecutionException {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(mUri, new String[]{"uid", "time", "txt"}, null, null, null);
        if (cursor == null) {
            throw new ExecutionException(new Throwable("query cursor is null"));
        }
        if (cursor.moveToFirst()) {
            LogValue value = new LogValue();
            value.uid = cursor.getLong(0);
            value.time = cursor.getLong(1);
            value.msg = cursor.getString(2);
            cursor.close();
            return value;
        }
        cursor.close();
        return null;
    }

    /**
     * 获取最后第一条
     */
    static LogValue queryLast(Context context) throws ExecutionException {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(mUri, new String[]{"uid", "time", "txt"}, null, null, null);
        if (cursor == null) {
            throw new ExecutionException(new Throwable("query cursor is null"));
        }
        if (cursor.moveToLast()) {
            LogValue value = new LogValue();
            value.uid = cursor.getLong(0);
            value.time = cursor.getLong(1);
            value.msg = cursor.getString(2);
            cursor.close();
            return value;
        }
        cursor.close();
        return null;
    }

    /**
     *
     * @param limitCount 限制获取的条数
     */
    static List<LogValue> query(Context context, long startId, int limitCount) throws ExecutionException {
        ContentResolver resolver = context.getContentResolver();
        String where = null;
        if (startId > 0) {
            where = "uid >= " + startId;
        }
        Cursor cursor = resolver.query(mUri, new String[]{"uid", "time", "txt"}, where, null, null);
        if (cursor == null) {
            throw new ExecutionException(new Throwable("query cursor is null"));
        }

        List<LogValue> list = new ArrayList<>();
        if (!cursor.moveToFirst()) {
            cursor.close();
            return list;
        }

        int count = 0;
        while (count < limitCount) {
            LogValue value = new LogValue();
            value.uid = cursor.getLong(0);
            value.time = cursor.getLong(1);
            value.msg = cursor.getString(2);
            list.add(value);
            count++;

            if (!cursor.moveToNext()) {
                break;
            }
        }
        cursor.close();
        return list;
    }

    /**
     * 获取当前共有多少条日志
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
     * 删除指定uid区域的内容
     * @param startId 开始uid
     * @param endId 结束uid （包含）
     */
    static void delete(Context context, long startId, long endId) {
        ContentResolver resolver = context.getContentResolver();
        String where = "uid >= ? and uid <= ?";
        resolver.delete(mUri, where, new String[]{String.valueOf(startId), String.valueOf(endId)});
    }

    static void deleteByTime(Context context, long startTime, long endTime) {
        ContentResolver resolver = context.getContentResolver();
        String where = "time >= ? and time <= ?";
        String[] args = new String[]{String.valueOf(startTime), String.valueOf(endTime)};
        resolver.delete(mUri, where, args);
    }
}
