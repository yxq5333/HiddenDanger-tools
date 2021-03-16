package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.config.ZCommonResource;
import com.hg.zero.constant.ZConstants;
import com.hg.zero.datetime.ZDateTimeUtils;
import com.hg.zero.dialog.ZDialogConfig;
import com.hg.zero.listener.ZOnRecyclerViewItemClickOldListener;
import com.hg.zero.listener.ZOnViewClickListener;
import com.hg.zero.toast.Zt;
import com.hg.zero.widget.refreshlayout.ZRefreshLayout;
import com.hg.zero.widget.statuslayout.ZStatusLayout;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.adapter.hiddendanger.hiddendanger.CheckTableDetailAdapter;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.CheckTable;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.CheckTableContent;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenDanger;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 检查内容界面
 *
 * @author HG
 */

public class CheckTableDetailActivity extends HDBaseMVPActivity<CheckTableDetailPresenter> implements CheckTableDetailContract.View {

    private final int DIALOG_CODE_SUBMIT = 1000;

    private ZRefreshLayout refreshLayout;
    private TextView checkDate;
    private TextView checkTableName;
    private Button submit;
    private Button freeTake;

    private CheckTableDetailAdapter adapter;
    private ArrayList<CheckTableContent> data = new ArrayList<>();
    private Company grandData;
    private CheckTable parentData;
    private int clickPosition;

