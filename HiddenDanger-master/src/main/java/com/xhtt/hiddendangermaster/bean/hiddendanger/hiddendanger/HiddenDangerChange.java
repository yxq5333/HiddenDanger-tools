package com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger;

import com.hg.hollowgoods.bean.CommonBean;
import com.hg.hollowgoods.bean.file.AppFile;
import com.xhtt.hiddendangermaster.util.uploadfile.WebFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-08.
 */
public class HiddenDangerChange extends CommonBean<HiddenDangerChange> {

    public HiddenDangerChange() {
        super(-1);
    }

    public HiddenDangerChange(int itemType) {
        super(itemType);
    }

    private String changePhoto;

    private String changeDescribe;

    private ArrayList<WebFile> changePhotoList;// 整改后照片

    private List<AppFile> appChangePhotoList;

    public String getChangeDescribe() {
        return changeDescribe;
    }

    public void setChangeDescribe(String changeDescribe) {
        this.changeDescribe = changeDescribe;
    }

    public ArrayList<WebFile> getChangePhotoList() {
        return changePhotoList;
    }

    public void setChangePhotoList(ArrayList<WebFile> changePhotoList) {
        this.changePhotoList = changePhotoList;
    }

    public String getChangePhoto() {
        return changePhoto;
    }

    public void setChangePhoto(String changePhoto) {
        this.changePhoto = changePhoto;
    }

    public List<AppFile> getAppChangePhotoList() {
        return appChangePhotoList;
    }

    public void setAppChangePhotoList(List<AppFile> appChangePhotoList) {
        this.appChangePhotoList = appChangePhotoList;
    }
}
