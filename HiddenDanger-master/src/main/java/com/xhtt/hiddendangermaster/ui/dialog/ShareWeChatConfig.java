package com.xhtt.hiddendangermaster.ui.dialog;

import com.hg.hollowgoods.ui.base.message.dialog2.DialogConfig;

/**
 * 分享微信配置
 * <p>
 * Created by Hollow Goods on 2020-09-15.
 */
public class ShareWeChatConfig extends DialogConfig.BaseConfig<ShareWeChatConfig> {

    public ShareWeChatConfig(int code) {
        super(code);
    }

    private String filepath;


    public String getFilepath() {
        return filepath;
    }

    public ShareWeChatConfig setFilepath(String filepath) {
        this.filepath = filepath;
        return this;
    }
}
