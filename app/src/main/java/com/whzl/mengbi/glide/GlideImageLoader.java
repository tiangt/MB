package com.whzl.mengbi.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader{

    private GlideImageLoader(){}
    private static GlideImageLoader glideImageLoader=null;


    public static GlideImageLoader getInstace(){
        if(glideImageLoader==null){
            glideImageLoader = new GlideImageLoader();
        }
        return glideImageLoader;
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path).into(imageView);

    }

    @Override
    public ImageView createImageView(Context context) {
        return super.createImageView(context);
    }
}
