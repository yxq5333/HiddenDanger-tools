package com.xhtt.hiddendangermaster.bean;

import com.google.gson.annotations.SerializedName;
import com.hg.zero.net.callback.contract.ZListLimitDataInfo;

/**
 * 列表分页数据
 * <p>
 * Created by Hollow Goods on 2020-04-10.
 */
public class ListLimitDataInfo implements ZListLimitDataInfo {

    private int totalCount;

    @SerializedName("currPage")
    private int pageIndex;

    private int pageSize;

    private int totalPage;

    private Object list;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public Object getList() {
        return list;
    }

    public void setList(Object list) {
        this.list = list;
    }
}
