package com.whzl.mengbi.ui.widget.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.whzl.mengbi.util.DeviceUtils;

/**
 * @author nobody
 * @date 2019/4/11
 */
public class UnclickLinearLayout extends LinearLayout {
    private static final int STATE_NORMAL = 0x000; // 正常状态（没有显示头部布局和底部布局）
    private static final int STATE_PULLING = 0x001; // 正在下拉或上拉，但没有达到加载上一个房间或加载下一个房间的要求的状态
    private static final int STATE_PREPARED = 0x002; // 达到刷新或加载的要求，松开手指就可以加载上一个房间或加载下一个房间的状态
    private static final int STATE_REFRESHING = 0x003; // 正在加载上一个房间或加载下一个房间的状态

    private Context context;

    private boolean isRefreshable; // 是否可以下拉刷新下一个房间的数据

    private boolean isLoadable; // 是否可以上拉加载上一个房间的数据
    private float moveHeight; //有效移动距离
    private float startY; // 记录手指按下时的Y坐标位置
    private float startX; // 记录手指按下时的Y坐标位置
    private float offsetY; // 记录手指拖动过程中Y坐标的偏移量
    private float screenHeight = 0;

    private View mTopLayout;
    private View mFootLayout;
    private int touchSlop;

    private boolean topIsAnim = false;
    private boolean footIsAnim = false;

    public void setOnRefreshListener(OnRefreshingListener listener) {
        this.listener = listener;
    }

    private OnRefreshingListener listener;

    public UnclickLinearLayout(Context context) {
        super(context);
        this.context = context;
//        init();
    }

    public UnclickLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
//        init();
    }

    public UnclickLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
