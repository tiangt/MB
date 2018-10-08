package com.whzl.mengbi.ui.widget.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @author nobody
 * @date 2018/9/24
 */
public class PkLayout extends LinearLayout {

    private ProgressBar progressBar;
    private ImageView ivFount;
    private Context context;
    private ImageView ivRight;
    private TextView tvTime;
    private Disposable disposable;
    private int initializeProgress;
    private ImageView ivRightLead;
    private ImageView ivLeftLead;
    private ImageView ivLeftResult;
    private ImageView ivRightResult;
    private ImageView ivRightCrown;
    private ImageView ivLeftCrown;
    private TextView tvLeftName;
    private TextView tvRightName;
    private TextView tvLeftScore;
    private TextView tvRightScore;
    private ImageView ivState;

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
        initializeProgress = typedArray.getInt(R.styleable.PkLayout_progress, 50);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater from = LayoutInflater.from(context);
        View inflate = from.inflate(R.layout.layout_pk, this, false);
        addView(inflate);
        progressBar = inflate.findViewById(R.id.pb_pk);
        ivFount = inflate.findViewById(R.id.iv_left);
        ivRight = inflate.findViewById(R.id.iv_right);
        tvTime = inflate.findViewById(R.id.tv_time_pk);
        ivRightLead = inflate.findViewById(R.id.iv_right_lead);
        ivLeftLead = inflate.findViewById(R.id.iv_left_lead);
        ivLeftResult = inflate.findViewById(R.id.iv_left_result);
        ivRightResult = inflate.findViewById(R.id.iv_right_result);
        ivRightCrown = inflate.findViewById(R.id.iv_right_crown);
        ivLeftCrown = inflate.findViewById(R.id.iv_left_crown);
        tvLeftName = inflate.findViewById(R.id.tv_left_name);
        tvRightName = inflate.findViewById(R.id.tv_right_name);
        tvLeftScore = inflate.findViewById(R.id.tv_left_score);
        tvRightScore = inflate.findViewById(R.id.tv_right_score);
        ivState = inflate.findViewById(R.id.iv_state);
        setProgress(initializeProgress);
    }

    public void setStateImg(Object object) {
        GlideImageLoader.getInstace().displayImage(context, object, ivState);
    }

    public void setLeftName(String name) {
        tvLeftName.setText(name);
    }

    public void setRightName(String name) {
        tvRightName.setText(name);
    }

    public void setLeftScore(String score) {
        tvLeftScore.setText(score + "票");
    }

    public void setRightScore(String score) {
        tvRightScore.setText(score + "票");
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

    public void timer(String state, int second) {
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
//                    tvTime.setText(context.getString(R.string.pk_time, (9 - aLong) / 60, (9 - aLong) % 60));
                    tvTime.setText(state + context.getString(R.string.pk_time, (second - aLong) / 60, (second - aLong) % 60));
                    if (aLong == second) {
                        disposable.dispose();
//                        ivLeftLead.setVisibility(GONE);
//                        ivRightLead.setVisibility(GONE);
//                        if (progressBar.getProgress() == 50) {
//                            setTied();
//                        }
//                        if (progressBar.getProgress() < 50) {
//                            setRightWin();
//                            ivRightCrown.setVisibility(VISIBLE);
//                        }
//                        if (progressBar.getProgress() > 50) {
//                            setLeftWin();
//                            ivLeftCrown.setVisibility(VISIBLE);
//                        }
                    }
                });
    }

    public void setAnimation(final int mProgressBar) {
        ValueAnimator animator = ValueAnimator.ofInt(initializeProgress, mProgressBar).setDuration(5000);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setProgress((int) valueAnimator.getAnimatedValue());
                if (progressBar.getProgress() == 50) {
                    if (ivLeftLead.getVisibility() == VISIBLE) {
                        ivLeftLead.setVisibility(GONE);
                    }
                    if (ivRightLead.getVisibility() == VISIBLE) {
                        ivRightLead.setVisibility(GONE);
                    }
                }
                if (progressBar.getProgress() < 50) {
                    ivRightLead.setVisibility(VISIBLE);
                    ivLeftLead.setVisibility(GONE);
                }
                if (progressBar.getProgress() > 50) {
                    ivLeftLead.setVisibility(VISIBLE);
                    ivRightLead.setVisibility(GONE);
                }
            }
        });
        animator.start();
    }

    public void destroy() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void setLeftLead() {
        ivLeftLead.setVisibility(VISIBLE);
        ivRightLead.setVisibility(GONE);
    }

    public void setRightLead() {
        ivLeftLead.setVisibility(GONE);
        ivRightLead.setVisibility(VISIBLE);
    }

    public void setLeftWin() {
        ivLeftResult.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_left_win));
        ivRightResult.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_right_lose));
        ivLeftCrown.setVisibility(VISIBLE);
        ivRightCrown.setVisibility(GONE);
    }

    public void setRightWin() {
        ivLeftResult.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_left_lose));
        ivRightResult.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_right_win));
        ivLeftCrown.setVisibility(GONE);
        ivRightCrown.setVisibility(VISIBLE);
    }

    public void setTied() {
        ivLeftResult.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_left_ping));
        ivRightResult.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_right_ping));
    }
}
