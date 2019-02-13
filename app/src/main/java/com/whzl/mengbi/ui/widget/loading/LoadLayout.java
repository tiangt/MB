package com.whzl.mengbi.ui.widget.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.whzl.mengbi.R;

/**
 * @author cliang
 * @date 2019.2.13
 */
public class LoadLayout extends RelativeLayout {

    private View inflate;
    private Context context;
    private ImageView ivLoading;

    public LoadLayout(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public LoadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public LoadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater from = LayoutInflater.from(context);
        inflate = from.inflate(R.layout.layout_load, this, false);
        addView(inflate);
        ivLoading = inflate.findViewById(R.id.iv_loading);
        Glide.with(context).asGif().load(R.drawable.load_three_ball).into(ivLoading);
    }

//    public void startLoad(Context context){
//        Glide.with(context).asGif().load(R.drawable.load_three_ball).into(ivLoading);
//    }
}