    @Override
    public Object registerEventBus() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_check_table_detail;
    }

    @Override
    public void initParamData() {

        super.initParamData();
        grandData = baseUI.getParam(ParamKey.GrandData, null);
        parentData = baseUI.getParam(ParamKey.ParentData, null);

        if (grandData == null) {
            grandData = new Company();
        }

        if (parentData == null) {
            parentData = new CheckTable();
        }
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(ZCommonResource.getBackIcon(), R.string.title_activity_check_table_detail);
        baseUI.setStatus(ZStatusLayout.Status.Loading);

        new Handler().postDelayed(() -> {

            refreshLayout = findViewById(R.id.ZRefreshLayout);
            checkDate = findViewById(R.id.tv_checkDate);
            checkTableName = findViewById(R.id.tv_checkTableName);
            submit = findViewById(R.id.btn_submit);
            freeTake = findViewById(R.id.btn_freeTake);

            if (parentData.getStatus() == CheckTable.STATUS_CHECKED) {
                submit.setVisibility(View.GONE);
                freeTake.setVisibility(View.GONE);
            }

            checkDate.setText("检查日期:");
            checkDate.append(TextUtils.isEmpty(parentData.getCheckDate()) ? ZDateTimeUtils.getDateTimeString(System.currentTimeMillis(), ZDateTimeUtils.DateFormatMode.LINE_YMD) : parentData.getCheckDate());
            checkTableName.setText("检查表名称:");
            checkTableName.append(TextUtils.isEmpty(parentData.getCheckTableName()) ? "" : parentData.getCheckTableName());

            refreshLayout.initRecyclerView();
            refreshLayout.setAdapter(adapter = new CheckTableDetailAdapter(baseUI.getBaseContext(), R.layout.item_check_table_detail, data, parentData.getStatus() == CheckTable.STATUS_CHECKED));

            refreshLayout.getRefreshLayout().autoRefresh();
        }, SystemConfig.DELAY_TIME_INIT_VIEW);
    }

    @Override
    public void setListener() {

        new Handler().postDelayed(() -> {

            refreshLayout.getRefreshLayout().setOnRefreshListener(refreshLayout -> doRefresh());

            adapter.setOnStatusChangedListener(new ZOnRecyclerViewItemClickOldListener(false) {
                @Override
                public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {

                    clickPosition = position;

                    if (view.getId() == R.id.rb_yes) {
                        mPresenter.changeContentStatus(parentData.getId(), data.get(clickPosition).getId(), CheckTableContent.STATUS_YES);
                    } else {
                        grandData.setCheckItemId(data.get(clickPosition).getId());

                        mPresenter.changeContentStatus(parentData.getId(), data.get(clickPosition).getId(), CheckTableContent.STATUS_NO);
                        baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                                new Enum[]{ParamKey.WorkType, ParamKey.GrandData, ParamKey.HiddenDangerAddWithOutContinue},
                                new Object[]{WorkType.Add, grandData, true}
                        );
                    }
                }
            });

            submit.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    checkItems();
                }
            });

            freeTake.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {

                    grandData.setCheckItemId(null);

                    baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                            new Enum[]{ParamKey.WorkType, ParamKey.GrandData},
                            new Object[]{WorkType.AddFreeTake, grandData}
                    );
                }
            });

            adapter.setOnItemClickListener(new ZOnRecyclerViewItemClickOldListener(false) {
                @Override
                public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {

                    if (data.get(position).getStatus() != null && data.get(position).getStatus() == CheckTableContent.STATUS_NO) {
                        if (data.get(position).getHiddenDanger() == null) {
                            Zt.info("没有添加隐患");
                        } else {
                            baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                                    new Enum[]{ParamKey.GrandData, ParamKey.WorkType, ParamKey.ParentData},
                                    new Object[]{grandData, WorkType.Detail, data.get(position).getHiddenDanger()}
                            );
                        }
                    }
                }
            });

            baseUI.setStatus(ZStatusLayout.Status.Default);
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Override
    public CheckTableDetailPresenter createPresenter() {
        return new CheckTableDetailPresenter();
    }

    @Override
    public void onEventUI(ZEvent item) {
        if (item.getEventActionCode() == EventActionCode.HIDDEN_DANGER_SUBMIT) {
            HiddenDanger hiddenDanger = item.getObj(ParamKey.Company, null);

            if (hiddenDanger != null) {
                WorkType workType = item.getObj(ParamKey.WorkType, null);

                if (workType != null) {
                    switch (workType) {
                        case Add:
                            data.get(clickPosition).setHiddenDanger(hiddenDanger);
                            break;
                    }
                }
            }
        }
    }

    private void doRefresh() {
        getData();
    }

    private void getData() {
        mPresenter.getData(parentData.getId());
    }

    @Override
    public void getDataSuccess(ArrayList<CheckTableContent> tempData) {

        data.clear();

        if (tempData != null) {
            data.addAll(tempData);
        }

        baseUI.getBaseContext().runOnUiThread(() -> {
            adapter.refreshData(data);
            getDataFinish();
        });
    }

    @Override
    public void getDataError() {

    }

    @Override
    public void getDataFinish() {
        new Handler().postDelayed(() -> refreshLayout.getRefreshLayout().finishRefresh(), SystemConfig.DELAY_TIME_REFRESH_DATA);
    }

    @Override
    public void changeContentStatusSuccess() {
        ZEvent event = new ZEvent(EventActionCode.CHANGE_CHECK_TABLE_CONTENT_STATUS);
        EventBus.getDefault().post(event);
    }

    @Override
    public void submitDataSuccess() {

        Zt.success("保存成功");

        ZEvent event = new ZEvent(EventActionCode.CHECK_TABLE_SUBMIT);
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

    private void checkItems() {

        StringBuilder sb = new StringBuilder();
        int i = 1;

        for (CheckTableContent t : data) {
            if (t.getStatus() == null) {
                if (i != 1) {
                    sb.append("\n");
                }

                sb.append("第");
                sb.append(i);
                sb.append("项");
                sb.append(" ");
                sb.append("未检查");
            }

            i++;
        }

        if (TextUtils.isEmpty(sb)) {
            baseUI.baseDialog.showProgressDialog(new ZDialogConfig.ProgressConfig(DIALOG_CODE_SUBMIT)
                    .setContent("保存中，请稍候……")
            );
            mPresenter.submitData(parentData.getId());
        } else {
            sb.append("\n\n");
            sb.append("请全部检查完成后再提交");

            baseUI.baseDialog.showWarningDialog(new ZDialogConfig.WarningConfig(ZConstants.DEFAULT_CODE)
                    .setTitle(R.string.z_tips_best)
                    .setContent(sb.toString())
                    .setPositiveButtonTxt("知道了")
            );
        }
    }
}
