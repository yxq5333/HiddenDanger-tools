package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.Adapter.FastAdapter.Bean.FastItemData;
import com.hg.hollowgoods.Adapter.FastAdapter.CallBack.OnFastClick;
import com.hg.hollowgoods.Adapter.FastAdapter.FastAdapter;
import com.hg.hollowgoods.Application.BaseApplication;
import com.hg.hollowgoods.Bean.AppFile;
import com.hg.hollowgoods.Bean.CommonBean.CommonBean;
import com.hg.hollowgoods.Bean.EventBus.Event;
import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.Constant.HGParamKey;
import com.hg.hollowgoods.UI.Base.BaseMVPActivity;
import com.hg.hollowgoods.UI.Base.Click.OnViewClickListener;
import com.hg.hollowgoods.UI.Base.Message.Dialog2.DateTimeDialogType;
import com.hg.hollowgoods.UI.Base.Message.Toast.t;
import com.hg.hollowgoods.Util.BeanUtils;
import com.hg.hollowgoods.Util.StringUtils;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Bean.HiddenDanger.ServiceSubmit;
import com.xhtt.hiddendanger.Bean.HiddenDanger.ServiceSubmitStatistics;
import com.xhtt.hiddendanger.Constant.Constants;
import com.xhtt.hiddendanger.Constant.EventActionCode;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 服务提交界面
 * <p>
 * Created by Hollow Goods on 2019-05-22
 */

public class ServiceSubmitActivity extends BaseMVPActivity<ServiceSubmitPresenter> implements ServiceSubmitContract.View {

    private final int DIALOG_CODE_SUBMIT = 1001;
    private final int DIALOG_CODE_ASK = 1002;

    private HGRefreshLayout refreshLayout;
    private TextView hiddenDangerNew;
    private TextView hiddenDangerChanging;
    private TextView hiddenDangerChanged;
    private Button submit;

    private FastAdapter adapter;
    private ArrayList<CommonBean> data = new ArrayList<>();
    private ServiceSubmit parentData = new ServiceSubmit();
    private Company company;
    private int clickPosition;
    private int clickSortNumber;
    private int changeTimeCheckedPosition = -1;
    private int dayCount = 0;
    private long changeTime;
    private int number1 = 0;
    private int number2 = 0;
    private int number3 = 0;

