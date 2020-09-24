package com.xhtt.hiddendangermaster.bean.knowledgebase;

import com.hg.widget.tablayout.listener.CustomTabEntity;
import com.xhtt.hiddendangermaster.R;

public class TabEntity implements CustomTabEntity {
    public String title;
    public int selectedIcon;
    public int unSelectedIcon;

    public TabEntity(String title) {
        this.title = title;
        this.selectedIcon = R.drawable.ic_android_green_24dp;
        this.unSelectedIcon = R.drawable.ic_android_green_24dp;
    }

    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
