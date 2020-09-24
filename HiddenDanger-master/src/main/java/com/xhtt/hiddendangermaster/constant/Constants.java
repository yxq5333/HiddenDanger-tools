package com.xhtt.hiddendangermaster.constant;

import com.hg.hollowgoods.ui.base.message.dialog2.ChoiceItem;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenLevel;

import java.util.ArrayList;

/**
 * Created by Hollow Goods on 2019-04-16.
 */
public class Constants {

    public static final int ARROW_RIGHT_RES = R.drawable.ic_my_arrow_right;

    public static final String DEFAULT_SEARCH_KEY = "@$*#XHTT#*$@";

    public static final int HISTORY_CODE_LAWS_LEGAL = 1000;
    public static final int HISTORY_CODE_LAWS_LAW = 1001;
    public static final int HISTORY_CODE_LAWS_RULE = 1002;
    public static final int HISTORY_CODE_LAWS_FILE = 1003;

    public static final int LIST_ITEM_TYPE_1 = 0;
    public static final int LIST_ITEM_TYPE_2 = 1;
    public static final int LIST_ITEM_TYPE_3 = 2;
    public static final int LIST_ITEM_TYPE_4 = 3;
    public static final int LIST_ITEM_TYPE_5 = 4;
    public static final int LIST_ITEM_TYPE_6 = 5;
    public static final int LIST_ITEM_TYPE_7 = 6;
    public static final int LIST_ITEM_TYPE_8 = 7;
    public static final int LIST_ITEM_TYPE_9 = 8;
    public static final int LIST_ITEM_TYPE_10 = 9;
    public static final int LIST_ITEM_TYPE_11 = 10;
    public static final int LIST_ITEM_TYPE_12 = 11;
    public static final int LIST_ITEM_TYPE_13 = 12;
    public static final int LIST_ITEM_TYPE_14 = 13;
    public static final int LIST_ITEM_TYPE_15 = 14;

    public static String[] PROPORTION = {
            "规上",
            "规下",
    };
    public static ArrayList<ChoiceItem> PROPORTION_OBJ = new ArrayList<ChoiceItem>() {
        {
            add(new ChoiceItem("规上", "年主营业务收入在2000万元及以上的法人工业企业"));
            add(new ChoiceItem("规下", "年主营业务收入在2000万元以下的法人工业企业"));
        }
    };
    public static ArrayList<HiddenLevel> HIDDEN_LEVEL = new ArrayList<HiddenLevel>() {
        {
            add(new HiddenLevel(1, "重大隐患"));
            add(new HiddenLevel(2, "一般隐患"));
        }
    };
    public static ArrayList<ChoiceItem> HIDDEN_LEVEL_OBJ = new ArrayList<ChoiceItem>() {
        {
            add(new ChoiceItem("重大隐患"));
            add(new ChoiceItem("一般隐患"));
        }
    };
    public static String[] BUSINESS = {
            "冶金",
            "有色",
            "建材",
            "机械",
            "轻工",
            "纺织",
            "烟草",
            "商贸",
            "化工",
    };
    public static ArrayList<ChoiceItem> BUSINESS_OBJ = new ArrayList<ChoiceItem>() {
        {
            add(new ChoiceItem("冶金"));
            add(new ChoiceItem("有色"));
            add(new ChoiceItem("建材"));
            add(new ChoiceItem("机械"));
            add(new ChoiceItem("轻工"));
            add(new ChoiceItem("纺织"));
            add(new ChoiceItem("烟草"));
            add(new ChoiceItem("商贸"));
            add(new ChoiceItem("化工"));
        }
    };
    public static String[] CHANGE_TIME = {
            "半个月",
            "1个月",
            "3个月",
    };
    public static ArrayList<ChoiceItem> CHANGE_TIME_OBJ = new ArrayList<ChoiceItem>() {{
        add(new ChoiceItem("半个月"));
        add(new ChoiceItem("1个月"));
        add(new ChoiceItem("3个月"));
    }};

    public static final int IMAGE_LOAD_TYPE_HIDDEN_DANGER_LIST = 4000;

    public static final int TAKE_PHOTO = R.drawable.ic_take_photo;
    public static final int HIDDEN_BASE = R.drawable.ic_hidden_base;

    public static final int CHANGE_TIME_RES = R.drawable.ic_change_time;

}
