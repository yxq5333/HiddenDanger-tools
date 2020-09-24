package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.adapter.fast.HGFastAdapter2;
import com.hg.hollowgoods.adapter.fast.bean.HGFastItem2;
import com.hg.hollowgoods.adapter.fast.callback.OnHGFastItemClickListener2Adapter;
import com.hg.hollowgoods.bean.eventbus.HGEvent;
import com.hg.hollowgoods.constant.HGCommonResource;
import com.hg.hollowgoods.constant.HGParamKey;
import com.hg.hollowgoods.ui.base.BaseActivity;
import com.hg.hollowgoods.util.anim.recyclerview.adapters.ScaleInAnimationAdapter;
import com.hg.hollowgoods.util.anim.recyclerview.animators.LandingAnimator;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger.CheckTableListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 检查表列表界面
 *
 * @author HG
 */

public class CheckTableChangeCompanyListActivity extends BaseActivity {

    private RecyclerView result;

    private Company parentData;
    private HGFastAdapter2 adapter;
    private List<HGFastItem2> data = new ArrayList<>();
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
        parentData = baseUI.getParam(ParamKey.ParentData, null);
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, R.string.title_activity_check_table_list);
        result = findViewById(R.id.rv_result);

        result.setHasFixedSize(true);
        result.setItemAnimator(new LandingAnimator());
        result.setLayoutManager(new LinearLayoutManager(baseUI.getBaseContext()));

        data.add(new HGFastItem2.Builder(10, HGFastItem2.ITEM_TYPE_INPUT)
                .setLabel("企业")
                .setContentHint("请选择")
                .setContent(parentData == null ? "" : parentData.getCompanyName())
                .setNotEmpty(true)
                .setOnlyRead(false)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        adapter = new HGFastAdapter2(baseUI, data);
        result.setAdapter(new ScaleInAnimationAdapter(adapter));

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
        adapter.setOnHGFastItemClickListener2(10, new OnHGFastItemClickListener2Adapter() {
            @Override
            public void onItemClick(int clickItemId) {
                baseUI.startMyActivity(CompanySelectorActivity.class,
                        new Enum[]{ParamKey.WorkType, HGParamKey.RequestCode},
                        new Object[]{WorkType.Selector, baseUI.getBaseContext().getClass().getName()}
                );
            }
        });
    }

    @Override
    public void onEventUI(HGEvent event) {
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
