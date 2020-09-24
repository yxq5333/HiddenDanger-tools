package com.xhtt.hiddendangermaster.ui.activity.knowledgebase.common;

import com.hg.hollowgoods.ui.base.mvp.BasePresenter;

/**
 * 详情管理层
 *
 * @author HG
 */

public class FileDetailPresenter extends BasePresenter<FileDetailContract.View, FileDetailContract.Model> implements FileDetailContract.Presenter {

    @Override
    public void afterAttachView() {
        mModel = new FileDetailModel(mView);
    }

}
