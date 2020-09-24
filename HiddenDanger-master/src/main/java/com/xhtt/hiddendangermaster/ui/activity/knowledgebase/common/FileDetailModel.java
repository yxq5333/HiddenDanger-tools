package com.xhtt.hiddendangermaster.ui.activity.knowledgebase.common;

/**
 * 详情数据层
 *
 * @author HG
 */

public class FileDetailModel implements FileDetailContract.Model {

    private FileDetailContract.View mView;

    public FileDetailModel(FileDetailContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public boolean isViewAttached() {
        return mView != null;
    }

}
