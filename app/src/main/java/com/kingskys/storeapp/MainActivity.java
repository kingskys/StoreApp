package com.kingskys.storeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        findViewById(R.id.btn_enter_logstore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterLogStoreScene();
            }
        });

        findViewById(R.id.btn_enter_datastore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterDataStoreScene();
            }
        });
    }

    private void enterLogStoreScene() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), LogStoreActivity.class);
        startActivity(intent);
    }

    private void enterDataStoreScene() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), DataStoreActivity.class);
        startActivity(intent);
    }


    private static void log(String msg) {
        Log.d("storeapp_", msg);
    }
}
