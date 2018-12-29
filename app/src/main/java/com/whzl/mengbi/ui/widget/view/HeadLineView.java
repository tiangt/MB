package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ViewFlipper;

import com.whzl.mengbi.R;

import java.util.List;

/**
 * 头条垂直轮播
 *
 * @author cliang
 * @date 2018.12.19
 */
public class HeadLineView extends ViewFlipper {

    private Context mContext;
    private int mInterval = 20000; //间隔时间

    public HeadLineView(Context context) {
        super(context);
        init(context);
    }

    public HeadLineView(Context context,AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        setFlipInterval(mInterval);//设置时间间隔
        setInAnimation(context, R.anim.flipper_in);
        setOutAnimation(context, R.anim.flipper_out);
    }

    public void setInterval(int i){
        mInterval = i;
    }

    /**
     * 设置循环滚动的View数组
     *
     * @param views
     */
    public void setViews(final List<View> views) {
        if (views == null || views.size() == 0) {
            return;
        }
        removeAllViews();
        for (int i = 0; i < views.size(); i++) {
            final int position = i;
            views.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position, views.get(position));
                    }
                }
            });
            addView(views.get(i));
        }
        startFlipping();
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }
}
