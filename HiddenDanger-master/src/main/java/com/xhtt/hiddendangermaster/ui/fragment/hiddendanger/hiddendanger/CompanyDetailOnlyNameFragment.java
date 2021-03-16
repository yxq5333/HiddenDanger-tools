package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.hg.zero.adapter.fast.ZFastAdapter2;
import com.hg.zero.adapter.fast.bean.ZFastItem2;
import com.hg.zero.adapter.fast.callback.ZOnFastItemClickListener2Adapter;
import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.constant.ZConstants;
import com.hg.zero.constant.ZParamKey;
import com.hg.zero.dialog.ZDialogConfig;
import com.hg.zero.toast.Zt;
import com.hg.zero.util.ZBeanUtils;
import com.hg.zero.widget.refreshlayout.ZRefreshLayout;
import com.hg.zero.widget.validatorinput.validator.ZValidatorFactory;
import com.hg.zero.widget.validatorinput.validator.ZValidatorType;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.CompanyOnlyName;
import com.xhtt.hiddendangermaster.constant.Constants;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPFragment;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.OnFragmentWorkListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业基本信息界面
 *
 * @author HG
 */

public class CompanyDetailOnlyNameFragment extends HDBaseMVPFragment<CompanyDetailPresenter> implements CompanyDetailContract.View {

    private final int DIALOG_CODE_SUBMIT = 1000;

    private ZRefreshLayout refreshLayout;

    private ZFastAdapter2 adapter;
    private List<ZFastItem2> data = new ArrayList<>();
    private CompanyOnlyName parentData;
    private WorkType workType;
    private Class<?> fromClass;
    private boolean isOnlySelector = true;
    private int clickPosition;
    private int clickSortNumber;
    private boolean isOnlyRead;
    private int businessCheckedPosition = -1;
    private int proportionCheckedPosition = -1;
    private OnFragmentWorkListener onFragmentWorkListener;

    @Override
    public Object registerEventBus() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_company_detail;
    }

    @Override
    public void initParamData() {

        super.initParamData();
        parentData = baseUI.getParam(ParamKey.ParentData, null);
        workType = baseUI.getParam(ParamKey.WorkType, null);
        fromClass = baseUI.getParam(ParamKey.FromClass, this.getClass());
        isOnlySelector = baseUI.getParam(ParamKey.IsOnlySelector, true);

        if (parentData == null) {
            parentData = new CompanyOnlyName();
        } else {
            int i = 0;
            for (String t : Constants.PROPORTION) {
                if (TextUtils.equals(t, parentData.getProportion())) {
                    proportionCheckedPosition = i;
                    break;
                }

                i++;
            }

            i = 0;
            for (String t : Constants.BUSINESS) {
                if (TextUtils.equals(t, parentData.getBusiness())) {
                    businessCheckedPosition = i;
                    break;
                }

                i++;
            }
        }

        if (workType == null) {
            workType = WorkType.Detail;
        }

        isOnlyRead = workType == WorkType.Detail;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleViewVisibility(false);

        refreshLayout = baseUI.findViewById(R.id.ZRefreshLayout);

        data.add(new ZFastItem2.Builder(10, ZFastItem2.ITEM_TYPE_INPUT)
                .setLabel("企业名称")
                .setContentHint("请选择")
                .setContent(parentData == null ? "" : parentData.getCompanyName())
                .setOnlyRead(false)
                .setNotEmpty(true)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        refreshLayout.initRecyclerView();
        refreshLayout.setAdapter(adapter = new ZFastAdapter2(baseUI, data));

        //        adapter.refreshDataAllData(data);
    }

    @Override
    public void setListener() {

        refreshLayout.setOnRefreshListener(refreshLayout -> doRefresh());

        // 企业名称
        adapter.setOnFastItemClickListener2(10, new ZOnFastItemClickListener2Adapter() {
            @Override
            public boolean onItemClick(int clickItemId) {
                if (!isOnlyRead) {
                    ZDialogConfig.InputConfig configInput = new ZDialogConfig.InputConfig(clickItemId)
                            .setHint("请输入企业名称")
                            .setContent(parentData.getCompanyName())
                            .setMaxLines(1)
                            .setInputType(ZConstants.INPUT_TYPE_NULL)
                            .setValidator(ZValidatorFactory.getValidator(ZValidatorType.MIN_LENGTH, "至少输入1个字", 1));
                    baseUI.baseDialog.showInputDialog(configInput);
                }

                return true;
            }
        });

        baseUI.baseDialog.addOnDialogClickListener((code, result, backData) -> {
            if (result) {
                String value;

                switch (code) {
                    case 10:
                        // 企业名称
                        value = backData.getData(ZParamKey.InputValue, "");
                        parentData.setCompanyName(value);
                        break;
                }
            }
        });

        if (onFragmentWorkListener != null) {
            onFragmentWorkListener.onFragmentWork();
        }
    }

    @Override
    public CompanyDetailPresenter createPresenter() {
        return new CompanyDetailPresenter();
    }

    @Override
    public void onEventUI(ZEvent item) {
        if (item.getEventActionCode() == EventActionCode.COMPANY_SELECTOR) {
            String companyName = item.getObj(ParamKey.StringData, "");
            Company company = item.getObj(ParamKey.Company, null);

            if (company != null) {
                companyName = company.getCompanyName();
            }

            if (workType == WorkType.Add) {
                parentData.setId(0);
            }
            parentData.setCompanyName(companyName);
            adapter.findItemById(10).setContent(companyName);

            adapter.processData();
        }
    }

    private void doRefresh() {
        new Handler().postDelayed(() -> refreshLayout.getRefreshLayout().finishRefresh(), 1000);
    }

    public ZRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    public void setOnFragmentWorkListener(OnFragmentWorkListener onFragmentWorkListener) {
        this.onFragmentWorkListener = onFragmentWorkListener;
    }

    public void submitData() {
        if (checkNotEmptyItems()) {
            baseUI.baseDialog.showProgressDialog(new ZDialogConfig.ProgressConfig(DIALOG_CODE_SUBMIT)
                    .setContent("提交中，请稍候……")
            );
            mPresenter.submitData(parentData, workType);
        }
    }

    @Override
    public void submitDataSuccess(Long id, Long serviceId) {

        Zt.success("提交成功");

        if (id != null) {
            parentData.setId(id);
        }

        if (serviceId != null) {
            parentData.setServiceId(serviceId);
            parentData.setTimes(1);
        }

        ZEvent event = new ZEvent(EventActionCode.COMPANY_SUBMIT, fromClass.getName());
        Company company = ZBeanUtils.copy(parentData, Company.class);
        event.addObj(ParamKey.Company, company);
        event.addObj(ParamKey.WorkType, workType);
        EventBus.getDefault().post(event);

        finishMyActivity();
    }

    @Override
    public void submitDataError() {

    }

    @Override
    public void submitDataFinish() {
        baseUI.baseDialog.closeDialog(DIALOG_CODE_SUBMIT);
    }

    public boolean checkNotEmptyItems() {
        return adapter.checkNotEmptyItem();
    }

    public CompanyOnlyName getCompanyData() {
        return parentData;
    }
}
