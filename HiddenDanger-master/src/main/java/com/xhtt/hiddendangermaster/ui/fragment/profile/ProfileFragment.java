package com.xhtt.hiddendangermaster.ui.fragment.profile;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.zero.adapter.fast.ZFastAdapter2;
import com.hg.zero.adapter.fast.bean.ZFastItem2;
import com.hg.zero.adapter.fast.callback.ZOnFastItemClickListener2Adapter;
import com.hg.zero.adapter.fast.callback.ZOnSystemProxyEventFinishListenerAdapter;
import com.hg.zero.anim.recyclerview.adapters.ZScaleInAnimationAdapter;
import com.hg.zero.anim.recyclerview.animators.ZLandingAnimator;
import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.config.ZSystemConfig;
import com.hg.zero.dialog.ZDialogConfig;
import com.hg.zero.file.ZFileUtils2;
import com.hg.zero.listener.ZOnViewClickListener;
import com.hg.zero.toast.Zt;
import com.hg.zero.ui.activity.plugin.ip.ZIPConfigHelper;
import com.hg.zero.util.ZSystemAppUtils;
import com.hg.zero.widget.itemdecoration.divider.ZHorizontalDividerItemDecoration;
import com.hg.zero.widget.statuslayout.ZStatusLayout;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.User;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.InterfaceApi;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.ui.activity.profile.AboutUsActivity;
import com.xhtt.hiddendangermaster.ui.activity.profile.AlterPasswordActivity;
import com.xhtt.hiddendangermaster.ui.activity.profile.CacheClearActivity;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPFragment;
import com.xhtt.hiddendangermaster.util.LoginUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 我的界面
 *
 * @author HG
 */

public class ProfileFragment extends HDBaseMVPFragment<ProfilePresenter> implements ProfileContract.View {

    private final int DIALOG_CODE_LOGIN_OUT = 1000;

    private RecyclerView result;
    private Button loginOut;

    private ZFastAdapter2 adapter;
    private ArrayList<ZFastItem2> data = new ArrayList<>();
    private User user;
    private HashMap<Integer, String> refreshTag = new HashMap<>();

    @Override
    public int bindLayout() {
        return R.layout.fragment_profile;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(R.string.title_activity_profile);
        baseUI.setStatus(ZStatusLayout.Status.Loading);

        new Handler().postDelayed(() -> {

            result = baseUI.findViewById(R.id.rv_result);
            loginOut = baseUI.findViewById(R.id.btn_loginOut);

            user = new User();

            result.setHasFixedSize(true);
            result.setItemAnimator(new ZLandingAnimator());
            result.addItemDecoration(new ZHorizontalDividerItemDecoration.Builder(baseUI.getBaseContext())
                    .colorResId(R.color.line)
                    .sizeResId(R.dimen.z_default_line_mini_size)
                    .build()
            );
            result.setLayoutManager(new LinearLayoutManager(baseUI.getBaseContext()));

            data.add(new ZFastItem2.Builder(10, ZFastItem2.ITEM_TYPE_INPUT)
                    .setLabel("用户名")
                    .setContentHint("请输入")
                    .setOnlyRead(false)
                    .setNotEmpty(false)
                    .setRightIconRes(R.drawable.ic_my_arrow_right)
                    .build()
            );

            data.add(new ZFastItem2.Builder(20, ZFastItem2.ITEM_TYPE_INPUT)
                    .setLabel("姓名")
                    .setContentHint("请输入")
                    .setOnlyRead(false)
                    .setNotEmpty(false)
                    .setRightIconRes(R.drawable.ic_my_arrow_right)
                    .build()
            );

            data.add(new ZFastItem2.Builder(30, ZFastItem2.ITEM_TYPE_INPUT)
                    .setLabel("手机")
                    .setOnlyRead(true)
                    .setNotEmpty(false)
                    .setRightIconRes(R.drawable.ic_my_arrow_right)
                    .build()
            );

            data.add(new ZFastItem2.Builder(40, ZFastItem2.ITEM_TYPE_INPUT)
                    .setLabel("修改密码")
                    .setOnlyRead(false)
                    .setNotEmpty(false)
                    .setRightIconRes(R.drawable.ic_my_arrow_right)
                    .build()
            );

            data.add(new ZFastItem2.Builder(50, ZFastItem2.ITEM_TYPE_INPUT)
                    .setLabel("清理缓存")
                    .setOnlyRead(false)
                    .setNotEmpty(false)
                    .setRightIconRes(R.drawable.ic_my_arrow_right)
                    .build()
            );

            data.add(new ZFastItem2.Builder(55, ZFastItem2.ITEM_TYPE_INPUT)
                    .setLabel("用户手册")
                    .setOnlyRead(false)
                    .setNotEmpty(false)
                    .setRightIconRes(R.drawable.ic_my_arrow_right)
                    .build()
            );

            data.add(new ZFastItem2.Builder(60, ZFastItem2.ITEM_TYPE_INPUT)
                    .setLabel("关于我们")
                    .setOnlyRead(false)
                    .setNotEmpty(false)
                    .setRightIconRes(R.drawable.ic_my_arrow_right)
                    .build()
            );

            adapter = new ZFastAdapter2(baseUI, data);
            result.setAdapter(new ZScaleInAnimationAdapter(adapter));

//            adapter.refreshDataAllData(data);

            refreshCache();

            mPresenter.getData();
        }, SystemConfig.DELAY_TIME_INIT_VIEW);
    }

