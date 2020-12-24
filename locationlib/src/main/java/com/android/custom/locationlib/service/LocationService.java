package com.android.custom.locationlib.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.custom.locationlib.GeocodingManager;
import com.android.custom.locationlib.ReverseGeocodingManager;
import com.android.custom.locationlib.bean.Latlng;
import com.android.custom.locationlib.constant.Constant;
import com.android.custom.locationlib.location.GoogleGeocoding;
import com.android.custom.locationlib.location.IGeocoding;
import com.android.custom.locationlib.regelocation.IReGe;
import com.google.gson.Gson;

/**
 * @ProjectName: SimpleLocation-master
 * @Package: com.mjzuo.location.service
 * @ClassName: LocationService
 * @Author: 1984629668@qq.com
 * @CreateDate: 2020/12/23 14:22
 */
public class LocationService extends Service {
    /**
     * 定位获取经纬度，包括LocationManager、基站地位
     */
    GeocodingManager siLoManager;
    /**
     * 反地理编码的manager，包括google反地理、高德反地理、百度反地理、腾讯反地理
     */
    ReverseGeocodingManager reGeManager;

    private static final String TAG = "LocationService";

    private double lat;
    private double lng;

    public static final String LOCATION_TAG = "location_tag";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: 开始定位...");
        init();
    }

    private void init() {
        ReverseGeocodingManager.ReGeOption reGeOption = new ReverseGeocodingManager.ReGeOption()
                .setReGeType(Constant.TENCENT_API)// 腾讯api返地理编码
                .setSn(true)// sn 签名校验方式
                .setIslog(true);// 打印log
        reGeManager = new ReverseGeocodingManager(this, reGeOption);
        reGeManager.addReGeListener(new IReGe.IReGeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(int state, Latlng latlng) {
                latlng.setLatitude(lat);
                latlng.setLongitude(lng);
                SharedPreferences sharedPreferences = getSharedPreferences(LOCATION_TAG, MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                String s = new Gson().toJson(latlng);
                edit.putString(LOCATION_TAG, s);
                edit.apply();
                Log.e(TAG, "reGeManager onSuccess:" + latlng.toString());
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFail(int errorCode, String error) {
                Log.e(TAG, "onFail: " + "errorCode:" + errorCode + ", error:" + error);
                init();
            }
        });


        GeocodingManager.GeoOption option = new GeocodingManager.GeoOption()
                .setGeoType(Constant.BS_OPENCELLID_API) // 使用openCellid服务器的基站地位
                .setOption(new GoogleGeocoding.SiLoOption()
                        .setGpsFirst(false));// locationManager定位方式时，gps优先
        siLoManager = new GeocodingManager(this, option);
        siLoManager.start(new IGeocoding.ISiLoResponseListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(Latlng latlng) {
                Log.e(TAG, "siLoManager onSuccess:" + latlng.toString());
                lat = latlng.getLatitude();
                lng = latlng.getLongitude();
                reGeManager.reGeToAddress(latlng);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFail(String msg) {
                Log.e(TAG, "error:" + msg);
                init();
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (siLoManager != null)
            siLoManager.stop();
        if (reGeManager != null) {
            reGeManager.stop();
        }
    }
}
