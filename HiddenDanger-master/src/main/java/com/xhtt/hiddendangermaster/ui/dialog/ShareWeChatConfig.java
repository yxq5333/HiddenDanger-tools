package com.xhtt.hiddendangermaster.ui.dialog;

import com.hg.zero.dialog.ZDialogConfig;
import com.hg.zero.dialog.ZOnDialogDismissListener2;

/**
 * 分享微信配置
 * <p>
 * Created by Hollow Goods on 2020-09-15.
 */
public class ShareWeChatConfig extends ZDialogConfig.BaseConfig2<ShareWeChatConfig> {

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

    @Override
    protected Object getTitle() {
        return title;
    }

    @Override
    protected Object getHint() {
        return hint;
    }

    @Override
    protected Object getContent() {
        return content;
    }

    @Override
    protected Object getNegativeButtonTxt() {
        return negativeButtonTxt;
    }

    @Override
    protected Object getPositiveButtonTxt() {
        return positiveButtonTxt;
    }

    @Override
    protected Integer getIconResId() {
        return iconResId;
    }

    @Override
    protected boolean isCancelable() {
        return cancelable;
    }

    @Override
    protected int getCode() {
        return code;
    }

    @Override
    protected ZOnDialogDismissListener2 getOnDialogDismissListener() {
        return onDialogDismissListener;
    }

    @Override
    protected boolean isAutoCloseDialog() {
        return isAutoCloseDialog;
    }

    @Override
    public ShareWeChatConfig setTitle(Object title) {
        this.title = title;
        return this;
    }

    @Override
    protected ShareWeChatConfig setHint(Object hint) {
        this.hint = hint;
        return this;
    }

    @Override
    public ShareWeChatConfig setContent(Object content) {
        this.content = content;
        return this;
    }

    @Override
    public ShareWeChatConfig setNegativeButtonTxt(Object negativeButtonTxt) {
        this.negativeButtonTxt = negativeButtonTxt;
        return this;
    }

    @Override
    public ShareWeChatConfig setPositiveButtonTxt(Object positiveButtonTxt) {
        this.positiveButtonTxt = positiveButtonTxt;
        return this;
    }

    @Override
    protected ShareWeChatConfig setIconResId(Integer iconResId) {
        this.iconResId = iconResId;
        return this;
    }

    @Override
    public ShareWeChatConfig setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    @Override
    public ShareWeChatConfig setCode(int code) {
        this.code = code;
        return this;
    }

    @Override
    public ShareWeChatConfig setOnDialogDismissListener(ZOnDialogDismissListener2 onDialogDismissListener) {
        this.onDialogDismissListener = onDialogDismissListener;
        return this;
    }

    @Override
    public ShareWeChatConfig setAutoCloseDialog(boolean autoCloseDialog) {
        this.isAutoCloseDialog = autoCloseDialog;
        return this;
    }
}
