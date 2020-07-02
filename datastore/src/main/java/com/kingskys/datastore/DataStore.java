package com.kingskys.datastore;

import android.content.Context;
import android.net.Uri;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DataStore {

    public static Uri getUri() {
        return DataStoreImp.mUri;
    }

    /**
     * 设置数据
     * @param value 内容
     */
    public static void set(@NonNull Context context, @Nullable String key, @Nullable String value) {
        if (key == null) {
            return;
        }
        key = key.trim();
        if (key.isEmpty()) {
            return;
        }
        DataStoreImp.update(context, key, value);
    }

    /**
     * 获取数据
     * @throws ExecutionException 获取 cursor 为 null
     */
    public static String get(@NonNull Context context, String key, @Nullable String defaultValue) throws ExecutionException {
        if (key == null) {
            return defaultValue;
        }
        key = key.trim();
        if (key.isEmpty()) {
            return defaultValue;
        }

        String value = DataStoreImp.query(context, key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 获取所有数据
     * @throws ExecutionException 获取 cursor 为 null
     */
    public static List<DataValue> get(@NonNull Context context) throws ExecutionException {
        return DataStoreImp.query(context);
    }

    /**
     * 删除所有数据
     */
    public static void delete(@NonNull Context context) {
        DataStoreImp.delete(context);
    }

    /**
     * 删除指定数据
     */
    public static void delete(@NonNull Context context, String key) {
        if (key == null) {
            return;
        }
        key = key.trim();
        if (key.isEmpty()) {
            return;
        }

        DataStoreImp.delete(context, key);
    }

    /**
     * 获取当前共有多少组数据
     */
    public static int getCount(@NonNull Context context) throws ExecutionException {
        return DataStoreImp.getCount(context);
    }
}
