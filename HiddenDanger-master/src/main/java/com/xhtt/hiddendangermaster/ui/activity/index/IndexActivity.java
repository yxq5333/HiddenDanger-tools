package com.xhtt.hiddendangermaster.ui.activity.index;

import android.os.Bundle;
import android.view.View;

import com.hg.hollowgoods.ui.base.BaseActivity;
import com.hg.hollowgoods.ui.base.click.OnViewClickListener;
import com.hg.hollowgoods.util.LogUtils;
import com.jaredrummler.android.widget.AnimatedSvgView;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.ui.activity.login.LoginActivity;

/**
 * 启动界面
 *
 * @author HG
 */

public class IndexActivity extends BaseActivity {

    private AnimatedSvgView animatedSvgView;
    private View gotoNext;

    private boolean canGotoNext = true;

    @Override
    public int bindLayout() {
        return R.layout.activity_index;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        animatedSvgView = findViewById(R.id.animatedSvgView);
        gotoNext = findViewById(R.id.gotoNext);

        baseUI.setCommonTitleViewVisibility(false);
        baseUI.hideActionBar();
        baseUI.setTranslucentActionBar(true);
    }

    @Override
    public void setListener() {

        baseUI.setOnPermissionsListener((isAgreeAll, requestCode, permissions, isAgree) -> {
            if (isAgreeAll) {
                gotoNext();
            }
        });

        gotoNext.setOnClickListener(new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                gotoNext();
            }
        });

        long start = System.currentTimeMillis();
        animatedSvgView.setOnStateChangeListener(state -> {

            switch (state) {
                case AnimatedSvgView.STATE_NOT_STARTED:
                    // 动画重新开始或者未开始
                    LogUtils.Log("动画重新开始或者未开始");
                    break;
                case AnimatedSvgView.STATE_TRACE_STARTED:
                    // 描边开始
                    LogUtils.Log("描边开始");
                    break;
                case AnimatedSvgView.STATE_FILL_STARTED:
                    // 填充开始
                    LogUtils.Log("填充开始");
                    break;
                case AnimatedSvgView.STATE_FINISHED:
                    // 动画结束
                    long end = System.currentTimeMillis();
                    LogUtils.Log("动画时长", (end - start) / 1000);
                    gotoNext();
                    break;
            }
        });

        animatedSvgView.start();
    }

    private void gotoNext() {
        if (canGotoNext) {
            canGotoNext = false;

            if (baseUI.requestIOPermission()) {
                baseUI.startMyActivity(LoginActivity.class);
                finish();
            } else {
                canGotoNext = true;
            }
        }
    }

}
