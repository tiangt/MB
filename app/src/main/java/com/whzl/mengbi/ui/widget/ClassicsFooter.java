package com.whzl.mengbi.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Engine;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.util.glide.GlideImageLoader;

/**
 * @author cliang
 * @date 2018.10.24
 */
public class ClassicsFooter extends LinearLayout implements RefreshFooter {

    private TextView mFooterText;//标题文本
    private ImageView mProgressView;//刷新动画视图

    public ClassicsFooter(Context context) {
        super(context);
        initView(context);
    }

    public ClassicsFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public ClassicsFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }

    @SuppressLint("ResourceAsColor")
    private void initView(Context context) {
        setGravity(Gravity.CENTER);
        mFooterText = new TextView(context);
        mProgressView = new ImageView(context);
        GlideImageLoader.getInstace().displayImage(context, R.drawable.home_footer_loading, mProgressView);
        addView(mProgressView, DensityUtil.dp2px(20), DensityUtil.dp2px(20));
        addView(mFooterText, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        setMinimumHeight(DensityUtil.dp2px(60));
    }

    @NonNull
    public View getView() {
        return this;//真实的视图就是自己，不能返回null
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int headHeight, int maxDragHeight) {
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        return 0;//延迟300毫秒之后再弹回
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
            case PullUpToLoad:
//                mProgressView.setVisibility(GONE);//隐藏动画
                break;
            case Loading:
                mFooterText.setText("加载更多…");
                mFooterText.setPadding(12, 0, 0, 0);
                mFooterText.setTextColor(Color.argb(255, 185, 185, 185));
                mProgressView.setVisibility(VISIBLE);//显示加载动画
                break;
            case ReleaseToLoad:
                break;

        }
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int maxDragHeight) {
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return false;
    }


}
