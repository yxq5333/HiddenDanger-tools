package com.xhtt.hiddendangermaster.bean.knowledgebase.accidentcase;

import com.google.gson.annotations.SerializedName;
import com.xhtt.hiddendangermaster.util.uploadfile.WebFile;

import java.util.ArrayList;

/**
 * 事故案例
 * Created by Hollow Goods on 2019-03-29.
 */
public class AccidentCase {

    private long id;

    private String title;
    private String content;

    private String createTime;

    private long date;

    @SerializedName("viewCnt")
    private int readCount;

    private ArrayList<WebFile> fileList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public ArrayList<WebFile> getFileList() {
        return fileList;
    }

    public void setFileList(ArrayList<WebFile> fileList) {
        this.fileList = fileList;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }
}
