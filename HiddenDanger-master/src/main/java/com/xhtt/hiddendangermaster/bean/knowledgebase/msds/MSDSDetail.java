package com.xhtt.hiddendangermaster.bean.knowledgebase.msds;

/**
 * Created by Hollow Goods on 2019-04-03.
 */
public class MSDSDetail {

    private String label;
    private String[] texts;
    private int itemType;

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String[] getTexts() {
        return texts;
    }

    public void setTexts(String[] texts) {
        this.texts = texts;
    }
}
