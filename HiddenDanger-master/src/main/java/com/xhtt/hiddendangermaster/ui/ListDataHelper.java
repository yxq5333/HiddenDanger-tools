package com.xhtt.hiddendangermaster.ui;

/**
 * 列表数据助手
 * <p>
 * Created by Hollow Goods on 2019-07-31.
 */
public interface ListDataHelper {

    /**
     * 下拉刷新
     */
    void doRefresh();

    /**
     * 上拉加载
     */
    default void doLoadMore() {

    }

    /**
     * 搜索
     */
    default void doSearch(String searchKey) {

    }

    /**
     * 数据改变刷新（用于删除数据、新增数据）
     */
    default void doDataChangeRefresh() {

    }

}
