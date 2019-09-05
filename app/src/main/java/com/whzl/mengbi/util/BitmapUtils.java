package com.whzl.mengbi.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author cliang
 * @date 2018.11.27
 */
public class BitmapUtils {

    /**
     * View转Bitmap
     *
     * @param v
     * @return
     */
    public static Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    /**
     * Bitmap加光晕
     *
     * @param map
     * @param haloWidthPx
     * @param haloColor
     * @return
     */
    public static Bitmap addHaloForImage(Bitmap map, int haloWidthPx, int haloColor) {
        if (map != null) {
            if (haloWidthPx < 0) {
                haloWidthPx = 20;
            }
            if (haloWidthPx != 0) {
                // method one
                Paint p = new Paint();
                p.setColor(haloColor);
                p.setAntiAlias(true);
                p.setFilterBitmap(true);
                MaskFilter bmf = new BlurMaskFilter(haloWidthPx, BlurMaskFilter.Blur.SOLID);
                p.setMaskFilter(bmf);
                Bitmap d = Bitmap.createBitmap(map.getWidth() + haloWidthPx * 2, map.getHeight() + haloWidthPx * 2, Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(d);
                c.drawBitmap(map.extractAlpha(), haloWidthPx, haloWidthPx, p);
                p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
                c.drawBitmap(map, haloWidthPx, haloWidthPx, p);
                map.recycle();
                System.gc();
                // end
                return d;
            }
        }
        return map;
    }

    /**
     * 圆形Bitmap
     *
     * @param bitmap
     * @return
     */
    public static Bitmap getOvalBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * ImageUrl 转 Bitmap
     *
     * @param path
     * @param listener
     */
    public static void returnBitmap(final String path, final HttpCallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtils.e("Thread.currentThread().getId() "+Thread.currentThread().getId());
                URL imageUrl = null;
                try {
                    imageUrl = new URL(path);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    Bitmap bitmap1 = createBitmapThumbnail(bitmap, false);
                    if (listener != null) {
                        listener.onFinish(bitmap1);
                    }
                    is.close();
                } catch (IOException e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                    e.printStackTrace();
                }
            }

        }).start();
    }

    public static Bitmap createBitmapThumbnail(Bitmap bitmap, boolean needRecycler) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = 80;
        int newHeight = 80;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
//        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postScale(0.8f, 0.8f);
        Bitmap newBitMap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        if (needRecycler) bitmap.recycle();
        return newBitMap;
    }

}
