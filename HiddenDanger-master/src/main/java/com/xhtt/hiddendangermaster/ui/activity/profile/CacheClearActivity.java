package com.xhtt.hiddendangermaster.ui.activity.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.config.ZCommonResource;
import com.hg.zero.config.ZSystemConfig;
import com.hg.zero.file.ZFileUtils2;
import com.hg.zero.listener.ZOnViewClickListener;
import com.hg.zero.util.ZFormatUtils;
import com.hg.zero.widget.rotatebar.ZRotateBar;
import com.hg.zero.widget.rotatebar.ZRotateBarBasic;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.ui.base.HDBaseActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * @ClassName:清理缓存界面
 * @Description:
 * @author: 马禛
 * @date: 2018年09月18日
 */
public class CacheClearActivity extends HDBaseActivity {

    private Button clear;
    private ZRotateBar rotateBar;

    private ZRotateBarBasic bar1;
    private ZRotateBarBasic bar2;
    private ZRotateBarBasic bar3;
    private ZRotateBarBasic bar4;

    @Override
    public int bindLayout() {
        return R.layout.activity_cache_clear;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(ZCommonResource.getBackIcon(), "清理缓存");

        clear = findViewById(R.id.btn_clear);
        rotateBar = findViewById(R.id.rotateBar);

        if (ZSystemConfig.isDebugMode()) {
            bar1 = new ZRotateBarBasic(0, "缓存");
            bar2 = new ZRotateBarBasic(0, "垃圾");
            bar3 = new ZRotateBarBasic(0, "Shit");
            bar4 = new ZRotateBarBasic(0, "Rubbish");
        } else {
            bar1 = new ZRotateBarBasic(0, "缓存");
            bar2 = new ZRotateBarBasic(0, "缓存");
            bar3 = new ZRotateBarBasic(0, "缓存");
            bar4 = new ZRotateBarBasic(0, "缓存");
        }

        rotateBar.addRatingBar(bar1);
        rotateBar.addRatingBar(bar2);
        rotateBar.addRatingBar(bar3);
        rotateBar.addRatingBar(bar4);

        initRotateBar();
    }

    @Override
    public void setListener() {

        clear.setOnClickListener(new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                ZFileUtils2.deleteAllFileFromPath(ZSystemConfig.appBaseDir());
                initRotateBar();
            }
        });

        rotateBar.setAnimatorListener(new ZRotateBar.AnimatorListener() {
            @Override
            public void onRotateStart() {

            }

            @Override
            public void onRotateEnd() {
                clear.setVisibility(View.VISIBLE);
                refreshCacheSize();
                ZEvent event = new ZEvent(EventActionCode.ClearCache);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onRatingStart() {

            }

            @Override
            public void onRatingEnd() {

            }
        });
    }

    public void initRotateBar() {

        clear.setVisibility(View.INVISIBLE);

        rotateBar.setCenterTextColor(ContextCompat.getColor(this, R.color.txt_color_dark));
        rotateBar.clear();

        bar1.setRatedColor(getResources().getColor(R.color.google_red));
        bar1.setOutlineColor(getResources().getColor(R.color.google_red));
        bar1.setRatingBarColor(ZFormatUtils.changeColorAlpha(getResources().getColor(R.color.google_red), 130));
        bar1.setRate(10);

        bar2.setRatedColor(getResources().getColor(R.color.google_yellow));
        bar2.setOutlineColor(getResources().getColor(R.color.google_yellow));
        bar2.setRatingBarColor(ZFormatUtils.changeColorAlpha(getResources().getColor(R.color.google_yellow), 130));
        bar2.setRate(10);

        bar3.setRatedColor(getResources().getColor(R.color.google_blue));
        bar3.setOutlineColor(getResources().getColor(R.color.google_blue));
        bar3.setRatingBarColor(ZFormatUtils.changeColorAlpha(getResources().getColor(R.color.google_blue), 130));
        bar3.setRate(10);

        bar4.setRatedColor(getResources().getColor(R.color.google_green));
        bar4.setOutlineColor(getResources().getColor(R.color.google_green));
        bar4.setRatingBarColor(ZFormatUtils.changeColorAlpha(getResources().getColor(R.color.google_green), 130));
        bar4.setRate(10);

        rotateBar.show();
    }

    private void refreshCacheSize() {
        rotateBar.setCenterText(ZFileUtils2.formatFileSize(ZFileUtils2.calcFileTotalSizeFromPath(ZSystemConfig.appBaseDir())));
    }

}
