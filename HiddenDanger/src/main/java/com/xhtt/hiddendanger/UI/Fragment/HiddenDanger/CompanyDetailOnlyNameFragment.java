package com.xhtt.hiddendanger.UI.Fragment.HiddenDanger;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.Adapter.FastAdapter.Bean.FastItemData;
import com.hg.hollowgoods.Adapter.FastAdapter.CallBack.OnFastClick;
import com.hg.hollowgoods.Adapter.FastAdapter.FastAdapter;
import com.hg.hollowgoods.Bean.CommonBean.CommonBean;
import com.hg.hollowgoods.Bean.EventBus.Event;
import com.hg.hollowgoods.Constant.HGConstants;
import com.hg.hollowgoods.Constant.HGParamKey;
import com.hg.hollowgoods.UI.Base.BaseMVPFragment;
import com.hg.hollowgoods.UI.Base.Message.Dialog2.ConfigInput;
import com.hg.hollowgoods.UI.Base.Message.Toast.t;
import com.hg.hollowgoods.Util.BeanUtils;
import com.hg.hollowgoods.Widget.ValidatorInput.Validator.ValidatorFactory;
import com.hg.hollowgoods.Widget.ValidatorInput.Validator.ValidatorType;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Bean.HiddenDanger.CompanyOnlyName;
import com.xhtt.hiddendanger.Constant.Constants;
import com.xhtt.hiddendanger.Constant.EventActionCode;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.Constant.WorkType;
import com.xhtt.hiddendanger.R;
import com.xhtt.hiddendanger.UI.Fragment.OnFragmentWorkListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 企业基本信息界面
 *
 * @author HG
 */

public class CompanyDetailOnlyNameFragment extends BaseMVPFragment<CompanyDetailPresenter> implements CompanyDetailContract.View {

    private final int DIALOG_CODE_SUBMIT = 1000;

    private HGRefreshLayout refreshLayout;

    private FastAdapter adapter;
    private ArrayList<CommonBean> data = new ArrayList<>();
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
    public int bindLayout() {
        return R.layout.fragment_company_detail;
    }

    @Override
    public void initParamData() {

        parentData = baseUI.getParam(ParamKey.ParentData.getValue(), null);
        workType = baseUI.getParam(ParamKey.WorkType.getValue(), null);
        fromClass = baseUI.getParam(ParamKey.FromClass.getValue(), this.getClass());
        isOnlySelector = baseUI.getParam(ParamKey.IsOnlySelector.getValue(), true);

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

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleViewVisibility(false);

        refreshLayout = baseUI.findViewById(R.id.hgRefreshLayout);

        refreshLayout.initRecyclerView();
        refreshLayout.setAdapter(adapter = new FastAdapter(baseUI.getBaseContext(), data, false, true));

        FastAdapter.setAllItemOnlyRead(parentData, false);
        data.addAll(adapter.getDetailItemData(parentData));

        adapter.refreshData(data);

        return this;
    }

    @Override
    public void setListener() {

        refreshLayout.setOnRefreshListener(refreshLayout -> doRefresh());

        adapter.setOnFastClick(new OnFastClick() {
            @Override
            public void onTakePhotoClick(View view, int position, int sortNumber) {

            }

            @Override
            public void onOpenAlbumClick(View view, int position, int sortNumber) {

            }

            @Override
            public void onSubmitClick(View view, int id) {

            }

            @Override
            public void onOperateClick(View view, int position, int sortNumber) {
                onFastItemClick(view, position, sortNumber);
            }

            @Override
            public void onFilePreClick(View view, int position, int sortNumber) {

            }

            @Override
            public void onNumberPickerClick(View view, int position, int sortNumber, Object num) {

            }

            @Override
            public void onEditClick(View view, int position) {

            }

            @Override
            public void onDeleteClick(View view, int position) {

            }

            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                FastItemData item = data.get(position).getData();
                int sortNumber = item.sortNumber;

                onFastItemClick(view, position, sortNumber);
            }
        });

