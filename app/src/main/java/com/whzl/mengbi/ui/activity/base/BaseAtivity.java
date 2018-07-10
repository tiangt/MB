package com.whzl.mengbi.ui.activity.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.whzl.mengbi.R;
import com.whzl.mengbi.eventbus.EventBusBean;
import com.whzl.mengbi.eventbus.EventBusUtils;
import com.whzl.mengbi.ui.widget.view.GenericToolbar;
import com.whzl.mengbi.ui.widget.view.Style;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.functions.Consumer;

/**
 * @function 是为我们所有的activity提供公共的行为
 */
public abstract class BaseAtivity extends AppCompatActivity{

    /**
     *  输出日志
     */

    private String TAG;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getComponentName().getShortClassName();
        if(isRegisterEventBus()){
            EventBusUtils.register(this);
        }
    }


    /**
     * 是否注册事件分发
     * @return true绑定EventBus事件分发，默认不绑定，子类需要绑定的话复写此方法返回true.
     */
    protected boolean isRegisterEventBus(){
        return false;
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(EventBusBean event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(EventBusBean event) {
        if (event != null) {
            receiveStickyEvent(event);
        }
    }



    /**
     * 接收到分发到事件
     * @param event 事件
     */
    protected void receiveEvent(EventBusBean event) {

    }

    /**
     * 接受到分发的粘性事件
     * @param event 粘性事件
     */
    protected void receiveStickyEvent(EventBusBean event) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isRegisterEventBus()){
            EventBusUtils.unregister(this);
        }
    }

    public boolean isActivityFinished() {
        if (Build.VERSION.SDK_INT >= 17) {
            return isFinishing() || isDestroyed();
        } else {
            return isFinishing();
        }
    }

    protected void showToast(String msg){
        if(isActivityFinished()){
            return;
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT);
    }

    protected void showToast(int msgRes){
        if(isActivityFinished()){
            return;
        }
        Toast.makeText(this, msgRes, Toast.LENGTH_SHORT);
    }

}
