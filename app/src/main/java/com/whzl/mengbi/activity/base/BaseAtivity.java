package com.whzl.mengbi.activity.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.whzl.mengbi.eventbus.EventBusBean;
import com.whzl.mengbi.eventbus.EventBusUtils;
import com.whzl.mengbi.util.PermissionUtils;
import com.whzl.mengbi.util.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @function 是为我们所有的activity提供公共的行为
 */
public abstract class BaseAtivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    /**
     *  输出日志
     */

    private String TAG;


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

    /**
     * 申请权限
     * @param activity
     * @param code
     */
    protected  void requestPermissions(Activity activity, int code){
        PermissionUtils.requestPermission(activity,code,mPermissionGrant);
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

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_READ_PHONE_STATE:
                    //Toast.makeText(LoginActivity.this, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
                    //ToastUtils.showToast("Result Permission Grant CODE_READ_PHONE_STATE");
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 当权限请求已完成时接收。
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this,requestCode,permissions,grantResults,mPermissionGrant);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isRegisterEventBus()){
            EventBusUtils.unregister(this);
        }
    }
}
