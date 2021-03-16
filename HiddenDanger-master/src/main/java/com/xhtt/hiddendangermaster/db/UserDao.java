package com.xhtt.hiddendangermaster.db;

import androidx.room.Dao;

import com.hg.zero.db.ZBaseDao2;
import com.xhtt.hiddendangermaster.bean.User;

/**
 * User Dao层
 * <p>
 * Created by Hollow Goods on 2020-12-17.
 */
@Dao
public interface UserDao extends ZBaseDao2<User> {

    @Override
    default String bindTableName() {
        return User.class.getSimpleName();
    }

    default User getUser() {
        return getFirst();
    }

    default void updateUser(User user) {

        deleteUser();

        if (user != null) {
            insert(user);
        }
    }

    default void deleteUser() {
        deleteAll();
    }

}
