package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.hg.hollowgoods.adapter.fast.HGFastAdapter2;
import com.hg.hollowgoods.adapter.fast.bean.HGFastItem2;
import com.hg.hollowgoods.adapter.fast.callback.OnHGFastItemClickListener2Adapter;
import com.hg.hollowgoods.bean.eventbus.HGEvent;
import com.hg.hollowgoods.bean.file.AppFile;
import com.hg.hollowgoods.constant.HGParamKey;
import com.hg.hollowgoods.ui.base.message.dialog2.ChoiceItem;
import com.hg.hollowgoods.ui.base.message.dialog2.DialogConfig;
import com.hg.hollowgoods.ui.base.message.toast.t;
import com.hg.hollowgoods.ui.base.mvp.BaseMVPFragment;
import com.hg.hollowgoods.util.BeanUtils;
import com.hg.hollowgoods.util.StringUtils;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.hg.hollowgoods.widget.validatorinput.validator.ValidatorFactory;
import com.hg.hollowgoods.widget.validatorinput.validator.ValidatorType;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.User;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenDanger;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenDangerChange;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenLevel;
import com.xhtt.hiddendangermaster.constant.Constants;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger.FreeTakeActivity;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger.HiddenDangerBaseActivity;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger.HiddenDangerDetailActivity;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.OnFragmentWorkListener;
import com.xhtt.hiddendangermaster.util.uploadfile.UploadFileUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 隐患排查界面
 *
 * @author HG
 */

public class HiddenDangerDetailFragment extends BaseMVPFragment<HiddenDangerDetailPresenter> implements HiddenDangerDetailContract.View {

    private final int DIALOG_CODE_SUBMIT = 1000;

    private HGRefreshLayout hiddenDangerRefreshLayout;
    private HGRefreshLayout changeRefreshLayout;
    private View changeView;

    private Company grandData;
    private HiddenDanger parentData;
    private HGFastAdapter2 hiddenDangerAdapter;
    private List<HGFastItem2> hiddenDangerData = new ArrayList<>();
    private HGFastAdapter2 changeAdapter;
    private List<HGFastItem2> changeData = new ArrayList<>();
    private WorkType workType;
    private boolean isOnlyRead = true;
    private String title = "";
    private UploadFileUtils uploadFileUtils = null;
    private HiddenLevel hiddenLevelChecked = null;
    private OnFragmentWorkListener onFragmentWorkListener;
    private HiddenDangerChange change;
    private long lastServiceId;
    private User user;

