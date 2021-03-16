package com.xhtt.hiddendangermaster.ui.base;

import androidx.annotation.CallSuper;

import com.hg.zero.ui.base.ZBaseActivity;

import org.xutils.x;

/**
 * 隐患排查基础Activity
 * <p>
 * Created by Hollow Goods on 2020-12-17.
 */
public abstract class HDBaseActivity extends ZBaseActivity {

    @CallSuper
    @Override
    public void initParamData() {
		super.initParamData();
        x.view().inject(this);
    }

}
