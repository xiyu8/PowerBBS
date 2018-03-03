package com.example.powerbbs.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 晞余 on 2018/1/18.
 */

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() { //finish()所有activity、清除容器、杀掉进程

        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }

        activities.clear();
        android.os.Process.killProcess(android.os.Process.myPid());

    }

}
