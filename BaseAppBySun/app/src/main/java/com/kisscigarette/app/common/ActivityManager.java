package com.kisscigarette.app.common;

import android.app.Activity;
import android.util.Log;

import java.util.Stack;

/**
 * activity管理类
 */
public class ActivityManager {

    /**
     * 接收activity的Stack
     */
    private static Stack<Activity> activityStack = new Stack<Activity>();

    /**
     * 将activity移出栈
     *
     * @param activity
     */
    public static void popActivity(Activity activity) {
        if (activity != null) {
            ActivityManager.activityStack.remove(activity);
            Log.d("ActivityManager->pop", activity.getClass().getSimpleName());
        }
    }

    /**
     * 结束指定activity
     *
     * @param activity
     */
    public static void endActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            ActivityManager.activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 获得当前的activity(即最上层)
     *
     * @return
     */
    public static Activity currentActivity() {
        Activity activity = null;
        if (!ActivityManager.activityStack.empty())
            activity = ActivityManager.activityStack.lastElement();
        return activity;
    }

    /**
     * 将activity推入栈内
     *
     * @param activity
     */
    public static void pushActivity(Activity activity) {
        if (ActivityManager.activityStack == null) {
            ActivityManager.activityStack = new Stack<Activity>();
        }
        ActivityManager.activityStack.add(activity);
        Log.d("ActivityManager->push", activity.getClass().getSimpleName());
    }

    /**
     * 弹出除cls外的所有activity
     *
     * @param cls
     */
    public static void popAllActivityExceptOne(Class<? extends Activity> cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }

    /**
     * 结束除cls之外的所有activity,执行结果都会清空Stack
     *
     * @param cls
     */
    public static void finishAllActivityExceptOne(Class<? extends Activity> cls) {
        while (!ActivityManager.activityStack.empty()) {
            Activity activity = currentActivity();
            if (activity.getClass().equals(cls)) {
                popActivity(activity);
            } else {
                endActivity(activity);
            }
        }
    }

    /**
     * 结束所有activity
     */
    public static void finishAllActivity() {
        if (ActivityManager.activityStack != null) {
            synchronized (ActivityManager.activityStack) {
                while (!ActivityManager.activityStack.empty()) {
                    Activity activity = currentActivity();
                    endActivity(activity);
                }
            }
        }
    }

    /**
     * 退出app
     */
    public static void exitApp() {
        finishAllActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
