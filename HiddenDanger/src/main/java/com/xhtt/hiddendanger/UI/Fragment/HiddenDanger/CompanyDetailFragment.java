package com.xhtt.hiddendanger.UI.Fragment.HiddenDanger;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
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
import com.hg.hollowgoods.Util.XUtils.GetHttpDataListener;
import com.hg.hollowgoods.Util.XUtils.RequestParamsHelper;
import com.hg.hollowgoods.Util.XUtils.XUtils2;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.hg.hollowgoods.Widget.ValidatorInput.Validator.ValidatorFactory;
import com.hg.hollowgoods.Widget.ValidatorInput.Validator.ValidatorType;
import com.xhtt.hiddendanger.Application.HiddenDangerApplication;
import com.xhtt.hiddendanger.Bean.Common.AreaAllInOne;
import com.xhtt.hiddendanger.Bean.Common.CommonChooseItem;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Bean.ResponseInfo;
import com.xhtt.hiddendanger.Constant.Constants;
import com.xhtt.hiddendanger.Constant.EventActionCode;
import com.xhtt.hiddendanger.Constant.InterfaceApi;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.Constant.WorkType;
import com.xhtt.hiddendanger.R;
import com.xhtt.hiddendanger.UI.Activity.Common.AreaSelectorActivity;
import com.xhtt.hiddendanger.UI.Activity.HiddenDanger.CompanySelectorActivity;
import com.xhtt.hiddendanger.UI.Activity.HiddenDanger.CompanySelectorForFreeTakeActivity;
import com.xhtt.hiddendanger.UI.Fragment.OnFragmentWorkListener;

import org.greenrobot.eventbus.EventBus;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 企业基本信息界面
 *
 * @author HG
 */

public class CompanyDetailFragment extends BaseMVPFragment<CompanyDetailPresenter> implements CompanyDetailContract.View {

    private final int DIALOG_CODE_SUBMIT = 1000;

    private HGRefreshLayout refreshLayout;

    private FastAdapter adapter;
    private ArrayList<CommonBean> data = new ArrayList<>();
    private Company parentData;
    private WorkType workType;
    private Class<?> fromClass;
    private boolean isOnlySelector = true;
    private int clickPosition;
    private int clickSortNumber;
    private boolean isOnlyRead;
    private int businessCheckedPosition = -1;
    private int proportionCheckedPosition = -1;
    private OnFragmentWorkListener onFragmentWorkListener;
    private boolean isFreeTake = false;

    private CommonChooseItem province;
    private CommonChooseItem city;
    private CommonChooseItem district;
    private CommonChooseItem town;

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
            parentData = new Company();
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

