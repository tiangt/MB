package com.whzl.mengbi.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;

import com.lht.paintview.util.Constant;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static com.facebook.common.internal.ByteStreams.copy;

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
}
