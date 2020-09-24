package com.xhtt.hiddendangermaster.bean.knowledgebase.dangerproduct;

import java.io.Serializable;

/**
 * Created by Hollow Goods on 2019-04-01.
 */
public class DangerProduct implements Serializable {

    private long id;
    private String nameProd;// 品名
    private String nameAlias;// 别名
    private String nameEn;// 英文名
    private String cas;// CAS
    private String typeDanger;// 危险性类别
    private Boolean isRankPoison;// 是否剧毒
    private Boolean isHighToxic;// 是否高毒
    private Boolean isEasyMakePoison;// 是否易制毒
    private Boolean isEasyExploding;// 是否易制爆
    private Boolean isKeySupervision;// 是否重点监管

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameProd() {
        return nameProd;
    }

    public void setNameProd(String nameProd) {
        this.nameProd = nameProd;
    }

    public String getNameAlias() {
        return nameAlias;
    }

    public void setNameAlias(String nameAlias) {
        this.nameAlias = nameAlias;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public String getTypeDanger() {
        return typeDanger;
    }

    public void setTypeDanger(String typeDanger) {
        this.typeDanger = typeDanger;
    }

    public Boolean getRankPoison() {
        return isRankPoison;
    }

    public void setRankPoison(Boolean rankPoison) {
        isRankPoison = rankPoison;
    }

    public Boolean getHighToxic() {
        return isHighToxic;
    }

    public void setHighToxic(Boolean highToxic) {
        isHighToxic = highToxic;
    }

    public Boolean getEasyMakePoison() {
        return isEasyMakePoison;
    }

    public void setEasyMakePoison(Boolean easyMakePoison) {
        isEasyMakePoison = easyMakePoison;
    }

    public Boolean getEasyExploding() {
        return isEasyExploding;
    }

    public void setEasyExploding(Boolean easyExploding) {
        isEasyExploding = easyExploding;
    }

    public Boolean getKeySupervision() {
        return isKeySupervision;
    }

    public void setKeySupervision(Boolean keySupervision) {
        isKeySupervision = keySupervision;
    }
}
