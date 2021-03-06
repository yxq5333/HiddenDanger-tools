package com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger;

import com.hg.zero.bean.ZCommonBean;
import com.hg.zero.file.ZAppFile;
import com.xhtt.hiddendangermaster.util.uploadfile.UploadFileUtils;
import com.xhtt.hiddendangermaster.util.uploadfile.WebFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hollow Goods on 2019-05-22.
 */
public class ServiceSubmit extends ZCommonBean<ServiceSubmit> {

    private long checkDate;

    private String changeDate;

    private List<WebFile> checkUserNamePhotoList;

    private List<WebFile> chargePersonPhotoList;

    public ServiceSubmit() {
        super(-1);
    }

    public long getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(long checkDate) {
        this.checkDate = checkDate;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    public List<WebFile> getCheckUserNamePhotoList() {
        return checkUserNamePhotoList;
    }

    public void setCheckUserNamePhotoList(ZAppFile appFile) {

        WebFile webFile = UploadFileUtils.appFile2WebFile(appFile);

        this.checkUserNamePhotoList = new ArrayList<>();
        this.checkUserNamePhotoList.add(webFile);
    }

    public List<WebFile> getChargePersonPhotoList() {
        return chargePersonPhotoList;
    }

    public void setChargePersonPhotoList(ZAppFile appFile) {

        WebFile webFile = UploadFileUtils.appFile2WebFile(appFile);

        this.chargePersonPhotoList = new ArrayList<>();
        this.chargePersonPhotoList.add(webFile);
    }
}
