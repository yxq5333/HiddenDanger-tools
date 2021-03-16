package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.hg.zero.adapter.fast.ZFastAdapter2;
import com.hg.zero.adapter.fast.bean.ZFastItem2;
import com.hg.zero.adapter.fast.callback.ZOnFastItemClickListener2Adapter;
import com.hg.zero.adapter.fast.callback.ZOnSystemProxyEventFinishListenerAdapter;
import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.constant.ZConstants;
import com.hg.zero.constant.ZParamKey;
import com.hg.zero.datetime.ZDateTimeUtils;
import com.hg.zero.dialog.ZChoiceItem;
import com.hg.zero.dialog.ZDialogConfig;
import com.hg.zero.file.ZAppFile;
import com.hg.zero.toast.Zt;
import com.hg.zero.util.ZBeanUtils;
import com.hg.zero.util.ZChoiceItemBuilder;
import com.hg.zero.widget.refreshlayout.ZRefreshLayout;
import com.hg.zero.widget.validatorinput.validator.ZValidatorFactory;
import com.hg.zero.widget.validatorinput.validator.ZValidatorType;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.User;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenDanger;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenDangerChange;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenDangerType;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenLevel;
import com.xhtt.hiddendangermaster.constant.Constants;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.constant.WorkType;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger.FreeTakeActivity;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger.HiddenDangerBaseActivity;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.hiddendanger.HiddenDangerDetailActivity;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPFragment;
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

public class HiddenDangerDetailFragment extends HDBaseMVPFragment<HiddenDangerDetailPresenter> implements HiddenDangerDetailContract.View {

    private final int DIALOG_CODE_SUBMIT = 1000;

    private ZRefreshLayout hiddenDangerRefreshLayout;
    private ZRefreshLayout changeRefreshLayout;
    private View changeView;

    private Company grandData;
    private HiddenDanger parentData;
    private ZFastAdapter2 hiddenDangerAdapter;
    private List<ZFastItem2> hiddenDangerData = new ArrayList<>();
    private ZFastAdapter2 changeAdapter;
    private List<ZFastItem2> changeData = new ArrayList<>();
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

        super.initParamData();

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

