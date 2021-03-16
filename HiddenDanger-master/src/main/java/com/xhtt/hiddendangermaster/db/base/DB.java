package com.xhtt.hiddendangermaster.db.base;

import androidx.room.Room;

import com.hg.zero.config.ZSystemConfig;
import com.xhtt.hiddendangermaster.application.MyApplication;

/**
 * Room层
 * <p>
 * Created by Hollow Goods on 2020-12-17.
 */
public class DB {

    /**
     * 数据库名称
     */
    private static final String DB_NAME = ZSystemConfig.defaultDbName();
    /**
     * DataBase层引用
     */
    private static AppDataBase appDataBase;

    public static AppDataBase get() {

        if (appDataBase == null) {
            synchronized (DB.class) {
                if (appDataBase == null) {
                    appDataBase = Room.databaseBuilder(MyApplication.createApplication(), AppDataBase.class, DB_NAME)
                            //下面注释表示允许主线程进行数据库操作，但是不推荐这样做。
                            //我这里是为了Demo展示，稍后会结束和LiveData和RxJava的使用
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }

        return appDataBase;
    }

}
