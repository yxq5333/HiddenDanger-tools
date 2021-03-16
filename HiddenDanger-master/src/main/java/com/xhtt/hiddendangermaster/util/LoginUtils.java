package com.xhtt.hiddendangermaster.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xhtt.hiddendangermaster.application.MyApplication;
import com.xhtt.hiddendangermaster.bean.User;
import com.xhtt.hiddendangermaster.db.base.DB;
import com.xhtt.hiddendangermaster.ui.activity.login.LoginActivity;

import java.util.List;

public class LoginUtils {

    public static boolean isLogin() {
        MyApplication myApplication = MyApplication.createApplication();
        return myApplication.getUser() != null;
    }

    public static void initUser() {
        MyApplication myApplication = MyApplication.createApplication();
        myApplication.setUser(DB.get().getUserDao().getUser());
    }

    public static void updateUser(User user) {
        DB.get().getUserDao().updateUser(user);
        MyApplication myApplication = MyApplication.createApplication();
        myApplication.setUser(user);
    }

    public static User getUser() {
        MyApplication myApplication = MyApplication.createApplication();
        if (myApplication.getUser() == null) {
            return new User();
        }
        return myApplication.getUser();
    }

    public static void destroyUser() {
        MyApplication myApplication = MyApplication.createApplication();
        myApplication.setUser(null);
        DB.get().getUserDao().deleteUser();
    }

    public static void autoExitApp(Context context) {

        LoginUtils.destroyUser();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
        );
        context.startActivity(intent);

        MyApplication myApplication = MyApplication.createApplication();
        List<Activity> activities = myApplication.getAllActivity();
        for (Activity t : activities) {
            if (t.getClass() != LoginActivity.class) {
                t.finish();
            }
        }
    }

}
