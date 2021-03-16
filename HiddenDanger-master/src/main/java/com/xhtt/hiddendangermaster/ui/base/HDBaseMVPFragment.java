package com.xhtt.hiddendangermaster.ui.base;

import androidx.annotation.CallSuper;

import com.hg.zero.ui.base.mvp.ZBaseMVPFragment;
import com.hg.zero.ui.base.mvp.ZIBasePresenter;

import org.xutils.x;

/**
 * 隐患排查MVP基础Fragment
 * <p>
 * Created by Hollow Goods on 2020-12-17.
 */
public abstract class HDBaseMVPFragment<P extends ZIBasePresenter> extends ZBaseMVPFragment<P> {

    @CallSuper
    @Override
    public void initParamData() {
        super.initParamData();
        x.view().inject(baseUI.getInitUI(), baseUI.rootView);
    }
}
