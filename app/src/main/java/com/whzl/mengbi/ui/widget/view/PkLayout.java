package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.whzl.mengbi.R;
import com.whzl.mengbi.util.glide.GlideImageLoader;

/**
 * @author nobody
 * @date 2018/9/24
 */
public class PkLayout extends LinearLayout {

    private ProgressBar progressBar;
    private ImageView ivFount;
    private Context context;
    private ImageView ivRight;

    public PkLayout(Context context) {
        this(context, null);
    }

    public PkLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PkLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PkLayout);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater from = LayoutInflater.from(context);
        View inflate = from.inflate(R.layout.layout_pk, this, false);
        addView(inflate);
        progressBar = inflate.findViewById(R.id.pb_pk);
        ivFount = inflate.findViewById(R.id.iv_left);
        ivRight = inflate.findViewById(R.id.iv_right);
    }

    public void setProgress(int progress) {
        progressBar.setProgress(progress);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setLeftImg(Object object) {
        GlideImageLoader.getInstace().displayImage(context, object, ivFount);
    }

    public void setRightImg(Object object) {
        GlideImageLoader.getInstace().displayImage(context, object, ivRight);
    }
}
