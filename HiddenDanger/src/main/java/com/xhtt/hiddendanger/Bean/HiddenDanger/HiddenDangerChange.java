package com.xhtt.hiddendanger.Bean.HiddenDanger;

import com.hg.hollowgoods.Adapter.FastAdapter.Annotation.Item.FastItem;
import com.hg.hollowgoods.Adapter.FastAdapter.Annotation.Item.FastItemFileMaxCount;
import com.hg.hollowgoods.Adapter.FastAdapter.Constant.FastItemMode;
import com.hg.hollowgoods.Bean.CommonBean.CommonBean;
import com.xhtt.hiddendanger.Constant.Constants;
import com.xhtt.hiddendanger.Constant.SystemConfig;
import com.xhtt.hiddendanger.Util.UploadFile.WebFile;

import java.util.ArrayList;

/**
 * Created by Hollow Goods on 2019-04-08.
 */
public class HiddenDangerChange extends CommonBean {

    public HiddenDangerChange() {
        super(-1);
    }

    public HiddenDangerChange(int itemType) {
        super(itemType);
    }

    @FastItem(sortNumber = 1, label = "整改后照片", mode = FastItemMode.File, rightIconName = "TAKE_PHOTO", rightIconNameClass = Constants.class)
    @FastItemFileMaxCount(maxCount = SystemConfig.FILE_MAX_COUNT)
    private String changePhoto;

    @FastItem(sortNumber = 5, label = "整改说明", contentHint = "请输入")
    private String changeDescribe;

    private ArrayList<WebFile> changePhotoList;// 整改后照片

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

}
