package com.whzl.mengbi.util.glide;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.whzl.mengbi.BuildConfig;
import com.whzl.mengbi.R;
import com.whzl.mengbi.util.UIUtil;
import com.youth.banner.loader.ImageLoader;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        if (isValidContextForGlide(context)) {
            Glide.with(context).load(path).into(imageView);
        }
    }

    public void displayImageOption(Context context, Object path, ImageView imageView, RequestOptions options) {
        //Glide 加载图片简单用法
        if (isValidContextForGlide(context)) {
            Glide.with(context).load(path).apply(options).into(imageView);
        }
    }

    public void displayImageNoCache(Context context, Object path, ImageView imageView) {
        if (isValidContextForGlide(context)) {
            RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);
            Glide.with(context).load(path).apply(options).into(imageView);
        }
    }

    public void circleCropImage(Context context, Object path, ImageView imageView) {
        if (isValidContextForGlide(context)) {
            RequestOptions options = new RequestOptions().circleCrop();
            Glide.with(context).load(path).apply(options).into(imageView);
        }
    }

    /**
     * 节目封面
     */
    public void displayProgramCover(Context context, Object object, ImageView imageView, int radius) {
        if (isValidContextForGlide(context)) {
            RoundedCorners roundedCorners = new RoundedCorners(UIUtil.dip2px(context, radius));
            RequestOptions requestOptions = new RequestOptions().transform(roundedCorners);
            Glide.with(context).load(object).apply(requestOptions)
                    .thumbnail(loadProgramTransform(context, R.drawable.ic_program_place, radius)).transition(withCrossFade()).into(imageView);
        }
    }

    private static RequestBuilder<Drawable> loadProgramTransform(Context context, @DrawableRes int placeholderId, float radius) {
        return Glide.with(context)
                .load(placeholderId)
                .apply(new RequestOptions().centerCrop()
                        .transform(new RoundedCorners(UIUtil.dip2px(context, radius))));
    }

    /**
     * 圆形头像
     */
    public void displayCircleAvatar(Context context, Object object, ImageView imageView) {
        if (isValidContextForGlide(context)) {
            RequestOptions options = new RequestOptions().circleCrop();
            Glide.with(context).load(object).apply(options)
                    .thumbnail(loadAvatarTransform(context, R.drawable.ic_avatar_place)).transition(withCrossFade()).into(imageView);
        }
    }

    private static RequestBuilder<Drawable> loadAvatarTransform(Context context, @DrawableRes int placeholderId) {
        return Glide.with(context)
                .load(placeholderId)
                .apply(new RequestOptions().circleCrop());
    }

    /**
     * 圆角头像
     */
    public void displayRoundAvatar(Context context, Object object, ImageView imageView, int radius) {
        if (isValidContextForGlide(context)) {
            RoundedCorners roundedCorners = new RoundedCorners(UIUtil.dip2px(context, radius));
            RequestOptions requestOptions = new RequestOptions().transform(roundedCorners);
            Glide.with(context).load(object).apply(requestOptions)
                    .thumbnail(loadRoundAvatarTransform(context, R.drawable.ic_avatar_place, radius)).transition(withCrossFade()).into(imageView);
        }
    }

    private static RequestBuilder<Drawable> loadRoundAvatarTransform(Context context, @DrawableRes int placeholderId, float radius) {
        return Glide.with(context)
                .load(placeholderId)
                .apply(new RequestOptions().centerCrop()
                        .transform(new RoundedCorners(UIUtil.dip2px(context, radius))));
    }

    @Override
    public ImageView createImageView(Context context) {
        return super.createImageView(context);
    }

    public void loadGif(Context context, Object model, final ImageView imageView, final GifListener gifListener) {
        if (isValidContextForGlide(context)) {
            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(context).asGif().apply(requestOptions).load(model).listener(new RequestListener<GifDrawable>() {

                @Override

                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                    if (gifListener != null) {
                        gifListener.onFail();
                    }
                    return false;

                }

                @Override

                public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                    if (gifListener != null) {
                        gifListener.onResourceReady();
                    }
                    return false;

                }

            }).into(imageView);
        }

    }

    /**
     * Gif播放完毕回调
     */

    public interface GifListener {

        void onResourceReady();

        void onFail();
    }

    /**
     * 圆角
     */
    public void loadRoundImage(Context context, Object object, ImageView imageView, int radius) {
        if (isValidContextForGlide(context)) {
            RoundedCorners roundedCorners = new RoundedCorners(UIUtil.dip2px(context, radius));
            RequestOptions requestOptions = new RequestOptions().transform(roundedCorners);
            Glide.with(context).load(object).apply(requestOptions).into(imageView);
        }
    }

    public class GlideRoundTransform extends BitmapTransformation {
        private final int VERSION = 1;
        private final String ID = BuildConfig.APPLICATION_ID + "GlideRoundedCornersTransform." + VERSION;
        private final byte[] ID_BYTES = ID.getBytes(CHARSET);


        private Context context;
        private int radius;

        public GlideRoundTransform(Context context, int radius) {
            super();
            this.context = context;
            this.radius = radius;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) {
                return null;
            }

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, UIUtil.dip2px(context, radius), UIUtil.dip2px(context, radius), paint);
            return result;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof GlideRoundTransform;
        }


        @Override
        public int hashCode() {
            return ID.hashCode();
        }


        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
            messageDigest.update(ID_BYTES);
        }
    }

}
