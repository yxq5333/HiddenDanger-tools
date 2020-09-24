package com.xhtt.hiddendangermaster.bean.knowledgebase.banner;

/**
 * Created by Hollow Goods on 2019-04-16.
 */
public enum BannerAppUrl {

    KnowledgeBase("知识库"),
    //
    ;

    private String url;

    BannerAppUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
