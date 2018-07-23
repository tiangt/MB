package com.whzl.mengbi.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.whzl.mengbi.BuildConfig;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


/**
 * @author shaw
 * @date 17/9/5
 */

public class PhotoUtil {

    public static final int SELECT_SINGLE_IMAGE = 10;
    public static final int CROP_IMAGE = 11;
    public static final int CAPTURE = 12;


    public static void capture(Activity activity, String tempCapturePath) {
        File file = new File(tempCapturePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uriForFile = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileprovider", file);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
        } else {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        activity.startActivityForResult(cameraIntent, CAPTURE);
    }

    public static void cropSelectedImage(Activity activity, Uri inUri, String tempOutPath) {
        File file = new File(tempOutPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        //true返回数据bitmap
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        activity.startActivityForResult(intent, CROP_IMAGE);
    }

    public static void cropCapturedImage(Activity activity, File capturedImageFile, String tempOutPath) {
        File fileOut = new File(tempOutPath);
        if (!fileOut.getParentFile().exists()) {
            fileOut.getParentFile().mkdir();
        }
        Uri uriCapture;
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uriCapture = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileprovider", capturedImageFile);
        } else {
            uriCapture = Uri.fromFile(capturedImageFile);
        }
        intent.setDataAndType(uriCapture, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        //true返回数据bitmap
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileOut));
        activity.startActivityForResult(intent, CROP_IMAGE);
    }

    public static void selectAlbums(Activity activity) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, SELECT_SINGLE_IMAGE);
    }

    public static boolean onActivityResult(Activity activity, String tempCapturePath, String tempCropPath, int requestCode, int resultCode, Intent data, UploadListener listenter) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_SINGLE_IMAGE:
                    Uri inUri = data.getData();
                    cropSelectedImage(activity, inUri, tempCropPath);
                    return true;
                case CROP_IMAGE:
                    //如果文件不存在
                    if (tempCropPath == null || "".equals(tempCropPath) || !new File(tempCropPath).exists()) {
                        ToastUtils.showToast("file not exists 1");
                        //开始上传
                    } else if (listenter != null) {
                        listenter.upload();
                    }
                    return true;
                case CAPTURE:
                    if (!TextUtils.isEmpty(tempCapturePath)) {
                        File file = new File(tempCapturePath);
                        if (file.exists())
                            cropCapturedImage(activity, file, tempCropPath);
                        else {
                            ToastUtils.showToast("file not exists 2");
                        }
                    } else {
                        ToastUtils.showToast("file not exists 3");
                    }
                    return true;
            }
        }

        return false;
    }

    public interface UploadListener {
        void upload();
    }
}
