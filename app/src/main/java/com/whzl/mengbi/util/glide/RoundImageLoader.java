package com.whzl.mengbi.util.glide;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.whzl.mengbi.ui.widget.view.RoundImageView;
import com.youth.banner.loader.ImageLoader;

/**
 * @author cliang
 * @date 2018.10.26
 */
public class RoundImageLoader extends ImageLoader {

    private RoundImageLoader() {
    }

    private static RoundImageLoader glideImageLoader = null;


    public static RoundImageLoader getInstace() {
        if (glideImageLoader == null) {
            glideImageLoader = new RoundImageLoader();
        }
        return glideImageLoader;
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }

    //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
    @Override
    public ImageView createImageView(Context context) {
        RoundImageView roundImageView = new RoundImageView(context);
        return roundImageView;
    }
}
