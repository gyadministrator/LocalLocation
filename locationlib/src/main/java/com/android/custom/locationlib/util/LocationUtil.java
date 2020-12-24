package com.android.custom.locationlib.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.custom.locationlib.service.LocationService;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * @ProjectName: SimpleLocation-master
 * @Package: com.mjzuo.location.util
 * @ClassName: LocationUtil
 * @Author: 1984629668@qq.com
 * @CreateDate: 2020/12/23 14:32
 */
public class LocationUtil implements EasyPermissions.PermissionCallbacks {
    @SuppressLint("StaticFieldLeak")
    private static Activity mActivity;
    private static Intent service;
    /**
     * 所要申请的权限
     */
    private static String[] perms = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE
    };

    public static void startLocation(Activity activity) {
        mActivity = activity;
        if (!EasyPermissions.hasPermissions(activity, perms)) {
            EasyPermissions.requestPermissions(activity
                    , "必要的权限"
                    , 0
                    , perms);
        }
        startService();
    }

    private static void startService() {
        if (mActivity == null) return;
        service = new Intent(mActivity, LocationService.class);
        mActivity.startService(service);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        startService();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(mActivity, "定位相关权限拒绝,后续将无法再定位", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public static void onDestroy() {
        if (mActivity != null && service != null) {
            mActivity.stopService(service);
        }
        mActivity = null;
    }

}
