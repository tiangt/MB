package com.whzl.mengbi.util.glide;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
import com.whzl.mengbi.util.UIUtil;
import com.youth.banner.loader.ImageLoader;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;

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

    }

    /**
     * Gif播放完毕回调
     */

    public interface GifListener {

        void onResourceReady();

        void onFail();
    }

    public void loadRoundImage(Context context, Object object, ImageView imageView, int radius) {
        if (isValidContextForGlide(context)) {
            RoundedCorners roundedCorners = new RoundedCorners(UIUtil.dip2px(context, radius));
            RequestOptions requestOptions = new RequestOptions().transform(roundedCorners);
            Glide.with(context).load(object).apply(requestOptions).into(imageView);
//            RequestOptions transform = new RequestOptions().transform(new GlideRoundTransform(context, radius));
//            Glide.with(context).load(object).apply(transform).into(imageView);
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


    public void loadOneTimeGif(Context context, Object model, final ImageView imageView, final GifListener2 gifListener) {
        if (isValidContextForGlide(context)) {
            RequestOptions options = new RequestOptions().skipMemoryCache(true);
            Glide.with(context).asGif().load(model).apply(options).listener(new RequestListener<GifDrawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                    try {
                        Field gifStateField = GifDrawable.class.getDeclaredField("state");
                        gifStateField.setAccessible(true);
                        Class gifStateClass = Class.forName("com.bumptech.glide.load.resource.gif.GifDrawable$GifState");
                        Field gifFrameLoaderField = gifStateClass.getDeclaredField("frameLoader");
                        gifFrameLoaderField.setAccessible(true);
                        Class gifFrameLoaderClass = Class.forName("com.bumptech.glide.load.resource.gif.GifFrameLoader");
                        Field gifDecoderField = gifFrameLoaderClass.getDeclaredField("gifDecoder");
                        gifDecoderField.setAccessible(true);
                        Class gifDecoderClass = Class.forName("com.bumptech.glide.gifdecoder.GifDecoder");
                        Object gifDecoder = gifDecoderField.get(gifFrameLoaderField.get(gifStateField.get(resource)));
                        Method getDelayMethod = gifDecoderClass.getDeclaredMethod("getDelay", int.class);
                        getDelayMethod.setAccessible(true);
                        //设置只播放一次
                        resource.setLoopCount(1);
                        //获得总帧数
                        int count = resource.getFrameCount();
                        int delay = 0;
                        for (int i = 0; i < count; i++) {
                            //计算每一帧所需要的时间进行累加
                            delay += (int) getDelayMethod.invoke(gifDecoder, i);
                        }
                        imageView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (gifListener != null) {
                                    gifListener.gifPlayComplete();
                                }
                            }
                        }, delay);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            }).into(imageView);
        }
    }

    /**
     * Gif播放完毕回调
     */
    public interface GifListener2 {
        void gifPlayComplete();
    }
}
