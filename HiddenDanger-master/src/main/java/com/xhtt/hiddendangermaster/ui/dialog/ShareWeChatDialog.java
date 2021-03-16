package com.xhtt.hiddendangermaster.ui.dialog;

import android.os.Bundle;
import android.view.View;

import com.hg.zero.dialog.ZDialogFragment2;
import com.hg.zero.listener.ZOnViewClickListener;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.util.WeChatUtils;

/**
 * 分享到微信对话框
 * <p>
 * Created by Hollow Goods on 2020-09-15.
 */
public class ShareWeChatDialog extends ZDialogFragment2<ShareWeChatConfig> {

    private View friend;
    private View moments;

    public ShareWeChatDialog(ShareWeChatConfig mConfig) {
        super(mConfig);
    }

    @Override
    public int bindLayout() {
        return R.layout.dialog_share_we_chat;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        friend = findViewById(R.id.ll_friend);
        moments = findViewById(R.id.ll_moments);
    }

    @Override
    public void setListener() {

        friend.setOnClickListener(new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                WeChatUtils.create().shareFile(WeChatUtils.SCENE_FRIEND, mConfig.getFilepath());
                closeDialog();
            }
        });

        moments.setOnClickListener(new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                WeChatUtils.create().shareFile(WeChatUtils.SCENE_MOMENTS, mConfig.getFilepath());
                closeDialog();
            }
        });
    }

}
