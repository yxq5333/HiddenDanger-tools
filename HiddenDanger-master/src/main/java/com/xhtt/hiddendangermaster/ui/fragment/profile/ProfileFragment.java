package com.xhtt.hiddendangermaster.ui.fragment.profile;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.adapter.fast.HGFastAdapter2;
import com.hg.hollowgoods.adapter.fast.bean.HGFastItem2;
import com.hg.hollowgoods.adapter.fast.callback.OnHGFastItemClickListener2Adapter;
import com.hg.hollowgoods.adapter.fast.callback.OnSystemProxyEventFinishListenerAdapter;
import com.hg.hollowgoods.bean.eventbus.HGEvent;
import com.hg.hollowgoods.constant.HGSystemConfig;
import com.hg.hollowgoods.ui.base.click.OnViewClickListener;
import com.hg.hollowgoods.ui.base.message.dialog2.DialogConfig;
import com.hg.hollowgoods.ui.base.message.toast.t;
import com.hg.hollowgoods.ui.base.mvp.BaseMVPFragment;
import com.hg.hollowgoods.util.SystemAppUtils;
import com.hg.hollowgoods.util.anim.recyclerview.adapters.ScaleInAnimationAdapter;
import com.hg.hollowgoods.util.anim.recyclerview.animators.LandingAnimator;
import com.hg.hollowgoods.util.file.FileUtils2;
import com.hg.hollowgoods.util.ip.IPConfigHelper;
import com.hg.hollowgoods.widget.HGStatusLayout;
import com.hg.hollowgoods.widget.itemdecoration.divider.HorizontalDividerItemDecoration;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.User;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.InterfaceApi;
import com.xhtt.hiddendangermaster.constant.SystemConfig;
import com.xhtt.hiddendangermaster.ui.activity.profile.AboutUsActivity;
import com.xhtt.hiddendangermaster.ui.activity.profile.AlterPasswordActivity;
import com.xhtt.hiddendangermaster.ui.activity.profile.CacheClearActivity;
import com.xhtt.hiddendangermaster.util.LoginUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 我的界面
 *
 * @author HG
 */

public class ProfileFragment extends BaseMVPFragment<ProfilePresenter> implements ProfileContract.View {

    private final int DIALOG_CODE_LOGIN_OUT = 1000;

    private RecyclerView result;
    private Button loginOut;

    private HGFastAdapter2 adapter;
    private ArrayList<HGFastItem2> data = new ArrayList<>();
    private User user;
    private HashMap<Integer, String> refreshTag = new HashMap<>();

