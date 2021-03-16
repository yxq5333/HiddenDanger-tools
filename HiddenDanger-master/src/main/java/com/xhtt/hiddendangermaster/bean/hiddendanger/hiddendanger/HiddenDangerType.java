package com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger;

import androidx.annotation.NonNull;

import com.hg.zero.adapter.ZSimpleChoiceItem;

/**
 * 隐患类型
 * <p>
 * Created by Hollow Goods on 2020-11-17.
 */
public class HiddenDangerType implements ZSimpleChoiceItem {

    private long id;

    private String name;

    public long getId() {
        return id;
    }

    public HiddenDangerType setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public HiddenDangerType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Long getSimpleChoiceItemId() {
        return id;
    }

    @NonNull
    @Override
    public Object getSimpleChoiceItem() {
        return name;
    }

    @Override
    public String getSimpleChoiceItemDescribe() {
        return null;
    }
}
