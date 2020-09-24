package com.xhtt.hiddendangermaster.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.hg.hollowgoods.util.LogUtils;

/**
 * AndroidManifest.xml meta-data 工具类
 * <p>
 * Created by Hollow Goods on 2019-12-07.
 */
public class MetaDataUtils {

    @SuppressWarnings({"unchecked"})
    public static <T> T getMetaData(Context context, String key) {

        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return (T) appInfo.metaData.get(key);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.Log(e.getMessage());
        }

        return null;
    }

}
