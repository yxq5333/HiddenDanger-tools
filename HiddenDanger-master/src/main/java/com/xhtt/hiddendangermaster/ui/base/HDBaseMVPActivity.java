package com.xhtt.hiddendangermaster.ui.base;

import androidx.annotation.CallSuper;

import com.hg.zero.ui.base.mvp.ZBaseMVPActivity;
import com.hg.zero.ui.base.mvp.ZIBasePresenter;

import org.xutils.x;

/**
 * 隐患排查MVP基础Activity
 * <p>
 * Created by Hollow Goods on 2020-12-17.
 */
public abstract class HDBaseMVPActivity<P extends ZIBasePresenter> extends ZBaseMVPActivity<P> {

    @CallSuper
    @Override
    public void initParamData() {
		super.initParamData();
        x.view().inject(this);
    }
}
