package com.kingskys.logstore;

import android.content.Context;
import android.net.Uri;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class LogStore {

    public static Uri getUri() {
        return LogStoreImp.mUri;
    }

    /**
     * 增加一条内容
     * @param value 内容
     */
    public static void add(Context context, String value) {
        LogStoreImp.insert(context, System.currentTimeMillis(), value);
    }

    /**
     * 获取第一条内容
     * @throws ExecutionException 获取 cursor 为 null
     */
    public static LogValue getFirst(Context context) throws ExecutionException {
        return LogStoreImp.query(context);
    }

    /**
     * 获取最后一条内容
     * @throws ExecutionException 获取 cursor 为 null
     */
    public static LogValue getLast(Context context) throws ExecutionException {
        return LogStoreImp.queryLast(context);
    }

    /**
     * 获取指定字符长度的内容
     * @param startId 开始uid 最小值为1，可以设置成0
     * @param limitCount 限制获取的条数
     * @throws ExecutionException 获取 cursor 为 null
     */
    public static List<LogValue> get(Context context, long startId, int limitCount) throws ExecutionException {
        return LogStoreImp.query(context, startId, limitCount);
    }

    /**
     * 删除所有内容
     */
    public static void delete(Context context) {
        LogStoreImp.delete(context);
    }

    /**
     * 删除指定uid区域内容
     * @param startId 开始uid
     * @param endId 结束uid （包含）
     */
    public static void delete(Context context, long startId, long endId) {
        if (startId > endId) {
            return;
        }
        LogStoreImp.delete(context, startId, endId);
    }

    public static void deleteByTime(Context context, long startTime, long endTime) {
        if (startTime > endTime) {
            return;
        }
        LogStoreImp.deleteByTime(context, startTime, endTime);
    }

    /**
     * 获取当前共有多少条日志
     */
    public static int getCount(Context context) throws ExecutionException {
        return LogStoreImp.getCount(context);
    }
}
