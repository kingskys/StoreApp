package com.kingskys.storeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.kingskys.logstore.LogStore;
import com.kingskys.logstore.LogValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class LogStoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_store);

        initView();
    }

    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);

    private void initView() {
        findViewById(R.id.btn_showCount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogCount();
            }
        });

        findViewById(R.id.btn_showAllLog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllLog();
            }
        });

        findViewById(R.id.btn_removeAllLog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllLogs();
            }
        });

        findViewById(R.id.btn_autoCreateLog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCreateLogs();
            }
        });

        findViewById(R.id.btn_showFirst).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFirst();
            }
        });

        findViewById(R.id.btn_showLast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLast();
            }
        });

        findViewById(R.id.btn_addLog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.input_addLog);
                addLog(editText.getText().toString());
            }
        });

        findViewById(R.id.btn_getLog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextStart = findViewById(R.id.input_getLog_startId);
                EditText editTextCount = findViewById(R.id.input_getLog_count);

                showLog(Integer.valueOf(editTextStart.getText().toString().trim(), 10), Integer.valueOf(editTextCount.getText().toString().trim(), 10));
            }
        });

        findViewById(R.id.btn_deleteLog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextStart = findViewById(R.id.input_deleteLog_startId);
                EditText editTextEnd = findViewById(R.id.input_deleteLog_endId);

                deleteLog(Integer.valueOf(editTextStart.getText().toString().trim(), 10), Integer.valueOf(editTextEnd.getText().toString().trim(), 10));
            }
        });
    }

    private void autoCreateLogs() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                log("开始生成日志");
                try {
                    for (int i = 0; i < 500; i++) {
                        addLog(String.format(Locale.ENGLISH, "我是log %d, hahahahhhaha %d %d\n sword %d 发 sfa~", i, i, i, i));

                        Thread.sleep(200 * Math.round(Math.random()));

                    }
                    log("生成日志完成");
                } catch (InterruptedException e) {
                    log("生成日志被打断");
                }
            }
        }).start();
    }

    private void showLogCount() {
        log("显示日志总数");
        try {
            int value = LogStore.getCount(getApplicationContext());
            log("日志总数：" + value);
        } catch (ExecutionException e) {
            log("显示日志总数出错：" + e);
        }
    }

    private void addLog(String msg) {
        log("添加日志：(" + msg + ")");
        LogStore.add(getApplicationContext(), msg);
        log("添加日志结束");
    }

    private void deleteLog(int startId, int endId) {
        log("删除日志[" + startId + "~" + endId + "]");
        LogStore.delete(getApplicationContext(), startId, endId);
        log("删除完成");
    }

    private void deleteAllLogs() {
        log("删除所有日志");
        LogStore.delete(getApplicationContext());
        log("删除所有完成");
    }

    private void showFirst() {
        log("显示第一条日志");
        try {
            LogValue value = LogStore.getFirst(getApplicationContext());
            if (value != null) {
                String dt = mFormat.format(new Date(value.getTime()));
                log(String.format(Locale.ENGLISH, "[uid:%d][%d][%s]: (%s)", value.getUid(), value.getTime(), dt, value.getMsg()));
            } else {
                log("没有日志");
            }
        } catch (ExecutionException e) {
            log("显示第一条日志时出错：" + e);
        }
    }

    private void showLast() {
        log("显示最后一条日志");
        try {
            LogValue value = LogStore.getLast(getApplicationContext());
            if (value != null) {
                String dt = mFormat.format(new Date(value.getTime()));
                log(String.format(Locale.ENGLISH, "[uid:%d][%d][%s]: (%s)", value.getUid(), value.getTime(), dt, value.getMsg()));
            } else {
                log("没有日志");
            }
        } catch (ExecutionException e) {
            log("显示最后一条日志时出错：" + e);
        }
    }

    private void showLog(int startId, int count) {
        log("开始显示从 " + startId + " 开始的 " + count + " 条日志");
        try {
            int idx = 0;
            List<LogValue> logs = LogStore.get(getApplicationContext(), startId, count);
            for (LogValue value : logs) {
                String dt = mFormat.format(new Date(value.getTime()));
                log(String.format(Locale.ENGLISH, "(%d)\t[uid:%d][%d][%s]: (%s)", idx, value.getUid(), value.getTime(), dt, value.getMsg()));
                idx++;
            }
            log("开始显示从 " + startId + " 开始的 " + count + " 条日志结束");
        } catch (ExecutionException e) {
            log("显示从 " + startId + " 开始的 " + count + " 条日志错误：" + e);
        }
    }

    private void showAllLog() {
        try {
            log("开始显示全部日志");
            int idx = 0;
            long startId = 0;
//            int errCount = 0;
            while (true) {
                List<LogValue> logs = LogStore.get(getApplicationContext(), startId, 100);
                for (LogValue value : logs) {
                    String dt = mFormat.format(new Date(value.getTime()));
                    log(String.format(Locale.ENGLISH, "(%d)\t[uid:%d][%d][%s]: (%s)", idx, value.getUid(), value.getTime(), dt, value.getMsg()));
                    idx++;
//                    if (idx + 1 != value.getUid()) {
//                        errCount++;
//                    }
                }

                if (logs.size() < 100) {
                    break;
                }

                startId = logs.get(logs.size() - 1).getUid() + 1;
            }
            log("显示全部日志结束");
//            log("错误数量：" + errCount);

        } catch (ExecutionException e) {
            log("显示全部日志的时候出错: " + e);
        }

    }

    private static void log(String msg) {
        Log.d("storeapp_", msg);
    }
}
