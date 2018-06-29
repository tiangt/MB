package com.whzl.mengbi.handler;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.whzl.mengbi.util.LogUtils;

import java.lang.ref.WeakReference;

public abstract class BaseHandler extends Handler {

    public static final int ZERO = 0;
    public static final int ONE = 1;

    /** 标记异步操作返回时目标界面已经消失时的处理状态 */
    public static final int ACTIVITY_GONE = -1;

    protected WeakReference<Activity> activityWeakReference;
    protected WeakReference<Fragment> fragmentWeakReference;

    private BaseHandler() {}//构造私有化,让调用者必须传递一个Activity 或者 Fragment的实例

    public BaseHandler(Activity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    public BaseHandler(Fragment fragment) {
        this.fragmentWeakReference = new WeakReference<>(fragment);
    }

    @Override
    public void handleMessage(Message msg) {
        if (activityWeakReference == null || activityWeakReference.get() == null || activityWeakReference.get().isFinishing()) {
            // 确认Activity是否不可用
            LogUtils.i("Activity is gone");
            handleMessage(msg, BaseHandler.ACTIVITY_GONE);
        } else if (fragmentWeakReference == null || fragmentWeakReference.get() == null || fragmentWeakReference.get().isRemoving()) {
            //确认判断Fragment不可用
            LogUtils.i("Fragment is gone");
            handleMessage(msg, BaseHandler.ACTIVITY_GONE);
        } else {
            handleMessage(msg, msg.what);
        }
    }

    /**
     * 抽象方法用户实现,用来处理具体的业务逻辑
     *
     * @param msg
     * @param what
     * {@link BaseHandler}
     */
    public abstract void handleMessage(Message msg, int what);

}
