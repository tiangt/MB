package com.whzl.mengbi.ui.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.whzl.mengbi.R;
import com.whzl.mengbi.eventbus.EventBusBean;
import com.whzl.mengbi.eventbus.EventBusUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.widget.view.GenericToolbar;
import com.whzl.mengbi.widget.view.Style;

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getComponentName().getShortClassName();
        if(isRegisterEventBus()){
            EventBusUtils.register(this);
        }
    }

    /**
     * 实例化toolbar标题
     */
    protected void  initToolBar(String title){
        new GenericToolbar.Builder(this)
                .setStatusBarStyle(Style.TRANSPARENT)
                .addTitleText(title)// 标题文本
                .setBackgroundColorResource(R.color.colorPrimary)// 背景颜色
                .addLeftIcon(1, R.drawable.ic_login_return,new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })// 响应左部图标的点击事件
                .setTextColor(getResources().getColor(R.color.colorPrimaryDark))// 会全局设置字体的颜色, 自定义标题除外
                .apply();
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
     * @param requestCode
     */
    protected void requstRxPermissions(Activity activity,String requestCode){
            RxPermissions rxPermissions = new RxPermissions(activity);
            rxPermissions.requestEach(requestCode)
                    .subscribe(new Consumer<Permission>() {
                        @Override
                        public void accept(Permission permission) throws Exception {
                            if(permission.granted){
                                //用户已同意该权限
                                LogUtils.d("用户已同意该权限");
                            }else if(permission.shouldShowRequestPermissionRationale){
                                // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                                LogUtils.d("用户拒绝了该权限，没有选中『不再询问』");
                            }else {
                                //用户拒绝了该权限，并且选中『不再询问』
                                LogUtils.d("用户拒绝了该权限，并且选中『不再询问』");
                            }
                        }
                    });
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
}
