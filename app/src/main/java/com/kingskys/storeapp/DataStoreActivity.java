package com.kingskys.storeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.kingskys.datastore.DataStore;
import com.kingskys.datastore.DataValue;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class DataStoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_store);

        initView();
    }

    private void initView() {
        findViewById(R.id.btn_showCount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCount();
            }
        });

        findViewById(R.id.btn_show_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAll();
            }
        });

        findViewById(R.id.btn_addvalue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextKey = findViewById(R.id.input_addvalue_key);
                String key = editTextKey.getText().toString();
                EditText editTextValue = findViewById(R.id.input_addvalue_value);
                String value = editTextValue.getText().toString();

                setValue(key, value);
            }
        });

        findViewById(R.id.btn_getvalue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.input_getvalue_key);
                String key = editText.getText().toString();
                getValue(key);
            }
        });

        findViewById(R.id.btn_delete_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll();
            }
        });

        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.input_deletevalue_key);
                String key = editText.getText().toString();
                delete(key);
            }
        });
    }

    private void setValue(String key, String value) {
        log("设置 {key:" + key + ", value: " + value + "}");
        DataStore.set(getApplicationContext(), key, value);
        log("设置 {key:" + key + ", value: " + value + "} 结束");
    }

    private void getValue(String key) {
        log("获取值 {key:" + key + "}");
        try {
            String v = DataStore.get(getApplicationContext(), key, "");
            log("值为: {" + v + "}");
        } catch (ExecutionException e) {
            log("获取值时，异常：" + e);
        }
    }

    private void showAll() {
        try {
            log("开始显示全部");
            int idx = 0;
            List<DataValue> datas = DataStore.get(getApplicationContext());
            for (DataValue value : datas) {
                log(String.format(Locale.ENGLISH, "(%d) {key: %s, value: %s}", idx, value.getKey(), value.getValue()));
                idx++;
            }
            log("显示全部结束");

        } catch (ExecutionException e) {
            log("显示全部日志的时候出错: " + e);
        }

    }

    private void deleteAll() {
        log("开始删除全部");
        DataStore.delete(getApplicationContext());
        log("删除全部结束");
    }

    private void delete(String key) {
        log("开始删除: {" + key + "}");
        DataStore.delete(getApplicationContext(), key);
        log("开始删除: {" + key + "}结束");
    }

    private void showCount() {
        log("显示总数");
        try {
            int value = DataStore.getCount(getApplicationContext());
            log("总数：" + value);
        } catch (ExecutionException e) {
            log("显示总数出错：" + e);
        }
    }

    private static void log(String msg) {
        Log.d("storeapp_", msg);
    }
}
