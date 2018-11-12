package com.whzl.mengbi.ui.widget.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whzl.mengbi.R;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.widget.recyclerview.SpacesItemDecoration;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @author nobody
 * @date 2018/9/24
 */
public class PkLayout extends LinearLayout implements View.OnClickListener {

    private final int PK_FIRST = 0;
    private final int PK_SECOND = 1;
    private final int PK_THIRD = 2;

    private View inflate;
    private ProgressBar progressBar;
    private ImageView ivFount;
    private Context context;
    private ImageView ivRight;
    private TextView tvTime;
    private Disposable disposable;
    private Disposable disposable2;
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
    private ValueAnimator animator;
    private TimeDwonListener listener;

    private RelativeLayout rlPkProgress;
    private PopupWindow popupWindow;
    private RecyclerView myFollow;
    private RecyclerView oppositeSide;
    private List<String> list;

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
        initPop(context);
    }

    private void init(Context context) {
        LayoutInflater from = LayoutInflater.from(context);
        inflate = from.inflate(R.layout.layout_pk_new, this, false);
//        inflate = from.inflate(R.layout.layout_pk_new, this, false);
        addView(inflate);
        progressBar = inflate.findViewById(R.id.pb_pk);
        ivFount = inflate.findViewById(R.id.iv_left);
        ivRight = inflate.findViewById(R.id.iv_right);
        tvTime = inflate.findViewById(R.id.tv_time_pk);
        ivRightLead = inflate.findViewById(R.id.iv_right_lead);
        ivLeftLead = inflate.findViewById(R.id.iv_left_lead);
        ivLeftResult = inflate.findViewById(R.id.iv_left_result);
        ivRightResult = inflate.findViewById(R.id.iv_right_result);
//        ivRightCrown = inflate.findViewById(R.id.iv_right_crown);
//        ivLeftCrown = inflate.findViewById(R.id.iv_left_crown);
//        tvLeftName = inflate.findViewById(R.id.tv_left_name);
//        tvRightName = inflate.findViewById(R.id.tv_right_name);
        tvLeftScore = inflate.findViewById(R.id.tv_left_score);
        tvRightScore = inflate.findViewById(R.id.tv_right_score);
        ivState = inflate.findViewById(R.id.iv_state);
        setProgress(initializeProgress);
    }

    private void initPop(Context context) {
        // PopupWindow 显示PK排名
        View popView = LayoutInflater.from(context).inflate(R.layout.pop_window_pk_rank, null);
        popupWindow = new PopupWindow(popView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        rlPkProgress = inflate.findViewById(R.id.rl_pk_progress);
        rlPkProgress.setOnClickListener(this::onClick);
        myFollow = popView.findViewById(R.id.rv_my_follow);
        oppositeSide = popView.findViewById(R.id.rv_opposite_side);

        list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i + "");
        }

        LinearLayoutManager followManager = new LinearLayoutManager(context);
        followManager.setOrientation(HORIZONTAL);
        followManager.setStackFromEnd(true);
        followManager.setReverseLayout(true);
        myFollow.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        myFollow.setLayoutManager(followManager);
        LinearLayoutManager oppositeManager = new LinearLayoutManager(context);
        oppositeManager.setOrientation(HORIZONTAL);
        oppositeSide.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        oppositeSide.setLayoutManager(oppositeManager);

        BaseListAdapter myFollowAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return list == null ? 0 : 5;
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_pk_follow, parent, false);
                return new PKViewHolder(itemView);
            }
        };
        myFollow.setAdapter(myFollowAdapter);

        BaseListAdapter oppositeAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return list == null ? 0 : 5;
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_pk_opposite, parent, false);
                return new PKOppoViewHolder(itemView);
            }
        };

        oppositeSide.setAdapter(oppositeAdapter);
    }

    class PKViewHolder extends BaseViewHolder {
        ImageView ivPkHead;
        ImageView ivPkLevel;
        TextView tvPkCount;

        public PKViewHolder(View itemView) {
            super(itemView);
            ivPkHead = itemView.findViewById(R.id.iv_circle_head);
            ivPkLevel = itemView.findViewById(R.id.iv_pk_level);
            tvPkCount = itemView.findViewById(R.id.tv_pk_count);
        }

        @Override
        public void onBindViewHolder(int position) {
            tvPkCount.setText(list.get(position));
            showRanking(position, ivPkLevel);
        }
    }

    class PKOppoViewHolder extends BaseViewHolder {
        ImageView ivPkHead;
        ImageView ivPkLevel;
        TextView tvPkCount;

        public PKOppoViewHolder(View itemView) {
            super(itemView);
            ivPkHead = itemView.findViewById(R.id.iv_circle_head);
            ivPkLevel = itemView.findViewById(R.id.iv_pk_level);
            tvPkCount = itemView.findViewById(R.id.tv_pk_count);
        }

        @Override
        public void onBindViewHolder(int position) {
            tvPkCount.setText(list.get(position));
            showRanking(position, ivPkLevel);
        }
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

    public void setLeftScore(int score) {
        tvLeftScore.setText(score + "票");
    }

    public void setRightScore(int score) {
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
                    LogUtils.e("ssssss  state" + aLong);
//                    tvTime.setText(context.getString(R.string.pk_time, (9 - aLong) / 60, (9 - aLong) % 60));

                    if (aLong < second - 1) {
                        if (aLong < second - 1) {
                            tvTime.setText(state + context.getString(R.string.pk_time, (second - aLong - 1) / 60, (second - aLong - 1) % 60));
                        }
                        if (aLong == second - 11 && "PK进行中 ".equals(state) && listener != null) {
                            listener.onTimeDownListener();
                        }
                        if (aLong >= second - 1) {
                            LogUtils.e("ssssss  state dispose");
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
                        }
                    }
                });

    }

    public void setAnimation(final int mProgressBar) {
        if (animator != null && animator.isRunning()) {
            animator.end();
            animator = null;
        }
        animator = ValueAnimator.ofInt(initializeProgress, mProgressBar).setDuration(2000);

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

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                initializeProgress = mProgressBar;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    public void destroy() {
        if (disposable != null) {
            disposable.dispose();
        }
        if (disposable2 != null) {
            disposable2.dispose();
        }

        if (popupWindow != null) {
            popupWindow.dismiss();
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
        ivLeftResult.setVisibility(VISIBLE);
        ivRightResult.setVisibility(VISIBLE);
        ivLeftResult.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_left_win));
        ivRightResult.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_right_lose));
        ivLeftCrown.setVisibility(VISIBLE);
        ivRightCrown.setVisibility(GONE);
        ivLeftLead.setVisibility(GONE);
    }

    public void setRightWin() {
        ivLeftResult.setVisibility(VISIBLE);
        ivRightResult.setVisibility(VISIBLE);
        ivLeftResult.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_left_lose));
        ivRightResult.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_right_win));
        ivLeftCrown.setVisibility(GONE);
        ivRightCrown.setVisibility(VISIBLE);
        ivRightLead.setVisibility(GONE);
    }

    public void setTied() {
        ivLeftResult.setVisibility(VISIBLE);
        ivRightResult.setVisibility(VISIBLE);
        ivLeftResult.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_left_ping));
        ivRightResult.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_right_ping));
    }

    public void setListener(TimeDwonListener listener) {
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_pk_progress:
                if (popupWindow != null) {
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    } else {
                        showPopupWindow(v);
                    }
                }
                break;
        }
    }

    public interface TimeDwonListener {
        void onTimeDownListener();

    }

    public void reset() {
        setProgress(50);
        setLeftScore(0);
        setRightScore(0);
        setLeftImg(null);
        setRightImg(null);
        setLeftName("");
        setRightName("");
        ivLeftCrown.setVisibility(GONE);
        ivRightCrown.setVisibility(GONE);
        ivLeftLead.setVisibility(GONE);
        ivRightLead.setVisibility(GONE);
        ivLeftResult.setVisibility(GONE);
        ivRightResult.setVisibility(GONE);
        tvTime.setText("");
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    public void setRightClickListener(OnClickListener listener) {
        ivRight.setOnClickListener(listener);
    }


    private void showPopupWindow(View view) {
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(false);
        popupWindow.setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.showAsDropDown(view);
    }

    private void showRanking(int ranking, ImageView imageView) {
        switch (ranking) {
            case PK_FIRST:
                GlideImageLoader.getInstace().displayImage(context, R.drawable.ic_pk_1, imageView);
                break;
            case PK_SECOND:
                GlideImageLoader.getInstace().displayImage(context, R.drawable.ic_pk_2, imageView);
                break;
            case PK_THIRD:
                GlideImageLoader.getInstace().displayImage(context, R.drawable.ic_pk_3, imageView);
                break;
        }
    }
}

