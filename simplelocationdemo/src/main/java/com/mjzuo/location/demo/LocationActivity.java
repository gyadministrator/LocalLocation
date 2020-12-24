package com.mjzuo.location.demo;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mjzuo.location.bean.Latlng;
import com.mjzuo.location.service.LocationService;
import com.mjzuo.location.util.LocationUtil;

import java.util.Timer;
import java.util.TimerTask;

public class LocationActivity extends AppCompatActivity {
    TextView tvSimpleLo;
    TextView tvSimpleAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_location_activity);

        tvSimpleLo = findViewById(R.id.tv_simple_location_txt);
        tvSimpleAd = findViewById(R.id.tv_simple_address_txt);

        LocationUtil.startLocation(this);

        //启动定时器
        timer.schedule(task, 0, 1000);
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(LocationService.LOCATION_TAG, MODE_PRIVATE);
                String s = sharedPreferences.getString(LocationService.LOCATION_TAG, "");
                Log.e("result", "run: " + s);
                if (!TextUtils.isEmpty(s)) {
                    final Latlng latlng = new Gson().fromJson(s, Latlng.class);
                    if (latlng != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvSimpleAd.setText(latlng.toString());
                                timer.cancel();
                            }
                        });
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationUtil.onDestroy();
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                initData();
            }
        }
    };


    private Timer timer = new Timer(true);

    //任务
    private TimerTask task = new TimerTask() {
        public void run() {
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
        }
    };
}
