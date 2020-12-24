package com.android.custom.locationlib.helper;

import android.content.Context;

import com.android.custom.locationlib.constant.Constant;
import com.android.custom.locationlib.location.BsGeocoding;
import com.android.custom.locationlib.location.GoogleGeocoding;
import com.android.custom.locationlib.location.IGeocoding;
import com.android.custom.locationlib.location.OpenCellidGeocoding;
import com.android.custom.locationlib.regelocation.BaiduGeRe;
import com.android.custom.locationlib.regelocation.GaodeGeRe;
import com.android.custom.locationlib.regelocation.GoogleRege;
import com.android.custom.locationlib.regelocation.IReGe;
import com.android.custom.locationlib.regelocation.TencentGeRe;

public class GeReFactory {

    public static IReGe getReGeByType(int reGeType){
        IReGe iReGe;
        switch (reGeType) {
            case Constant.GOOGLE_API:
                iReGe = new GoogleRege();
                break;
            case Constant.BAIDU_API:
                iReGe = new BaiduGeRe();
                break;
            case Constant.TENCENT_API:
                iReGe = new TencentGeRe();
                break;
            case Constant.GAODE_API:
                iReGe = new GaodeGeRe();
                break;
            default:
                iReGe = new GoogleRege();
        }
        return iReGe;
    }

    public static IGeocoding getGeocodingType(Context context, int geocodingType){
        IGeocoding iGeocoding;
        switch (geocodingType) {
            case Constant.LM_API:
                iGeocoding = new GoogleGeocoding(context);
                break;
            case Constant.BS_UNWIRED_API:
                iGeocoding = new BsGeocoding(context);
                break;
            case Constant.BS_OPENCELLID_API:
                iGeocoding = new OpenCellidGeocoding(context);
                break;
            default:
                iGeocoding =  new GoogleGeocoding(context);
        }
        return iGeocoding;
    }
}
