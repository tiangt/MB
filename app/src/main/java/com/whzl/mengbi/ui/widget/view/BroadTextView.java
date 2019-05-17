package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.whzl.mengbi.chat.room.message.events.BroadEvent;
import com.whzl.mengbi.util.UIUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @author nobody
 * @date 2018/8/2
 */
public class BroadTextView extends AppCompatTextView {
    public final static String TAG = BroadTextView.class.getSimpleName();

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
     * 最大滚动长度
     */
    private float maxTranslateX = 0.0f;
    /**
     * 是否开始滚动
     */
    public boolean isStarting = false;
    /**
     * 绘图样式
     */
    private Paint paint = null;
    private Disposable mDispose;

    public BroadEvent getRunWayEvent() {
        return broadEvent;
    }

    /**
     * 文本内容
     */
    private BroadEvent broadEvent;

    public BroadTextView(Context context) {
        this(context, null);
    }

    public BroadTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BroadTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        viewWidth = UIUtil.getScreenWidthPixels(getContext()) - UIUtil.dip2px(getContext(), 100);
        viewWidth = UIUtil.dip2px(context, 250);
    }

    public void init(BroadEvent event, RunStateListener listener) {
        broadEvent = event;
        this.listener = listener;
        setVisibility(GONE);
        try {
            event.showRunWay(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        paint = getPaint();
        String text = getText().toString();
        step = 0f;
        textLength = paint.measureText(text);
        maxTranslateX = textLength + viewWidth + 50;
        dispose();
    }

    public void dispose() {
        if (mDispose != null) {
            mDispose.dispose();
        }
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
        invalidate();
        setVisibility(VISIBLE);
    }


    public void stopScroll() {
        isStarting = false;
        setVisibility(GONE);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (!isStarting || broadEvent == null) {
            return;
        }
//        canvas.translate(viewWidth - step, 0);
        canvas.translate(-step, 0);
        super.onDraw(canvas);
        //滚动速度
//        if (step <= viewWidth && step + 2.5 > viewWidth) {
        if (textLength < viewWidth) {
            mDispose = Observable.just(1)
                    .delay(3, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> {
//                        step += 2.5;
//                        invalidate();
                        if (listener != null) {
                            listener.finishSingleRun();
                        }
                    });

        } else {
            if (step + viewWidth >= textLength+10) {
                mDispose = Observable.just(1)
                        .delay(3, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(integer -> {
                            step = 0;
                            if (listener != null) {
                                listener.finishSingleRun();
                            }
                        });
            } else {
                step += 2;
                invalidate();
            }
        }

//            return;
//        }
//        step += 2.5;
//        if (step > maxTranslateX) {
//            if (listener != null) {
//                listener.finishSingleRun();
//            }
//
//            step = 0;
//        }
//        invalidate();
    }

    private RunStateListener listener;

    public interface RunStateListener {
        void finishSingleRun();
    }
}
