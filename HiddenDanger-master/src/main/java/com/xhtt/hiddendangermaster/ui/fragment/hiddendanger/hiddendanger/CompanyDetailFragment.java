package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.hg.hollowgoods.adapter.fast.HGFastAdapter2;
import com.hg.hollowgoods.adapter.fast.bean.HGFastItem2;
import com.hg.hollowgoods.adapter.fast.callback.OnHGFastItemClickListener2Adapter;
import com.hg.hollowgoods.bean.eventbus.HGEvent;
import com.hg.hollowgoods.constant.HGParamKey;
import com.hg.hollowgoods.ui.base.message.dialog2.ChoiceItem;
import com.hg.hollowgoods.ui.base.message.dialog2.DialogConfig;
import com.hg.hollowgoods.ui.base.message.toast.t;
import com.hg.hollowgoods.ui.base.mvp.BaseMVPFragment;
import com.hg.hollowgoods.util.BeanUtils;
import com.hg.hollowgoods.util.xutils.RequestParamsHelper;
import com.hg.hollowgoods.util.xutils.XUtils2;
import com.hg.hollowgoods.util.xutils.callback.base.GetHttpDataListener;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.application.MyApplication;
import com.xhtt.hiddendangermaster.bean.ResponseInfo;
import com.xhtt.hiddendangermaster.bean.hiddendanger.common.AreaAllInOne;
import com.xhtt.hiddendangermaster.bean.hiddendanger.common.CommonChooseItem;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.constant.Constants;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.InterfaceApi;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.common.AreaSelectorActivity;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger.CompanySelectorActivity;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger.CompanySelectorForFreeTakeActivity;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.OnFragmentWorkListener;

import org.greenrobot.eventbus.EventBus;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业基本信息界面
 *
 * @author HG
 */

public class CompanyDetailFragment extends BaseMVPFragment<CompanyDetailPresenter> implements CompanyDetailContract.View {

    private final int DIALOG_CODE_SUBMIT = 1000;

    private HGRefreshLayout refreshLayout;

