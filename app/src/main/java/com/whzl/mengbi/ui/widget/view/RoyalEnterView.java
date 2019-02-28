package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.UIUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class RoyalEnterView extends AppCompatTextView {
    public final static String TAG = RoyalEnterView.class.getSimpleName();
    private boolean stopInval = false;

    /**
     * 文本长度
     */
    private float textLength = 0f;
    private float viewWidth = 0f;
    /**
     * 文字的横坐标
     */
    private float step = 0f;
    /**
     * 是否开始滚动
     */
    public boolean isStarting = false;
    /**
     * 绘图样式
     */
    private Paint paint = null;

    public RoyalEnterView(Context context) {
        this(context, null);
    }

    public RoyalEnterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoyalEnterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init() {
        paint = getPaint();
        String text = getText().toString();
        step = 0f;
        textLength = paint.measureText(text);
        viewWidth = UIUtil.dip2px(getContext(), 275);
        LogUtils.e("ssssssssss  "+textLength+"    "+viewWidth);
    }


    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.step = step;
        ss.isStarting = isStarting;
        return ss;

    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        step = ss.step;
        isStarting = ss.isStarting;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {

            @Override
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
            boolean[] b = new boolean[1];
            try {
                in.readBooleanArray(b);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (b != null && b.length > 0) {
                isStarting = b[0];
            }
            step = in.readFloat();
        }
    }

    public void startScroll() {
        isStarting = true;
        stopInval = false;
        invalidate();
    }


    public void stopScroll() {
        isStarting = false;
        stopInval = true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (stopInval) {
            return;
        }
        if (!isStarting) {
            return;
        }
        canvas.translate(-step, 0);
        super.onDraw(canvas);
//        if (viewWidth > textLength + 30) {
//            return;
//        }
        Observable.just(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
//                    if (textLength > viewWidth && textLength - step + 130 <= viewWidth) {
//                        return;
//                    }
                    step += 1.5;
                    invalidate();
                });
    }

    private RunStateListener listener;

    public interface RunStateListener {
        void finishSingleRun();
    }
}