    @Override
    public Activity addToExitGroup() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_service_submit;
    }

    @Override
    public void initParamData() {
        company = baseUI.getParam(ParamKey.ParentData.getValue(), null);
    }

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, company == null ? R.string.title_activity_service_submit : company.getCompanyName());

        refreshLayout = findViewById(R.id.hgRefreshLayout);
        hiddenDangerNew = findViewById(R.id.tv_hiddenDangerNew);
        hiddenDangerChanging = findViewById(R.id.tv_hiddenDangerChanging);
        hiddenDangerChanged = findViewById(R.id.tv_hiddenDangerChanged);
        submit = findViewById(R.id.btn_submit);

        parentData.setCheckDate(getNowDate());
        changeTime = parentData.getCheckDate();

        FastAdapter.setAllItemOnlyRead(parentData, true);
        parentData.addOnlyReadItem("changeDate", false);

        adapter = new FastAdapter(baseUI.getBaseContext(), data, false, true);

        refreshLayout.initRecyclerView();
        refreshLayout.setAdapter(adapter);

        data.addAll(adapter.getDetailItemData(parentData));
        adapter.refreshData(data);

        if (company != null) {
            mPresenter.getData(company.getServiceId());
        }

        return this;
    }

    @Override
    public void setListener() {

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
                if (sortNumber == 20) {
                    clickPosition = position;
                    clickSortNumber = sortNumber;

                    baseUI.baseDialog.showDateTimeDialog(null, changeTime, DateTimeDialogType.YMD, sortNumber);
                } else {
                    onFastItemClick(view, position, sortNumber);
                }
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

        baseUI.baseDialog.setOnDialogClickListener((code, result, bundle) -> {
            if (result) {
                switch (code) {
                    case 20:
                        changeTimeCheckedPosition = bundle.getInt(HGParamKey.Position.getValue(), -1);
                        if (changeTimeCheckedPosition != -1) {
                            long nowDate = getNowDate();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(nowDate);

                            switch (changeTimeCheckedPosition) {
                                case 0:
                                    dayCount = 15;
                                    calendar.add(Calendar.DATE, dayCount);
                                    break;
                                case 1:
                                    calendar.add(Calendar.MONTH, 1);
                                    dayCount = (int) ((calendar.getTimeInMillis() - nowDate) / (24L * 60 * 60 * 1000));
                                    break;
                                case 2:
                                    calendar.add(Calendar.MONTH, 3);
                                    dayCount = (int) ((calendar.getTimeInMillis() - nowDate) / (24L * 60 * 60 * 1000));
                                    break;
                            }
                            changeTime = calendar.getTimeInMillis();
                            parentData.setChangeDate(StringUtils.getDateTimeString(calendar.getTimeInMillis(), StringUtils.DateFormatMode.LINE_YMD));
                        } else {
                            changeTime = bundle.getLong(HGParamKey.DateTimeInMillis.getValue(), 0);
                            long nowDate = getNowDate();

                            if (changeTime > nowDate) {
                                parentData.setChangeDate(StringUtils.getDateTimeString(changeTime, StringUtils.DateFormatMode.LINE_YMD));
                                dayCount = (int) ((changeTime - nowDate) / (24L * 60 * 60 * 1000));
                            } else {
                                t.warning("整改时间必须大于今天");
                            }
                        }
                        break;
                    case DIALOG_CODE_ASK:
                        baseUI.startMyActivity(SignActivity.class);
                        break;
                }

                adapter.refreshFastItem(parentData, clickPosition);
            }
        });

        submit.setOnClickListener(new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (adapter.checkNotEmptyItem(parentData)) {

                    StringBuilder sb = new StringBuilder();
                    sb.append("确定提交吗？");
                    sb.append("<br>");

                    if (number1 == 0 && number2 == 0 && number3 == 0) {
                        sb.append("<br>");
                        sb.append("（1）未排查出新隐患");
                        sb.append("<br>");
                        sb.append("（2）隐患复查整改数量为0 ");
                        sb.append("<br>");
                    }

                    sb.append("<br>");
                    sb.append("<font color=\"#4699F9\">注：登录PC端可导出检查记录表</font>");


                    baseUI.baseDialog.showAlertDialog(R.string.tips_best, sb.toString(), DIALOG_CODE_ASK);
                }
            }
        });
    }

    @Override
    public ServiceSubmitPresenter createPresenter() {
        return new ServiceSubmitPresenter(this);
    }

    @Override
    public void onEventUI(Event event) {
        if (event.getEventActionCode() == EventActionCode.SIGN_BACK) {
            ArrayList<AppFile> appFiles = event.getObj(ParamKey.SignData.getValue(), null);
            if (!BeanUtils.isCollectionEmpty(appFiles)) {
                parentData.setCheckUserNamePhotoList(appFiles.get(0));

                if (appFiles.size() > 1) {
                    parentData.setChargePersonPhotoList(appFiles.get(1));
                }
            }

            doSubmit();
        }
    }

    private void onFastItemClick(View view, int position, int sortNumber) {

        clickPosition = position;
        clickSortNumber = sortNumber;

        switch (sortNumber) {
            case 20:
                // 整改时间
                baseUI.baseDialog.showSingleDialog("请选择整改时间", Constants.CHANGE_TIME_OBJ, changeTimeCheckedPosition, sortNumber);
                break;
        }
    }

    @Override
    public void getDataSuccess(ServiceSubmitStatistics tempData) {
        runOnUiThread(() -> {

            number1 = tempData.getNewHiddenDangerCount();
            hiddenDangerNew.setText(String.valueOf(number1));

            number2 = tempData.getChangingCount();
            hiddenDangerChanging.setText(String.valueOf(number2));

            number3 = tempData.getChangedCount();
            hiddenDangerChanged.setText(String.valueOf(number3));
        });
    }

    @Override
    public void getDataError() {
        hiddenDangerNew.setText("0");
        hiddenDangerChanging.setText("0");
        hiddenDangerChanged.setText("0");
    }

    @Override
    public void getDataFinish() {

    }

    @Override
    public void submitServiceSuccess(long newServiceId) {

        t.success("提交成功");

        Event event = new Event(EventActionCode.SERVICE_SUBMIT);
        event.addObj(ParamKey.BackData.getValue(), newServiceId);
        EventBus.getDefault().post(event);

        finishMyActivity();
    }

    @Override
    public void submitServiceError() {

    }

    @Override
    public void submitServiceFinish() {
        baseUI.baseDialog.closeDialog(DIALOG_CODE_SUBMIT);
    }

    private void doSubmit() {
        baseUI.baseDialog.showProgressDialog("提交中，请稍候……", DIALOG_CODE_SUBMIT);
        mPresenter.submitService(company.getServiceId(), dayCount, parentData);
    }

    private long getNowDate() {

        BaseApplication baseApplication = BaseApplication.create();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(baseApplication.getNowTime());

        Calendar nowDate = Calendar.getInstance();
        nowDate.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE),
                0,
                0,
                0
        );

        return nowDate.getTimeInMillis() / 1000 * 1000;
    }

}