    private HGFastAdapter2 adapter;
    private List<HGFastItem2> data = new ArrayList<>();
    private Company parentData;
    private WorkType workType;
    private Class<?> fromClass;
    private boolean isOnlySelector = true;
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
    public Object registerEventBus() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_company_detail;
    }

    @Override
    public void initParamData() {

        parentData = baseUI.getParam(ParamKey.ParentData, null);
        workType = baseUI.getParam(ParamKey.WorkType, null);
        fromClass = baseUI.getParam(ParamKey.FromClass, this.getClass());
        isOnlySelector = baseUI.getParam(ParamKey.IsOnlySelector, true);

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

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleViewVisibility(false);

        refreshLayout = baseUI.findViewById(R.id.hgRefreshLayout);

        data.add(new HGFastItem2.Builder(10, HGFastItem2.ITEM_TYPE_INPUT)
                .setLabel("企业名称")
                .setContentHint("请选择")
                .setContent(parentData == null ? "" : parentData.getCompanyName())
                .setOnlyRead(false)
                .setNotEmpty(true)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        data.add(new HGFastItem2.Builder(15, HGFastItem2.ITEM_TYPE_INPUT)
                .setLabel("行政区域")
                .setContentHint("请选择")
                .setOnlyRead(false)
                .setNotEmpty(false)
                .setHide(!parentData.isShowOther())
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        data.add(new HGFastItem2.Builder(20, HGFastItem2.ITEM_TYPE_INPUT)
                .setLabel("单位地址")
                .setContentHint("请输入")
                .setContent(parentData == null ? "" : parentData.getAddress())
                .setOnlyRead(false)
                .setNotEmpty(false)
                .setHide(!parentData.isShowOther())
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        data.add(new HGFastItem2.Builder(30, HGFastItem2.ITEM_TYPE_INPUT)
                .setLabel("主要联系人")
                .setContentHint("请输入")
                .setContent(parentData == null ? "" : parentData.getMainPeople())
                .setOnlyRead(false)
                .setNotEmpty(false)
                .setHide(!parentData.isShowOther())
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        data.add(new HGFastItem2.Builder(40, HGFastItem2.ITEM_TYPE_INPUT)
                .setLabel("联系电话")
                .setContentHint("请输入")
                .setContent(parentData == null ? "" : parentData.getMainPeoplePhone())
                .setOnlyRead(false)
                .setNotEmpty(false)
                .setHide(!parentData.isShowOther())
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        ChoiceItem checkedItem = null;
        for (ChoiceItem t : Constants.BUSINESS_OBJ) {
            if (TextUtils.equals(t.getItem() + "", parentData.getBusiness())) {
                checkedItem = t;
                break;
            }
        }
        data.add(new HGFastItem2.Builder(50, HGFastItem2.ITEM_TYPE_SINGLE_CHOICE)
                .setLabel("行业")
                .setContentHint("请选择")
                .setContent(parentData == null ? "" : parentData.getBusiness())
                .setSingleCheckedItem(checkedItem)
                .setChoiceItems(Constants.BUSINESS_OBJ)
                .setOnlyRead(false)
                .setNotEmpty(false)
                .setHide(!parentData.isShowOther())
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        checkedItem = null;
        for (ChoiceItem t : Constants.PROPORTION_OBJ) {
            if (TextUtils.equals(t.getItem() + "", parentData.getProportion())) {
                checkedItem = t;
                break;
            }
        }
        data.add(new HGFastItem2.Builder(60, HGFastItem2.ITEM_TYPE_SINGLE_CHOICE)
                .setLabel("规模情况")
                .setContentHint("请选择")
                .setContent(parentData == null ? "" : parentData.getProportion())
                .setSingleCheckedItem(checkedItem)
                .setChoiceItems(Constants.PROPORTION_OBJ)
                .setOnlyRead(false)
                .setNotEmpty(false)
                .setHide(!parentData.isShowOther())
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        data.add(new HGFastItem2.Builder(70, HGFastItem2.ITEM_TYPE_SINGLE_CHOICE)
                .setLabel("人员数量(人)")
                .setContentHint("请输入")
                .setContent(parentData == null ? "" : parentData.getPeopleCount())
                .setOnlyRead(false)
                .setNotEmpty(false)
                .setHide(!parentData.isShowOther())
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        refreshLayout.initRecyclerView();
        refreshLayout.setAdapter(adapter = new HGFastAdapter2(baseUI, data));

//        adapter.refreshDataAllData(data);

        if (parentData.getProvinceId() != null && parentData.isShowOther()) {
            Map<String, Object> request = new HashMap<>();
            request.put("provinceId", parentData.getProvinceId());
            request.put("cityId", parentData.getCityId());
            request.put("areaId", parentData.getDistrictId());
            getAreaName(request);
        }
    }

    @Override
    public void setListener() {

        refreshLayout.setOnRefreshListener(refreshLayout -> doRefresh());

        // 企业名称
        adapter.setOnHGFastItemClickListener2(10, new OnHGFastItemClickListener2Adapter() {
            @Override
            public void onItemClick(int clickItemId) {
                if (!isOnlyRead) {
                    baseUI.startMyActivity(isFreeTake ? CompanySelectorForFreeTakeActivity.class : CompanySelectorActivity.class,
                            new Enum[]{ParamKey.WorkType, HGParamKey.RequestCode},
                            new Object[]{isOnlySelector ? WorkType.InputOnly : WorkType.Selector, baseUI.getBaseContext().getClass().getName()}
                    );
                }
            }
        });

        // 行政区域
        adapter.setOnHGFastItemClickListener2(15, new OnHGFastItemClickListener2Adapter() {
            @Override
            public void onItemClick(int clickItemId) {
                if (!isOnlyRead) {
                    baseUI.startMyActivity(AreaSelectorActivity.class);
                }
            }
        });

        baseUI.baseDialog.addOnDialogClickListener((code, result, backData) -> {

            if (result) {
                String value;

                switch (code) {
                    case 20:
                        // 单位地址
                        value = backData.getData(HGParamKey.InputValue, "");
                        parentData.setAddress(value);
                        break;
                    case 30:
                        // 主要联系人
                        value = backData.getData(HGParamKey.InputValue, "");
                        parentData.setMainPeople(value);
                        break;
                    case 40:
                        // 主要联系人联系电话
                        value = backData.getData(HGParamKey.InputValue, "");
                        parentData.setMainPeoplePhone(value);
                        break;
                    case 50:
                        // 行业
                        if (!isOnlyRead) {
                            businessCheckedPosition = backData.getData(HGParamKey.Position, -1);
                            if (businessCheckedPosition != -1) {
                                parentData.setBusiness(Constants.BUSINESS[businessCheckedPosition]);
                            } else {
                                parentData.setBusiness("");
                            }
                        }
                        break;
                    case 60:
                        // 规模情况
                        proportionCheckedPosition = backData.getData(HGParamKey.Position, -1);
                        if (proportionCheckedPosition != -1) {
                            parentData.setProportion(Constants.PROPORTION[proportionCheckedPosition]);
                        } else {
                            parentData.setProportion("");
                        }
                        break;
                    case 70:
                        // 人员数量
                        value = backData.getData(HGParamKey.InputValue, "");
                        parentData.setPeopleCount(value);
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
    public void onEventUI(HGEvent item) {
        if (item.getEventActionCode() == EventActionCode.COMPANY_SELECTOR
                || item.getEventActionCode() == EventActionCode.COMPANY_SUBMIT) {
            String companyName = item.getObj(ParamKey.StringData, "");
            Company company = item.getObj(ParamKey.Company, null);

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
            adapter.findItemById(10).setContent(companyName);

            adapter.processData();
        } else if (item.getEventActionCode() == EventActionCode.CHECKED_AREA) {
            province = item.getObj(ParamKey.Province, new CommonChooseItem());
            city = item.getObj(ParamKey.City, new CommonChooseItem());
            district = item.getObj(ParamKey.District, new CommonChooseItem());
            town = item.getObj(ParamKey.Town, new CommonChooseItem());

            String area = province.getItem()
                    + " " + city.getItem()
                    + " " + district.getItem()
                    + " " + town.getItem();
            parentData.setManageArea(area);
            adapter.findItemById(15).setContent(area);

            adapter.processData();
        }
    }

    private void doRefresh() {
        new Handler().postDelayed(() -> refreshLayout.getRefreshLayout().finishRefresh(), 1000);
    }

    public HGRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    public void setOnFragmentWorkListener(OnFragmentWorkListener onFragmentWorkListener) {
        this.onFragmentWorkListener = onFragmentWorkListener;
    }

    public void submitData() {
        if (checkNotEmptyItems()) {
            baseUI.baseDialog.showProgressDialog(new DialogConfig.ProgressConfig(DIALOG_CODE_SUBMIT)
                    .setText("提交中，请稍候……")
            );
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

        HGEvent event = new HGEvent(EventActionCode.COMPANY_SUBMIT, fromClass.getName());
        event.addObj(ParamKey.Company, parentData);
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
                            String area = province.getItem()
                                    + " " + city.getItem()
                                    + " " + district.getItem()
                                    + " " + town.getItem();
                            parentData.setManageArea(area);
                            adapter.findItemById(15).setContent(area);

                            new Handler().postDelayed(() -> adapter.processData(), 500);
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
