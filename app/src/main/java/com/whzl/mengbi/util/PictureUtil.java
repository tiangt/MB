package com.whzl.mengbi.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PictureUtil {


    /**
     * 保存方法
     */
    public static void saveBitmap(Bitmap bm, String picName, Context context) {
        File f = new File(context.getCacheDir(), picName);
        if (f.exists()) {
            if (deleteFileSafely(f)) {
                LogUtils.e("图片已删除");
            }
            //f.delete();
        }
        File newF = new File(context.getCacheDir(), picName);
        try {
            FileOutputStream out = new FileOutputStream(newF);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Log.i("Save Pic", "已经保存" + newF.getAbsolutePath());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //这种方法状态栏是空白，显示不了状态栏的信息
    public static void saveCurrentImage(Activity activity, Context context, String picName, String dirName) {
        //获取当前屏幕的大小
        int width = activity.getWindow().getDecorView().getRootView().getWidth();
        int height = activity.getWindow().getDecorView().getRootView().getHeight();
        //生成相同大小的图片
        Bitmap temBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //找到当前页面的跟布局
        View view = activity.getWindow().getDecorView().getRootView();
        //设置缓存
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //从缓存中获取当前屏幕的图片
        temBitmap = view.getDrawingCache();

        saveImageToGallery(context, temBitmap, picName, dirName);
        view.destroyDrawingCache();//必须清除，否则同一acticity下会造成截屏还是一致的
    }

    //这种方法状态栏是空白，显示不了状态栏的信息
    public static void saveCurrentViewImage(Activity activity, Context context, String picName, View view, String dirName) {
        //获取当前屏幕的大小
        int width = activity.getWindow().getDecorView().getRootView().getWidth();
        int height = activity.getWindow().getDecorView().getRootView().getHeight();
        //生成相同大小的图片
        Bitmap temBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //设置缓存
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //从缓存中获取当前屏幕的图片
        temBitmap = view.getDrawingCache();

        saveImageToGallery(context, temBitmap, picName, dirName);
        view.destroyDrawingCache();//必须清除，否则同一acticity下会造成截屏还是一致的
    }

    public static void saveToMedia(String picName, Context context) {
        File f = new File(context.getCacheDir(), picName);
        // 其次把文件插入到系统图库

        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    f.getAbsolutePath(), picName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        /*Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(f);
		intent.setData(uri);*/
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + context.getCacheDir() + "/" + picName))/*intent*/);
        ToastUtils.showToast("图片已保存至手机");
    }

    public static void saveImageToGallery(Context context, Bitmap bmp, String picName, String dirName) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), dirName);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, picName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), picName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
    }


    /**
     * 把bitmap转换成String
     *
     * @param filePath
     * @return
     */
    public static String bitmapToString(String filePath) {

        Bitmap bm = getSmallBitmap(filePath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    /**
     * 根据路径获得图片并压缩返回bitmap用于显示
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeFile(filePath, options);

        //内存不足时可被回收
        options.inPurgeable = true;

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 720, 1280);
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 根据路径获得图片并压缩返回bitmap用于显示
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static BitmapDrawable getSmallBitmap(Resources resources, int id, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeFile(filePath, options);

        //内存不足时可被回收
        options.inPurgeable = true;

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        InputStream is = resources.openRawResource(id);

        Bitmap bm = BitmapFactory.decodeStream(is, null, options);

        BitmapDrawable bd = new BitmapDrawable(resources, bm);
        return bd;
    }


    /**
     * 头像裁剪
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Bitmap getSmallFaceBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        //内存不足时可被回收
        options.inPurgeable = true;

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 100, 100);
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        /*Bitmap tempBitmap = BitmapFactory.decodeFile(filePath, options);
        //获取这个图片的宽和高
        int width = tempBitmap.getWidth();
        int height = tempBitmap.getHeight();

        //定义预转换成的图片的宽度和高度
        int newWidth = 100;
        int newHeight = 100;

        //计算缩放率，新尺寸除原始尺寸
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();

		//matrix.postScale()

        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(tempBitmap, 0, 0,
                width, height, matrix, true);*/

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 根据路径删除图片
     *
     * @param path
     */
    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 添加到图库
     */
    public static void galleryAddPic(Context context, String path) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 获取保存图片的目录
     *
     * @return
     */
    public static File getAlbumDir() {
        File dir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                getAlbumName());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取保存 隐患检查的图片文件夹名称
     *
     * @return
     */
    public static String getAlbumName() {
        return "ledui";
    }

    public static ByteArrayOutputStream compressImage(ByteArrayOutputStream os, Bitmap image) {

        if (os == null || image == null) {
            return null;
        }

        image.compress(Bitmap.CompressFormat.JPEG, 100, os);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (os.toByteArray().length > 2097152) { // 循环判断如果压缩后图片是否大于2048kb,大于继续压缩
            options -= 10;// 每次都减少10
            os.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, os);// 这里压缩options%，把压缩后的数据存放到baos中
        }

        return os;

    }

    /**
     * 安全删除文件.
     *
     * @param file
     * @return
     */
    public static boolean deleteFileSafely(File file) {
        if (file != null) {
            String tmpPath = file.getParent() + File.separator + System.currentTimeMillis();
            File tmp = new File(tmpPath);
            file.renameTo(tmp);
            return tmp.delete();
        }
        return false;
    }

}