    @Override
    public Object registerEventBus() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_hidden_danger_detail;
    }

    @Override
    public void initParamData() {

        hiddenDangerRefreshLayout = baseUI.findViewById(R.id.hiddenDangerHGRefreshLayout);
        changeRefreshLayout = baseUI.findViewById(R.id.changeHGRefreshLayout);
        changeView = baseUI.findViewById(R.id.cv_changeView);

        workType = baseUI.getParam(ParamKey.WorkType, null);
        parentData = baseUI.getParam(ParamKey.ParentData, null);
        grandData = baseUI.getParam(ParamKey.GrandData, null);
        lastServiceId = baseUI.getParam(ParamKey.LastServiceId, 0L);

        if (workType == null) {
            workType = WorkType.Detail;
        }

        initParentData();

        boolean isShowChangePhoto;
        switch (workType) {
            case Add:
                title = "添加隐患";
                isOnlyRead = false;
                isShowChangePhoto = false;
                addDefaultHiddenLevel();
                break;
            case AddFreeTake:
                title = "随拍随记";
                isOnlyRead = false;
                isShowChangePhoto = false;
                addDefaultHiddenLevel();
                break;
            case Edit:
                title = "编辑隐患";
                isOnlyRead = false;
                isShowChangePhoto = false;
                break;
            case Change:
                title = "整改复查";
                isOnlyRead = true;
                isShowChangePhoto = true;
                break;
            default:
                title = "隐患排查";
                isOnlyRead = true;
                isShowChangePhoto = true;
                break;
        }

        changeView.setVisibility(isShowChangePhoto ? View.VISIBLE : View.GONE);

        initViewMode();
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleViewVisibility(false);

        uploadFileUtils = new UploadFileUtils(baseUI.getBaseContext());

        hiddenDangerRefreshLayout.initRecyclerView();
        hiddenDangerRefreshLayout.getRecyclerView().setNestedScrollingEnabled(false);
        changeRefreshLayout.initRecyclerView();
        changeRefreshLayout.getRecyclerView().setNestedScrollingEnabled(false);

        hiddenDangerData.add(new HGFastItem2.Builder(10, HGFastItem2.ITEM_TYPE_DATE)
                .setLabel("检查日期")
                .setContentHint("请选择")
                .setContent(parentData.getCheckDateShow())
                .setDateFormatMode(StringUtils.DateFormatMode.LINE_YMD)
                .setNotEmpty(true)
                .setOnlyRead(isOnlyRead)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        hiddenDangerData.add(new HGFastItem2.Builder(20, HGFastItem2.ITEM_TYPE_INPUT)
                .setLabel("隐患地点")
                .setContentHint("请输入")
                .setContent(parentData.getHiddenLocation())
                .setNotEmpty(false)
                .setOnlyRead(isOnlyRead)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        hiddenDangerData.add(new HGFastItem2.Builder(25, HGFastItem2.ITEM_TYPE_FILE)
                .setLabel("隐患照片")
                .setContentHint("请选择")
                .setAppFiles(parentData.getAppHiddenPhotoList())
                .setMaxCount(SystemConfig.FILE_MAX_COUNT)
                .setNotEmpty(false)
                .setOnlyRead(isOnlyRead)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        hiddenDangerData.add(new HGFastItem2.Builder(30, HGFastItem2.ITEM_TYPE_INPUT)
                .setLabel("隐患描述")
                .setContentHint("请输入或选择")
                .setContent(parentData.getHiddenDescribe())
                .setNotEmpty(true)
                .setOnlyRead(isOnlyRead)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        ChoiceItem checkedItem = null;
        for (ChoiceItem t : Constants.HIDDEN_LEVEL_OBJ) {
            if (TextUtils.equals(t.getItem() + "", parentData.getHiddenLevel())) {
                checkedItem = t;
                break;
            }
        }
        hiddenDangerData.add(new HGFastItem2.Builder(40, HGFastItem2.ITEM_TYPE_SINGLE_CHOICE)
                .setLabel("隐患等级")
                .setContentHint("请选择")
                .setContent(parentData.getHiddenLevel())
                .setSingleCheckedItem(checkedItem)
                .setChoiceItems(Constants.HIDDEN_LEVEL_OBJ)
                .setNotEmpty(false)
                .setOnlyRead(isOnlyRead)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        hiddenDangerData.add(new HGFastItem2.Builder(50, HGFastItem2.ITEM_TYPE_INPUT)
                .setLabel("参考依据")
                .setContentHint("请输入")
                .setContent(parentData.getReference())
                .setNotEmpty(false)
                .setOnlyRead(isOnlyRead)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        hiddenDangerData.add(new HGFastItem2.Builder(60, HGFastItem2.ITEM_TYPE_INPUT)
                .setLabel("整改措施")
                .setContentHint("请输入")
                .setContent(parentData.getChangeFunction())
                .setNotEmpty(false)
                .setOnlyRead(isOnlyRead)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        hiddenDangerAdapter = new HGFastAdapter2(baseUI, hiddenDangerData);
        hiddenDangerRefreshLayout.setAdapter(hiddenDangerAdapter);

//        hiddenDangerAdapter.refreshDataAllData(hiddenDangerData);
        changeData.add(new HGFastItem2.Builder(1, HGFastItem2.ITEM_TYPE_FILE)
                .setLabel("整改后照片")
                .setContentHint("请选择")
                .setAppFiles(parentData.getAppChangePhotoList())
                .setMaxCount(SystemConfig.FILE_MAX_COUNT)
                .setNotEmpty(false)
                .setOnlyRead(workType != WorkType.Change)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        changeData.add(new HGFastItem2.Builder(5, HGFastItem2.ITEM_TYPE_INPUT)
                .setLabel("整改说明")
                .setContentHint("请输入")
                .setContent(parentData.getChangeDescribe())
                .setNotEmpty(false)
                .setOnlyRead(workType != WorkType.Change)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        changeAdapter = new HGFastAdapter2(baseUI, changeData);
        changeRefreshLayout.setAdapter(changeAdapter);

//        changeAdapter.refreshDataAllData(changeData);

        if (workType == WorkType.Add || workType == WorkType.AddFreeTake) {
            mPresenter.getUserData();
        }
    }

    @Override
    public void setListener() {

        // 隐患描述
        hiddenDangerAdapter.setOnHGFastItemClickListener2(30, new OnHGFastItemClickListener2Adapter() {
            @Override
            public void onItemClick(int clickItemId) {
                if (!isOnlyRead) {
                    DialogConfig.InputConfig configInput = new DialogConfig.InputConfig(clickItemId)
                            .setHint("请输入隐患描述")
                            .setText(parentData.getHiddenDescribe())
                            .setMaxLines(3)
                            .setValidator(ValidatorFactory.getValidator(ValidatorType.MIN_LENGTH, "至少输入1个字", 1),
                                    ValidatorFactory.getValidator(ValidatorType.MAX_LENGTH, "最多输入500个字", 500)
                            );
                    baseUI.baseDialog.showInputDialog(configInput);
                }
            }

            @Override
            public void onRightIconClick(int clickItemId) {
                if (!isOnlyRead) {
                    baseUI.startMyActivity(HiddenDangerBaseActivity.class);
                }
            }
        });

        baseUI.baseDialog.addOnDialogClickListener((code, result, data) -> {

            if (result) {
                String strValue;
                long longValue;

                switch (code) {
                    case 5:
                        // 整改说明
                        strValue = data.getString(HGParamKey.InputValue.toString(), "");
                        change.setChangeDescribe(strValue);
                        break;
                    case 10:
                        // 检查日期
                        longValue = data.getLong(HGParamKey.DateTimeInMillis.toString(), 0);
                        parentData.setCheckDateShow(longValue + "");
                        break;
                    case 20:
                        // 隐患地点
                        strValue = data.getString(HGParamKey.InputValue.toString(), "");
                        parentData.setHiddenLocation(strValue);
                        break;
                    case 30:
                        // 隐患描述
                        strValue = data.getString(HGParamKey.InputValue.toString(), "");
                        parentData.setHiddenDescribe(strValue);
                        break;
                    case 40:
                        // 隐患等级
                        int levelPosition = data.getInt(HGParamKey.Position.toString(), -1);
                        if (levelPosition != -1) {
                            hiddenLevelChecked = Constants.HIDDEN_LEVEL.get(levelPosition);
                            parentData.setHiddenLevel(hiddenLevelChecked.getLevelName());
                            parentData.setLevel(hiddenLevelChecked.getLevelId());
                        } else {
                            parentData.setHiddenLevel("");
                            parentData.setLevel(null);
                        }
                        break;
                    case 50:
                        // 参考依据
                        strValue = data.getString(HGParamKey.InputValue.toString(), "");
                        parentData.setReference(strValue);
                        break;
                    case 60:
                        // 整改措施
                        strValue = data.getString(HGParamKey.InputValue.toString(), "");
                        parentData.setChangeFunction(strValue);
                        break;
                    case DIALOG_CODE_SUBMIT:
                        int position = data.getInt(HGParamKey.Position.toString(), -1);

                        if (position != -1) {
                            baseUI.baseDialog.showProgressDialog(new DialogConfig.ProgressConfig(DIALOG_CODE_SUBMIT)
                                    .setText("提交中，请稍候……")
                            );

                            if (position == 0) {
                                submitFile(parentData.getStatus());
                            } else {
                                submitData(cacheFiles);
                            }
                        }
                        break;
                }
            }
        });

        uploadFileUtils.setOnUploadProgressListener((total, current) -> {

        });

        if (onFragmentWorkListener != null) {
            onFragmentWorkListener.onFragmentWork();
        }
    }

    @Override
    public HiddenDangerDetailPresenter createPresenter() {
        return new HiddenDangerDetailPresenter();
    }

    @Override
    public void onEventUI(HGEvent item) {

        if (item.getEventActionCode() == EventActionCode.UPLOAD_PHOTO) {
            if (item.isFromMe(this.getClass().getName())) {
                boolean status = item.getObj(ParamKey.Status, false);

                if (status) {
                    ArrayList<AppFile> files = item.getObj(ParamKey.BackData, null);
                    submitData(files);
                }
            }
        } else if (item.getEventActionCode() == EventActionCode.SELECTOR_HIDDEN_DANGER_STORE) {
            HiddenDanger hiddenDanger = item.getObj(ParamKey.BackData, null);
            if (hiddenDanger != null) {
                parentData.setHiddenDescribe(hiddenDanger.getHiddenDescribe());
                parentData.setReference(hiddenDanger.getReference());
                parentData.setChangeFunction(hiddenDanger.getChangeFunction());

                hiddenDangerAdapter.findItemById(30).setContent(parentData.getHiddenDescribe());
                hiddenDangerAdapter.findItemById(50).setContent(parentData.getReference());
                hiddenDangerAdapter.findItemById(60).setContent(parentData.getChangeFunction());

                hiddenDangerAdapter.processData();
            }
        }

        hiddenDangerAdapter.onEventUI(item);
        changeAdapter.onEventUI(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        hiddenDangerAdapter.onActivityResult(requestCode, resultCode, data);
        changeAdapter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public HGRefreshLayout getHiddenDangerRefreshLayout() {
        return hiddenDangerRefreshLayout;
    }

    public HGRefreshLayout getChangeRefreshLayout() {
        return changeRefreshLayout;
    }

    public void setOnFragmentWorkListener(OnFragmentWorkListener onFragmentWorkListener) {
        this.onFragmentWorkListener = onFragmentWorkListener;
    }

    public void submitFile(long companyId, String companyName, Long serviceId) {
        grandData.setId(companyId);
        grandData.setCompanyName(companyName);
        grandData.setServiceId(serviceId);
        submitFile(null);
    }

    public void submitFile(Integer status) {
        if (hiddenDangerAdapter.checkNotEmptyItem()) {
            parentData.setStatus(status);
            submit();
        }
    }

    private void submit() {

        baseUI.baseDialog.showProgressDialog(new DialogConfig.ProgressConfig(DIALOG_CODE_SUBMIT)
                .setText("提交中，请稍候……")
        );

        List<AppFile> uploadFiles = new ArrayList<>();
        List<AppFile> hiddenPhoto = hiddenDangerAdapter.findItemById(25).getAppFiles();
        List<AppFile> changePhoto = changeAdapter.findItemById(1).getAppFiles();

        parentData.setAppHiddenPhotoList(hiddenPhoto);
        parentData.setAppChangePhotoList(changePhoto);
        change.setAppChangePhotoList(changePhoto);

        if (hiddenPhoto != null) {
            for (AppFile t : hiddenPhoto) {
                t.setTag(25);
            }

            uploadFiles.addAll(hiddenPhoto);
        }

        if (changePhoto != null) {
            for (AppFile t : changePhoto) {
                t.setTag(1);
            }
            uploadFiles.addAll(changePhoto);
        }

        uploadFileUtils.doUpload(this.getClass().getName(), uploadFiles);
    }

    private ArrayList<AppFile> cacheFiles = new ArrayList<>();

    private void submitData(ArrayList<AppFile> files) {
        if (hiddenDangerAdapter.checkNotEmptyItem()) {
            cacheFiles.clear();
            cacheFiles.addAll(files);

            ArrayList<AppFile> hiddenPhotos = new ArrayList<>();
            ArrayList<AppFile> changePhotos = new ArrayList<>();

            for (AppFile t : files) {
                if ((int) t.getTag() == 25) {
                    hiddenPhotos.add(t);
                } else if ((int) t.getTag() == 1) {
                    changePhotos.add(t);
                }
            }

            parentData.setAppHiddenPhotoList(hiddenPhotos);
            parentData.setAppChangePhotoList(changePhotos);
            parentData.setChangeDescribe(change.getChangeDescribe());

            mPresenter.submitData(parentData, grandData, workType, lastServiceId);
        }
    }

    @Override
    public void submitDataSuccess(Long id) {

        baseUI.baseDialog.closeDialog(DIALOG_CODE_SUBMIT);
        t.success("提交成功");

        if (id != null) {
            parentData.setId(id);
        }

        if (parentData.getTimes() == 0) {
            parentData.setTimes(grandData.getTimes());
            parentData.setServiceId(grandData.getServiceId());
        }
        parentData.setChangePhotoList(change.getChangePhotoList());
        parentData.setChangeDescribe(change.getChangeDescribe());
        parentData.setAppChangePhotoList(change.getAppChangePhotoList());

        HGEvent event = new HGEvent(EventActionCode.HIDDEN_DANGER_SUBMIT);
        event.addObj(ParamKey.Company, parentData);
        event.addObj(ParamKey.WorkType, workType);
        EventBus.getDefault().post(event);

        if (getActivity() instanceof FreeTakeActivity) {
            if (((FreeTakeActivity) getActivity()).action == FreeTakeActivity.ACTION_SUBMIT_ONLY) {
                finishMyActivity();
            } else {
                parentData = null;
                initParentData();
                initViewMode();
                addDefaultHiddenLevel();

                hiddenDangerAdapter.processData();
                changeAdapter.processData();
            }
        } else if (getActivity() instanceof HiddenDangerDetailActivity) {
            if (((HiddenDangerDetailActivity) getActivity()).action == HiddenDangerDetailActivity.ACTION_SUBMIT_ONLY) {
                finishMyActivity();
            } else {
                parentData = null;
                initParentData();
                initViewMode();
                addDefaultHiddenLevel();

                hiddenDangerAdapter.processData();
                changeAdapter.processData();
            }
        } else {
            finishMyActivity();
        }
    }


    @Override
    public void submitDataError() {
        baseUI.baseDialog.closeDialog(DIALOG_CODE_SUBMIT);
    }

    @Override
    public void submitDataFinish() {

    }

    /**
     * 添加默认隐患等级
     */
    private void addDefaultHiddenLevel() {
        for (HiddenLevel t : Constants.HIDDEN_LEVEL) {
            if (t.getLevelName().equals("一般隐患")) {
                hiddenLevelChecked = t;
                parentData.setLevel(t.getLevelId());
                parentData.setHiddenLevel(t.getLevelName());
                break;
            }
        }
    }

    @Override
    public void getUserDataSuccess(User user) {
        this.user = user;
        parentData.setCheckPeople(user == null ? "" : user.getName());
    }

    private void initParentData() {

        if (parentData == null) {
            parentData = new HiddenDanger();
            parentData.setCheckDateShow(System.currentTimeMillis() + "");
        } else {
            parentData = BeanUtils.copy(parentData, HiddenDanger.class);

            if (TextUtils.isEmpty(parentData.getCheckDateShow())) {
                parentData.setCheckDateShow(System.currentTimeMillis() + "");
            }

            for (HiddenLevel t : Constants.HIDDEN_LEVEL) {
                if (parentData.getLevel() != null && t.getLevelId() == parentData.getLevel()) {
                    hiddenLevelChecked = t;
                    break;
                }
            }
        }

        parentData.setCheckPeople(user == null ? "" : user.getName());

        if (grandData == null) {
            grandData = new Company();
        }

        change = new HiddenDangerChange();
        change.setChangePhotoList(parentData.getChangePhotoList());
        change.setChangeDescribe(parentData.getChangeDescribe());
        change.setAppChangePhotoList(parentData.getAppChangePhotoList());
    }

    private void initViewMode() {
        if (hiddenDangerData.size() > 0) {
            hiddenDangerAdapter.findItemById(10).setContent(parentData.getCheckDateShow());
            hiddenDangerAdapter.findItemById(20).setContent(parentData.getHiddenLocation());
            hiddenDangerAdapter.findItemById(25).setAppFiles(parentData.getAppHiddenPhotoList());
            hiddenDangerAdapter.findItemById(30).setContent(parentData.getHiddenDescribe());
            hiddenDangerAdapter.findItemById(40).setContent(parentData.getHiddenLevel());
            hiddenDangerAdapter.findItemById(50).setContent(parentData.getReference());
            hiddenDangerAdapter.findItemById(60).setContent(parentData.getChangeFunction());

            hiddenDangerAdapter.processData();
        }
    }

}
