package com.whzl.mengbi.ui.widget.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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

    private static final int STATE_VERTIVAL = 0x004;
    private static final int STATE_HORIZEN = 0x005;
    private static final int STATE_ORIGIN = 0x006;

    private Context context;

    private boolean isRefreshable; // 是否可以下拉刷新下一个房间的数据

    private boolean isLoadable; // 是否可以上拉加载上一个房间的数据
    private float moveHeight; //有效移动距离
    private float startY; // 记录手指按下时的Y坐标位置
    private float startX; // 记录手指按下时的X坐标位置
    private float offsetY; // 记录手指拖动过程中Y坐标的偏移量
    private float offsetX; // 记录手指拖动过程中X坐标的偏移量
    private float screenHeight = 0;
    private float screenWidth = 0;

    private View mTopLayout;
    private View mFootLayout;
    private int touchSlop;

    private boolean topIsAnim = false;
    private boolean footIsAnim = false;

    private int direction = STATE_ORIGIN;

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    private boolean canScroll = false;

    public void setOnRefreshListener(OnRefreshingListener listener) {
        this.listener = listener;
    }

    private OnRefreshingListener listener;

    public UnclickLinearLayout(Context context) {
        super(context);
        this.context = context;
    }

    public UnclickLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public UnclickLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void init() {
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        int[] screenSize = DeviceUtils.getScreenSize(context);
        screenWidth = screenSize[0];
        screenHeight = screenSize[1] /*- DeviceUtils.getStatusBarHeight(context)*/;
        moveHeight = screenHeight / 3;
        setCurrentState(STATE_NORMAL);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!canScroll) {
            return false;
        }

        switch (ev.getAction()) {
            // 手指按下时，判断是否可以下拉或上拉加载
            case MotionEvent.ACTION_DOWN:
                if (topIsAnim || footIsAnim) {
                    return false;
                }
                isRefreshable = false;
                isLoadable = false;
                startY = (int) ev.getRawY();
                startX = (int) ev.getRawX();
                break;
            // 手指移动时，判断是否在下拉或上拉加载，如果是，则动态改变头部布局或底部布局的状态
            case MotionEvent.ACTION_MOVE:
                if (direction != STATE_HORIZEN && Math.abs(ev.getRawY() - startY) > touchSlop) {
                    direction = STATE_VERTIVAL;
                    offsetY = ev.getRawY() - startY;//下拉大于0
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
                    if (direction != STATE_VERTIVAL && Math.abs(ev.getRawX() - startX) > touchSlop) {
                        direction = STATE_HORIZEN;
                        offsetX = ev.getRawX() - startX;
//                        setChildMagin((int) (ev.getRawX() - startX));
                    }
                }
                break;
            // 手指抬起时，判断是否下拉或上拉到可以加载房间的程度，如果达到程度，则进行加载
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (offsetY > 0 && direction == STATE_VERTIVAL) {//向下拉  加载上一个房间的数据
                    if (isRefreshable) {
                        setCurrentState(STATE_PREPARED);
                    } else {
                        setCurrentState(STATE_PULLING);
                        setFootMargin((int) screenHeight);
                    }
                } else if (offsetY < 0 && direction == STATE_VERTIVAL) {
                    if (isLoadable) {
                        setCurrentState(STATE_PREPARED);
                    } else {
                        setCurrentState(STATE_PULLING);
                        setHeadMagin((int) screenHeight);
                    }
                }

                if (direction == STATE_HORIZEN) {
                    if (offsetX > 0) {

                    } else {

                    }
                }

                isRefreshable = false;
                isLoadable = false;

                direction = STATE_ORIGIN;
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
                if (offsetY > 0) {
                    listener.onRefresh(this, true);
                } else {
                    listener.onRefresh(this, false);
                }
                break;
        }
    }

    public void reset() {
        destroy();
        if (offsetY > 0) {
            Animation animation = new AlphaAnimation(1, 0);
            animation.setDuration(600);
            animation.setStartOffset(200);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    topIsAnim = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    topIsAnim = false;
                    setHeadMagin((int) screenHeight);
                    listener.onRefreshEnd();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mTopLayout.startAnimation(animation);
        } else {
            Animation animation = new AlphaAnimation(1, 0);
            animation.setDuration(600);
            animation.setStartOffset(200);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    footIsAnim = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    footIsAnim = false;
                    setFootMargin((int) screenHeight);
                    listener.onRefreshEnd();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mFootLayout.startAnimation(animation);
        }
    }


    public interface OnRefreshingListener {
        void onRefresh(UnclickLinearLayout unclickLinearLayout, boolean isTop);

        void onRefreshEnd();
    }

    private void setHeadMaginByScroll(final int toMarginBottom, boolean b) {
        final RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) mTopLayout.getLayoutParams();
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) (screenHeight - offsetY), toMarginBottom);
        valueAnimator.setDuration(100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp1.bottomMargin = (int) animation.getAnimatedValue();
                lp1.topMargin = -(int) animation.getAnimatedValue();
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
        valueAnimator.setDuration(100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp1.topMargin = (int) animation.getAnimatedValue();
                lp1.bottomMargin = -(int) animation.getAnimatedValue();
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
        lp1.topMargin = -marginBottom;
        mTopLayout.setLayoutParams(lp1);
    }

    /**
     * 设置底部布局位置
     */
    private void setFootMargin(int maginTop) {
        RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) mFootLayout.getLayoutParams();
        lp2.topMargin = maginTop;
        lp2.bottomMargin = -maginTop;
        mFootLayout.setLayoutParams(lp2);
    }

    private void setChildMagin(int margin) {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof ConstraintLayout) {
                ConstraintLayout childAt = (ConstraintLayout) getChildAt(i);
                for (int j = 0; j < (childAt.getChildCount()); j++) {
                    if (childAt.getChildAt(j) instanceof RatioRelativeLayout) {
                        continue;
                    }
                    ConstraintLayout.LayoutParams lp1 = (ConstraintLayout.LayoutParams) childAt.getChildAt(j).getLayoutParams();
                    lp1.leftMargin = margin;
                    lp1.rightMargin = -margin;
                    childAt.getChildAt(j).setLayoutParams(lp1);
                }
            }
        }
    }


    public void setHeadView(View headView) {
        this.mTopLayout = headView;
    }

    public void setFootView(View footView) {
        this.mFootLayout = footView;
    }

    public void destroy() {
        if (mTopLayout != null) {
            mTopLayout.clearAnimation();
        }
        if (mFootLayout != null) {
            mFootLayout.clearAnimation();
        }
    }
}