    @Override
    public int bindLayout() {
        return R.layout.fragment_profile;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(R.string.title_activity_profile);
        baseUI.setStatus(HGStatusLayout.Status.Loading);

        new Handler().postDelayed(() -> {

            result = baseUI.findViewById(R.id.rv_result);
            loginOut = baseUI.findViewById(R.id.btn_loginOut);

            user = new User();

            result.setHasFixedSize(true);
            result.setItemAnimator(new LandingAnimator());
            result.addItemDecoration(new HorizontalDividerItemDecoration.Builder(baseUI.getBaseContext())
                    .colorResId(R.color.line)
                    .sizeResId(R.dimen.default_line_mini_size)
                    .build()
            );
            result.setLayoutManager(new LinearLayoutManager(baseUI.getBaseContext()));

            data.add(new HGFastItem2.Builder(10, HGFastItem2.ITEM_TYPE_INPUT)
                    .setLabel("用户名")
                    .setContentHint("请输入")
                    .setOnlyRead(false)
                    .setNotEmpty(false)
                    .setRightIconRes(R.drawable.ic_my_arrow_right)
                    .build()
            );

            data.add(new HGFastItem2.Builder(20, HGFastItem2.ITEM_TYPE_INPUT)
                    .setLabel("姓名")
                    .setContentHint("请输入")
                    .setOnlyRead(false)
                    .setNotEmpty(false)
                    .setRightIconRes(R.drawable.ic_my_arrow_right)
                    .build()
            );

            data.add(new HGFastItem2.Builder(30, HGFastItem2.ITEM_TYPE_INPUT)
                    .setLabel("手机")
                    .setOnlyRead(true)
                    .setNotEmpty(false)
                    .setRightIconRes(R.drawable.ic_my_arrow_right)
                    .build()
            );

            data.add(new HGFastItem2.Builder(40, HGFastItem2.ITEM_TYPE_INPUT)
                    .setLabel("修改密码")
                    .setOnlyRead(false)
                    .setNotEmpty(false)
                    .setRightIconRes(R.drawable.ic_my_arrow_right)
                    .build()
            );

            data.add(new HGFastItem2.Builder(50, HGFastItem2.ITEM_TYPE_INPUT)
                    .setLabel("清理缓存")
                    .setOnlyRead(false)
                    .setNotEmpty(false)
                    .setRightIconRes(R.drawable.ic_my_arrow_right)
                    .build()
            );

            data.add(new HGFastItem2.Builder(55, HGFastItem2.ITEM_TYPE_INPUT)
                    .setLabel("用户手册")
                    .setOnlyRead(false)
                    .setNotEmpty(false)
                    .setRightIconRes(R.drawable.ic_my_arrow_right)
                    .build()
            );

            data.add(new HGFastItem2.Builder(60, HGFastItem2.ITEM_TYPE_INPUT)
                    .setLabel("关于我们")
                    .setOnlyRead(false)
                    .setNotEmpty(false)
                    .setRightIconRes(R.drawable.ic_my_arrow_right)
                    .build()
            );

            adapter = new HGFastAdapter2(baseUI, data);
            result.setAdapter(new ScaleInAnimationAdapter(adapter));

//            adapter.refreshDataAllData(data);

            refreshCache();

            mPresenter.getData();
        }, SystemConfig.DELAY_TIME_INIT_VIEW);
    }

    @Override
    public void setListener() {

        new Handler().postDelayed(() -> {

            adapter.addOnSystemProxyEventFinishListener(new OnSystemProxyEventFinishListenerAdapter() {
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
            adapter.setOnHGFastItemClickListener2(40, new OnHGFastItemClickListener2Adapter() {
                @Override
                public void onItemClick(int clickItemId) {
                    baseUI.startMyActivity(AlterPasswordActivity.class);
                }
            });

            // 清理缓存
            adapter.setOnHGFastItemClickListener2(50, new OnHGFastItemClickListener2Adapter() {
                @Override
                public void onItemClick(int clickItemId) {
                    baseUI.startMyActivity(CacheClearActivity.class);
                }
            });

            // 用户手册
            adapter.setOnHGFastItemClickListener2(55, new OnHGFastItemClickListener2Adapter() {
                @Override
                public void onItemClick(int clickItemId) {
                    new SystemAppUtils().readFile(baseUI.getBaseContext(),
                            IPConfigHelper.create().getNowIPConfig().getRequestUrl(InterfaceApi.DownloadFile.getUrl() + "operation.pdf"),
                            "用户手册"
                    );
                }
            });

            // 关于我们
            adapter.setOnHGFastItemClickListener2(60, new OnHGFastItemClickListener2Adapter() {
                @Override
                public void onItemClick(int clickItemId) {
                    baseUI.startMyActivity(AboutUsActivity.class);
                }
            });

            loginOut.setOnClickListener(new OnViewClickListener(false) {
                @Override
                public void onViewClick(View view, int id) {
                    baseUI.baseDialog.showAlertDialog(new DialogConfig.AlertConfig(DIALOG_CODE_LOGIN_OUT)
                            .setTitle(R.string.tips_best)
                            .setText("是否确定退出登录？")
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

            baseUI.setStatus(HGStatusLayout.Status.Default);
        }, SystemConfig.DELAY_TIME_SET_LISTENER);
    }

    @Override
    public ProfilePresenter createPresenter() {
        return new ProfilePresenter();
    }

    @Override
    public void onEventUI(HGEvent item) {
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
            t.info("由于修改了用户名，请重新登录！");
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
                adapter.findItemById(50).setContent(FileUtils2.formatFileSize(FileUtils2.calcFileTotalSizeFromPath(HGSystemConfig.APP_BASE_PATH)));
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
