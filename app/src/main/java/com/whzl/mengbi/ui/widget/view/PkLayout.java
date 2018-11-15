package com.whzl.mengbi.ui.widget.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
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

import com.whzl.mengbi.chat.room.message.messageJson.PkJson;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

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
    private Context context;
    private TextView tvTime;
    private Disposable disposable;
    private Disposable disposable2;
    private int initializeProgress;
    private TextView tvLeftScore;
    private TextView tvRightScore;
    private ValueAnimator animator;
    private TimeDwonListener listener;
    private RelativeLayout rlPkProgress;
    private PopupWindow popupWindow;
    private RecyclerView myFollow;
    private RecyclerView oppositeSide;
    private List<String> list;
    private TextView tvPkTitle;
    private List<PkJson.ContextBean.LaunchPkUserFansBean> pkUserFansBeans;
    private List<PkJson.ContextBean.LaunchPkUserFansBean> launchPkUserFansBeans;
    private String mvpPunishWay;
    private String punishWay;
    private BaseListAdapter myFollowAdapter;
    private BaseListAdapter oppositeAdapter;

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
        addView(inflate);
        progressBar = inflate.findViewById(R.id.pb_pk);
        tvTime = inflate.findViewById(R.id.tv_time_pk);
        tvLeftScore = inflate.findViewById(R.id.tv_left_score);
        tvRightScore = inflate.findViewById(R.id.tv_right_score);
        tvPkTitle = inflate.findViewById(R.id.tv_pk_title);
        setProgress(initializeProgress);
    }

    public void setPkFanRank(List<PkJson.ContextBean.LaunchPkUserFansBean> pkUserFansBeans,
                             List<PkJson.ContextBean.LaunchPkUserFansBean> launchPkUserFansBeans) {
        this.pkUserFansBeans = pkUserFansBeans;
        this.launchPkUserFansBeans = launchPkUserFansBeans;
        Log.d("chen", pkUserFansBeans + "");
        oppositeAdapter.notifyDataSetChanged();
        myFollowAdapter.notifyDataSetChanged();
    }

    public void setMvpPunishWay(String mvpPunishWay) {
        this.mvpPunishWay = mvpPunishWay;
    }

    public void setPunishWay(String punishWay) {
        this.punishWay = punishWay;
    }

    private void initPop(Context context) {
        // PopupWindow 显示PK排名
        View popView = LayoutInflater.from(context).inflate(R.layout.pop_window_pk_rank, null);
        popupWindow = new PopupWindow(popView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        rlPkProgress = inflate.findViewById(R.id.rl_pk_progress);
        rlPkProgress.setOnClickListener(this::onClick);
        myFollow = popView.findViewById(R.id.rv_my_follow);
        oppositeSide = popView.findViewById(R.id.rv_opposite_side);

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

        myFollowAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return pkUserFansBeans == null ? 0 : 5;
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_pk_follow, parent, false);
                return new PKViewHolder(itemView);
            }
        };
        myFollow.setAdapter(myFollowAdapter);

        oppositeAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return launchPkUserFansBeans == null ? 0 : 5;
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
            tvPkCount.setText(pkUserFansBeans.get(position).nickname);
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
            tvPkCount.setText(launchPkUserFansBeans.get(position).nickname);
            showRanking(position, ivPkLevel);
        }
    }

    public void setLeftScore(int score) {
        tvLeftScore.setText(score + "");
    }

    public void setRightScore(int score) {
        tvRightScore.setText(score + "");
    }

    public void setProgress(int progress) {
        progressBar.setProgress(progress);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void timer(String state, int second) {
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    LogUtils.e("ssssss  state" + aLong);
                    if (aLong < second - 1) {
                        if (aLong < second - 1) {
                            if ("惩罚时刻 ".equals(state)) {
                                if ((second - aLong - 1) > (second - 61)) {
                                    Log.d("chenliang", "PKlayout = " + mvpPunishWay);
                                    if (!TextUtils.isEmpty(mvpPunishWay) && !"".equals(mvpPunishWay)) {
                                        //MVP挑选惩罚
                                        tvPkTitle.setText(mvpPunishWay);
                                    } else if (!TextUtils.isEmpty(punishWay) && !"".equals(punishWay)) {
                                        //非MVP接受到的惩罚
                                        tvPkTitle.setText(punishWay);
                                    }else{
                                        tvPkTitle.setText("MVP挑选惩罚^ ");
                                    }
                                    tvTime.setText((second - aLong - 1) + "s");
                                } else {
                                    //惩罚倒计时60秒后
                                    if (TextUtils.isEmpty(mvpPunishWay) || "".equals(mvpPunishWay)
                                            || TextUtils.isEmpty(punishWay) || "".equals(punishWay)) {
                                        //未选择惩罚内容
                                        tvPkTitle.setText("自定义 ");
                                        tvTime.setText((second - aLong - 1) + "s");
                                    } else {
                                        tvPkTitle.setText(state + " ^ ");
                                        tvTime.setText((second - aLong - 1) + "s");
                                    }
                                }
                            } else {
                                tvPkTitle.setText(state);
                                tvTime.setText((second - aLong - 1) + "s");
                            }
                        }
                        if (aLong == second - 11 && "PK进行中 ".equals(state) && listener != null) {
                            listener.onTimeDownListener();
                        }

                        if ("PK进行中 ".equals(state)) {
                            tvPkTitle.setText(state);
                            tvTime.setText((second - aLong - 1) + "s");
                        }
                    } else if (aLong >= second - 1) {
                        LogUtils.e("ssssss  state dispose");
                        disposable.dispose();
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
        tvTime.setText("");
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    public void setRightClickListener(OnClickListener listener) {
//        ivRight.setOnClickListener(listener);
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

