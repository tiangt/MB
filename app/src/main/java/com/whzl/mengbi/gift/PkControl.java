package com.whzl.mengbi.gift;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.player.KSYTextureView;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.chat.room.message.messageJson.PkJson;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.model.entity.PKResultBean;
import com.whzl.mengbi.model.entity.PkInfoBean;
import com.whzl.mengbi.model.entity.PunishWaysBean;
import com.whzl.mengbi.model.entity.ResponseInfo;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.widget.recyclerview.SpacesItemDecoration;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.PkLayout;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.whzl.mengbi.util.ToastUtils.showToast;

/**
 * @author nobody
 * @date 2018/10/9
 */
public class PkControl {
    private PkLayout pkLayout;

    private PkJson.ContextBean bean;
    private int mProgramId;
    private int mAnchorId;
    private TextView tvCountDown;
    private Disposable disposable;
    private PKResultBean.UserInfoBean myPkInfo;
    private PKResultBean.UserInfoBean otherPkInfo;
    private AnimatorSet animatorSetsuofang;

    private int jumpProgramId;
    private String jumpNick;

    private Activity context;

    private RelativeLayout layout;
    private CircleImageView ivRightHead;
    private TextView tvRightName;
    private KSYTextureView ksyTextureView;
    private PopupWindow pkResultPop;
    private SVGAImageView svgaImageView;
    private PopupWindow mvpWindow;

    private ImageView ivClose;
    private RecyclerView rvPunishment;
    private Button btnPunishment;
    private BaseListAdapter mvpAdapter;
    private int punishWayId;
    private String punishWayName;
    private long mUserId;
    private String streamAddress;
    private String streamType;
    private ImageView ivCountDown;
    private List<PunishWaysBean.ListBean> punishWays = new ArrayList<>();
    private List<Boolean> mSelectedList;

    public void setBean(PkJson.ContextBean bean) {
        this.bean = bean;
    }

    public void setTvCountDown(TextView tvCountDown) {
        this.tvCountDown = tvCountDown;
    }

    public void setmProgramId(int mProgramId) {
        this.mProgramId = mProgramId;
    }

    public void setmAnchorId(int mAnchorId) {
        this.mAnchorId = mAnchorId;
    }

    public void setUserId(long userId) {
        this.mUserId = userId;
    }

    public void setIvCountDown(ImageView ivCountDown) {
        this.ivCountDown = ivCountDown;
    }

    public void setRightInfo(RelativeLayout layout, CircleImageView ivRightHead, TextView tvRightName) {
        this.layout = layout;
        this.ivRightHead = ivRightHead;
        this.tvRightName = tvRightName;
    }

    public PkControl(PkLayout pkLayout, Activity context) {
        this.pkLayout = pkLayout;
        this.context = context;
    }

    public void setStartAnim(SVGAImageView svgaImageView) {
        this.svgaImageView = svgaImageView;
    }

    public void setOtherLive(KSYTextureView ksyTextureView) {
        this.ksyTextureView = ksyTextureView;
    }

