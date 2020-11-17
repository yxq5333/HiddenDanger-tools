package com.xhtt.hiddendanger.UI.Fragment.HiddenDanger;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.Adapter.FastAdapter.Bean.FastItemData;
import com.hg.hollowgoods.Adapter.FastAdapter.CallBack.OnFastClick;
import com.hg.hollowgoods.Adapter.FastAdapter.FastAdapter;
import com.hg.hollowgoods.Bean.AppFile;
import com.hg.hollowgoods.Bean.CommonBean.CommonBean;
import com.hg.hollowgoods.Bean.EventBus.Event;
import com.hg.hollowgoods.Bean.EventBus.HGEventActionCode;
import com.hg.hollowgoods.Constant.HGParamKey;
import com.hg.hollowgoods.UI.Base.BaseMVPFragment;
import com.hg.hollowgoods.UI.Base.Message.Dialog2.ConfigInput;
import com.hg.hollowgoods.UI.Base.Message.Dialog2.DateTimeDialogType;
import com.hg.hollowgoods.UI.Base.Message.Toast.t;
import com.hg.hollowgoods.Util.BeanUtils;
import com.hg.hollowgoods.Util.SystemAppUtils;
import com.hg.hollowgoods.Widget.ValidatorInput.Validator.ValidatorFactory;
import com.hg.hollowgoods.Widget.ValidatorInput.Validator.ValidatorType;
import com.hg.hollowgoods.widget.HGRefreshLayout;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Bean.HiddenDanger.HiddenDanger;
import com.xhtt.hiddendanger.Bean.HiddenDanger.HiddenDangerChange;
import com.xhtt.hiddendanger.Bean.HiddenDanger.HiddenLevel;
import com.xhtt.hiddendanger.Constant.Constants;
import com.xhtt.hiddendanger.Constant.EventActionCode;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.Constant.SystemConfig;
import com.xhtt.hiddendanger.Constant.WorkType;
import com.xhtt.hiddendanger.R;
import com.xhtt.hiddendanger.UI.Activity.HiddenDanger.FreeTakeActivity;
import com.xhtt.hiddendanger.UI.Activity.HiddenDanger.HiddenDangerBaseActivity;
import com.xhtt.hiddendanger.UI.Activity.HiddenDanger.HiddenDangerDetailActivity;
import com.xhtt.hiddendanger.UI.Fragment.OnFragmentWorkListener;
import com.xhtt.hiddendanger.Util.UploadFile.UploadFileUtils;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.ArrayList;

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
    private FastAdapter hiddenDangerAdapter;
    private ArrayList<CommonBean> hiddenDangerData = new ArrayList<>();
    private FastAdapter changeAdapter;
    private ArrayList<CommonBean> changeData = new ArrayList<>();
    private WorkType workType;
    private boolean isOnlyRead = true;
    private int clickPosition;
    private int clickSortNumber;
    private String title = "";
    private UploadFileUtils uploadFileUtils = null;
    private HiddenLevel hiddenLevelChecked = null;
    private OnFragmentWorkListener onFragmentWorkListener;
    private HiddenDangerChange change;
    private long lastServiceId;
    private User user;

    @Override
    public int bindLayout() {
        return R.layout.fragment_hidden_danger_detail;
    }

    @Override
    public void initParamData() {

        hiddenDangerRefreshLayout = baseUI.findViewById(R.id.hiddenDangerHGRefreshLayout);
        changeRefreshLayout = baseUI.findViewById(R.id.changeHGRefreshLayout);
        changeView = baseUI.findViewById(R.id.cv_changeView);

        workType = baseUI.getParam(ParamKey.WorkType.getValue(), null);
        parentData = baseUI.getParam(ParamKey.ParentData.getValue(), null);
        grandData = baseUI.getParam(ParamKey.GrandData.getValue(), null);
        lastServiceId = baseUI.getParam(ParamKey.LastServiceId.getValue(), 0L);

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

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleViewVisibility(false);

        uploadFileUtils = new UploadFileUtils(baseUI.getBaseContext());

        hiddenDangerRefreshLayout.initRecyclerView();
        hiddenDangerRefreshLayout.getRecyclerView().setNestedScrollingEnabled(false);
        changeRefreshLayout.initRecyclerView();
        changeRefreshLayout.getRecyclerView().setNestedScrollingEnabled(false);

        hiddenDangerAdapter = new FastAdapter(baseUI.getBaseContext(), hiddenDangerData, false, true);
        hiddenDangerRefreshLayout.setAdapter(hiddenDangerAdapter);

        hiddenDangerData.addAll(hiddenDangerAdapter.getDetailItemData(parentData));
        hiddenDangerAdapter.refreshData(hiddenDangerData);

        changeAdapter = new FastAdapter(baseUI.getBaseContext(), changeData, false, true);
        changeRefreshLayout.setAdapter(changeAdapter);

        changeData.addAll(changeAdapter.getDetailItemData(change));
        changeAdapter.refreshData(changeData);

        if (workType == WorkType.Add || workType == WorkType.AddFreeTake) {
            mPresenter.getUserData();
        }

        return this;
    }

    @Override
    public void setListener() {

        hiddenDangerAdapter.setOnFastClick(onFastClick);
        changeAdapter.setOnFastClick(onFastClick);

        baseUI.baseDialog.setOnDialogClickListener((code, result, data) -> {

            if (result) {
                String strValue;
                long longValue;

                switch (code) {
                    case 5:
                        // 整改说明
                        strValue = data.getString(HGParamKey.InputValue.getValue(), "");
                        change.setChangeDescribe(strValue);
                        break;
                    case 10:
                        // 检查日期
                        longValue = data.getLong(HGParamKey.DateTimeInMillis.getValue(), 0);
                        parentData.setCheckDateShow(longValue + "");
                        break;
                    case 20:
                        // 隐患地点
                        strValue = data.getString(HGParamKey.InputValue.getValue(), "");
                        parentData.setHiddenLocation(strValue);
                        break;
                    case 30:
                        // 隐患描述
                        strValue = data.getString(HGParamKey.InputValue.getValue(), "");
                        parentData.setHiddenDescribe(strValue);
                        break;
                    case 40:
                        // 隐患等级
                        int levelPosition = data.getInt(HGParamKey.Position.getValue(), -1);
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
                        strValue = data.getString(HGParamKey.InputValue.getValue(), "");
                        parentData.setReference(strValue);
                        break;
                    case 60:
                        // 整改措施
                        strValue = data.getString(HGParamKey.InputValue.getValue(), "");
                        parentData.setChangeFunction(strValue);
                        break;
                    case DIALOG_CODE_SUBMIT:
                        int position = data.getInt(HGParamKey.Position.getValue(), -1);

                        if (position != -1) {
                            baseUI.baseDialog.showProgressDialog("提交中，请稍候……", DIALOG_CODE_SUBMIT);

                            if (position == 0) {
                                submitFile(parentData.getStatus());
                            } else {
                                submitData(cacheFiles);
                            }
                        }
                        break;
                }

                if (code == 5) {
                    changeAdapter.refreshFastItem(change, clickPosition);
                } else {
                    hiddenDangerAdapter.refreshFastItem(parentData, clickPosition);
                }
            }
        });

        baseUI.setOnPermissionsListener((isAgreeAll, requestCode, permissions, isAgree) -> {
            if (isAgreeAll) {
                switch (requestCode) {
                    case FastAdapter.REQUEST_CODE_TAKE_PHOTO:
                        hiddenDangerAdapter.takePhoto(baseUI.getBaseContext(), clickPosition, clickSortNumber);
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
    public void onEventUI(Event item) {

        switch (item.getEventActionCode()) {
            case HGEventActionCode.REMOVE_IMAGE:
                if (clickSortNumber == 1) {
//                    change.getMedia().get(clickSortNumber).remove(item.getObj(HGParamKey.Position.getValue(), 0));
                    ArrayList<AppFile> temp = change.getMedia().get(clickSortNumber);
                    int p = item.getObj(HGParamKey.Position.getValue(), 0);
                    temp.remove(p);
                    changeAdapter.refreshFastItem(change, clickPosition);
                } else {
                    ArrayList<AppFile> temp = parentData.getMedia().get(clickSortNumber);
                    int p = item.getObj(HGParamKey.Position.getValue(), 0);
                    temp.remove(p);
//                    parentData.getMedia().get(clickSortNumber).remove(item.getObj(HGParamKey.Position.getValue(), 0));
                    hiddenDangerAdapter.refreshFastItem(parentData, clickPosition);
                }
                break;
            case EventActionCode.UPLOAD_PHOTO:
                if (item.isFromMe(this.getClass().getName())) {
                    boolean status = item.getObj(ParamKey.Status.getValue(), false);

                    if (status) {
                        ArrayList<AppFile> files = item.getObj(ParamKey.BackData.getValue(), null);
                        submitData(files);
                    }
                }
                break;
            case EventActionCode.SELECTOR_HIDDEN_DANGER_STORE:
                HiddenDanger hiddenDanger = item.getObj(ParamKey.BackData.getValue(), null);
                if (hiddenDanger != null) {
                    parentData.setHiddenDescribe(hiddenDanger.getHiddenDescribe());
                    parentData.setReference(hiddenDanger.getReference());
                    parentData.setChangeFunction(hiddenDanger.getChangeFunction());

                    hiddenDangerAdapter.refreshFastItem(parentData);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (clickSortNumber == 1) {
            changeAdapter.onActivityResultForImage(baseUI.getBaseContext(), change, requestCode, resultCode, data);
        } else {
            hiddenDangerAdapter.onActivityResultForImage(baseUI.getBaseContext(), parentData, requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private OnFastClick onFastClick = new OnFastClick() {
        @Override
        public void onTakePhotoClick(View view, int position, int sortNumber) {

            clickPosition = position;
            clickSortNumber = sortNumber;

            if (baseUI.requestPermission(FastAdapter.REQUEST_CODE_TAKE_PHOTO, Manifest.permission.CAMERA)) {
                if (sortNumber == 1) {
                    changeAdapter.takePhoto(baseUI.getBaseContext(), position, sortNumber);
                } else {
                    hiddenDangerAdapter.takePhoto(baseUI.getBaseContext(), position, sortNumber);
                }
            }
        }

        @Override
        public void onOpenAlbumClick(View view, int position, int sortNumber) {

            clickPosition = position;
            clickSortNumber = sortNumber;

            if (sortNumber == 1) {
                changeAdapter.openAlbum(baseUI.getBaseContext(), parentData, SystemConfig.FILE_MAX_COUNT, position, sortNumber);
            } else {
                hiddenDangerAdapter.openAlbum(baseUI.getBaseContext(), parentData, SystemConfig.FILE_MAX_COUNT, position, sortNumber);
            }
        }

        @Override
        public void onSubmitClick(View view, int id) {

        }

        @Override
        public void onOperateClick(View view, int position, int sortNumber) {

            switch (sortNumber) {
                case 30:
                    // 隐患描述
                    baseUI.startMyActivity(HiddenDangerBaseActivity.class);
                    break;
                default:
                    onFastItemClick(view, position, sortNumber);
                    break;
            }
        }

        @Override
        public void onFilePreClick(View view, int position, int sortNumber) {

            clickPosition = position;
            clickSortNumber = sortNumber;

            ArrayList<AppFile> media;
            if (sortNumber == 1) {
                media = change.getMedia().get(sortNumber);
            } else {
                media = parentData.getMedia().get(sortNumber);
            }

            if (media == null || media.size() == 0) {
                onFastItemClick(view, position, sortNumber);
            } else {
                if (sortNumber == 1) {
                    boolean canRemove = !change.getOnlyReadItem("changePhoto");
                    for (AppFile t : media) {
                        t.setCanRemove(canRemove);
                    }
                } else {
                    for (AppFile t : media) {
                        t.setCanRemove(!isOnlyRead);
                    }
                }

                new SystemAppUtils().imagePre(baseUI.getBaseContext(), media);
            }
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

            RecyclerView recyclerView = null;

            try {
                Class temp = holder.getClass();
                Class clazz = temp.getSuperclass();
                Field field = clazz.getDeclaredField("mOwnerRecyclerView");

                if (field != null) {
                    //设置些属性是可以访问的
                    field.setAccessible(true);
                    //得到此属性的值
                    recyclerView = (RecyclerView) field.get(holder);
                }
            } catch (Exception ignored) {

            }

            if (recyclerView != null) {
                FastItemData item;

                if (recyclerView == hiddenDangerRefreshLayout.getRecyclerView()) {
                    item = hiddenDangerData.get(position).getData();
                } else {
                    item = changeData.get(position).getData();
                }

                int sortNumber = item.sortNumber;
                onFastItemClick(view, position, sortNumber);
            }
        }
    };

    private void onFastItemClick(View view, int position, int sortNumber) {

        clickPosition = position;
        clickSortNumber = sortNumber;
        ConfigInput configInput;

        switch (sortNumber) {
            case 1:
                // 整改后照片
                if (!change.getOnlyReadItem("changePhoto")) {
                    changeAdapter.showImageWindow(view, position, sortNumber);
                }
                break;
            case 5:
                // 整改说明
                if (!change.getOnlyReadItem("changeDescribe")) {
                    configInput = new ConfigInput(baseUI.getBaseContext())
                            .setHint("请输入整改说明")
                            .setText(change.getChangeDescribe())
                            .setMaxLines(3)
                            .setValidator(ValidatorFactory.getValidator(ValidatorType.MAX_LENGTH, "最多输入500个字", 500));
                    baseUI.baseDialog.showInputDialog(configInput, sortNumber);
                }
                break;
            case 10:
                // 检查日期
                if (!isOnlyRead) {
                    String value;
                    if (TextUtils.isEmpty(parentData.getCheckDateShow())) {
                        value = System.currentTimeMillis() + "";
                    } else {
                        value = parentData.getCheckDateShow();
                    }
                    baseUI.baseDialog.showDateTimeDialog(null, Long.parseLong(value), DateTimeDialogType.YMD, sortNumber);
                }
                break;
            case 20:
                // 隐患地点
                if (!isOnlyRead) {
                    configInput = new ConfigInput(baseUI.getBaseContext())
                            .setHint("请输入隐患地点")
                            .setText(parentData.getHiddenLocation())
                            .setMaxLines(3)
                            .setValidator(ValidatorFactory.getValidator(ValidatorType.MAX_LENGTH, "最多输入100个字", 100));
                    baseUI.baseDialog.showInputDialog(configInput, sortNumber);
                }
                break;
            case 25:
                // 隐患照片
                if (!isOnlyRead) {
                    hiddenDangerAdapter.showImageWindow(view, position, sortNumber);
                }
                break;
            case 30:
                // 隐患描述
                if (!isOnlyRead) {
                    configInput = new ConfigInput(baseUI.getBaseContext())
                            .setHint("请输入隐患描述")
                            .setText(parentData.getHiddenDescribe())
                            .setMaxLines(3)
                            .setValidator(ValidatorFactory.getValidator(ValidatorType.MIN_LENGTH, "至少输入1个字", 1),
                                    ValidatorFactory.getValidator(ValidatorType.MAX_LENGTH, "最多输入500个字", 500)
                            );
                    baseUI.baseDialog.showInputDialog(configInput, sortNumber);
                }
                break;
            case 40:
                // 隐患等级
                if (!isOnlyRead) {
                    int pos = Constants.HIDDEN_LEVEL.indexOf(hiddenLevelChecked);
                    baseUI.baseDialog.showSingleDialog("请选择隐患等级", Constants.HIDDEN_LEVEL_OBJ, pos, sortNumber);
                }
                break;
            case 50:
                // 参考依据
                if (!isOnlyRead) {
                    configInput = new ConfigInput(baseUI.getBaseContext())
                            .setHint("请输入参考依据")
                            .setText(parentData.getReference())
                            .setMaxLines(3)
                            .setValidator(ValidatorFactory.getValidator(ValidatorType.MAX_LENGTH, "最多输入500个字", 500));
                    baseUI.baseDialog.showInputDialog(configInput, sortNumber);
                }
                break;
            case 60:
                // 整改措施
                if (!isOnlyRead) {
                    configInput = new ConfigInput(baseUI.getBaseContext())
                            .setHint("请输入整改措施")
                            .setText(parentData.getChangeFunction())
                            .setMaxLines(3)
                            .setValidator(ValidatorFactory.getValidator(ValidatorType.MAX_LENGTH, "最多输入500个字", 500));
                    baseUI.baseDialog.showInputDialog(configInput, sortNumber);
                }
                break;
        }
    }

    public boolean checkNotEmptyItems() {
        return hiddenDangerAdapter.checkNotEmptyItem(parentData);
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
        if (hiddenDangerAdapter.checkNotEmptyItem(parentData)) {
            parentData.setStatus(status);
            submit();
        }
    }

    private void submit() {

        baseUI.baseDialog.showProgressDialog("提交中，请稍候……", DIALOG_CODE_SUBMIT);

        ArrayList<AppFile> uploadFiles = new ArrayList<>();
        ArrayList<AppFile> hiddenPhoto = parentData.getMedia().get(25);
        ArrayList<AppFile> changePhoto = change.getMedia().get(1);

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
        if (hiddenDangerAdapter.checkNotEmptyItem(parentData)) {
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

            parentData.getMedia().put(25, hiddenPhotos);
            parentData.getMedia().put(1, changePhotos);
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
        parentData.getMedia().put(1, change.getMedia().get(1));

        Event event = new Event(EventActionCode.HIDDEN_DANGER_SUBMIT);
        event.addObj(ParamKey.Company.getValue(), parentData);
        event.addObj(ParamKey.WorkType.getValue(), workType);
        EventBus.getDefault().post(event);

        if (getActivity() instanceof FreeTakeActivity) {
            if (((FreeTakeActivity) getActivity()).action == FreeTakeActivity.ACTION_SUBMIT_ONLY) {
                finishMyActivity();
            } else {
                parentData = null;
                initParentData();
                initViewMode();
                addDefaultHiddenLevel();

                hiddenDangerAdapter.refreshFastItem(parentData);
                changeAdapter.refreshFastItem(change);
            }
        } else if (getActivity() instanceof HiddenDangerDetailActivity) {
            if (((HiddenDangerDetailActivity) getActivity()).action == HiddenDangerDetailActivity.ACTION_SUBMIT_ONLY) {
                finishMyActivity();
            } else {
                parentData = null;
                initParentData();
                initViewMode();
                addDefaultHiddenLevel();

                hiddenDangerAdapter.refreshFastItem(parentData);
                changeAdapter.refreshFastItem(change);
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
            parentData = new HiddenDanger(FastAdapter.ITEM_TYPE_ITEM);
            parentData.setCheckDateShow(System.currentTimeMillis() + "");
        } else {
            parentData = BeanUtils.copy(parentData, HiddenDanger.class);
            parentData.setItemType(FastAdapter.ITEM_TYPE_ITEM);

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

        change = new HiddenDangerChange(FastAdapter.ITEM_TYPE_ITEM);
        change.setChangePhotoList(parentData.getChangePhotoList());
        change.setChangeDescribe(parentData.getChangeDescribe());
        change.setMedia(parentData.getMedia());
    }

    private void initViewMode() {

        FastAdapter.setAllItemOnlyRead(parentData, isOnlyRead);
        FastAdapter.setAllItemOnlyRead(change, isOnlyRead);

        if (workType == WorkType.Change) {
            change.addOnlyReadItem("changePhoto", false);
            change.addOnlyReadItem("changeDescribe", false);
        }
    }

}