//        init();
    }

    public void init() {
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        touchSlop = 0;
        screenHeight = DeviceUtils.getScreenHeight(context) - DeviceUtils.getStatusBarHeight(context);
        moveHeight = screenHeight / 4;
        setCurrentState(STATE_NORMAL);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            // 手指按下时，判断是否可以下拉或上拉加载
            case MotionEvent.ACTION_DOWN:
                if (topIsAnim || footIsAnim) {
                    return false;
                }
                isRefreshable = false;
                isLoadable = false;
                startY = (int) ev.getY();
                startX = (int) ev.getX();
                break;
            // 手指移动时，判断是否在下拉或上拉加载，如果是，则动态改变头部布局或底部布局的状态
            case MotionEvent.ACTION_MOVE:
                offsetY = ev.getY() - startY;//下拉大于0
                if (Math.abs(ev.getX() - startX) < Math.abs(ev.getY() - startY)) {

                    if (offsetY > 0) {//向下拉  加载上一个房间的数据
                        setHeadMagin((int) (screenHeight - offsetY));
                        setFootMargin((int) (screenHeight));
                        if (offsetY >= moveHeight) {
                            isRefreshable = true;
                        } else {
                            isRefreshable = false;
                        }
                    } else if (offsetY < 0) {//向上拖  加载下一个房间的数据
                        setFootMargin((int) (screenHeight + offsetY));
                        setHeadMagin((int) (screenHeight));
                        if (offsetY <= -moveHeight) {
                            isLoadable = true;
                        } else {
                            isLoadable = false;
                        }
                    }
                } else {
                }
                break;
            // 手指抬起时，判断是否下拉或上拉到可以加载房间的程度，如果达到程度，则进行加载
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (offsetY > 0) {//向下拉  加载上一个房间的数据
                    if (isRefreshable) {
                        setCurrentState(STATE_PREPARED);
                    } else {
                        setCurrentState(STATE_PULLING);
                        setFootMargin((int) screenHeight);
                    }
                } else if (offsetY < 0) {
                    if (isLoadable) {
                        setCurrentState(STATE_PREPARED);
                    } else {
                        setCurrentState(STATE_PULLING);
                        setHeadMagin((int) screenHeight);
                    }
                }
                isRefreshable = false;
                isLoadable = false;
                break;
        }
        return true;
    }

    /**
     * 根据当前的状态进行相应的处理
     */
    private void setCurrentState(int state) {
        switch (state) {
            // 普通状态：头部布局和尾部布局都隐藏，头部布局中不显示进度条，底部布局中不显示进度条
            case STATE_NORMAL:
                setHeadMagin((int) screenHeight);
                setFootMargin((int) screenHeight);
                mTopLayout.setVisibility(View.VISIBLE);
                mFootLayout.setVisibility(View.VISIBLE);
                break;
            // 正在下拉后上拉，但没有达到加载房间的要求的状态：
            case STATE_PULLING:
                if (offsetY > 0) //下拉回退
                    setHeadMaginByScroll((int) screenHeight, false);
                else      //上拉回退
                    setFootMaginByScroll((int) screenHeight, false);
                break;
            // 正在准备加载房间的状态：
            case STATE_PREPARED:
                if (offsetY > 0 && isRefreshable) {//下拉加载下一个房间的数据
                    setHeadMaginByScroll(0, true);
//                    setCurrentState(STATE_REFRESHING);
                } else if (offsetY < 0 && isLoadable) {//上拉加载上一个房间的数据
                    setFootMaginByScroll(0, true);
//                    setCurrentState(STATE_REFRESHING);
                }
                break;
            case STATE_REFRESHING:
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (offsetY > 0) {
//                            setHeadMagin((int) screenHeight);
//                        } else {
//                            setFootMargin((int) screenHeight);
//                        }
////                        mLoadding.setVisibility(View.VISIBLE);
//                    }
//                }, 1000);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mLoadding.setVisibility(View.GONE);
//                    }
//                },3000);
                listener.onRefresh(this);
                break;
        }
    }

    public void reset() {
        mTopLayout.clearAnimation();
        mFootLayout.clearAnimation();
        if (offsetY > 0) {
            Animation animation = new AlphaAnimation(1, 0);
            animation.setDuration(300);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    topIsAnim = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    topIsAnim = false;
                    setHeadMagin((int) screenHeight);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mTopLayout.startAnimation(animation);
//            setHeadMagin((int) screenHeight);
        } else {
            Animation animation = new AlphaAnimation(1, 0);
            animation.setDuration(300);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    footIsAnim = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    footIsAnim = false;
                    setFootMargin((int) screenHeight);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mFootLayout.startAnimation(animation);
//            setFootMargin((int) screenHeight);
        }
    }

    public interface OnRefreshingListener {
        void onRefresh(UnclickLinearLayout unclickLinearLayout);
    }

    private void setHeadMaginByScroll(final int toMarginBottom, boolean b) {
        final RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) mTopLayout.getLayoutParams();
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) (screenHeight - offsetY), toMarginBottom);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp1.bottomMargin = (int) animation.getAnimatedValue();
                mTopLayout.setLayoutParams(lp1);
                if ((int) animation.getAnimatedValue() == toMarginBottom) {
                    topIsAnim = false;
                    if (b) {
                        setCurrentState(STATE_REFRESHING);
                    }
                }
            }
        });
        valueAnimator.start();
        topIsAnim = true;
    }

    private void setFootMaginByScroll(final int toMarginTop, boolean b) {
        final RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) mFootLayout.getLayoutParams();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(mFootLayout.getTop(), toMarginTop);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp1.topMargin = (int) animation.getAnimatedValue();
                mFootLayout.setLayoutParams(lp1);
                if ((int) animation.getAnimatedValue() == toMarginTop) {
                    footIsAnim = false;
                    if (b) {
                        setCurrentState(STATE_REFRESHING);
                    }
                }
            }
        });
        valueAnimator.start();
        footIsAnim = true;
    }

    /**
     * 设置头部布局位置
     */
    private void setHeadMagin(int marginBottom) {
        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) mTopLayout.getLayoutParams();
        lp1.bottomMargin = marginBottom;
        mTopLayout.setLayoutParams(lp1);
    }

    /**
     * 设置底部布局位置
     */
    private void setFootMargin(int maginTop) {
        RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) mFootLayout.getLayoutParams();
        lp2.topMargin = maginTop;
        mFootLayout.setLayoutParams(lp2);
    }


    public void setHeadView(View headView) {
        this.mTopLayout = headView;
    }

    public void setFootView(View footView) {
        this.mFootLayout = footView;
    }


}
