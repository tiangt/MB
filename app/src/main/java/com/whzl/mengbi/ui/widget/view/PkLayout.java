package com.whzl.mengbi.ui.widget.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.PkJson;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.model.entity.PKFansBean;
import com.whzl.mengbi.model.entity.PKResultBean;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.dialog.GuessBetDialog;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.PkQualifyingLevelUtils;
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
    //    public PopupWindow popupWindow;
    private RecyclerView myFollow;
    private RecyclerView oppositeSide;
    private List<String> list;
    private TextView tvPkTitle;
    private ArrayList<PKFansBean> pkUserFansBeans = new ArrayList<>();
    private ArrayList<PKFansBean> launchPkUserFansBeans = new ArrayList<>();
    private String mvpPunishWay;
    private String punishWay;
    private BaseListAdapter myFollowAdapter;
    private BaseListAdapter oppositeAdapter;
    private RelativeLayout rlPunishWay;
    private LinearLayout llPkProgress;
    private PopupWindow mvpPopupWindow;
    public TextView tvFansRank;
    private RxTimerUtil rxTimerUtil;
    private ImageView ivMyRank;
    private ImageView ivOtherRank;
    private ConstraintLayout containerPkGuess;
    private TextView tvLeftOdds;
    private TextView tvRightOdds;
    private LinearLayout containerLeftOdds;
    private LinearLayout containerRightOdds;
    private long mUserId;
    private int guessId;
    private int mProgramId;
    private int mAnchorId;
    private double squareOdds;
    private double counterOdds;
    private int squareId;

    public void setmUserId(long mUserId) {
        this.mUserId = mUserId;
    }

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
    }


    private void init(Context context) {
        rxTimerUtil = new RxTimerUtil();
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
        rlPunishWay.setOnClickListener(this);
        myFollow = inflate.findViewById(R.id.rv_my_follow);
        oppositeSide = inflate.findViewById(R.id.rv_opposite_side);
        ivMyRank = inflate.findViewById(R.id.iv_my_rank);
        ivOtherRank = inflate.findViewById(R.id.iv_other_rank);
        containerPkGuess = inflate.findViewById(R.id.container_pk_guess);
        tvLeftOdds = inflate.findViewById(R.id.tv_left_odds);
        tvRightOdds = inflate.findViewById(R.id.tv_right_odds);
        containerLeftOdds = inflate.findViewById(R.id.container_left_odds);
        containerRightOdds = inflate.findViewById(R.id.container_right_odds);
        containerLeftOdds.setOnClickListener(this);
        containerRightOdds.setOnClickListener(this);
        setProgress(initializeProgress);
        initRv(context);
    }

    public void setPkGuessVisibility(int visibility) {
        if (containerPkGuess != null) {
            containerPkGuess.setVisibility(visibility);
        }
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

    private void initRv(Context context) {
// PopupWindow 显示PK排名
        llPkProgress = inflate.findViewById(R.id.ll_pk_progress);
        llPkProgress.setOnClickListener(this::onClick);

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
                int count;
                if (pkUserFansBeans == null) {
                    count = 0;
                } else if (pkUserFansBeans.size() < 3) {
                    count = pkUserFansBeans.size();
                } else {
                    count = 3;
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
                int count;
                if (launchPkUserFansBeans == null) {
                    count = 0;
                } else if (launchPkUserFansBeans.size() < 3) {
                    count = launchPkUserFansBeans.size();
                } else {
                    count = 3;
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

    public void initGuessing(PKResultBean bean) {
        if (bean == null) {
            return;
        }
        if ("N".equals(bean.pkType)) {
            ivMyRank.setVisibility(GONE);
            ivOtherRank.setVisibility(GONE);
        } else {
            ivMyRank.setVisibility(VISIBLE);
            ivOtherRank.setVisibility(VISIBLE);
        }
        if (((LiveDisplayActivity) context).mProgramId == bean.launchPkUserProgramId) {
            int userLevelIcon = PkQualifyingLevelUtils.getInstance().getUserLevelIcon(bean.launchPkUserPkRank.rankId);
            GlideImageLoader.getInstace().displayImage(context, userLevelIcon, ivMyRank);
            int userLevelIcon2 = PkQualifyingLevelUtils.getInstance().getUserLevelIcon(bean.pkUserPkRank.rankId);
            GlideImageLoader.getInstace().displayImage(context, userLevelIcon2, ivOtherRank);
        } else {
            int userLevelIcon = PkQualifyingLevelUtils.getInstance().getUserLevelIcon(bean.pkUserPkRank.rankId);
            GlideImageLoader.getInstace().displayImage(context, userLevelIcon, ivMyRank);
            int userLevelIcon2 = PkQualifyingLevelUtils.getInstance().getUserLevelIcon(bean.launchPkUserPkRank.rankId);
            GlideImageLoader.getInstace().displayImage(context, userLevelIcon2, ivOtherRank);
        }
    }

    public void initGuessingByEvent(PkJson.ContextBean bean) {
        if (bean == null || bean.pkUserRankValue <= 0 || bean.launchPkUserRankValue <= 0) {
            return;
        }
        if ("N".equals(bean.pkType)) {
            ivMyRank.setVisibility(GONE);
            ivOtherRank.setVisibility(GONE);
        } else {
            ivMyRank.setVisibility(VISIBLE);
            ivOtherRank.setVisibility(VISIBLE);
        }
        if (((LiveDisplayActivity) context).mProgramId == bean.launchUserProgramId) {
            int userLevelIcon = PkQualifyingLevelUtils.getInstance().getUserLevelIcon(bean.launchPkUserRankValue);
            GlideImageLoader.getInstace().displayImage(context, userLevelIcon, ivMyRank);
            int userLevelIcon2 = PkQualifyingLevelUtils.getInstance().getUserLevelIcon(bean.pkUserRankValue);
            GlideImageLoader.getInstace().displayImage(context, userLevelIcon2, ivOtherRank);
        } else {
            int userLevelIcon = PkQualifyingLevelUtils.getInstance().getUserLevelIcon(bean.pkUserRankValue);
            GlideImageLoader.getInstace().displayImage(context, userLevelIcon, ivMyRank);
            int userLevelIcon2 = PkQualifyingLevelUtils.getInstance().getUserLevelIcon(bean.launchPkUserRankValue);
            GlideImageLoader.getInstace().displayImage(context, userLevelIcon2, ivOtherRank);
        }
    }


    public void setPkGuessOdds(int userId, double squareOdds, double counterOdds) {
        if (((LiveDisplayActivity) context).mAnchorId == userId) {
            tvLeftOdds.setText(context.getString(R.string.tv_odd_guess, squareOdds));
            tvRightOdds.setText(context.getString(R.string.tv_odd_guess, counterOdds));
        } else {
            tvLeftOdds.setText(context.getString(R.string.tv_odd_guess, counterOdds));
            tvRightOdds.setText(context.getString(R.string.tv_odd_guess, squareOdds));
        }
    }

    public void setGuessBetArgument(long mUserId, int guessId, int mProgramId, int mAnchorId, int squareId, double squareOdds, double counterOdds) {
        this.mUserId = mUserId;
        this.guessId = guessId;
        this.mProgramId = mProgramId;
        this.mAnchorId = mAnchorId;
        this.squareId = squareId;
        this.squareOdds = squareOdds;
        this.counterOdds = counterOdds;
    }


    class PKViewHolder extends BaseViewHolder {
        ImageView ivPkHead;
        TextView tvPkCount;

        public PKViewHolder(View itemView) {
            super(itemView);
            ivPkHead = itemView.findViewById(R.id.iv_circle_head);
            tvPkCount = itemView.findViewById(R.id.tv_pk_count);
        }

        @Override
        public void onBindViewHolder(int position) {
            if (position < pkUserFansBeans.size()) {
                PKFansBean pkFansBean = pkUserFansBeans.get(position);
                tvPkCount.setText(String.valueOf(position + 1));
                if (TextUtils.isEmpty(pkFansBean.avatar)) {
                    String jpg = ImageUrl.getAvatarUrl(pkFansBean.userId, "jpg", pkFansBean.lastUpdateTime);
                    GlideImageLoader.getInstace().displayImage(getContext(), jpg, ivPkHead);
                } else {
                    GlideImageLoader.getInstace().displayImage(context, pkFansBean.avatar, ivPkHead);
                }
            } else {
//                tvPkCount.setText("");
                GlideImageLoader.getInstace().displayImage(context, null, ivPkHead);
            }
//            showRanking(position, ivPkLevel);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            if (onclickRankListener != null) {
                onclickRankListener.onClickRankListener(0);
            }
        }
    }

    class PKOppoViewHolder extends BaseViewHolder {
        ImageView ivPkHead;
        TextView tvPkCount;

        public PKOppoViewHolder(View itemView) {
            super(itemView);
            ivPkHead = itemView.findViewById(R.id.iv_circle_head);
            tvPkCount = itemView.findViewById(R.id.tv_pk_count);
        }

        @Override
        public void onBindViewHolder(int position) {
            if (position < launchPkUserFansBeans.size()) {
                PKFansBean pkFansBean = launchPkUserFansBeans.get(position);
                tvPkCount.setText(String.valueOf(position + 1));
                if (TextUtils.isEmpty(pkFansBean.avatar)) {
                    String jpg = ImageUrl.getAvatarUrl(pkFansBean.userId, "jpg", pkFansBean.lastUpdateTime);
                    GlideImageLoader.getInstace().displayImage(getContext(), jpg, ivPkHead);
                } else {
                    GlideImageLoader.getInstace().displayImage(context, pkFansBean.avatar, ivPkHead);
                }
            } else {
//                tvPkCount.setText("");
                GlideImageLoader.getInstace().displayImage(context, null, ivPkHead);
            }
//            showRanking(position, ivPkLevel);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            if (onclickRankListener != null) {
                onclickRankListener.onClickRankListener(1);
            }
        }
    }

    public void setOnclickRankListener(OnclickRankListener onclickRankListener) {
        this.onclickRankListener = onclickRankListener;
    }

    private OnclickRankListener onclickRankListener;

    public interface OnclickRankListener {
        void onClickRankListener(int direction);
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
        GlideImageLoader.getInstace().displayImage(context, null, ivMyRank);
        GlideImageLoader.getInstace().displayImage(context, null, ivOtherRank);

        tvLeftOdds.setText("赔率");
        tvRightOdds.setText("赔率");
    }

    public void setListener(TimeDwonListener listener) {
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_punish_way:
                if (clickLintener != null) {
                    clickLintener.onClick(v);
                }
                break;
            case R.id.container_left_odds:
                if (mAnchorId == squareId) {
                    GuessBetDialog.Companion.newInstance(mUserId, guessId, mProgramId, "SQUARE_ARGUMENT", squareOdds)
                            .setShowBottom(true)
                            .show(((LiveDisplayActivity) context).getSupportFragmentManager());
                } else {
                    GuessBetDialog.Companion.newInstance(mUserId, guessId, mProgramId, "COUNTER_ARGUMENT", counterOdds)
                            .setShowBottom(true)
                            .show(((LiveDisplayActivity) context).getSupportFragmentManager());
                }

                break;
            case R.id.container_right_odds:
                if (mAnchorId == squareId) {
                    GuessBetDialog.Companion.newInstance(mUserId, guessId, mProgramId, "COUNTER_ARGUMENT", counterOdds)
                            .setShowBottom(true)
                            .show(((LiveDisplayActivity) context).getSupportFragmentManager());
                } else {
                    GuessBetDialog.Companion.newInstance(mUserId, guessId, mProgramId, "SQUARE_ARGUMENT", squareOdds)
                            .setShowBottom(true)
                            .show(((LiveDisplayActivity) context).getSupportFragmentManager());
                }
                break;
        }
    }

    public interface TimeDwonListener {
        void onTimeDownListener();
    }

    public void reset() {
        pkUserFansBeans.clear();
        launchPkUserFansBeans.clear();
        if (myFollowAdapter != null) {
            myFollowAdapter.notifyDataSetChanged();
        }
        if (oppositeAdapter != null) {
            oppositeAdapter.notifyDataSetChanged();
        }
//        hidePkWindow();
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

//    private void showPopupWindow(View view) {
//        popupWindow.setOutsideTouchable(false);
//        popupWindow.setFocusable(false);
//        popupWindow.setTouchInterceptor(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }
//        });
//        popupWindow.showAsDropDown(view);
//        ((LiveDisplayActivity) context).closeDrawLayout();
//    }

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

//    public void hidePkWindow() {
//        if (popupWindow != null && popupWindow.isShowing()) {
//            popupWindow.dismiss();
//        }
//    }

    private PunishWayClick clickLintener;

    public interface PunishWayClick {
        void onClick(View view);

    }

    public void setPunishWayOnClick(PunishWayClick listener) {
        this.clickLintener = listener;
    }
}

