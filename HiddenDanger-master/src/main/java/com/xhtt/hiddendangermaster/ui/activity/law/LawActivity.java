package com.xhtt.hiddendangermaster.ui.activity.law;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hg.zero.config.ZCommonResource;
import com.hg.zero.util.ZTextViewUtils;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.ui.base.HDBaseActivity;

/**
 * 版权声明和隐私保护界面
 *
 * @author HG
 */
public class LawActivity extends HDBaseActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_law;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(ZCommonResource.getBackIcon(), "免责声明");

        int colorBlue = Color.parseColor("#2F6699");
        int colorGrey = Color.parseColor("#666666");
        int colorRed = Color.parseColor("#DB0A10");

        TextView txt = findViewById(R.id.tv_txt);

        ZTextViewUtils.getBuilder(this, "")

                .append("本系统提醒您：")
                .setForegroundColor(colorGrey)
                .setBold()
                .append("在使用本系统的所有功能之前，请您务必仔细阅读并透彻理解本声明。您可以选择不使用本系统，但如果您使用本系统，您的使用行为将被视为对本声明全部内容的认可。\n\n")
                .setForegroundColor(colorGrey)

                .append("免责声明：")
                .setBold()
                .setForegroundColor(colorRed)
                .append("鉴于本系统只提供平台，数据的选取方式是平台用户共同录入的数据，所以本系统对检索/使用的数据结果不承担责任。如果因本 系统的检索/使用结果作为任何商业行业或者学术研究的依据而产生不良后果，本系统不承担任何法律责任。\n\n")
                .setForegroundColor(colorGrey)

                .append("关于隐私权：")
                .setBold()
                .setForegroundColor(colorRed)
                .append("访问者在本系统注册时提供的一些个人资料，本系统除您本人同意外不会将用户的任何资料以任何方式泄露给第三方。当政府部门、司法机关等依照法定程序要求本系统披露个人资料时，本系统将根据执法单位之要求或为公共安全之目的提供个人资料，在此情况下的披露，本系统不承担任何责任。\n\n")
                .setForegroundColor(colorGrey)

                .append("关于版权：\n")
                .setBold()
                .setForegroundColor(colorRed)
                .append("一、凡本系统注明“原创”的所有作品，其版权属于常州欣华天泰安全信息系统工程有限公司和本系统所有。其他媒体、网站或个人转载使用时不得进行商业性的原版原式的转载，也不得歪曲和篡改本系统所发布的内容。\n")
                .setForegroundColor(colorGrey)
                .append("二、凡本系统转载其它媒体作品的目的在于传递更多信息，并不代表本系统赞同其观点和对其真实性负责；其他媒体、网站或个人转载使用时必须保留本站注明的文章来源，并自负法律责任。\n")
                .setForegroundColor(colorGrey)
                .append("三、被本系统授权使用的单位，不应超越授权范围。\n")
                .setForegroundColor(colorGrey)
                .append("四、如因作品内容、版权和其它问题需要同本系统联系的，请在本系统发布该作品后的30日内进行。\n\n")
                .setForegroundColor(colorGrey)

                .append("关于解释权：")
                .setBold()
                .setForegroundColor(colorRed)
                .append("本系统之声明以及其修改权、更新权及最终解释权均属本站所有。")
                .setForegroundColor(colorGrey)

                .into(txt);
    }

    @Override
    public void setListener() {

    }

}
