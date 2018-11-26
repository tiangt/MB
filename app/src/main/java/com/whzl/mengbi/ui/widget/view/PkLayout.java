package com.whzl.mengbi.ui.widget.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.model.entity.PKFansBean;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.RxTimerUtil;
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
    private PkProgressView progressBar;
    private Context context;
    private TextView tvTime;
    private Disposable disposable;
    public int initializeProgress;
    private TextView tvLeftScore;
    private TextView tvRightScore;
    private ValueAnimator animator;
    private TimeDwonListener listener;
    private PopupWindow popupWindow;
    private RecyclerView myFollow;
    private RecyclerView oppositeSide;
    private List<String> list;
    private TextView tvPkTitle;
    private List<PKFansBean> pkUserFansBeans = new ArrayList<>();
    private List<PKFansBean> launchPkUserFansBeans = new ArrayList<>();
    private String mvpPunishWay;
    private String punishWay;
    private BaseListAdapter myFollowAdapter;
    private BaseListAdapter oppositeAdapter;
    private RelativeLayout rlPunishWay;
    private LinearLayout llPkProgress;
    private PopupWindow mvpPopupWindow;
    private TextView tvFansRank;
    private RxTimerUtil rxTimerUtil;

    public PkLayout(Context context) {
        this(context, null);
    }

    public PkLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PkLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PkProgressView);
        initializeProgress = typedArray.getInt(R.styleable.PkProgressView_defaultPercent, 50);
        init(context);
        initPop(context);
    }

    private void init(Context context) {
        rxTimerUtil=new RxTimerUtil();
        LayoutInflater from = LayoutInflater.from(context);
        inflate = from.inflate(R.layout.layout_pk_new, this, false);
        addView(inflate);
        progressBar = inflate.findViewById(R.id.pb_pk);
        tvTime = inflate.findViewById(R.id.tv_time_pk);
        tvLeftScore = inflate.findViewById(R.id.tv_left_score);
        tvRightScore = inflate.findViewById(R.id.tv_right_score);
        tvPkTitle = inflate.findViewById(R.id.tv_pk_title);
        rlPunishWay = inflate.findViewById(R.id.rl_punish_way);
        tvFansRank = inflate.findViewById(R.id.tv_fans_rank);
        rlPunishWay.setOnClickListener(this::onClick);
        setProgress(initializeProgress);
    }

    public void setPkFanRank(List<PKFansBean> pkUserFansBeans,
                             List<PKFansBean> launchPkUserFansBeans) {
        if (this.pkUserFansBeans != null) {
            this.pkUserFansBeans.clear();
        }
        this.pkUserFansBeans.addAll(pkUserFansBeans);
        if (this.launchPkUserFansBeans != null) {
            this.launchPkUserFansBeans.clear();
        }
        this.launchPkUserFansBeans.addAll(launchPkUserFansBeans);
        oppositeAdapter.notifyDataSetChanged();
        myFollowAdapter.notifyDataSetChanged();
    }

    public void setLeftPkFans(List<PKFansBean> pkFans) {
        if (this.pkUserFansBeans != null) {
            this.pkUserFansBeans.clear();
        }
        this.pkUserFansBeans.addAll(pkFans);
        myFollowAdapter.notifyDataSetChanged();
    }

    public void setRightPkFans(List<PKFansBean> pkFans) {
        if (this.launchPkUserFansBeans != null) {
            this.launchPkUserFansBeans.clear();
        }
        this.launchPkUserFansBeans.addAll(pkFans);
        oppositeAdapter.notifyDataSetChanged();
    }

    public void setMvpPunishWay(String mvpPunishWay, PopupWindow popupWindow) {
        this.mvpPunishWay = mvpPunishWay;
        this.mvpPopupWindow = popupWindow;
    }

    public void setPunishWay(String punishWay, PopupWindow popupWindow) {
        this.punishWay = punishWay;
        this.mvpPopupWindow = popupWindow;
    }

    private void initPop(Context context) {
        // PopupWindow 显示PK排名
        View popView = LayoutInflater.from(context).inflate(R.layout.pop_window_pk_rank, null);
        popupWindow = new PopupWindow(popView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        llPkProgress = inflate.findViewById(R.id.ll_pk_progress);
        llPkProgress.setOnClickListener(this::onClick);
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
                int count = 0;
                if (pkUserFansBeans == null) {
                    count = 0;
                } else if (pkUserFansBeans.size() < 5) {
                    count = pkUserFansBeans.size();
                } else {
                    count = 5;
                }
                return count;
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_pk_follow, parent, false);
                return new PKViewHolder(itemView);
            }
        };
        myFollow.setAdapter(myFollowAdapter);
        myFollow.scrollToPosition(0);

        oppositeAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                int count = 0;
                if (launchPkUserFansBeans == null) {
                    count = 0;
                } else if (launchPkUserFansBeans.size() < 5) {
                    count = launchPkUserFansBeans.size();
                } else {
                    count = 5;
                }
                return count;
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
            if (position < pkUserFansBeans.size()) {
                PKFansBean pkFansBean = pkUserFansBeans.get(position);
                tvPkCount.setText((int) pkFansBean.score + "");
                if (TextUtils.isEmpty(pkFansBean.avatar)) {
                    String jpg = ImageUrl.getAvatarUrl(pkFansBean.userId, "jpg", pkFansBean.lastUpdateTime);
                    GlideImageLoader.getInstace().displayImage(getContext(), jpg, ivPkHead);
                } else {
                    GlideImageLoader.getInstace().displayImage(context, pkFansBean.avatar, ivPkHead);
                }
            } else {
                tvPkCount.setText("");
                GlideImageLoader.getInstace().displayImage(context, null, ivPkHead);
            }
            showRanking(position, ivPkLevel);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
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
            if (position < launchPkUserFansBeans.size()) {
                PKFansBean pkFansBean = launchPkUserFansBeans.get(position);
                tvPkCount.setText((int) pkFansBean.score + "");
                if (TextUtils.isEmpty(pkFansBean.avatar)) {
                    String jpg = ImageUrl.getAvatarUrl(pkFansBean.userId, "jpg", pkFansBean.lastUpdateTime);
                    GlideImageLoader.getInstace().displayImage(getContext(), jpg, ivPkHead);
                } else {
                    GlideImageLoader.getInstace().displayImage(context, pkFansBean.avatar, ivPkHead);
                }
            } else {
                tvPkCount.setText("");
                GlideImageLoader.getInstace().displayImage(context, null, ivPkHead);
            }
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
        progressBar.setDefaultPercent(progress);
    }

    public PkProgressView getProgressBar() {
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
                                    if (!TextUtils.isEmpty(mvpPunishWay)) {
                                        //MVP挑选惩罚
                                        tvPkTitle.setText("惩罚:" + mvpPunishWay);
                                    } else if (!TextUtils.isEmpty(punishWay)) {
                                        //非MVP接受到的惩罚
                                        tvPkTitle.setText("惩罚:" + punishWay);
                                    } else {
                                        tvPkTitle.setText("MVP挑选惩罚^ ");
                                    }
                                    tvTime.setText((second - aLong - 1) + "s");
                                } else {
                                    //惩罚倒计时60秒后
                                    if (!TextUtils.isEmpty(mvpPunishWay) || !TextUtils.isEmpty(punishWay)) {
                                        if (mvpPopupWindow != null) {
                                            mvpPopupWindow.dismiss();
                                        }
                                        //未选择惩罚内容
                                        if (TextUtils.isEmpty(mvpPunishWay)) {
                                            if (TextUtils.isEmpty(punishWay)) {
                                                tvPkTitle.setText("惩罚:自定义");
                                                tvTime.setText((second - aLong - 1) + "s");
                                            } else {
                                                tvPkTitle.setText("惩罚:" + punishWay);
                                                tvTime.setText((second - aLong - 1) + "s");
                                            }
                                        } else {
                                            tvPkTitle.setText("惩罚:" + mvpPunishWay);
                                            tvTime.setText((second - aLong - 1) + "s");
                                        }
                                    } else {
                                        if (mvpPopupWindow != null) {
                                            mvpPopupWindow.dismiss();
                                        }
                                        tvPkTitle.setText(state + " ^ ");
                                        tvTime.setText((second - aLong - 1) + "s");
                                    }
                                }
                            } else {
                                tvPkTitle.setText(state);
                                tvTime.setText((second - aLong - 1) + "s");
                            }
                        }
                        if (aLong == second - 11 && "PK倒计时 ".equals(state) && listener != null) {
                            listener.onTimeDownListener();
                        }

                        if ("PK倒计时 ".equals(state)) {
                            if (1 == second - aLong - 1) {
                                tvPkTitle.setText(state);
                                tvTime.setText((second - aLong - 1) + "s");
                                rxTimerUtil.timer(1000, new RxTimerUtil.IRxNext() {
                                    @Override
                                    public void doNext(long number) {
                                        tvTime.setText(0 + "s");
                                    }
                                });
                            } else {
                                tvPkTitle.setText(state);
                                tvTime.setText((second - aLong - 1) + "s");
                            }

                        }
                    } else if (aLong >= second - 1 && !disposable.isDisposed()) {
                        LogUtils.e("ssssss  state dispose");
//                        popupWindow.dismiss();
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
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                setProgress(animatedValue);
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
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
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
            case R.id.ll_pk_progress:
                if (popupWindow != null) {
                    if (popupWindow.isShowing()) {
                        tvFansRank.setText("点击打开助力粉丝榜");
                        popupWindow.dismiss();
                    } else {
                        tvFansRank.setText("点击关闭助力粉丝榜");
                        showPopupWindow(v);
                    }
                }
                break;

            case R.id.rl_punish_way:
                if (clickLintener != null) {
                    clickLintener.onClick(v);
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
        initializeProgress = 50;
        launchPkUserFansBeans.clear();
        pkUserFansBeans.clear();
        tvTime.setText("");
        tvPkTitle.setText("");
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
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
            default:
                GlideImageLoader.getInstace().displayImage(context, null, imageView);
                break;
        }
    }

    public void hidePkWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    private PunishWayClick clickLintener;

    public interface PunishWayClick {
        void onClick(View view);
    }

    public void setPunishWayOnClick(PunishWayClick listener) {
        this.clickLintener = listener;
    }
}

