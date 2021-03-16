package com.xhtt.hiddendangermaster.ui.activity.knowledgebase.common;

import com.hg.zero.ui.base.mvp.ZBasePresenter;

/**
 * 详情管理层
 *
 * @author HG
 */

public class FileDetailPresenter extends ZBasePresenter<FileDetailContract.View, FileDetailContract.Model> implements FileDetailContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new FileDetailModel(mView);
    }

}
