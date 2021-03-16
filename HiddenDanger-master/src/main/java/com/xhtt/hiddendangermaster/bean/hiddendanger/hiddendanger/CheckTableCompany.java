package com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger;

import com.google.gson.annotations.SerializedName;
import com.hg.zero.bean.ZCommonBean;

/**
 * Created by Hollow Goods on 2019-05-15.
 */
public class CheckTableCompany extends ZCommonBean<CheckTableCompany> {

    @SerializedName("name")
    private String companyName;

    public CheckTableCompany(int itemType) {
        super(itemType);
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
