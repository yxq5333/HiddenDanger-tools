package com.xhtt.hiddendangermaster.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xhtt.hiddendangermaster.application.MyApplication;
import com.xhtt.hiddendangermaster.bean.User;
import com.xhtt.hiddendangermaster.db.UserDBHelper;
import com.xhtt.hiddendangermaster.ui.activity.login.LoginActivity;

import java.util.List;

public class LoginUtils {

    public static boolean isLogin() {
        MyApplication myApplication = MyApplication.create();
        return myApplication.getUser() != null;
    }

    public static void initUser() {
        MyApplication myApplication = MyApplication.create();
        myApplication.setUser(new UserDBHelper().findFirst());
    }

    public static void updateUser(User user) {
        new UserDBHelper().updateAll(user);
        MyApplication myApplication = MyApplication.create();
        myApplication.setUser(user);
    }

    public static User getUser() {
        MyApplication myApplication = MyApplication.create();
        if (myApplication.getUser() == null) {
            return new User();
        }
        return myApplication.getUser();
    }

    public static void destroyUser() {
        MyApplication myApplication = MyApplication.create();
        myApplication.setUser(null);
        new UserDBHelper().deleteAll();
    }

    public static void autoExitApp(Context context) {

        LoginUtils.destroyUser();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
        );
        context.startActivity(intent);

        MyApplication myApplication = MyApplication.create();
        List<Activity> activities = myApplication.getAllActivity();
        for (Activity t : activities) {
            if (t.getClass() != LoginActivity.class) {
                t.finish();
            }
        }
    }

}
