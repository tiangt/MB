package com.whzl.mengbi.ui.common;

import android.app.Activity;
import android.text.TextUtils;

import com.whzl.mengbi.util.LogUtils;

import java.util.Stack;

/**
 * @author shaw
 * @date 16/8/9
 */
public class ActivityStackManager {
    private static final String TAG = "ActivityStackManager";
    private static Stack<Activity> mActivityStack;

    private static ActivityStackManager instance = null;

    /**
     * <单例方法>
     *
     * @return 该对象的实例
     */
    public static ActivityStackManager getInstance() {
        if (instance == null) {
            synchronized (ActivityStackManager.class) {
                if (instance == null) {
                    instance = new ActivityStackManager();
                }
            }
        }
        return instance;
    }

    /**
     * <获取当前栈顶Activity>
     *
     * @return
     */
    public Activity getTopActivity() {
        if (mActivityStack == null || mActivityStack.size() == 0) {
            return null;
        }
        Activity activity = mActivityStack.lastElement();

        LogUtils.d("get top activity:" + activity.getClass().getSimpleName());
        return activity;
    }

    /**
     * <将Activity入栈>
     *
     * @param activity
     */
    public void pushActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<Activity>();
        }
        LogUtils.d("push stack activity:" + activity.getClass().getSimpleName());
        mActivityStack.add(activity);
    }

    /**
     * <将Activity出栈>
     *
     * @param activity
     */
    public void popActivity(Activity activity) {
        if (activity != null && mActivityStack.contains(activity)) {
            LogUtils.d("remove current activity:" + activity.getClass().getSimpleName());
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void popActivity(String activity) {
        if (mActivityStack != null) {
            for (int i = 0; i < mActivityStack.size(); i++) {
                LogUtils.d("remove current activity:" + mActivityStack.get(i).getClass().getSimpleName());
                if (TextUtils.equals(mActivityStack.get(i).getClass().getSimpleName(), activity)) {
                    popActivity(mActivityStack.get(i));
                    break;
                }
            }
        }
    }

    /**
     * <退出栈中所有Activity,当前的activity除外>
     *
     * @param cls
     */
    public void popAllActivityExceptMain(Class cls) {
        while (true) {
            Activity activity = getTopActivity();
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
     * <退出栈中所有Activity>
     */
    public void clear() {
        int size = mActivityStack.size();
        for (int i = size - 1; i >= 0; i--) {
            Activity activity = mActivityStack.get(i);
            activity.finish();
        }
        mActivityStack.clear();
    }

    public boolean empty() {
        return mActivityStack.empty();
    }

    public int size() {
        return mActivityStack.size();
    }

}
