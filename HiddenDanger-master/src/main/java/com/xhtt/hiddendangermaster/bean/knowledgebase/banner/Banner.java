package com.xhtt.hiddendangermaster.bean.knowledgebase.banner;

import com.xhtt.hiddendangermaster.util.uploadfile.WebFile;

import java.util.ArrayList;

/**
 * Created by Hollow Goods on 2019-04-16.
 */
public class Banner {

    public static final int TYPE_URL = 1;
    public static final int TYPE_APP = 2;

    public static final int LOCATION_MAIN_ACTIVITY = 1;
    public static final int LOCATION_CONTENT = 2;

    private long id;

    private int linkType;

    private String httpUrl;

    private String appUrl;

    private ArrayList<WebFile> fileList;

    private Integer res;

    public Integer getRes() {
        return res;
    }

    public void setRes(Integer res) {
        this.res = res;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public ArrayList<WebFile> getFileList() {
        return fileList;
    }

    public void setFileList(ArrayList<WebFile> fileList) {
        this.fileList = fileList;
    }
}
