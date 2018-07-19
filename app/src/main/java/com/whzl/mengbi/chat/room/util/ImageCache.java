package com.whzl.mengbi.chat.room.util;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * author: yaobo wu
 * date:   On 2018/7/19
 */
public class ImageCache {
    private Map<String, Bitmap> imageCache = new HashMap<>();
    private ReadWriteLock rwlock = new ReentrantReadWriteLock();
    private static class ImageCacheHolder {
        private static final ImageCache instance = new ImageCache();
    }

    public static final ImageCache getInstance(){
        return ImageCacheHolder.instance;
    }

    public Bitmap getBitmapByUrl(String url) {
        Bitmap bitmap = null;
        rwlock.readLock().lock();
        if (imageCache.containsKey(url)) {
            bitmap = imageCache.get(url);
        }
        rwlock.readLock().unlock();
        return bitmap;
    }

    public void putBitmap(String url, Bitmap bitmap) {
        rwlock.writeLock().lock();
        imageCache.put(url, bitmap);
        rwlock.writeLock().unlock();
    }
}
