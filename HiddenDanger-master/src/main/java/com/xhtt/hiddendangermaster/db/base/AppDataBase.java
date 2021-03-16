package com.xhtt.hiddendangermaster.db.base;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.xhtt.hiddendangermaster.bean.User;
import com.xhtt.hiddendangermaster.db.UserDao;

/**
 * DataBase层
 * <p>
 * Created by Hollow Goods on 2020-12-17.
 */
@Database(entities = {User.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract UserDao getUserDao();

}
