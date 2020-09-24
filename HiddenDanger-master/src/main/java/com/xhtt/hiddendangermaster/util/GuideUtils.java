package com.xhtt.hiddendangermaster.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * 导航工具类——打开第三方APP
 * <p>
 * Created by Hollow Goods on 2020-03-11.
 */
public class GuideUtils {

    /**
     * 高德地图路径规划
     */
    public static boolean routeByAMap(Context context, String pointName, double lat, double lng) {

        Intent intent = new Intent("android.intent.action.VIEW",
                Uri.parse("amapuri://route/plan/"
                        + "?sourceApplication=" + context.getPackageName()
                        + "&route=" + lat
                        + "&dlat=" + lat
                        + "&dlon=" + lng
                        + "&dname=" + pointName
                        + "&t=" + 0
                        + "&dev=0"
                ));
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setPackage("com.autonavi.minimap");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (isInstallAPP("com.autonavi.minimap")) {
            context.startActivity(intent);
            return true;
        }

        return false;
    }

    /**
     * 高德地图导航
     */
    public static boolean guideByAMap(Context context, double lat, double lng) {

        Intent intent = new Intent("android.intent.action.VIEW",
                Uri.parse("androidamap://navi"
                        + "?sourceApplication=" + context.getPackageName()
                        + "&lat=" + lat
                        + "&lon=" + lng
                        + "&dev=0&style=2"));
        intent.setPackage("com.autonavi.minimap");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (isInstallAPP("com.autonavi.minimap")) {
            context.startActivity(intent);
            return true;
        }

        return false;
    }

    /**
     * 百度地图路径规划
     */
    public static boolean routeByBaiDuMap(Context context, String pointName, double lat, double lng) {

        Intent intent = new Intent();
        intent.setData(Uri.parse("baidumap://map/direction"
                + "?src=" + context.getPackageName()
                + "&destination=" + "name:" + pointName + "|latlng:" + lat + "," + lng
                + "&coord_type=bd09ll"
                + "&mode=driving"
        ));

        if (isInstallAPP("com.baidu.BaiduMap")) {
            // 启动调用
            context.startActivity(intent);
            return true;
        }

        return false;
    }

    /**
     * 百度地图导航
     */
    public static boolean guideByBaiDuMap(Context context, double lat, double lng) {

        Intent intent = new Intent();
        intent.setData(Uri.parse("baidumap://map/navi"
                + "?location=" + lat + "," + lng
                + "&src=" + context.getPackageName()
        ));

        if (isInstallAPP("com.baidu.BaiduMap")) {
            // 启动调用
            context.startActivity(intent);
            return true;
        }

        return false;
    }

    private static boolean isInstallAPP(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

}
