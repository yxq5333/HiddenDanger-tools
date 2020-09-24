package com.xhtt.hiddendangermaster.constant;

/**
 * Created by Hollow Goods on 2019-04-01.
 */
public enum LawType {

    Legal(1),
    Law(2),
    Rule(3),
    File(4),
    //
    ;

    private int value;

    LawType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
