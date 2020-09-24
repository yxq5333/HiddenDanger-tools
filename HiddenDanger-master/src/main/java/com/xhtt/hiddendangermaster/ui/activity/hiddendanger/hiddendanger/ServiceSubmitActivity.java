package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hg.hollowgoods.adapter.fast.HGFastAdapter2;
import com.hg.hollowgoods.adapter.fast.bean.HGFastItem2;
import com.hg.hollowgoods.adapter.fast.callback.OnSystemProxyEventFinishListenerAdapter;
import com.hg.hollowgoods.application.BaseApplication;
import com.hg.hollowgoods.bean.eventbus.HGEvent;
import com.hg.hollowgoods.bean.file.AppFile;
import com.hg.hollowgoods.constant.HGCommonResource;
import com.hg.hollowgoods.ui.base.click.OnViewClickListener;
import com.hg.hollowgoods.ui.base.message.dialog2.ChoiceItem;
import com.hg.hollowgoods.ui.base.message.dialog2.DialogConfig;
import com.hg.hollowgoods.ui.base.message.toast.t;
import com.hg.hollowgoods.ui.base.mvp.BaseMVPActivity;
import com.hg.hollowgoods.util.BeanUtils;
import com.hg.hollowgoods.util.StringUtils;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.ServiceSubmit;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.ServiceSubmitStatistics;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.ParamKey;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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

    private HGFastAdapter2 adapter;
    private List<HGFastItem2> data = new ArrayList<>();
    private ServiceSubmit parentData = new ServiceSubmit();
    private Company company;
    private int changeTimeCheckedPosition = -1;
    private int dayCount = 0;
    private long changeTime;
    private int number1 = 0;
    private int number2 = 0;
    private int number3 = 0;

    @Override
    public Object registerEventBus() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_service_submit;
    }

    @Override
    public void initParamData() {
        company = baseUI.getParam(ParamKey.ParentData, null);
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, company == null ? R.string.title_activity_service_submit : company.getCompanyName());

        refreshLayout = findViewById(R.id.hgRefreshLayout);
        hiddenDangerNew = findViewById(R.id.tv_hiddenDangerNew);
        hiddenDangerChanging = findViewById(R.id.tv_hiddenDangerChanging);
        hiddenDangerChanged = findViewById(R.id.tv_hiddenDangerChanged);
        submit = findViewById(R.id.btn_submit);

        parentData.setCheckDate(getNowDate());
        changeTime = parentData.getCheckDate();

        data.add(new HGFastItem2.Builder(10, HGFastItem2.ITEM_TYPE_DATE)
                .setLabel("检查时间")
                .setContentHint("请选择")
                .setContent(parentData.getCheckDate() + "")
                .setOnlyRead(true)
                .setNotEmpty(true)
                .build()
        );

        List<ChoiceItem> choiceItems = new CopyOnWriteArrayList<>();
        ChoiceItem choiceItem = new ChoiceItem("半个月");
        choiceItems.add(choiceItem);
        choiceItem = new ChoiceItem("1个月");
        choiceItems.add(choiceItem);
        choiceItem = new ChoiceItem("3个月");
        choiceItems.add(choiceItem);
        data.add(new HGFastItem2.Builder(20, HGFastItem2.ITEM_TYPE_SINGLE_CHOICE)
                .setLabel("整改时间")
                .setContentHint("请选择")
                .setChoiceItems(choiceItems)
                .setOnlyRead(false)
                .setNotEmpty(true)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        adapter = new HGFastAdapter2(baseUI, data);
        refreshLayout.initRecyclerView();
        refreshLayout.setAdapter(adapter);

//        adapter.refreshDataAllData(data);

        if (company != null) {
            mPresenter.getData(company.getServiceId());
        }
    }

    @Override
    public void setListener() {

        adapter.addOnSystemProxyEventFinishListener(new OnSystemProxyEventFinishListenerAdapter() {
            @Override
            public void onSingleChoiceFinish(int itemId, ChoiceItem choiceItem) {
                if (itemId == 20) {
                    HGFastItem2 item2 = adapter.findItemById(itemId);
                    changeTimeCheckedPosition = item2.getChoiceItems().indexOf(choiceItem);

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
                    }
                }
            }
        });

        baseUI.baseDialog.addOnDialogClickListener((code, result, bundle) -> {
            if (result) {
                switch (code) {
                    case DIALOG_CODE_ASK:
                        baseUI.startMyActivity(SignActivity.class);
                        break;
                }

                adapter.processData();
            }
        });

        submit.setOnClickListener(new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (adapter.checkNotEmptyItem()) {

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


                    baseUI.baseDialog.showAlertDialog(new DialogConfig.AlertConfig(DIALOG_CODE_ASK)
                            .setTitle(R.string.tips_best)
                            .setText(sb.toString())
                    );
                }
            }
        });
    }

    @Override
    public ServiceSubmitPresenter createPresenter() {
        return new ServiceSubmitPresenter(this);
    }

    @Override
    public void onEventUI(HGEvent event) {
        if (event.getEventActionCode() == EventActionCode.SIGN_BACK) {
            ArrayList<AppFile> appFiles = event.getObj(ParamKey.SignData, null);
            if (!BeanUtils.isCollectionEmpty(appFiles)) {
                parentData.setCheckUserNamePhotoList(appFiles.get(0));

                if (appFiles.size() > 1) {
                    parentData.setChargePersonPhotoList(appFiles.get(1));
                }
            }

            doSubmit();
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

        HGEvent event = new HGEvent(EventActionCode.SERVICE_SUBMIT);
        event.addObj(ParamKey.BackData, newServiceId);
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
        baseUI.baseDialog.showProgressDialog(new DialogConfig.ProgressConfig(DIALOG_CODE_SUBMIT)
                .setText("提交中，请稍候……")
        );
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