        hiddenDangerData.add(new ZFastItem2.Builder(10, ZFastItem2.ITEM_TYPE_DATE)
                .setLabel("检查日期")
                .setContentHint("请选择")
                .setContent(parentData.getCheckDateShow())
                .setDateFormatMode(ZDateTimeUtils.DateFormatMode.LINE_YMD)
                .setNotEmpty(true)
                .setOnlyRead(isOnlyRead)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        hiddenDangerData.add(new ZFastItem2.Builder(20, ZFastItem2.ITEM_TYPE_INPUT)
                .setLabel("隐患地点")
                .setContentHint("请输入")
                .setContent(parentData.getHiddenLocation())
                .buildInputConfig(new ZDialogConfig.InputConfig(ZConstants.DEFAULT_CODE)
                        .setOpenHistory(true)
                        .setHistoryKey("hidden.danger.location")
                )
                .setNotEmpty(false)
                .setOnlyRead(isOnlyRead)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        hiddenDangerData.add(new ZFastItem2.Builder(25, ZFastItem2.ITEM_TYPE_FILE)
                .setLabel("隐患照片")
                .setContentHint("请选择")
                .setAppFiles(parentData.getAppHiddenPhotoList())
                .setMaxCount(SystemConfig.FILE_MAX_COUNT)
                .setNotEmpty(false)
                .setOnlyRead(isOnlyRead)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        hiddenDangerData.add(new ZFastItem2.Builder(30, ZFastItem2.ITEM_TYPE_INPUT)
                .setLabel("隐患描述")
                .setContentHint("请输入或选择")
                .setContent(parentData.getHiddenDescribe())
                .setNotEmpty(true)
                .setOnlyRead(isOnlyRead)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        ZChoiceItem checkedItem = null;
        for (ZChoiceItem t : Constants.HIDDEN_LEVEL_OBJ) {
            if (TextUtils.equals(t.getItem() + "", parentData.getHiddenLevel())) {
                checkedItem = t;
                break;
            }
        }
        hiddenDangerData.add(new ZFastItem2.Builder(40, ZFastItem2.ITEM_TYPE_SINGLE_CHOICE)
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

        hiddenDangerData.add(new ZFastItem2.Builder(200, ZFastItem2.ITEM_TYPE_SINGLE_CHOICE)
                .setLabel("隐患大类")
                .setContentHint("请选择")
                .setSingleCheckedItem(TextUtils.isEmpty(parentData.getTypeFirst()) ? null : new ZChoiceItem(parentData.getTypeFirst()))
                .setNotEmpty(false)
                .setOnlyRead(isOnlyRead)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .setPackageField("dangerLat")
                .build()
        );

        hiddenDangerData.add(new ZFastItem2.Builder(201, ZFastItem2.ITEM_TYPE_SINGLE_CHOICE)
                .setLabel("细分类别")
                .setContentHint("请选择")
                .setSingleCheckedItem(TextUtils.isEmpty(parentData.getTypeSecond()) ? null : new ZChoiceItem(parentData.getTypeSecond()))
                .setNotEmpty(false)
                .setOnlyRead(isOnlyRead)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .setPackageField("categorySub")
                .build()
        );

        hiddenDangerData.add(new ZFastItem2.Builder(50, ZFastItem2.ITEM_TYPE_INPUT)
                .setLabel("参考依据")
                .setContentHint("请输入")
                .setContent(parentData.getReference())
                .setNotEmpty(false)
                .setOnlyRead(isOnlyRead)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        hiddenDangerData.add(new ZFastItem2.Builder(60, ZFastItem2.ITEM_TYPE_INPUT)
                .setLabel("整改措施")
                .setContentHint("请输入")
                .setContent(parentData.getChangeFunction())
                .setNotEmpty(false)
                .setOnlyRead(isOnlyRead)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        hiddenDangerData.add(new ZFastItem2.Builder(202, ZFastItem2.ITEM_TYPE_INPUT)
                .setLabel("整改部门")
                .setContentHint("请输入")
                .setContent(parentData.getChangeDepartment())
                .setNotEmpty(false)
                .setOnlyRead(isOnlyRead)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .setPackageField("departRect")
                .build()
        );

        hiddenDangerData.add(new ZFastItem2.Builder(203, ZFastItem2.ITEM_TYPE_INPUT)
                .setLabel("责任人")
                .setContentHint("请输入")
                .setContent(parentData.getDutyPeople())
                .setNotEmpty(false)
                .setOnlyRead(isOnlyRead)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .setPackageField("personLia")
                .build()
        );

        hiddenDangerAdapter = new ZFastAdapter2(baseUI, hiddenDangerData);
        hiddenDangerRefreshLayout.setAdapter(hiddenDangerAdapter);

//        hiddenDangerAdapter.refreshDataAllData(hiddenDangerData);
        changeData.add(new ZFastItem2.Builder(1, ZFastItem2.ITEM_TYPE_FILE)
                .setLabel("整改后照片")
                .setContentHint("请选择")
                .setAppFiles(parentData.getAppChangePhotoList())
                .setMaxCount(SystemConfig.FILE_MAX_COUNT)
                .setNotEmpty(false)
                .setOnlyRead(workType != WorkType.Change)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        changeData.add(new ZFastItem2.Builder(5, ZFastItem2.ITEM_TYPE_INPUT)
                .setLabel("整改说明")
                .setContentHint("请输入")
                .setContent(parentData.getChangeDescribe())
                .setNotEmpty(false)
                .setOnlyRead(workType != WorkType.Change)
                .setRightIconRes(R.drawable.ic_my_arrow_right)
                .build()
        );

        changeAdapter = new ZFastAdapter2(baseUI, changeData);
        changeRefreshLayout.setAdapter(changeAdapter);

//        changeAdapter.refreshDataAllData(changeData);

        if (workType == WorkType.Add || workType == WorkType.AddFreeTake) {
            mPresenter.getUserData();
        }
    }

    @Override
    public void initViewDelay() {
        mPresenter.getHiddenDangerFirstType();
    }

    @Override
    public void setListener() {

        // 隐患描述
        hiddenDangerAdapter.setOnFastItemClickListener2(30, new ZOnFastItemClickListener2Adapter() {
            @Override
            public boolean onItemClick(int clickItemId) {
                if (!isOnlyRead) {
                    ZDialogConfig.InputConfig configInput = new ZDialogConfig.InputConfig(clickItemId)
                            .setHint("请输入隐患描述")
                            .setContent(parentData.getHiddenDescribe())
                            .setMaxLines(3)
                            .setValidator(ZValidatorFactory.getValidator(ZValidatorType.MIN_LENGTH, "至少输入1个字", 1),
                                    ZValidatorFactory.getValidator(ZValidatorType.MAX_LENGTH, "最多输入500个字", 500)
                            );
                    baseUI.baseDialog.showInputDialog(configInput);
                }

                return true;
            }

            @Override
            public boolean onRightIconClick(int clickItemId) {
                if (!isOnlyRead) {
                    baseUI.startMyActivity(HiddenDangerBaseActivity.class);
                }

                return true;
            }
        });

        hiddenDangerAdapter.addOnSystemProxyEventFinishListener(new ZOnSystemProxyEventFinishListenerAdapter() {
            @Override
            public void onSingleChoiceFinish(int itemId, ZChoiceItem ZChoiceItem) {
                if (itemId == 200) {
                    hiddenDangerAdapter.findItemById(201).setSingleCheckedItem(null)
                            .setContent("")
                            .setChoiceItems(null);
                    hiddenDangerAdapter.processData();

                    mPresenter.getHiddenDangerSecondType(ZChoiceItem.getItem() + "");
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
                        strValue = data.getData(ZParamKey.InputValue, "");
                        change.setChangeDescribe(strValue);
                        break;
                    case 10:
                        // 检查日期
                        longValue = data.getData(ZParamKey.DateTimeInMillis, 0L);
                        parentData.setCheckDateShow(longValue + "");
                        break;
                    case 20:
                        // 隐患地点
                        strValue = data.getData(ZParamKey.InputValue, "");
                        parentData.setHiddenLocation(strValue);
                        break;
                    case 30:
                        // 隐患描述
                        strValue = data.getData(ZParamKey.InputValue, "");
                        parentData.setHiddenDescribe(strValue);
                        break;
                    case 40:
                        // 隐患等级
                        int levelPosition = data.getData(ZParamKey.Position, -1);
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
                        strValue = data.getData(ZParamKey.InputValue, "");
                        parentData.setReference(strValue);
                        break;
                    case 60:
                        // 整改措施
                        strValue = data.getData(ZParamKey.InputValue, "");
                        parentData.setChangeFunction(strValue);
                        break;
                    case DIALOG_CODE_SUBMIT:
                        int position = data.getData(ZParamKey.Position, -1);

                        if (position != -1) {
                            baseUI.baseDialog.showProgressDialog(new ZDialogConfig.ProgressConfig(DIALOG_CODE_SUBMIT)
                                    .setContent("提交中，请稍候……")
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
    public void onEventUI(ZEvent item) {

        if (item.getEventActionCode() == EventActionCode.UPLOAD_PHOTO) {
            if (item.isFromMe(this.getClass().getName())) {
                boolean status = item.getObj(ParamKey.Status, false);

                if (status) {
                    ArrayList<ZAppFile> files = item.getObj(ParamKey.BackData, null);
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

    public ZRefreshLayout getHiddenDangerRefreshLayout() {
        return hiddenDangerRefreshLayout;
    }

    public ZRefreshLayout getChangeRefreshLayout() {
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

        baseUI.baseDialog.showProgressDialog(new ZDialogConfig.ProgressConfig(DIALOG_CODE_SUBMIT)
                .setContent("提交中，请稍候……")
        );

        List<ZAppFile> uploadFiles = new ArrayList<>();
        List<ZAppFile> hiddenPhoto = hiddenDangerAdapter.findItemById(25).getAppFiles();
        List<ZAppFile> changePhoto = changeAdapter.findItemById(1).getAppFiles();

        parentData.setAppHiddenPhotoList(hiddenPhoto);
        parentData.setAppChangePhotoList(changePhoto);
        change.setAppChangePhotoList(changePhoto);

        if (hiddenPhoto != null) {
            for (ZAppFile t : hiddenPhoto) {
                t.setTag(25);
            }

            uploadFiles.addAll(hiddenPhoto);
        }

        if (changePhoto != null) {
            for (ZAppFile t : changePhoto) {
                t.setTag(1);
            }
            uploadFiles.addAll(changePhoto);
        }

        uploadFileUtils.doUpload(this.getClass().getName(), uploadFiles);
    }

    private ArrayList<ZAppFile> cacheFiles = new ArrayList<>();

    private void submitData(ArrayList<ZAppFile> files) {
        if (hiddenDangerAdapter.checkNotEmptyItem()) {
            cacheFiles.clear();
            cacheFiles.addAll(files);

            ArrayList<ZAppFile> hiddenPhotos = new ArrayList<>();
            ArrayList<ZAppFile> changePhotos = new ArrayList<>();

            for (ZAppFile t : files) {
                if ((int) t.getTag() == 25) {
                    hiddenPhotos.add(t);
                } else if ((int) t.getTag() == 1) {
                    changePhotos.add(t);
                }
            }

            parentData.setTypeFirst(hiddenDangerAdapter.findItemById(200).getContent());
            parentData.setTypeSecond(hiddenDangerAdapter.findItemById(201).getContent());
            parentData.setChangeDepartment(hiddenDangerAdapter.findItemById(202).getContent());
            parentData.setDutyPeople(hiddenDangerAdapter.findItemById(203).getContent());

            parentData.setAppHiddenPhotoList(hiddenPhotos);
            parentData.setAppChangePhotoList(changePhotos);
            parentData.setChangeDescribe(change.getChangeDescribe());

            mPresenter.submitData(parentData, grandData, workType, lastServiceId);
        }
    }

    @Override
    public void submitDataSuccess(Long id) {

        baseUI.baseDialog.closeDialog(DIALOG_CODE_SUBMIT);
        Zt.success("提交成功");

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

        ZEvent event = new ZEvent(EventActionCode.HIDDEN_DANGER_SUBMIT);
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

    @Override
    public void getHiddenDangerFirstTypeSuccess(List<HiddenDangerType> tempData) {

        ZFastItem2 item2 = hiddenDangerAdapter.findItemById(200);
        item2.setChoiceItems(ZChoiceItemBuilder.build(tempData));

        if (!ZBeanUtils.isCollectionEmpty(tempData) && item2.getSingleCheckedItem() != null) {
            for (ZChoiceItem t : item2.getChoiceItems()) {
                if (TextUtils.equals(t.getItem() + "", item2.getSingleCheckedItem().getItem() + "")) {
                    item2.setSingleCheckedItem(t);
                    break;
                }
            }

            if (hiddenDangerAdapter.findItemById(201).getSingleCheckedItem() != null) {
                hiddenDangerAdapter.findItemById(201).getSingleCheckedItem().setTag(true);
                mPresenter.getHiddenDangerSecondType(item2.getContent());
            }
        }
    }

    @Override
    public void getHiddenDangerSecondTypeSuccess(List<HiddenDangerType> tempData) {

        ZFastItem2 item2 = hiddenDangerAdapter.findItemById(201);
        item2.setChoiceItems(ZChoiceItemBuilder.build(tempData));

        if (!ZBeanUtils.isCollectionEmpty(tempData) && item2.getSingleCheckedItem() != null && item2.getSingleCheckedItem().getTag() != null) {
            item2.getSingleCheckedItem().setTag(null);

            for (ZChoiceItem t : item2.getChoiceItems()) {
                if (TextUtils.equals(t.getItem() + "", item2.getSingleCheckedItem().getItem() + "")) {
                    item2.setSingleCheckedItem(t);
                    break;
                }
            }
        }
    }

    private void initParentData() {

        if (parentData == null) {
            parentData = new HiddenDanger();
            parentData.setCheckDateShow(System.currentTimeMillis() + "");
        } else {
            parentData = ZBeanUtils.copy(parentData, HiddenDanger.class);

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
//            hiddenDangerAdapter.findItemById(40).setContent(parentData.getHiddenLevel());
            hiddenDangerAdapter.findItemById(50).setContent(parentData.getReference());
            hiddenDangerAdapter.findItemById(60).setContent(parentData.getChangeFunction());
            hiddenDangerAdapter.findItemById(200).setContent(parentData.getTypeFirst());
            hiddenDangerAdapter.findItemById(201).setContent(parentData.getTypeSecond());
            hiddenDangerAdapter.findItemById(202).setContent(parentData.getChangeDepartment());
            hiddenDangerAdapter.findItemById(203).setContent(parentData.getDutyPeople());

            hiddenDangerAdapter.processData();
        }
    }

}
