package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.zero.adapter.fast.ZFastAdapter2;
import com.hg.zero.adapter.fast.bean.ZFastItem2;
import com.hg.zero.adapter.fast.callback.ZOnFastItemClickListener2Adapter;
import com.hg.zero.anim.recyclerview.adapters.ZScaleInAnimationAdapter;
import com.hg.zero.anim.recyclerview.animators.ZLandingAnimator;
import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.config.ZCommonResource;
import com.hg.zero.constant.ZParamKey;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.ui.base.HDBaseActivity;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.CheckTableListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 检查表列表界面
 *
 * @author HG
 */

public class CheckTableChangeCompanyListActivity extends HDBaseActivity {

    private RecyclerView result;

    private Company parentData;
    private ZFastAdapter2 adapter;
    private List<ZFastItem2> data = new ArrayList<>();
    private CheckTableListFragment checkTableListFragment;

    @Override
    public Object registerEventBus() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_check_table_list;
    }

    @Override
    public void initParamData() {
        super.initParamData();
        parentData = baseUI.getParam(ParamKey.ParentData, null);
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(ZCommonResource.getBackIcon(), R.string.title_activity_check_table_list);
        result = findViewById(R.id.rv_result);

        result.setHasFixedSize(true);
        result.setItemAnimator(new ZLandingAnimator());
        result.setLayoutManager(new LinearLayoutManager(baseUI.getBaseContext()));

        data.add(new ZFastItem2.Builder(10, ZFastItem2.ITEM_TYPE_INPUT)
                .setLabel("企业")
                .setContentHint("请选择")
                .setContent(parentData == null ? "" : parentData.getCompanyName())
                .setNotEmpty(true)
                .setOnlyRead(false)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        adapter = new ZFastAdapter2(baseUI, data);
        result.setAdapter(new ZScaleInAnimationAdapter(adapter));

//        adapter.refreshDataAllData(data);

        checkTableListFragment = new CheckTableListFragment();
        checkTableListFragment.setFragmentArguments(
                "1",
                new Enum[]{ParamKey.ParentData},
                new Object[]{parentData}
        );

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_content, checkTableListFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void setListener() {

        // 企业名称
        adapter.setOnFastItemClickListener2(10, new ZOnFastItemClickListener2Adapter() {
            @Override
            public boolean onItemClick(int clickItemId) {

                baseUI.startMyActivity(CompanySelectorActivity.class,
                        new Enum[]{ParamKey.WorkType, ZParamKey.RequestCode},
                        new Object[]{WorkType.Selector, baseUI.getBaseContext().getClass().getName()}
                );

                return true;
            }
        });
    }

    @Override
    public void onEventUI(ZEvent event) {
        if (event.isFromMe(this.getClass().getName())) {
            Company company;

            if (event.getEventActionCode() == EventActionCode.COMPANY_SELECTOR) {
                String companyName = event.getObj(ParamKey.StringData, "");
                company = event.getObj(ParamKey.Company, null);

                if (!TextUtils.isEmpty(companyName)) {

                } else {
                    parentData = company;

                    adapter.findItemById(10).setContent(company.getCompanyName());
                    adapter.processData();
                    checkTableListFragment.doRefresh(company);
                }
            } else if (event.getEventActionCode() == EventActionCode.COMPANY_SUBMIT) {
                company = event.getObj(ParamKey.Company, null);
                parentData = company;

                adapter.findItemById(10).setContent(company.getCompanyName());
                adapter.processData();
                checkTableListFragment.doRefresh(company);
            }
        } else {
            Company company;

            if (event.getEventActionCode() == EventActionCode.COMPANY_SUBMIT) {
                company = event.getObj(ParamKey.Company, null);
                parentData = company;

                adapter.findItemById(10).setContent(company.getCompanyName());
                adapter.processData();
                checkTableListFragment.doRefresh(company);
            }
        }
    }

}
