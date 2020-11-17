package com.xhtt.hiddendanger.UI.Activity.HiddenDanger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.Bean.EventBus.Event;
import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.Constant.HGConstants;
import com.hg.hollowgoods.UI.Base.BaseMVPActivity;
import com.hg.hollowgoods.UI.Base.Click.OnRecyclerViewItemClickListener;
import com.hg.hollowgoods.UI.Base.Click.OnViewClickListener;
import com.hg.hollowgoods.UI.Base.Message.Toast.t;
import com.hg.hollowgoods.Util.StringUtils;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.xhtt.hiddendanger.Adapter.HiddenDanger.CheckTableDetailAdapter;
import com.xhtt.hiddendanger.Bean.HiddenDanger.CheckTable;
import com.xhtt.hiddendanger.Bean.HiddenDanger.CheckTableContent;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Bean.HiddenDanger.HiddenDanger;
import com.xhtt.hiddendanger.Constant.EventActionCode;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.Constant.SystemConfig;
import com.xhtt.hiddendanger.Constant.WorkType;
import com.xhtt.hiddendanger.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 检查内容界面
 *
 * @author HG
 */

public class CheckTableDetailActivity extends BaseMVPActivity<CheckTableDetailPresenter> implements CheckTableDetailContract.View {

    private final int DIALOG_CODE_SUBMIT = 1000;

    private HGRefreshLayout refreshLayout;
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
    public Activity addToExitGroup() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_check_table_detail;
    }

    @Override
    public void initParamData() {

        grandData = baseUI.getParam(ParamKey.GrandData.getValue(), null);
        parentData = baseUI.getParam(ParamKey.ParentData.getValue(), null);

        if (grandData == null) {
            grandData = new Company();
        }

        if (parentData == null) {
            parentData = new CheckTable();
        }
    }

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, R.string.title_activity_check_table_detail);
        baseUI.setDataMode(baseUI.DATA_MODE_LOAD_DATA_CENTER);

        new Handler().postDelayed(() -> {

            refreshLayout = findViewById(R.id.hgRefreshLayout);
            checkDate = findViewById(R.id.tv_checkDate);
            checkTableName = findViewById(R.id.tv_checkTableName);
            submit = findViewById(R.id.btn_submit);
            freeTake = findViewById(R.id.btn_freeTake);

            if (parentData.getStatus() == CheckTable.STATUS_CHECKED) {
                submit.setVisibility(View.GONE);
                freeTake.setVisibility(View.GONE);
            }

            checkDate.setText("检查日期:");
            checkDate.append(TextUtils.isEmpty(parentData.getCheckDate()) ? StringUtils.getDateTimeString(System.currentTimeMillis(), StringUtils.DateFormatMode.LINE_YMD) : parentData.getCheckDate());
            checkTableName.setText("检查表名称:");
            checkTableName.append(TextUtils.isEmpty(parentData.getCheckTableName()) ? "" : parentData.getCheckTableName());

            refreshLayout.initRecyclerView();
            refreshLayout.setAdapter(adapter = new CheckTableDetailAdapter(baseUI.getBaseContext(), R.layout.item_check_table_detail, data, parentData.getStatus() == CheckTable.STATUS_CHECKED));

            refreshLayout.getRefreshLayout().autoRefresh();
        }, SystemConfig.DELAY_TIME_INIT_VIEW);

        return this;
    }

    @Override
    public void setListener() {

        new Handler().postDelayed(() -> {

            refreshLayout.getRefreshLayout().setOnRefreshListener(refreshLayout -> doRefresh());

            adapter.setOnStatusChangedListener(new OnRecyclerViewItemClickListener(false) {
                @Override
                public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {

                    clickPosition = position;

                    if (view.getId() == R.id.rb_yes) {
                        mPresenter.changeContentStatus(parentData.getId(), data.get(clickPosition).getId(), CheckTableContent.STATUS_YES);
                    } else {
                        grandData.setCheckItemId(data.get(clickPosition).getId());

                        mPresenter.changeContentStatus(parentData.getId(), data.get(clickPosition).getId(), CheckTableContent.STATUS_NO);
                        baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                                new String[]{ParamKey.WorkType.getValue(), ParamKey.GrandData.getValue(), ParamKey.HiddenDangerAddWithOutContinue.getValue()},
                                new Object[]{WorkType.Add, grandData, true}
                        );
                    }
                }
            });

            submit.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    checkItems();
                }
            });

            freeTake.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                            new String[]{ParamKey.WorkType.getValue(), ParamKey.GrandData.getValue()},
                            new Object[]{WorkType.AddFreeTake, grandData}
                    );
                }
            });

            adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener(false) {
                @Override
                public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {

                    if (data.get(position).getStatus() != null && data.get(position).getStatus() == CheckTableContent.STATUS_NO) {
                        if (data.get(position).getHiddenDanger() == null) {
                            t.info("没有添加隐患");
                        } else {
                            baseUI.startMyActivity(HiddenDangerDetailActivity.class,
                                    new String[]{ParamKey.GrandData.getValue(), ParamKey.WorkType.getValue(), ParamKey.ParentData.getValue()},
                                    new Object[]{grandData, WorkType.Detail, data.get(position).getHiddenDanger()}
                            );
                        }
                    }
                }
            });

            baseUI.setDataMode(baseUI.DATA_MODE_HAS_DATA);
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Override
    public CheckTableDetailPresenter createPresenter() {
        return new CheckTableDetailPresenter();
    }

    @Override
    public void onEventUI(Event item) {

        switch (item.getEventActionCode()) {
            case EventActionCode.HIDDEN_DANGER_SUBMIT:
                HiddenDanger hiddenDanger = item.getObj(ParamKey.Company.getValue(), null);

                if (hiddenDanger != null) {
                    WorkType workType = item.getObj(ParamKey.WorkType.getValue(), null);

                    if (workType != null) {
                        switch (workType) {
                            case Add:
                                data.get(clickPosition).setHiddenDanger(hiddenDanger);
                                break;
                        }
                    }
                }
                break;
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
        Event event = new Event(EventActionCode.CHANGE_CHECK_TABLE_CONTENT_STATUS);
        EventBus.getDefault().post(event);
    }

    @Override
    public void submitDataSuccess() {

        t.success("保存成功");

        Event event = new Event(EventActionCode.CHECK_TABLE_SUBMIT);
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
            baseUI.baseDialog.showProgressDialog("保存中，请稍候……", DIALOG_CODE_SUBMIT);
            mPresenter.submitData(parentData.getId());
        } else {
            sb.append("\n\n");
            sb.append("请全部检查完成后再提交");

            baseUI.baseDialog.showWarningDialog(R.string.tips_best, sb.toString(), "知道了", true, HGConstants.DEFAULT_CODE);
        }
    }
}