    @Override
    public void setListener() {

        new Handler().postDelayed(() -> {

            adapter.addOnSystemProxyEventFinishListener(new ZOnSystemProxyEventFinishListenerAdapter() {
                @Override
                public void onInputFinish(int itemId, String content) {
                    if (itemId == 10) {
                        // 用户名
                        refreshTag.put(itemId, content);
                        mPresenter.updateUserData(content, user.getName());
                    } else if (itemId == 20) {
                        // 姓名
                        refreshTag.put(itemId, content);
                        mPresenter.updateUserData(user.getUsername(), content);
                    }
                }
            });

            // 修改密码
            adapter.setOnFastItemClickListener2(40, new ZOnFastItemClickListener2Adapter() {
                @Override
                public boolean onItemClick(int clickItemId) {
                    baseUI.startMyActivity(AlterPasswordActivity.class);
                    return true;
                }
            });

            // 清理缓存
            adapter.setOnFastItemClickListener2(50, new ZOnFastItemClickListener2Adapter() {
                @Override
                public boolean onItemClick(int clickItemId) {
                    baseUI.startMyActivity(CacheClearActivity.class);
                    return true;
                }
            });

            // 用户手册
            adapter.setOnFastItemClickListener2(55, new ZOnFastItemClickListener2Adapter() {
                @Override
                public boolean onItemClick(int clickItemId) {
                    new ZSystemAppUtils().readFile(baseUI.getBaseContext(),
                            ZIPConfigHelper.get().getNowIPConfig().getRequestUrl(InterfaceApi.DownloadFile.getUrl() + "operation.pdf"),
                            "用户手册"
                    );
                    return true;
                }
            });

            // 关于我们
            adapter.setOnFastItemClickListener2(60, new ZOnFastItemClickListener2Adapter() {
                @Override
                public boolean onItemClick(int clickItemId) {
                    baseUI.startMyActivity(AboutUsActivity.class);
                    return true;
                }
            });

            loginOut.setOnClickListener(new ZOnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.baseDialog.showAlertDialog(new ZDialogConfig.AlertConfig(DIALOG_CODE_LOGIN_OUT)
                            .setTitle(R.string.z_tips_best)
                            .setContent("是否确定退出登录？")
                    );
                }
            });

            baseUI.baseDialog.addOnDialogClickListener((code, result, bundle) -> {
                if (result) {
                    switch (code) {
                        case DIALOG_CODE_LOGIN_OUT:
                            doLoginOut();
                            break;
                    }
                }
            });

            baseUI.setStatus(ZStatusLayout.Status.Default);
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Override
    public ProfilePresenter createPresenter() {
        return new ProfilePresenter();
    }

    @Override
    public void onEventUI(ZEvent item) {
        if (item.getEventActionCode() == EventActionCode.ClearCache) {
            refreshCache();
        }
    }

    @Override
    public void getDataSuccess(User user) {
        if (user != null) {
            this.user = user;

            adapter.findItemById(10).setContent(user.getUsername());
            adapter.findItemById(20).setContent(user.getName());
            adapter.findItemById(30).setContent(user.getPhone());

            adapter.processData();
        }
    }

    @Override
    public void getDataError() {

    }

    @Override
    public void getDataFinish() {

    }

    @Override
    public void updateUserDataSuccess() {

        if (refreshTag.get(10) != null) {
            user.setUsername(refreshTag.get(10));
            adapter.findItemById(10).setContent(user.getUsername());
            Zt.info("由于修改了用户名，请重新登录！");
            doLoginOut();
            return;
        }

        if (refreshTag.get(20) != null) {
            user.setName(refreshTag.get(20));
            adapter.findItemById(20).setContent(user.getName());
            user.setPassword(LoginUtils.getUser().getPassword());
            LoginUtils.updateUser(user);
        }

        refreshTag.clear();

        adapter.processData();
    }

    @Override
    public void updateUserDataError() {

    }

    private void doLoginOut() {
        LoginUtils.autoExitApp(baseUI.getBaseContext());
    }

    private void refreshCache() {
        if (user != null && adapter != null) {
            new Thread(() -> baseUI.getBaseContext().runOnUiThread(() -> {
                adapter.findItemById(50).setContent(ZFileUtils2.formatFileSize(ZFileUtils2.calcFileTotalSizeFromPath(ZSystemConfig.appBaseDir())));
                adapter.processData();
            })).start();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshCache();
    }
}
