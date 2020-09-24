package com.xhtt.hiddendangermaster.ui.activity.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hg.hollowgoods.constant.HGCommonResource;
import com.hg.hollowgoods.constant.HGSystemConfig;
import com.hg.hollowgoods.ui.base.BaseActivity;
import com.hg.hollowgoods.ui.base.click.OnViewClickListener;
import com.hg.hollowgoods.util.APPUtils;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.util.UpdateAPPUtils;

/**
 * 关于我们界面
 * <p>
 * Created by Hollow Goods on 2019-05-22
 */
public class AboutUsActivity extends BaseActivity {

    private Button update;

    @Override
    public int bindLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, R.string.title_activity_about_us);

        update = findViewById(R.id.btn_update);
        TextView version = findViewById(R.id.tv_version);
        version.setText("V");
        version.append(APPUtils.getAppVersionName(baseUI.getBaseContext()));
        version.append("-");
        version.append(HGSystemConfig.IS_DEBUG_MODEL ? "debug" : "release");
        version.append(" ");
        version.append("2019");
    }

    @Override
    public void setListener() {
        update.setOnClickListener(new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                new UpdateAPPUtils(baseUI.getBaseContext()).checkUpdate(true);
            }
        });
    }

}
