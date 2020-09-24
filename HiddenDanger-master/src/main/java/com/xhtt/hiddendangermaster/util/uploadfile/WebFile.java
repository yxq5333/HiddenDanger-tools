package com.xhtt.hiddendangermaster.util.uploadfile;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Hollow Goods on 2019-04-03.
 */
public class WebFile implements Serializable {

    /**
     * 文件原始名称
     */
    @SerializedName("fileOriginalName")
    private String fileOldName;

    /**
     * 文件保存名称（用于图片请求）
     */
    @SerializedName(value = "name", alternate = {"fileName"})
    private String fileSaveName;

    public String getFileOldName() {
        return fileOldName;
    }

    public void setFileOldName(String fileOldName) {
        this.fileOldName = fileOldName;
    }

    public String getFileSaveName() {
        return fileSaveName;
    }

    public void setFileSaveName(String fileSaveName) {
        this.fileSaveName = fileSaveName;
    }
}
