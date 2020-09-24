package com.xhtt.hiddendanger.Bean.HiddenDanger;

import com.google.gson.annotations.SerializedName;
import com.hg.hollowgoods.Adapter.FastAdapter.Annotation.Item.FastItem;
import com.hg.hollowgoods.Adapter.FastAdapter.Constant.FastItemMode;
import com.hg.hollowgoods.Bean.CommonBean.CommonBean;
import com.xhtt.hiddendanger.Constant.Constants;

/**
 * Created by Hollow Goods on 2019-05-15.
 */
public class CheckTableCompany extends CommonBean {

    @FastItem(sortNumber = 10, label = "选择企业", isNotEmpty = true, contentHint = "请选择", mode = FastItemMode.Choose, rightIconName = "ARROW_RIGHT_RES", rightIconNameClass = Constants.class)
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
