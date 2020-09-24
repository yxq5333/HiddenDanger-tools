package com.xhtt.hiddendangermaster.bean.knowledgebase.common;

import com.xhtt.hiddendangermaster.util.uploadfile.WebFile;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 文件详情封装类
 * Created by Hollow Goods on 2019-04-03.
 */
public class FileDetail implements Serializable {

    private String activityTitle;// 界面标题
    private String title;// 文件标题
    private String releaseTime;// 发布时间
    private String author;// 作者
    private String content;// 正文
    private ArrayList<WebFile> webFiles;// 附件
    private String memo;// 备注

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<WebFile> getWebFiles() {
        return webFiles;
    }

    public void setWebFiles(ArrayList<WebFile> webFiles) {
        this.webFiles = webFiles;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
