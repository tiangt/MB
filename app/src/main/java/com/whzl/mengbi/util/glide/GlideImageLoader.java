package com.whzl.mengbi.util.glide;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {

    private GlideImageLoader() {
    }

    private static GlideImageLoader glideImageLoader = null;


    public static GlideImageLoader getInstace() {
        if (glideImageLoader == null) {
            glideImageLoader = new GlideImageLoader();
        }
        return glideImageLoader;
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path).into(imageView);
    }

    public void circleCropImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(context).load(path).apply(options).into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        return super.createImageView(context);
    }

    public void loadGif(Context context, Object model, final ImageView imageView, final GifListener gifListener) {

        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(context).asGif().apply(requestOptions).load(model).listener(new RequestListener<GifDrawable>() {

            @Override

            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                gifListener.onFail();
                return false;

            }

            @Override

            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                gifListener.onResourceReady();
                return false;

            }

        }).into(imageView);

    }

    /**
     * Gif播放完毕回调
     */

    public interface GifListener {

        void onResourceReady();

        void onFail();
    }

}
