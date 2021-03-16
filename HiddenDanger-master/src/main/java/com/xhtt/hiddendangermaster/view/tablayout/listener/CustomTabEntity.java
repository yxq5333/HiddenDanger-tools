package com.xhtt.hiddendangermaster.view.tablayout.listener;

import androidx.annotation.DrawableRes;

/**
 * Created by Hollow Goods on unknown.
 */
public interface CustomTabEntity {
    String getTabTitle();

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();
}