package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.Adapter.FastAdapter.Bean.FastItemData;
import com.hg.hollowgoods.Adapter.FastAdapter.CallBack.OnFastClick;
import com.hg.hollowgoods.Adapter.FastAdapter.FastAdapter;
import com.hg.hollowgoods.Bean.CommonBean.CommonBean;
import com.hg.hollowgoods.Bean.EventBus.Event;
import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.UI.Base.BaseActivity;
import com.hg.hollowgoods.Util.RecyclerViewAnim.adapters.ScaleInAnimationAdapter;
import com.hg.hollowgoods.Util.RecyclerViewAnim.animators.LandingAnimator;
import com.xhtt.hiddendanger.Bean.HiddenDanger.CheckTableCompany;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Constant.EventActionCode;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.Constant.WorkType;
import com.xhtt.hiddendanger.R;
import com.xhtt.hiddendanger.UI.Fragment.HiddenDanger.CheckTableListFragment;

import java.util.ArrayList;

/**
 * 检查表列表界面
 *
 * @author HG
 */

public class CheckTableChangeCompanyListActivity extends BaseActivity {

    private RecyclerView result;

    private Company parentData;
    private CheckTableCompany checkTableCompany = new CheckTableCompany(FastAdapter.ITEM_TYPE_LIST);
    private FastAdapter adapter;
    private ArrayList<CommonBean> data = new ArrayList<>();
    private int clickPosition;
    private int clickSortNumber;
    private CheckTableListFragment checkTableListFragment;

    @Override
    public Activity addToExitGroup() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_check_table_list;
    }

    @Override
    public void initParamData() {
        parentData = baseUI.getParam(ParamKey.ParentData.getValue(), null);
        if (parentData != null) {
            checkTableCompany.setCompanyName(parentData.getCompanyName());
        }
    }

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, R.string.title_activity_check_table_list);
        result = findViewById(R.id.rv_result);

        result.setHasFixedSize(true);
        result.setItemAnimator(new LandingAnimator());
        result.setLayoutManager(new LinearLayoutManager(baseUI.getBaseContext()));

        FastAdapter.setAllItemOnlyRead(checkTableCompany, false);

        adapter = new FastAdapter(baseUI.getBaseContext(), data, false, true);
        result.setAdapter(new ScaleInAnimationAdapter(adapter));

        data.addAll(adapter.getDetailItemData(checkTableCompany));
        adapter.refreshData(data);

        checkTableListFragment = new CheckTableListFragment();
        checkTableListFragment.setFragmentArguments(
                "1",
                new String[]{ParamKey.ParentData.getValue()},
                new Object[]{parentData}
        );

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_content, checkTableListFragment);
        ft.commitAllowingStateLoss();

        return this;
    }

    @Override
    public void setListener() {

        adapter.setOnFastClick(new OnFastClick() {
            @Override
            public void onTakePhotoClick(View view, int i, int i1) {

            }

            @Override
            public void onOpenAlbumClick(View view, int i, int i1) {

            }

            @Override
            public void onSubmitClick(View view, int i) {

            }

            @Override
            public void onOperateClick(View view, int position, int sortNumber) {
                onFastItemClick(view, position, sortNumber);
            }

            @Override
            public void onFilePreClick(View view, int i, int i1) {

            }

            @Override
            public void onNumberPickerClick(View view, int i, int i1, Object o) {

            }

            @Override
            public void onEditClick(View view, int i) {

            }

            @Override
            public void onDeleteClick(View view, int i) {

            }

            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {
                FastItemData item = data.get(position).getData();
                int sortNumber = item.sortNumber;
                onFastItemClick(view, position, sortNumber);
            }
        });
    }

    @Override
    public void onEventUI(Event event) {
        if (event.isFromMe(this.getClass().getName())) {
            Company company;

            switch (event.getEventActionCode()) {
                case EventActionCode.COMPANY_SELECTOR:
                    String companyName = event.getObj(ParamKey.StringData.getValue(), "");
                    company = event.getObj(ParamKey.Company.getValue(), null);

                    if (!TextUtils.isEmpty(companyName)) {

                    } else {
                        parentData = company;

                        checkTableCompany.setCompanyName(company.getCompanyName());
                        adapter.refreshFastItem(checkTableCompany, 0);
                        checkTableListFragment.doRefresh(company);
                    }
                    break;
                case EventActionCode.COMPANY_SUBMIT:
                    company = event.getObj(ParamKey.Company.getValue(), null);
                    parentData = company;

                    checkTableCompany.setCompanyName(company.getCompanyName());
                    adapter.refreshFastItem(checkTableCompany, 0);
                    checkTableListFragment.doRefresh(company);
                    break;
            }
        } else {
            Company company;

            switch (event.getEventActionCode()) {
                case EventActionCode.COMPANY_SUBMIT:
                    company = event.getObj(ParamKey.Company.getValue(), null);
                    parentData = company;

                    checkTableCompany.setCompanyName(company.getCompanyName());
                    adapter.refreshFastItem(checkTableCompany, 0);
                    checkTableListFragment.doRefresh(company);
                    break;
            }
        }
    }

    private void onFastItemClick(View view, int position, int sortNumber) {

        clickPosition = position;
        clickSortNumber = sortNumber;

        switch (sortNumber) {
            case 10:
                // 企业名称
                baseUI.startMyActivity(CompanySelectorActivity.class,
                        new String[]{ParamKey.WorkType.getValue(), ParamKey.FromClass.getValue()},
                        new Object[]{WorkType.Selector, this.getClass()}
                );
                break;
        }
    }

}
