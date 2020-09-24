package com.xhtt.hiddendangermaster.bean.knowledgebase.laws;

import com.xhtt.hiddendangermaster.util.uploadfile.WebFile;

import java.util.ArrayList;

/**
 * Created by Hollow Goods on 2019-04-01.
 */
public class Laws {

    private String title;
    private String content;
    private ArrayList<WebFile> fileList;
    private String memo;

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

    public ArrayList<WebFile> getFileList() {
        return fileList;
    }

    public void setFileList(ArrayList<WebFile> fileList) {
        this.fileList = fileList;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
