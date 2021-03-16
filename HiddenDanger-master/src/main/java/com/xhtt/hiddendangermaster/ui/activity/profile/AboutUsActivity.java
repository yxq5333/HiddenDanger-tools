package com.xhtt.hiddendangermaster.ui.activity.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hg.zero.config.ZCommonResource;
import com.hg.zero.config.ZSystemConfig;
import com.hg.zero.listener.ZOnViewClickListener;
import com.hg.zero.util.ZAppUtils;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.ui.base.HDBaseActivity;
import com.xhtt.hiddendangermaster.util.UpdateAPPUtils;

/**
 * 关于我们界面
 * <p>
 * Created by Hollow Goods on 2019-05-22
 */
public class AboutUsActivity extends HDBaseActivity {

    private Button update;

    private UpdateAPPUtils updateAPPUtils;

    @Override
    public int bindLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(ZCommonResource.getBackIcon(), R.string.title_activity_about_us);

        update = findViewById(R.id.btn_update);
        TextView version = findViewById(R.id.tv_version);
        version.setText("V");
        version.append(ZAppUtils.getAppVersionName(baseUI.getBaseContext()));
        version.append("-");
        version.append(ZSystemConfig.isDebugMode() ? "debug" : "release");
        version.append(" ");
        version.append("2019");

        updateAPPUtils = new UpdateAPPUtils(baseUI.getBaseContext());
    }

    @Override
    public void setListener() {
        update.setOnClickListener(new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                updateAPPUtils.checkUpdate(true);
            }
        });
    }

}
