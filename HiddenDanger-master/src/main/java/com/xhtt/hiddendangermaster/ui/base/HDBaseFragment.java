package com.xhtt.hiddendangermaster.ui.base;

import androidx.annotation.CallSuper;

import com.hg.zero.ui.base.ZBaseFragment;

import org.xutils.x;

/**
 * 隐患排查基础Fragment
 * <p>
 * Created by Hollow Goods on 2020-12-17.
 */
public abstract class HDBaseFragment extends ZBaseFragment {

    @CallSuper
    @Override
    public void initParamData() {
        super.initParamData();
        x.view().inject(baseUI.getInitUI(), baseUI.rootView);
    }
}