    public void init() {
        switch (bean.busiCode) {
            case "PK_ACCEPT_REQUEST": //接收PK请求
                pkLayout.setVisibility(View.VISIBLE);
                layout.setVisibility(View.VISIBLE);
                startPKAnim();
                startCountDown(5);
                pkLayout.timer("PK进行中 ", bean.pkSurPlusSecond);
                if (bean.launchUserProgramId == mProgramId) {
                    pkLayout.setPkFanRank(bean.getLaunchPkUserFans(), bean.getPkUserFans());
                    GlideImageLoader.getInstace().displayImage(context, bean.pkUserInfo.avatar, ivRightHead);
                    tvRightName.setText(bean.pkUserInfo.nickname);
                    jumpProgramId = bean.pkUserProgramId;
                    jumpNick = bean.pkUserInfo.nickname;
                    if (bean.pkUserLiveAndStreamAddress.showStreams != null) {
                        for (int i = 0; i < bean.pkUserLiveAndStreamAddress.showStreams.size(); i++) {
                            streamType = bean.pkUserLiveAndStreamAddress.showStreams.get(i).streamType;
                            streamAddress = bean.pkUserLiveAndStreamAddress.showStreams.get(i).streamAddress;
                            setDateSourceForPlayer2(streamAddress);
                        }
                        otherSideLive();
                    }
                } else if (bean.pkUserProgramId == mProgramId) {
                    pkLayout.setPkFanRank(bean.getLaunchPkUserFans(), bean.getPkUserFans());
                    GlideImageLoader.getInstace().displayImage(context, bean.launchPkUserInfo.avatar, ivRightHead);
                    tvRightName.setText(bean.launchPkUserInfo.nickname);
                    jumpProgramId = bean.launchUserProgramId;
                    jumpNick = bean.launchPkUserInfo.nickname;
                    if (bean.launchPkUserLiveAndStreamAddress.showStreams != null) {
                        for (int i = 0; i < bean.launchPkUserLiveAndStreamAddress.showStreams.size(); i++) {
                            streamType = bean.launchPkUserLiveAndStreamAddress.showStreams.get(i).streamType;
                            streamAddress = bean.launchPkUserLiveAndStreamAddress.showStreams.get(i).streamAddress;
                            setDateSourceForPlayer2(streamAddress);
                        }
                        otherSideLive();
                    }
                }
                break;
            case "PK_SCORE"://PK分数
                if (bean.launchPkUserScore == 0 && bean.pkUserScore == 0) {
                    return;
                }
                if (mAnchorId == bean.launchPkUserId) {
                    pkLayout.setLeftScore(bean.launchPkUserScore);
                    pkLayout.setRightScore(bean.pkUserScore);
                    double a = bean.launchPkUserScore;
                    double b = bean.launchPkUserScore + bean.pkUserScore;
                    double v = (a / b) * 100;
                    pkLayout.setAnimation((int) v);
                } else if (mAnchorId == bean.pkUserId) {
                    pkLayout.setLeftScore(bean.pkUserScore);
                    pkLayout.setRightScore(bean.launchPkUserScore);
                    double a = bean.pkUserScore;
                    double b = bean.launchPkUserScore + bean.pkUserScore;
                    double v = (a / b) * 100;
                    pkLayout.setAnimation((int) v);
                }
                break;
            case "PK_RESULT"://PK结果
                if (bean.launchPkUserId == mAnchorId) {
                    if (bean.launchPkUserScore == bean.pkUserScore) {
                        showPKResult(0);
                    } else if (bean.launchPkUserScore > bean.pkUserScore) {
                        showPKResult(1);
                    } else if (bean.launchPkUserScore < bean.pkUserScore) {
                        showPKResult(2);
                    }
                } else if (bean.pkUserId == mAnchorId) {
                    if (bean.launchPkUserScore == bean.pkUserScore) {
                        showPKResult(0);
                    } else if (bean.pkUserScore > bean.launchPkUserScore) {
                        showPKResult(1);
                    } else if (bean.pkUserScore < bean.launchPkUserScore) {
                        showPKResult(2);
                    }
                }
                break;
            case "PK_TIE_START"://平局时间开始
                pkLayout.setVisibility(View.VISIBLE);
                pkLayout.timer("平局 ", bean.pkTieSurplusSecond);
                layout.setVisibility(View.GONE);
                break;
            case "PK_PUNISH_START"://惩罚时间开始
                pkLayout.setVisibility(View.VISIBLE);
                pkLayout.timer("惩罚时刻 ", bean.pkPunishSurplusSecond);
                if (null != pkResultPop) {
                    pkResultPop.dismiss();
                }
//                if (null != bean.mvpUserBean) {
//                    if (mUserId == bean.mvpUserBean.userId) {
                showPunishment();
//                    }
//                }
                break;
            case "PK_TIE_FINISH"://平局时间结束
                layout.setVisibility(View.GONE);
                pkResultPop.dismiss();
                pkLayout.setVisibility(View.GONE);
                pkLayout.reset();
                break;
            case "PK_PUNISH_FINISH"://惩罚时间结束
                layout.setVisibility(View.GONE);
                pkLayout.setVisibility(View.GONE);
                pkLayout.reset();
                break;
        }
        pkLayout.setListener(new PkLayout.TimeDwonListener() {
            @Override
            public void onTimeDownListener() {
                endPkCountDown();
            }
        });

//        pkLayout.setRightClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showJumpLiveHouseDialog(jumpProgramId, jumpNick);
//            }
//        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJumpLiveHouseDialog(jumpProgramId, jumpNick);
            }
        });
    }

    public void initNet(PKResultBean bean) {
        if ("T".equals(bean.pkStatus) || "T".equals(bean.punishStatus) || "T".equals(bean.tieStatus)) {
            pkLayout.setVisibility(View.VISIBLE);
            if (mProgramId == bean.launchPkUserProgramId) {
                myPkInfo = bean.launchUserInfo;
                otherPkInfo = bean.pkUserInfo;
                GlideImageLoader.getInstace().displayImage(context, bean.pkUserInfo.avatar, ivRightHead);
                tvRightName.setText(bean.pkUserInfo.nickname);
                pkLayout.setLeftScore(bean.launchPkUserScore);
                pkLayout.setRightScore(bean.pkUserScore);
                jumpProgramId = bean.pkUserProgramId;
                jumpNick = bean.pkUserInfo.nickname;
                if (bean.launchPkUserScore == 0 && bean.pkUserScore == 0) {
                    pkLayout.setProgress(50);
                } else {
                    double a = bean.launchPkUserScore;
                    double b = bean.launchPkUserScore + bean.pkUserScore;
                    double v = (a / b) * 100;
                    pkLayout.setProgress((int) v);
                }
            } else if (mProgramId == bean.pkUserProgramId) {
                myPkInfo = bean.pkUserInfo;
                otherPkInfo = bean.launchUserInfo;
                GlideImageLoader.getInstace().displayImage(context, bean.launchUserInfo.avatar, ivRightHead);
                tvRightName.setText(bean.launchUserInfo.nickname);
                pkLayout.setLeftScore(bean.pkUserScore);
                pkLayout.setRightScore(bean.launchPkUserScore);
                jumpProgramId = bean.launchPkUserProgramId;
                jumpNick = bean.launchUserInfo.nickname;
                if (bean.pkUserScore == 0 && bean.launchPkUserScore == 0) {
                    pkLayout.setProgress(50);
                } else {
                    double a = bean.pkUserScore;
                    double b = bean.launchPkUserScore + bean.pkUserScore;
                    double v = (a / b) * 100;
                    pkLayout.setProgress((int) v);
                }
            }

            if ("T".equals(bean.pkStatus)) {
                pkLayout.timer("PK进行中 ", bean.pkSurPlusSecond);
                if (pkLayout.getProgressBar().getProgress() > 50) {
//                    pkLayout.setLeftLead();
                } else if (pkLayout.getProgressBar().getProgress() < 50) {
//                    pkLayout.setRightLead();
                }
            }
            if ("T".equals(bean.punishStatus)) {
                pkLayout.timer("惩罚时刻 ", bean.punishSurPlusSecond);
                if (pkLayout.getProgressBar().getProgress() > 50) {
                    if (null != bean.mvpUser) {
//                        if (mUserId == bean.mvpUser.userId) {
                        showPunishment();
//                        }
                    }
                } else if (pkLayout.getProgressBar().getProgress() < 50) {
                    if (null != bean.mvpUser) {
//                        if (mUserId == bean.mvpUser.userId) {
                        showPunishment();
//                        }
                    }
                }
            }
            if ("T".equals(bean.tieStatus)) {
                pkLayout.timer("平局 ", bean.tieSurPlusSecond);
            }
            pkLayout.setListener(new PkLayout.TimeDwonListener() {
                @Override
                public void onTimeDownListener() {
                    endPkCountDown();
                }
            });
//            pkLayout.setRightClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showJumpLiveHouseDialog(jumpProgramId, jumpNick);
//                }
//            });
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showJumpLiveHouseDialog(jumpProgramId, jumpNick);
                }
            });
        }
    }

    private void endPkCountDown() {
        ivCountDown.setImageResource(R.drawable.anim_pk_countdown);
        AnimationDrawable animationDrawable = (AnimationDrawable) ivCountDown.getDrawable();
        animationDrawable.start();

        int duration = 0;
        for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
            duration += animationDrawable.getDuration(i);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                ivCountDown.setVisibility(View.GONE);
                animationDrawable.stop();
            }

        }, duration);
    }

    private void startCountDown(int count) {
        //组合动画
        if (animatorSetsuofang == null) {
            animatorSetsuofang = new AnimatorSet();
        }
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(tvCountDown, "scaleX", 1f, 3f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(tvCountDown, "scaleY", 1f, 3f, 1f);

        animatorSetsuofang.setDuration(500);
        animatorSetsuofang.setInterpolator(new OvershootInterpolator());
        animatorSetsuofang.play(scaleX).with(scaleY);//两个动画同时开始

        tvCountDown.setVisibility(View.VISIBLE);
        tvCountDown.setText(String.valueOf(count));
        animatorSetsuofang.start();
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    LogUtils.e("ssssssss  animal" + aLong);
                    tvCountDown.setText(String.valueOf(count - 1 - aLong));
                    animatorSetsuofang.start();
                    if (aLong >= count - 1) {
                        LogUtils.e("ssssssss  animal  dispose");
                        disposable.dispose();
                        tvCountDown.setVisibility(View.GONE);
                    }
                });
    }

    public void destroy() {
        if (disposable != null) {
            disposable.dispose();
        }
        if (animatorSetsuofang != null) {
            animatorSetsuofang.end();
            animatorSetsuofang = null;
        }
    }

    private void showJumpLiveHouseDialog(int programId, String nickName) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.alert);
        dialog.setMessage(context.getString(R.string.jump_live_house, nickName));
        dialog.setNegativeButton(R.string.cancel, null);
        dialog.setPositiveButton(R.string.confirm, (dialog1, which) -> {
            if (disposable != null) {
                disposable.dispose();
            }
            tvCountDown.setVisibility(View.GONE);
            Intent intent = new Intent(context, LiveDisplayActivity.class);
            intent.putExtra(BundleConfig.PROGRAM_ID, programId);
            context.startActivity(intent);
        });
        dialog.show();
    }

    private void otherSideLive() {
        ksyTextureView.setDecodeMode(KSYMediaPlayer.KSYDecodeMode.KSY_DECODE_MODE_AUTO);
        ksyTextureView.setOnPreparedListener(iMediaPlayer -> {
            ksyTextureView.setVisibility(View.VISIBLE);
            ksyTextureView.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            ksyTextureView.start();
        });

        ksyTextureView.setOnCompletionListener(iMediaPlayer -> {
            ksyTextureView.stop();
            ksyTextureView.release();
        });

    }

    private void setDateSourceForPlayer2(String stream) {
        try {
            ksyTextureView.setDataSource(stream);
            ksyTextureView.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * PK结束
     */
    private void showPKResult(int status) {
        View popView = LayoutInflater.from(context).inflate(R.layout.pop_pk_end, null);
        pkResultPop = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pkResultPop.setOutsideTouchable(true);
        pkResultPop.setFocusable(true);
        pkResultPop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        TextView leftResult = popView.findViewById(R.id.tv_left_result);
        TextView rightResult = popView.findViewById(R.id.tv_right_result);
        TextView mvpTitle = popView.findViewById(R.id.tv_mvp_title);
        CircleImageView leftHead = popView.findViewById(R.id.iv_left_head);
        CircleImageView rightHead = popView.findViewById(R.id.iv_right_head);
        TextView mvpName = popView.findViewById(R.id.tv_mvp_name);
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        popView.measure(width, height);
        pkLayout.measure(width, height);
        int offsetX = Math.abs(popView.getMeasuredWidth() - pkLayout.getMeasuredWidth()) / 2;
        int offsetY = -(popView.getMeasuredHeight() + pkLayout.getMeasuredHeight() + 10);

        pkLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    PopupWindowCompat.showAsDropDown(pkResultPop, pkLayout, offsetX, offsetY, Gravity.START);
                }
            }
        });
        showToast("MVP = "+bean.mvpUserBean.nickname);
        if (null != bean.mvpUserBean) {
            mvpName.setText(bean.mvpUserBean.nickname);
        }
        if (status == 0) {
            leftResult.setText("平");
            rightResult.setText("平");
            mvpTitle.setVisibility(View.INVISIBLE);
            GlideImageLoader.getInstace().displayImage(context, bean.pkUserInfo.avatar, leftHead);
            GlideImageLoader.getInstace().displayImage(context, bean.launchPkUserInfo.avatar, rightHead);
        } else if (status == 1) {
            leftResult.setText("胜");
            rightResult.setText("败");
            rightResult.setTextColor(Color.argb(125, 255, 255, 255));
            GlideImageLoader.getInstace().displayImage(context, bean.pkUserInfo.avatar, leftHead);
            GlideImageLoader.getInstace().displayImage(context, bean.launchPkUserInfo.avatar, rightHead);
        } else if (status == 2) {
            leftResult.setText("败");
            leftResult.setTextColor(Color.argb(125, 255, 255, 255));
            rightResult.setText("胜");
            GlideImageLoader.getInstace().displayImage(context, bean.pkUserInfo.avatar, leftHead);
            GlideImageLoader.getInstace().displayImage(context, bean.launchPkUserInfo.avatar, rightHead);
        }
    }

    /**
     * 惩罚
     */
    private void showPunishment() {
        View popView = LayoutInflater.from(context).inflate(R.layout.pop_mvp, null);
        mvpWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mvpWindow.setOutsideTouchable(false);
        mvpWindow.setFocusable(false);
        mvpWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        popView.measure(width, height);
        pkLayout.measure(width, height);
        int offsetX = Math.abs(popView.getMeasuredWidth() - pkLayout.getMeasuredWidth()) / 2;
        int offsetY = -(popView.getMeasuredHeight() + pkLayout.getMeasuredHeight() - 130);

        ivClose = popView.findViewById(R.id.iv_close_mvp);
        rvPunishment = popView.findViewById(R.id.rv_punishment);
        btnPunishment = popView.findViewById(R.id.btn_punishment);

        initRecy();

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvpWindow.dismiss();
            }
        });

        pkLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    PopupWindowCompat.showAsDropDown(mvpWindow, pkLayout, offsetX, offsetY, Gravity.START);
                }
            }
        });

