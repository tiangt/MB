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

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.whzl.mengbi.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


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
     * @param activity
     * @return
     */
    public static String  getDevice(Activity activity){

        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean){
                            deviceId = DeviceUtils.getDeviceId(activity);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return deviceId;
    }

    /**
     * 从相机获取图片
     * @param activity
     */
    public static void getPicFromCamera(Activity activity) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(new Observer<Boolean>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(aBoolean){
                            //用于保存调用相机拍照后所生成的文件
                            tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
                            //跳转到调用系统相机
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            //判断版本
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
                                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                Uri contentUri = FileProvider.getUriForFile(activity, "com.whzl.mengbi.fileprovider", tempFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                            } else {    //否则使用Uri.fromFile(file)方法获取Uri
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                            }
                            activity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
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
     * @param activity
     */
    public static void getPicFromAlbm(Activity activity){
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                     .subscribe(new Observer<Boolean>() {
                         @Override
                         public void onSubscribe(Disposable d) {

                         }

                         @Override
                         public void onNext(Boolean aBoolean) {
                            if(aBoolean){
                                //用户已同意该权限
                                Matisse.from(activity)
                                        .choose(MimeType.allOf())//选择mime类型
                                        .countable(true)
                                        .maxSelectable(1)//图片选择最多的数量
                                        .gridExpectedSize(activity.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                        .thumbnailScale(0.85f)
                                        .imageEngine(new PicassoEngine())
                                        .theme(R.style.Matisse_Zhihu)
                                        .forResult(ALBUM_REQUEST_CODE);
                            }else{
                                //用户已拒绝该权限
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

}