        baseUI.baseDialog.setOnDialogClickListener((code, result, backData) -> {

            if (result) {
                String value;

                switch (code) {
                    case 10:
                        // 企业名称
                        value = backData.getString(HGParamKey.InputValue.getValue(), "");
                        parentData.setCompanyName(value);
                        break;
                    case 20:
                        // 单位地址
                        value = backData.getString(HGParamKey.InputValue.getValue(), "");
                        parentData.setAddress(value);
                        break;
                    case 30:
                        // 主要联系人
                        value = backData.getString(HGParamKey.InputValue.getValue(), "");
                        parentData.setMainPeople(value);
                        break;
                    case 40:
                        // 主要联系人联系电话
                        value = backData.getString(HGParamKey.InputValue.getValue(), "");
                        parentData.setMainPeoplePhone(value);
                        break;
                    case 50:
                        // 行业
                        if (!isOnlyRead) {
                            businessCheckedPosition = backData.getInt(HGParamKey.Position.getValue(), -1);
                            if (businessCheckedPosition != -1) {
                                parentData.setBusiness(Constants.BUSINESS[businessCheckedPosition]);
                            } else {
                                parentData.setBusiness("");
                            }
                        }
                        break;
                    case 60:
                        // 规模情况
                        proportionCheckedPosition = backData.getInt(HGParamKey.Position.getValue(), -1);
                        if (proportionCheckedPosition != -1) {
                            parentData.setProportion(Constants.PROPORTION[proportionCheckedPosition]);
                        } else {
                            parentData.setProportion("");
                        }
                        break;
                    case 70:
                        // 人员数量
                        value = backData.getString(HGParamKey.InputValue.getValue(), "");
                        parentData.setPeopleCount(value);
                        break;
                }

                adapter.refreshFastItem(parentData, clickPosition);
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
    public void onEventUI(Event item) {

        switch (item.getEventActionCode()) {
            case EventActionCode.COMPANY_SELECTOR:
                String companyName = item.getObj(ParamKey.StringData.getValue(), "");
                Company company = item.getObj(ParamKey.Company.getValue(), null);

                if (company != null) {
                    companyName = company.getCompanyName();
                }

                if (workType == WorkType.Add) {
                    parentData.setId(0);
                }
                parentData.setCompanyName(companyName);

                adapter.refreshFastItem(parentData, clickPosition);
                break;
        }
    }

    private void doRefresh() {
        new Handler().postDelayed(() -> refreshLayout.getRefreshLayout().finishRefresh(), 1000);
    }

    private void onFastItemClick(View view, int position, int sortNumber) {

        clickPosition = position;
        clickSortNumber = sortNumber;
        ConfigInput configInput;

        switch (sortNumber) {
            case 10:
                // 企业名称
                if (!isOnlyRead) {
                    configInput = new ConfigInput(baseUI.getBaseContext())
                            .setHint("请输入企业名称")
                            .setText(parentData.getCompanyName())
                            .setMaxLines(1)
                            .setInputType(HGConstants.INPUT_TYPE_DEFAULT)
                            .setValidator(ValidatorFactory.getValidator(ValidatorType.MIN_LENGTH, "至少输入1个字", 1));
                    baseUI.baseDialog.showInputDialog(configInput, sortNumber);
                }
                break;
            case 20:
                // 单位地址
                if (!isOnlyRead) {
                    configInput = new ConfigInput(baseUI.getBaseContext())
                            .setHint("请输入单位地址")
                            .setText(parentData.getAddress())
                            .setMaxLines(1)
                            .setValidator(ValidatorFactory.getValidator(ValidatorType.MAX_LENGTH, "最多输入100个字", 100));
                    baseUI.baseDialog.showInputDialog(configInput, sortNumber);
                }
                break;
            case 30:
                // 主要联系人
                if (!isOnlyRead) {
                    configInput = new ConfigInput(baseUI.getBaseContext())
                            .setHint("请输入主要联系人")
                            .setText(parentData.getMainPeople())
                            .setMaxLines(1)
                            .setValidator(ValidatorFactory.getValidator(ValidatorType.MAX_LENGTH, "最多输入10个字", 10));
                    baseUI.baseDialog.showInputDialog(configInput, sortNumber);
                }
                break;
            case 40:
                // 主要联系人联系电话
                if (!isOnlyRead) {
                    configInput = new ConfigInput(baseUI.getBaseContext())
                            .setHint("请输入联系电话")
                            .setText(parentData.getMainPeoplePhone())
                            .setMaxLines(1)
                            .setInputType(HGConstants.INPUT_TYPE_NUMBER)
                            .setValidator(ValidatorFactory.getValidator(ValidatorType.MAX_LENGTH, "最多输入11个字", 11),
                                    ValidatorFactory.getValidator(ValidatorType.PHONE, "手机号不合法", null)
                            );
                    baseUI.baseDialog.showInputDialog(configInput, sortNumber);
                }
                break;
            case 50:
                // 行业
                if (!isOnlyRead) {
                    baseUI.baseDialog.showSingleDialog("请选择行业", Constants.BUSINESS_OBJ, businessCheckedPosition, sortNumber);
                }
                break;
            case 60:
                // 规模情况
                if (!isOnlyRead) {
                    baseUI.baseDialog.showSingleDialog("请选择规模情况", Constants.PROPORTION_OBJ, proportionCheckedPosition, sortNumber);
                }
                break;
            case 70:
                // 人员数量
                if (!isOnlyRead) {
                    configInput = new ConfigInput(baseUI.getBaseContext())
                            .setHint("请输入人员数量(人)")
                            .setText(parentData.getPeopleCount())
                            .setMaxLines(1)
                            .setInputType(HGConstants.INPUT_TYPE_NUMBER)
                            .setValidator(ValidatorFactory.getValidator(ValidatorType.MAX_LENGTH, "最多输入12个字", 12));
                    baseUI.baseDialog.showInputDialog(configInput, sortNumber);
                }
                break;
        }
    }

    public HGRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    public void setOnFragmentWorkListener(OnFragmentWorkListener onFragmentWorkListener) {
        this.onFragmentWorkListener = onFragmentWorkListener;
    }

    public void submitData() {
        if (adapter.checkNotEmptyItem(parentData)) {
            baseUI.baseDialog.showProgressDialog("提交中，请稍候……", DIALOG_CODE_SUBMIT);
            mPresenter.submitData(parentData, workType);
        }
    }

    @Override
    public void submitDataSuccess(Long id, Long serviceId) {

        t.success("提交成功");

        if (id != null) {
            parentData.setId(id);
        }

        if (serviceId != null) {
            parentData.setServiceId(serviceId);
            parentData.setTimes(1);
        }

        Event event = new Event(EventActionCode.COMPANY_SUBMIT, fromClass.getName());
        Company company = BeanUtils.copy(parentData, Company.class);
        event.addObj(ParamKey.Company.getValue(), company);
        event.addObj(ParamKey.WorkType.getValue(), workType);
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
        return adapter.checkNotEmptyItem(parentData);
    }

    public CompanyOnlyName getCompanyData() {
        return parentData;
    }
}
