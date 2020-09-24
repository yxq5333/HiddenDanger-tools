package com.xhtt.hiddendanger.Bean.HiddenDanger;

import com.hg.hollowgoods.Adapter.FastAdapter.Annotation.Item.FastItem;
import com.hg.hollowgoods.Adapter.FastAdapter.Constant.FastItemMode;
import com.hg.hollowgoods.Bean.AppFile;
import com.hg.hollowgoods.Bean.CommonBean.CommonBean;
import com.hg.hollowgoods.Util.StringUtils;
import com.xhtt.hiddendanger.Constant.Constants;
import com.xhtt.hiddendanger.Util.UploadFile.UploadFileUtils;
import com.xhtt.hiddendanger.Util.UploadFile.WebFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hollow Goods on 2019-05-22.
 */
public class ServiceSubmit extends CommonBean {

    @FastItem(sortNumber = 10, label = "检查时间", mode = FastItemMode.Choose, rightIconName = "ARROW_RIGHT_RES", rightIconNameClass = Constants.class, isDate = true, dateFormatMode = StringUtils.DateFormatMode.LINE_YMD)
    private long checkDate;

    @FastItem(sortNumber = 20, label = "整改时间", contentHint = "请选择", mode = FastItemMode.Choose, rightIconName = "CHANGE_TIME_RES", rightIconNameClass = Constants.class)
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

    public void setCheckUserNamePhotoList(AppFile appFile) {

        WebFile webFile = UploadFileUtils.appFile2WebFile(appFile);

        this.checkUserNamePhotoList = new ArrayList<>();
        this.checkUserNamePhotoList.add(webFile);
    }

    public List<WebFile> getChargePersonPhotoList() {
        return chargePersonPhotoList;
    }

    public void setChargePersonPhotoList(AppFile appFile) {

        WebFile webFile = UploadFileUtils.appFile2WebFile(appFile);

        this.chargePersonPhotoList = new ArrayList<>();
        this.chargePersonPhotoList.add(webFile);
    }
}
