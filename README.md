### SimpleLocation

怎样使用?

工程的Gradle引入方式：

    repositories {
            google()
            jcenter()
            mavenCentral()
        }

    allprojects {
        repositories {
            google()
            jcenter()
            maven { url 'https://jitpack.io' }
            mavenCentral()
        }
    }

  dependencies {
		implementation 'com.github.gyadministrator:LocalLocation:1.1'
	}

本篇主要介绍谷歌自带的LocationManager 获取手机定位的方法，以及通过谷歌服务Geocoder 来进行反地理编码。接口api都比较简单，细节可以查看代码。

其实LocationManager 的api使用起来很简单，难点在于不同android机型，不同系统可能存在着很多未知的坑。最大的坑就是有的手机系统底层的定位服务是直接连接的google服务器，手机厂商没有将定位的服务器重定向为国内的定位服务商，致使我们通过LocationManager根本拿不到定位信息。针对这种情况，我在项目里，提供了基站定位的方式来获取当前的经纬度信息。通过mmc、mnc、lac、cid来查询基站信息的服务器，使用的是cellocation.com api免费接口，但是由于近期19/09/05无法进行访问，所以我又提供了调用openCellid 服务器的api接口来进行基站地位。

由于Geocoder 进行反地理编码需要一个未包含在android核心框架之中的后端服务，如果平台中没有这个后端服务，Geocoder查询方法将返回空列表。不幸的是，国内很多手机厂商都没有内置这种服务（例如百度、高德服务），所以Geocoder也就不能使用了。针对这种情况，我在demo里除了提供了Geocoder 之外，又额外添加了高德反地理编码api，腾讯反地理编码api，百度反地理编码api，白名单方式和sn签名校验方式都有。大家只需要传入申请的key即可。大家也可以将项目的依赖直接打包成jar包使用。

这里贴下使用获取经纬度方法的代码片段：

```java
        GeocodingManager.GeoOption option = new GeocodingManager.GeoOption()
                .setGeoType(Constant.LM_API) // 使用openCellid服务器的基站地位
                .setOption(new GoogleGeocoding.SiLoOption()
                        .setGpsFirst(true));// locationManager定位方式时，gps优先
        siLoManager = new GeocodingManager(this, option);
        siLoManager.start(new IGeocoding.ISiLoResponseListener() {
            @Override
            public void onSuccess(Latlng latlng) {
                Log.e(LOG_TAG,"siLoManager onSuccess:" + latlng);
                tvSimpleLo.setText("latlng:" + latlng.getLatitude()
                        + "\n,long:" + latlng.getLongitude()
                        + "\n,provider:" + latlng.getProvider());
                reGeManager.reGeToAddress(latlng);
            }

            @Override
            public void onFail(String msg) {
                Log.e(LOG_TAG,"error:" + msg);
                tvSimpleAd.setText("error:" + msg);
            }
        });
```

贴一下使用反地理编码的代码片段：

```java
        ReverseGeocodingManager.ReGeOption reGeOption = new ReverseGeocodingManager.ReGeOption()
                .setReGeType(Constant.BAIDU_API)// 百度api返地理编码
                .setSn(true)// sn 签名校验方式
                .setIslog(true);// 打印log
        reGeManager = new ReverseGeocodingManager(this, reGeOption);
        reGeManager.addReGeListener(new IReGe.IReGeListener() {
            @Override
            public void onSuccess(int state, Latlng latlng) {
                Log.e(LOG_TAG,"reGeManager onSuccess:" + latlng);
            }

            @Override
            public void onFail(int errorCode, String error) {
                Log.e(LOG_TAG,"error:" + error);
            }
        });
```

activity中使用
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