        if (workType == WorkType.AddFreeTake) {
            isFreeTake = true;
            workType = WorkType.Add;
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

        if (parentData.getProvinceId() != null && parentData.isShowOther()) {
            Map<String, Object> request = new HashMap<>();
            request.put("provinceId", parentData.getProvinceId());
            request.put("cityId", parentData.getCityId());
            request.put("areaId", parentData.getDistrictId());
            getAreaName(request);
        }

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

//                if (sortNumber == 10) {
//                    // 企业名称
//                    clickPosition = position;
//                    clickSortNumber = sortNumber;
//                    ConfigInput configInput;
//
//                    configInput = new ConfigInput(baseUI.getBaseContext())
//                            .setHint("请输入企业名称")
//                            .setText(parentData.getCompanyName())
//                            .setMaxLines(1)
//                            .setValidator(
//                                    ValidatorFactory.getValidator(ValidatorType.MIN_LENGTH, "至少输入1个字", 1),
//                                    ValidatorFactory.getValidator(ValidatorType.MAX_LENGTH, "最多输入100个字", 100)
//                            );
//                    baseUI.baseDialog.showInputDialog(configInput, sortNumber);
//                } else {
                    onFastItemClick(view, position, sortNumber);
//                }
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
            case EventActionCode.COMPANY_SUBMIT:
                String companyName = item.getObj(ParamKey.StringData.getValue(), "");
                Company company = item.getObj(ParamKey.Company.getValue(), null);

                if (company != null) {
                    companyName = company.getCompanyName();
                    parentData.setId(company.getId());
                    parentData.setServiceId(company.getServiceId());
                    parentData.setTimes(company.getTimes());
                } else {
                    if (workType == WorkType.Add) {
                        parentData.setId(0);
                    }
                }

                if (workType == WorkType.Add && !isFreeTake) {
                    parentData.setId(0);
                }
                parentData.setCompanyName(companyName);

                adapter.refreshFastItem(parentData, clickPosition);
                break;
            case EventActionCode.CHECKED_AREA:
                province = item.getObj(ParamKey.Province.getValue(), new CommonChooseItem());
                city = item.getObj(ParamKey.City.getValue(), new CommonChooseItem());
                district = item.getObj(ParamKey.District.getValue(), new CommonChooseItem());
                town = item.getObj(ParamKey.Town.getValue(), new CommonChooseItem());

                parentData.setManageArea(province.getItem()
                        + " " + city.getItem()
                        + " " + district.getItem()
                        + " " + town.getItem()
                );

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
                    baseUI.startMyActivity(isFreeTake ? CompanySelectorForFreeTakeActivity.class : CompanySelectorActivity.class,
                            new String[]{ParamKey.WorkType.getValue(), ParamKey.FromClass.getValue()},
                            new Object[]{isOnlySelector ? WorkType.InputOnly : WorkType.Selector, baseUI.getBaseContext().getClass()}
                    );
                }
                break;
            case 15:
                // 行政区域
                if (!isOnlyRead) {
                    baseUI.startMyActivity(AreaSelectorActivity.class);
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
            mPresenter.submitData(parentData, province, city, district, town, workType);
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
        event.addObj(ParamKey.Company.getValue(), parentData);
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

    public Company getCompanyData() {
        return parentData;
    }

    public void getAreaName(Map<String, Object> request) {

        RequestParams params = RequestParamsHelper.buildKeyValueRequestParam(
                HttpMethod.GET,
                RequestParamsHelper.buildCommonRequestApi(InterfaceApi.GetAreaAllInOne.getUrl()),
                null,
                request
        );
        params.addHeader("token", MyApplication.createApplication().getToken());

        new XUtils2.BuilderGetHttpData().setGetHttpDataListener(new GetHttpDataListener() {
            @Override
            public void onGetSuccess(String result) {

                ResponseInfo responseInfo = new Gson().fromJson(result, ResponseInfo.class);

                if (responseInfo.getCode() == ResponseInfo.CODE_SUCCESS) {
                    AreaAllInOne tempData = new Gson().fromJson(
                            new Gson().toJson(responseInfo.getData()),
                            AreaAllInOne.class
                    );

                    if (tempData != null) {
                        if (!BeanUtils.isCollectionEmpty(tempData.getProvinces())) {
                            for (CommonChooseItem t : tempData.getProvinces()) {
                                if (t.getId() == parentData.getProvinceId()) {
                                    province = t;
                                    break;
                                }
                            }
                        }

                        if (!BeanUtils.isCollectionEmpty(tempData.getCities())) {
                            for (CommonChooseItem t : tempData.getCities()) {
                                if (t.getId() == parentData.getCityId()) {
                                    city = t;
                                    break;
                                }
                            }
                        }

                        if (!BeanUtils.isCollectionEmpty(tempData.getDistricts())) {
                            for (CommonChooseItem t : tempData.getDistricts()) {
                                if (t.getId() == parentData.getDistrictId()) {
                                    district = t;
                                    break;
                                }
                            }
                        }

                        if (!BeanUtils.isCollectionEmpty(tempData.getTowns())) {
                            for (CommonChooseItem t : tempData.getTowns()) {
                                if (t.getId() == parentData.getTownId()) {
                                    town = t;
                                    break;
                                }
                            }
                        }

                        if (province != null && city != null && district != null && town != null) {
                            parentData.setManageArea(province.getItem()
                                    + " " + city.getItem()
                                    + " " + district.getItem()
                                    + " " + town.getItem()
                            );

                            new Handler().postDelayed(() -> adapter.refreshFastItem(parentData, 1), 500);
                        }
                    }
                } else {
                    if (responseInfo.getCode() == ResponseInfo.CODE_FAIL) {
                        t.error(responseInfo.getMsg());
                    } else if (responseInfo.getCode() == ResponseInfo.CODE_TOKEN_OVERDUE) {
                        t.error("授权已过期，请重新登录");
                        HGEvent event = new HGEvent(EventActionCode.TokenOverdue);
                        EventBus.getDefault().post(event);
                    } else {
                        t.error(R.string.network_error);
                    }
                }
            }

            @Override
            public void onGetError(Throwable ex) {

            }

            @Override
            public void onGetFinish() {

            }
        }).getHttpData(params);
    }
}