//        initRecy();
        getPunishWays();
    }

    private void initRecy() {
        mSelectedList = new ArrayList<Boolean>();
        rvPunishment.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        rvPunishment.setFocusableInTouchMode(false);
        rvPunishment.setHasFixedSize(true);
        rvPunishment.setLayoutManager(new GridLayoutManager(context, 3));
        rvPunishment.addItemDecoration(new SpacesItemDecoration(20));
        mvpAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return punishWays == null ? 0 : punishWays.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(context).inflate(R.layout.item_punishment, parent, false);
                return new MVPViewHolder(itemView);
            }
        };
        rvPunishment.setAdapter(mvpAdapter);
    }


    class MVPViewHolder extends BaseViewHolder {
        TextView tvPunishment;

        public MVPViewHolder(View itemView) {
            super(itemView);
            tvPunishment = itemView.findViewById(R.id.tv_punishment);
        }

        @Override
        public void onBindViewHolder(int position) {
            tvPunishment.setText(punishWays.get(position).getName());
            if (mSelectedList.get(position)) {
                tvPunishment.setBackgroundResource(R.drawable.shape_item_2);
                tvPunishment.setTextColor(Color.WHITE);
            } else {
                tvPunishment.setBackgroundResource(R.drawable.shape_item_1);
                tvPunishment.setTextColor(Color.argb(255, 42, 46, 54));
            }
            tvPunishment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mSelectedList.size(); i++) {
                        mSelectedList.set(i, false);
                    }
                    mSelectedList.set(position, true);
                    showToast(position + "");
                    btnPunishment.setVisibility(View.VISIBLE);
                    btnPunishment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setMVPPunishment(punishWays.get(position).getId(),
                                    punishWays.get(position).getName());
                        }
                    });
                    mvpAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void startPKAnim() {
        svgaImageView.setLoops(1);
        SVGAParser parser = new SVGAParser(context);
        try {
            parser.parse("start_pk.svga", new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    SVGADrawable drawable = new SVGADrawable(videoItem);
                    svgaImageView.setImageDrawable(drawable);
                    svgaImageView.startAnimation();
                }

                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            System.out.print(true);
        }
    }

    private void setMVPPunishment(int punishWayId, String punishWayName) {
        HashMap hashMap = new HashMap();
        if (null == bean.mvpUserBean) {
            return;
        }
        hashMap.put("wayId", punishWayId);
        hashMap.put("userId", bean.mvpUserBean.userId);
        hashMap.put("anchorId", mAnchorId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.PUNISH_WAY, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        String jsonStr = result.toString();
                        ResponseInfo responseInfo = GsonUtils.GsonToBean(jsonStr, ResponseInfo.class);
                        if (responseInfo.getCode() == 200) {
                            showToast("Success");
                            //选择惩罚方式
                            pkLayout.setPunishWayName(punishWayName);
                            mvpWindow.dismiss();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("errorMsg" + errorMsg.toString());
                    }
                });
    }

    private void getPunishWays() {
        HashMap map = new HashMap();
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        ApiFactory.getInstance().getApi(Api.class)
                .getPunishWays(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<PunishWaysBean>() {
                    @Override
                    public void onSuccess(PunishWaysBean dataBean) {
                        if (dataBean != null) {
                            for (int i = 0; i < dataBean.getList().size(); i++) {
                                punishWays.add(dataBean.getList().get(i));
                                mSelectedList.add(false);
                            }
                            mvpAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }
}
