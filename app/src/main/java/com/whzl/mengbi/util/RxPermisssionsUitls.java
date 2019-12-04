package com.whzl.mengbi.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.CompoundButton;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.whzl.mengbi.R;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * @author shaw
 */
public class RxPermisssionsUitls {
    //相机请求码
    public static final int CAMERA_REQUEST_CODE = 1;
    //相册请求码
    public static final int ALBUM_REQUEST_CODE = 2;
    //调用照相机返回图片文件
    private static File tempFile;
    //设备标识ID
    private static String deviceId;

    /**
     * 设备信息权限
     *
     * @param activity
     * @return
     */
    public static void getDevice(Activity activity, OnPermissionListener listener) {

        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            listener.onGranted();
                        }else {
                            listener.onDeny();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 从相机获取图片
     *
     * @param activity
     */
    public static void getPicFromCamera(Activity activity, String tempPath) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            PhotoUtil.capture(activity, tempPath);
                        } else {
                            ToastUtils.showToast(R.string.permission_denid);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 从相册获取图片
     *
     * @param activity
     */
    public static void getPicFromAlbm(Activity activity) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            //用户已同意该权限
                            PhotoUtil.selectAlbums(activity);
                        } else {
                            //用户已拒绝该权限
                            ToastUtils.showToast(R.string.permission_storage_denid);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static void getWrite(Activity activity, OnPermissionListener listener) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            listener.onGranted();
                        }else {
                            listener.onDeny();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



    private OnPermissionListener permissionListener;

    public interface OnPermissionListener {
        void onGranted();
        void onDeny();
    }

}
