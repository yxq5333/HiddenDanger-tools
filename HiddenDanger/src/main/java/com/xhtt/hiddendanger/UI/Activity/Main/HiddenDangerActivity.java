package com.xhtt.hiddendanger.UI.Activity.Main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.UI.Base.BaseMVPActivity;
import com.hg.hollowgoods.Util.LogUtils;
import com.xhtt.hiddendanger.R;

/**
 * 主界面
 *
 * @author HG
 */

public class HiddenDangerActivity extends BaseMVPActivity<HiddenDangerPresenter> implements HiddenDangerContract.View {

    @Override
    public Activity addToExitGroup() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_hidden_danger;
    }

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, "隐患排查");

        return null;
    }

    @Override
    public void setListener() {

    }

    @Override
    public HiddenDangerPresenter createPresenter() {
        return new HiddenDangerPresenter();
    }

    @Override
    public void onCenterTitleClick(View view) {
        LogUtils.Log(IPConfigHelper.create().getNowIPConfig().getRequestUrl());
    }
}
