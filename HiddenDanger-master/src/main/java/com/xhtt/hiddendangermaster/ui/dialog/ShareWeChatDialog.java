package com.xhtt.hiddendangermaster.ui.dialog;

import android.os.Bundle;
import android.view.View;

import com.hg.hollowgoods.ui.base.click.OnViewClickListener;
import com.hg.hollowgoods.ui.base.message.dialog2.HGDialogFragment2;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.util.WeChatUtils;

/**
 * 分享到微信对话框
 * <p>
 * Created by Hollow Goods on 2020-09-15.
 */
public class ShareWeChatDialog extends HGDialogFragment2<ShareWeChatConfig> {

    private View friend;
    private View moments;

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

        friend.setOnClickListener(new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                WeChatUtils.create().init(context).shareFile(WeChatUtils.SCENE_FRIEND, mConfig.getFilepath());
            }
        });

        moments.setOnClickListener(new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                WeChatUtils.create().init(context).shareFile(WeChatUtils.SCENE_MOMENTS, mConfig.getFilepath());
            }
        });
    }

}
