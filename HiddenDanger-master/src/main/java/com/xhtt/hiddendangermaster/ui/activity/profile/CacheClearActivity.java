package com.xhtt.hiddendangermaster.ui.activity.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import com.hg.hollowgoods.bean.eventbus.HGEvent;
import com.hg.hollowgoods.constant.HGCommonResource;
import com.hg.hollowgoods.constant.HGSystemConfig;
import com.hg.hollowgoods.ui.base.BaseActivity;
import com.hg.hollowgoods.ui.base.click.OnViewClickListener;
import com.hg.hollowgoods.util.FormatUtils;
import com.hg.hollowgoods.util.file.FileUtils2;
import com.hg.widget.rotatebar.RotateBar;
import com.hg.widget.rotatebar.RotateBarBasic;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.constant.EventActionCode;

import org.greenrobot.eventbus.EventBus;

/**
 * @ClassName:清理缓存界面
 * @Description:
 * @author: 马禛
 * @date: 2018年09月18日
 */
public class CacheClearActivity extends BaseActivity {

    private Button clear;
    private RotateBar rotateBar;

    private RotateBarBasic bar1;
    private RotateBarBasic bar2;
    private RotateBarBasic bar3;
    private RotateBarBasic bar4;

    @Override
    public int bindLayout() {
        return R.layout.activity_cache_clear;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, "清理缓存");

        clear = findViewById(R.id.btn_clear);
        rotateBar = findViewById(R.id.rotateBar);

        if (HGSystemConfig.IS_DEBUG_MODEL) {
            bar1 = new RotateBarBasic(0, "缓存");
            bar2 = new RotateBarBasic(0, "垃圾");
            bar3 = new RotateBarBasic(0, "Shit");
            bar4 = new RotateBarBasic(0, "Rubbish");
        } else {
            bar1 = new RotateBarBasic(0, "缓存");
            bar2 = new RotateBarBasic(0, "缓存");
            bar3 = new RotateBarBasic(0, "缓存");
            bar4 = new RotateBarBasic(0, "缓存");
        }

        rotateBar.addRatingBar(bar1);
        rotateBar.addRatingBar(bar2);
        rotateBar.addRatingBar(bar3);
        rotateBar.addRatingBar(bar4);

        initRotateBar();
    }

    @Override
    public void setListener() {

        clear.setOnClickListener(new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                FileUtils2.deleteAllFileFromPath(HGSystemConfig.APP_BASE_PATH);
                initRotateBar();
            }
        });

        rotateBar.setAnimatorListener(new RotateBar.AnimatorListener() {
            @Override
            public void onRotateStart() {

            }

            @Override
            public void onRotateEnd() {
                clear.setVisibility(View.VISIBLE);
                refreshCacheSize();
                HGEvent event = new HGEvent(EventActionCode.ClearCache);
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
        bar1.setRatingBarColor(FormatUtils.changeColorAlpha(getResources().getColor(R.color.google_red), 130));
        bar1.setRate(10);

        bar2.setRatedColor(getResources().getColor(R.color.google_yellow));
        bar2.setOutlineColor(getResources().getColor(R.color.google_yellow));
        bar2.setRatingBarColor(FormatUtils.changeColorAlpha(getResources().getColor(R.color.google_yellow), 130));
        bar2.setRate(10);

        bar3.setRatedColor(getResources().getColor(R.color.google_blue));
        bar3.setOutlineColor(getResources().getColor(R.color.google_blue));
        bar3.setRatingBarColor(FormatUtils.changeColorAlpha(getResources().getColor(R.color.google_blue), 130));
        bar3.setRate(10);

        bar4.setRatedColor(getResources().getColor(R.color.google_green));
        bar4.setOutlineColor(getResources().getColor(R.color.google_green));
        bar4.setRatingBarColor(FormatUtils.changeColorAlpha(getResources().getColor(R.color.google_green), 130));
        bar4.setRate(10);

        rotateBar.show();
    }

    private void refreshCacheSize() {
        rotateBar.setCenterText(FileUtils2.formatFileSize(FileUtils2.calcFileTotalSizeFromPath(HGSystemConfig.APP_BASE_PATH)));
    }

}
