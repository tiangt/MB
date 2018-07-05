package com.whzl.mengbi.util.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

    public void circleCropImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        RequestOptions options = new RequestOptions() .circleCrop();
        Glide.with(context).load(path).apply(options).into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        return super.createImageView(context);
    }
}
