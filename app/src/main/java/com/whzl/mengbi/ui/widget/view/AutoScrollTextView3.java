package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.whzl.mengbi.util.RxTimerUtil;

/**
 * 主播昵称
 *
 * @author cliang
 * @date 2018.11.6
 */
public class AutoScrollTextView3 extends AppCompatTextView implements View.OnClickListener {

    public final static String TAG = AutoScrollTextView.class.getSimpleName();
    private final float DEF_TEXT_SIZE = 13.0F;

    private float textLength = 0f;//文本长度
    private float viewWidth = 0f;
    private float step = 0f;//文字的横坐标
    private float y = 0f;//文字的纵坐标
    private float temp_view_plus_text_length = 0.0f;//用于计算的临时变量
    private float temp_view_plus_two_text_length = 0.0f;//用于计算的临时变量
    public boolean isStarting = false;//是否开始滚动
    private Paint paint = null;//绘图样式
    private String text;//文本内容

    private float mTextSize;//This is text size
    private int mTextColor; //This is text color
    private int scrollTimes = 1;
    private RxTimerUtil rxTimerUtil;

    public void setScrollTimes(int scrollTimes) {
        this.scrollTimes = scrollTimes;
    }

    public AutoScrollTextView3(Context context) {
        super(context, null);
        initView();
    }

    public AutoScrollTextView3(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initView();
    }

    public AutoScrollTextView3(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }


    private void initView() {
        rxTimerUtil = new RxTimerUtil();
        setOnClickListener(this);
    }

    public void init(WindowManager windowManager, int width) {
        paint = getPaint();
        text = getText().toString();
        textLength = paint.measureText(text);
//        viewWidth = width;
        step = 0f;
//        temp_view_plus_text_length = width + textLength + 50;
//        temp_view_plus_two_text_length = width + textLength * 3;
        y = getTextSize() + getPaddingTop();
    }

//    @Override
//    public Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//        SavedState ss = new SavedState(superState);
//
//        ss.step = step;
//        ss.isStarting = isStarting;
//        return ss;
//
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
    }

    //    /**
//     * Set the text size, if this value is < 0, set to the default size
//     *
//     * @param textSize
//     */
//    public void setTextSize(float textSize) {
//        this.mTextSize = textSize;
//        paint.setTextSize(mTextSize <= 0 ? DEF_TEXT_SIZE : UIUtil.sp2px(getContext(),mTextSize));
//        requestLayout();
////        invalidate();
//    }
//
    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        paint.setColor(mTextColor);
        invalidate();
    }

//    @Override
//    public void onRestoreInstanceState(Parcelable state) {
//        if (!(state instanceof SavedState)) {
//            super.onRestoreInstanceState(state);
//            return;
//        }
//        SavedState ss = (SavedState) state;
//        super.onRestoreInstanceState(ss.getSuperState());
//
//        step = ss.step;
//        isStarting = ss.isStarting;
//    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    public static class SavedState extends BaseSavedState {
        public boolean isStarting = false;
        public float step = 0.0f;

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBooleanArray(new boolean[]{isStarting});
            out.writeFloat(step);
        }


        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
        };

        private SavedState(Parcel in) {
            super(in);
            if (in == null) {
                return;
            }
            boolean[] b = null;
            in.readBooleanArray(b);
            if (b != null && b.length > 0)
                isStarting = b[0];
            step = in.readFloat();
        }
    }


    public void startScroll() {
        isStarting = true;
        invalidate();
    }


    public void stopScroll() {
        isStarting = false;
        invalidate();
    }

    public void destyoy() {
        if (rxTimerUtil != null) {
            rxTimerUtil.cancel();
        }
        scrollTimes = 1;
        isStarting = false;
        invalidate();
    }

    @Nullable
    @Override
    public void onDraw(Canvas canvas) {
        if (paint != null && scrollTimes < 3) {
            canvas.drawText(text, viewWidth - step, y, paint);
        }
        if (paint != null && scrollTimes == 3) {
            if (paint.measureText(text) > viewWidth) {
                canvas.drawText(text.substring(0, 4) + "...", viewWidth - step, y, paint);
            } else {
                canvas.drawText(text, viewWidth - step, y, paint);
            }
            rxTimerUtil.timer(5 * 60 * 1000, number -> {
                step = 0f;
                scrollTimes = 1;
                startScroll();
            });
        }
        if (!isStarting) {
            return;
        }
        step += 1.0;
        if (scrollTimes < 3) {
            if (step > viewWidth + textLength) {
                step = 0;
                scrollTimes += 1;
            }
        } else {
            if (step == viewWidth) {
                step = viewWidth;
                if (runStateListener != null) {
                    runStateListener.finishSingleRun();
                }
                if (listener != null) {
                    listener.stopScroll();
                }
                isStarting = false;
            }
        }
        invalidate();
    }

    @Override
    public void onClick(View v) {
        if (isStarting)
            stopScroll();
        else
            startScroll();
    }

    private MarqueeStateListener listener;

    public interface MarqueeStateListener {
        void stopScroll();
    }

    public void setOnMarqueeListener(MarqueeStateListener listener) {
        this.listener = listener;
    }

    private RunStateListener runStateListener;

    public interface RunStateListener {
        void finishSingleRun();
    }

    public void setOnRunStateListener(RunStateListener runStateListener) {
        this.runStateListener = runStateListener;
    }
}
