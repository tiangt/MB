package com.whzl.mengbi.gift;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.chat.room.message.messageJson.PkJson;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.config.PkConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.IsSubProgramBean;
import com.whzl.mengbi.model.entity.PKFansBean;
import com.whzl.mengbi.model.entity.PKResultBean;
import com.whzl.mengbi.model.entity.PunishWaysBean;
import com.whzl.mengbi.model.entity.ResponseInfo;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.PkRankDialog;
import com.whzl.mengbi.ui.widget.recyclerview.SpacesItemDecoration;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.PkLayout;
import com.whzl.mengbi.util.ClickUtil;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.WeakHandler;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import org.jetbrains.annotations.NotNull;

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

    private TextView tvRightName;
    private PopupWindow pkResultPop;
    private SVGAImageView svgaImageView;
    private PopupWindow mvpWindow;
    private ImageView ivCountDown;
    private ImageView ivClose;
    private RecyclerView rvPunishment;
    private Button btnPunishment;
    private LinearLayout rlOtherSideInfo;

    private BaseListAdapter mvpAdapter;
    private int punishWayId;
    private String punishWayName;
    private long mUserId;
    private String streamAddress;
    private String streamType;
    private List<PunishWaysBean.ListBean> punishWays = new ArrayList<>();
    private List<Boolean> mSelectedList;
    private long mvpUserId;
    private String leftAvatar;
    private String rightAvatar;
    private List<PKFansBean> pkUserFansBeans = new ArrayList<>();
    private List<PKFansBean> launchPkUserFansBeans = new ArrayList<>();
    private boolean isClose;
    private String punishment;
    private boolean isMvp;
    private boolean needShow;
    private AnimationDrawable animationDrawable;
    private WeakHandler handler = new WeakHandler();
    private LinearLayout llLeftEffect;
    private TextView tvLeftAddEffect;
    private TextView tvLeftSecondEffect;
    private LinearLayout llRightEffect;
    private TextView tvRightAddEffect;
    private TextView tvRightSecondEffect;
    private Disposable leftCardDispose;
    private Disposable rightCardDispose;

    private int maxLaunchPkScroe;
    private int maxAcceptPkScroe;
    private TextView btnOtherFollow;

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

    public void setRightInfo(TextView tvRightName, TextView btnOtherFollow) {
        this.tvRightName = tvRightName;
        this.btnOtherFollow = btnOtherFollow;
    }

    public PkControl(PkLayout pkLayout, Activity context) {
        this.pkLayout = pkLayout;
        this.context = context;
        initEvent();
    }

    private void initEvent() {
        pkLayout.setPunishWayOnClick(view -> {
            if (ClickUtil.isFastClick()) {
                if (needShow) {
                    showPunishment(isMvp);
                }
            }
        });

        pkLayout.setOnclickRankListener(direction -> PkRankDialog.Companion.newInstance(direction, mProgramId)
                .show(((LiveDisplayActivity) context).getSupportFragmentManager()));
    }

    public void setStartAnim(SVGAImageView svgaImageView) {
        this.svgaImageView = svgaImageView;
    }

    public void setOtherSideInfo(LinearLayout otherPkInfo) {
        this.rlOtherSideInfo = otherPkInfo;
    }

    public void init() {
        LogUtils.e("sssssss  " + bean.busiCode);
        switch (bean.busiCode) {
            case PkConfig.PK_ACCEPT_REQUEST: //接收PK请求
                pkLayout.setVisibility(View.VISIBLE);
                startPKAnim();
                startCountDown(5);
                rlOtherSideInfo.setVisibility(View.VISIBLE);
                pkLayout.timer("PK倒计时 ", bean.pkSurPlusSecond);
                if (bean.launchUserProgramId == mProgramId) {
                    leftAvatar = ImageUrl.getAvatarUrl(bean.launchPkUserInfo.userId, "jpg", System.currentTimeMillis());
                    rightAvatar = ImageUrl.getAvatarUrl(bean.pkUserInfo.userId, "jpg", System.currentTimeMillis());
                    tvRightName.setText(bean.pkUserInfo.nickname);
                    jumpProgramId = bean.pkUserProgramId;
                    jumpNick = bean.pkUserInfo.nickname;
                    isSubProgram((long) SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L), bean.pkUserProgramId);
                } else if (bean.pkUserProgramId == mProgramId) {
                    leftAvatar = ImageUrl.getAvatarUrl(bean.pkUserInfo.userId, "jpg", System.currentTimeMillis());
                    rightAvatar = ImageUrl.getAvatarUrl(bean.launchPkUserInfo.userId, "jpg", System.currentTimeMillis());
                    tvRightName.setText(bean.launchPkUserInfo.nickname);
                    jumpProgramId = bean.launchUserProgramId;
                    jumpNick = bean.launchPkUserInfo.nickname;
                    isSubProgram((long) SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L), bean.launchUserProgramId);
                }
                break;
            case PkConfig.PK_SCORE://PK分数
                if (bean.launchPkUserScore == 0 && bean.pkUserScore == 0) {
                    return;
                }
                if (mAnchorId == bean.launchPkUserId) {
                    if (maxLaunchPkScroe > bean.launchPkUserScore || maxAcceptPkScroe > bean.pkUserScore) {
                        return;
                    }
                    maxLaunchPkScroe = bean.launchPkUserScore;
                    maxAcceptPkScroe = bean.pkUserScore;
                    pkLayout.setLeftScore(bean.launchPkUserScore);
                    pkLayout.setRightScore(bean.pkUserScore);
                    double a = bean.launchPkUserScore;
                    double b = bean.launchPkUserScore + bean.pkUserScore;
                    double v = (a / b) * 100;
                    if ((int) v >= 80) {
                        pkLayout.setAnimation(80);
                    } else if ((int) v <= 20) {
                        pkLayout.setAnimation((20));
                    } else {
                        pkLayout.setAnimation((int) v);
                    }
                } else if (mAnchorId == bean.pkUserId) {
                    if (maxAcceptPkScroe > bean.pkUserScore || maxLaunchPkScroe > bean.launchPkUserScore) {
                        return;
                    }
                    maxLaunchPkScroe = bean.launchPkUserScore;
                    maxAcceptPkScroe = bean.pkUserScore;
                    pkLayout.setLeftScore(bean.pkUserScore);
                    pkLayout.setRightScore(bean.launchPkUserScore);
                    double a = bean.pkUserScore;
                    double b = bean.launchPkUserScore + bean.pkUserScore;
                    double v = (a / b) * 100;
                    if ((int) v >= 80) {
                        pkLayout.setAnimation(80);
                    } else if ((int) v <= 20) {
                        pkLayout.setAnimation((20));
                    } else {
                        pkLayout.setAnimation((int) v);
                    }
                }
                break;
            case PkConfig.PK_RESULT://PK结果
                maxLaunchPkScroe = 0;
                maxAcceptPkScroe = 0;
//                pkLayout.hidePkWindow();
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
//                pkLayout.hidePkWindow();

                expCardDestroy();

                break;
            case PkConfig.PK_TIE_START://平局时间开始
                pkLayout.setVisibility(View.VISIBLE);
                pkLayout.timer("平局 ", bean.pkTieSurplusSecond);
                break;
            case PkConfig.PK_PUNISH_START://惩罚时间开始
                pkLayout.setVisibility(View.VISIBLE);
                pkLayout.timer("惩罚时刻 ", bean.pkPunishSurplusSecond);
                if (null != pkResultPop) {
                    pkResultPop.dismiss();
                }

                if (mvpUserId != 0) {
                    if (mvpUserId == mUserId) {
                        needShow = true;
                        showPunishment(true);
                        isMvp = true;
                    } else {
                        needShow = true;
                        isMvp = false;
                    }
                } else {
                    needShow = true;
                    isMvp = false;
                    pkLayout.setPunishWay(bean.punishWay, mvpWindow);
                }
                break;
            case PkConfig.PK_TIE_FINISH://平局时间结束
                rlOtherSideInfo.setVisibility(View.GONE);
                pkLayout.reset();
//                pkLayout.hidePkWindow();
                pkLayout.setVisibility(View.GONE);
                if (pkResultPop != null && pkResultPop.isShowing()) {
                    pkResultPop.dismiss();
                }
                needShow = false;
                break;
            case PkConfig.PK_PUNISH_FINISH://惩罚时间结束
                rlOtherSideInfo.setVisibility(View.GONE);
                pkLayout.reset();
//                pkLayout.hidePkWindow();
                pkLayout.setVisibility(View.GONE);
                if (mvpWindow != null && mvpWindow.isShowing()) {
                    mvpWindow.dismiss();
                }
                needShow = false;
                break;
            case PkConfig.PK_SCORE_PUSH://用户分数推送
                if (mProgramId == bean.changeUserProgramId) {
                    pkLayout.setLeftPkFans(bean.userFans);
                } else {
                    pkLayout.setRightPkFans(bean.userFans);
                }
                break;
            case PkConfig.PK_PUNISH_WAY: //惩罚方式
                if (!TextUtils.isEmpty(bean.punishWay)) {
                    if (mvpWindow != null && mvpWindow.isShowing()) {
                        mvpWindow.dismiss();
                    }
                    needShow = false;
                    pkLayout.setPunishWay(bean.punishWay, mvpWindow);
                }
                break;

            case PkConfig.PK_OPEN_EXP_CARD: //PK经验卡
                int effSecond = bean.effSecond;
                if (bean.openExpCardUserId == mAnchorId) {
                    if (leftCardDispose != null) {
                        leftCardDispose.dispose();
                    }
                    tvLeftAddEffect.setText(bean.expCardMultiple / 100f + "");
                    leftCardDispose = Observable.interval(1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                LogUtils.e("ssssssss  leftSecond" + aLong + "  " + effSecond);
                                tvLeftSecondEffect.setText(effSecond - aLong + "秒");
                                if (aLong >= effSecond) {
                                    LogUtils.e("ssssssss  leftSecond  dispose");
                                    leftCardDispose.dispose();
                                    tvLeftSecondEffect.setText("");
                                    llLeftEffect.setVisibility(View.GONE);
                                }
                            });
                    llLeftEffect.setVisibility(View.VISIBLE);
                } else {
                    tvRightAddEffect.setText(bean.expCardMultiple / 100f + "");
                    if (rightCardDispose != null) {
                        rightCardDispose.dispose();
                    }
                    rightCardDispose = Observable.interval(1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                LogUtils.e("ssssssss  rightSecond" + aLong + "  " + effSecond);
                                tvRightSecondEffect.setText(effSecond - aLong + "秒");
                                if (aLong >= effSecond) {
                                    LogUtils.e("ssssssss  rightSecond  dispose");
                                    rightCardDispose.dispose();
                                    tvRightSecondEffect.setText("");
                                    llRightEffect.setVisibility(View.GONE);
                                }
                            });
                    llRightEffect.setVisibility(View.VISIBLE);
                }
                break;

        }
        pkLayout.setListener(new PkLayout.TimeDwonListener() {
            @Override
            public void onTimeDownListener() {
                endPkCountDown();
            }
        });

        rlOtherSideInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJumpLiveHouseDialog(jumpProgramId, jumpNick);
            }
        });
    }

    private void isSubProgram(long userId, int programId) {
        if (userId == 0L) {
            btnOtherFollow.setVisibility(View.VISIBLE);
            btnOtherFollow.setOnClickListener(v -> {
                ((LiveDisplayActivity) context).login();
            });
            return;
        }
        HashMap map = new HashMap();
        map.put("userId", userId);
        map.put("programId", programId);
        ApiFactory.getInstance().getApi(Api.class)
                .isSubProgram(ParamsUtils.getSignPramsMap(map))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<IsSubProgramBean>() {
                    @Override
                    public void onSuccess(IsSubProgramBean isSubProgramBean) {
                        if (!isSubProgramBean.isSubs) {
                            btnOtherFollow.setVisibility(View.VISIBLE);
                            btnOtherFollow.setOnClickListener(v -> {
                                addSub(userId, programId);
                            });
                        }
                    }
                });
    }

    private void addSub(long userId, int programId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", programId + "");
        paramsMap.put("userId", userId + "");
        ApiFactory.getInstance().getApi(Api.class)
                .addSub(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>() {
                    @Override
                    public void onSuccess(JsonElement jsonElement) {
                        ToastUtils.showToastUnify(context, "关注成功");
                        btnOtherFollow.setVisibility(View.GONE);
                    }
                });
    }

    public void initNet(PKResultBean bean) {
        LogUtils.e("ssssssss  pkStatus " + bean.pkStatus + "    punishStatus  " + bean.punishStatus + "   tieStatus " + bean.tieStatus);
        if ("T".equals(bean.pkStatus) || "T".equals(bean.punishStatus) || "T".equals(bean.tieStatus)) {
            pkLayout.setVisibility(View.VISIBLE);
            startPKAnim();
            if (mProgramId == bean.launchPkUserProgramId) {
                myPkInfo = bean.launchUserInfo;
                otherPkInfo = bean.pkUserInfo;
                leftAvatar = bean.launchUserInfo.avatar;
                rightAvatar = bean.pkUserInfo.avatar;
                tvRightName.setText(bean.pkUserInfo.nickname);
                pkLayout.setLeftScore(bean.launchPkUserScore);
                pkLayout.setRightScore(bean.pkUserScore);
                jumpProgramId = bean.pkUserProgramId;
                jumpNick = bean.pkUserInfo.nickname;
                if (bean.launchPkUserScore == 0 && bean.pkUserScore == 0) {
                    pkLayout.setProgress(50);
                    pkLayout.initializeProgress = 50;
                } else {
                    double a = bean.launchPkUserScore;
                    double b = bean.launchPkUserScore + bean.pkUserScore;
                    double v = (a / b) * 100;
                    pkLayout.setProgress((int) v);
                    if ((int) v <= 20) {
                        pkLayout.initializeProgress = 20;
                    } else if ((int) v >= 80) {
                        pkLayout.initializeProgress = 80;
                    } else {
                        pkLayout.initializeProgress = (int) v;
                    }
                }
                isSubProgram((long) SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L), bean.pkUserProgramId);
            } else if (mProgramId == bean.pkUserProgramId) {
                myPkInfo = bean.pkUserInfo;
                otherPkInfo = bean.launchUserInfo;
                rightAvatar = bean.launchUserInfo.avatar;
                leftAvatar = bean.pkUserInfo.avatar;
                tvRightName.setText(bean.launchUserInfo.nickname);
                pkLayout.setLeftScore(bean.pkUserScore);
                pkLayout.setRightScore(bean.launchPkUserScore);
                jumpProgramId = bean.launchPkUserProgramId;
                jumpNick = bean.launchUserInfo.nickname;
                if (bean.pkUserScore == 0 && bean.launchPkUserScore == 0) {
                    pkLayout.setProgress(50);
                    pkLayout.initializeProgress = 50;
                } else {
                    double a = bean.pkUserScore;
                    double b = bean.launchPkUserScore + bean.pkUserScore;
                    double v = (a / b) * 100;
                    pkLayout.setProgress((int) v);
                    if ((int) v <= 20) {
                        pkLayout.initializeProgress = 20;
                    } else if ((int) v >= 80) {
                        pkLayout.initializeProgress = 80;
                    } else {
                        pkLayout.initializeProgress = (int) v;
                    }
                }
                isSubProgram((long) SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L), bean.launchPkUserProgramId);
            }

            if ("T".equals(bean.pkStatus)) {
                pkLayout.timer("PK倒计时 ", bean.pkSurPlusSecond);
                if (pkLayout.getProgressBar().getProgress() > 50) {
                } else if (pkLayout.getProgressBar().getProgress() < 50) {
                }
            }
            if ("T".equals(bean.punishStatus)) {
                pkLayout.timer("惩罚时刻 ", bean.punishSurPlusSecond);
                if (bean.mvpUser != null) {
                    punishment = bean.punishWay;
                    if (bean.mvpUser.userId == mUserId) {
                        if (TextUtils.isEmpty(bean.punishWay)) {
                            needShow = true;
                            showPunishment(true);
                            isMvp = true;
                        } else {
                            needShow = false;
                            pkLayout.setPunishWay(bean.punishWay, mvpWindow);
                        }
                    } else {
                        if (punishment.isEmpty()) {
                            needShow = true;
                        } else {
                            needShow = false;
                        }
                        isMvp = false;
                        pkLayout.setPunishWay(bean.punishWay, mvpWindow);
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
            rlOtherSideInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showJumpLiveHouseDialog(jumpProgramId, jumpNick);
                }
            });
            if (mProgramId == bean.launchPkUserProgramId) {
                pkLayout.setPkFanRank(bean.launchPkUserFans, bean.pkUserFans);
            } else {
                pkLayout.setPkFanRank(bean.pkUserFans, bean.launchPkUserFans);
            }

        }

        expCardDestroy();
        if (mProgramId == bean.launchPkUserProgramId) {
            if (bean.lanchPkEffect != null && bean.lanchPkEffect.addMultiple != 0) {
                tvLeftAddEffect.setText(bean.lanchPkEffect.addMultiple / 100f + "");
                int effSecond = bean.lanchPkEffect.effSecond;
                if (effSecond > 0) {
                    leftCardDispose = Observable.interval(1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                LogUtils.e("ssssssss  leftSecond" + aLong);
                                tvLeftSecondEffect.setText(effSecond - aLong + "秒");
                                if (aLong >= effSecond) {
                                    LogUtils.e("ssssssss  leftSecond  dispose");
                                    leftCardDispose.dispose();
                                    tvLeftSecondEffect.setText("");
                                    llLeftEffect.setVisibility(View.GONE);
                                }
                            });
                    llLeftEffect.setVisibility(View.VISIBLE);
                }
            }
            if (bean.pkEffect != null && bean.pkEffect.addMultiple != 0) {
                int effSecond = bean.pkEffect.effSecond;
                tvRightAddEffect.setText(bean.pkEffect.addMultiple / 100f + "");
                if (effSecond > 0) {
                    rightCardDispose = Observable.interval(1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                LogUtils.e("ssssssss  rightSecond" + aLong);
                                tvRightSecondEffect.setText(effSecond - aLong + "秒");
                                if (aLong >= effSecond) {
                                    LogUtils.e("ssssssss  rightSecond  dispose");
                                    rightCardDispose.dispose();
                                    tvRightSecondEffect.setText("");
                                    llRightEffect.setVisibility(View.GONE);
                                }
                            });
                    llRightEffect.setVisibility(View.VISIBLE);
                }
            }
        } else if (mProgramId == bean.pkUserProgramId) {
            if (bean.lanchPkEffect != null && bean.lanchPkEffect.addMultiple != 0) {
                tvRightAddEffect.setText(bean.lanchPkEffect.addMultiple / 100f + "");
                int effSecond = bean.lanchPkEffect.effSecond;
                rightCardDispose = Observable.interval(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> {
                            LogUtils.e("ssssssss  rightSecond" + aLong + "  " + effSecond);
                            tvRightSecondEffect.setText(effSecond - aLong + "秒");
                            if (aLong >= effSecond) {
                                LogUtils.e("ssssssss  rightSecond  dispose");
                                rightCardDispose.dispose();
                                tvRightSecondEffect.setText("");
                                llRightEffect.setVisibility(View.GONE);
                            }
                        });
                llRightEffect.setVisibility(View.VISIBLE);
            }
            if (bean.pkEffect != null && bean.pkEffect.addMultiple != 0) {
                tvLeftAddEffect.setText(bean.pkEffect.addMultiple / 100f + "");
                int effSecond = bean.pkEffect.effSecond;
                leftCardDispose = Observable.interval(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> {
                            LogUtils.e("ssssssss  leftSecond" + aLong + "  " + effSecond);
                            tvLeftSecondEffect.setText(effSecond - aLong + "秒");
                            if (aLong >= effSecond) {
                                LogUtils.e("ssssssss  leftSecond  dispose");
                                leftCardDispose.dispose();
                                tvLeftSecondEffect.setText("");
                                llLeftEffect.setVisibility(View.GONE);
                            }
                        });
                llLeftEffect.setVisibility(View.VISIBLE);
            }
        }


    }

    private void endPkCountDown() {
        if (ivCountDown == null) {
            return;
        }
        ivCountDown.setImageResource(R.drawable.anim_pk_countdown);
        animationDrawable = (AnimationDrawable) ivCountDown.getDrawable();
        ivCountDown.setVisibility(View.VISIBLE);
        animationDrawable.start();

        int duration = 0;
        for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
            duration += animationDrawable.getDuration(i);
        }

        handler.postDelayed(() -> {
            LogUtils.e("Thread.currentThread().getId() " + Thread.currentThread().getId());
            ivCountDown.setVisibility(View.GONE);
            animationDrawable.stop();
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
                    if (animatorSetsuofang == null) {
                        disposable.dispose();
                    }
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
        maxAcceptPkScroe = 0;
        maxLaunchPkScroe = 0;
        if (disposable != null) {
            disposable.dispose();
        }
        expCardDestroy();
        if (animatorSetsuofang != null) {
            tvCountDown.clearAnimation();
            tvCountDown.setVisibility(View.GONE);
        }
        isMvp = false;
        needShow = false;
        if (svgaImageView != null) {
            svgaImageView.stopAnimation();
        }
        if (animationDrawable != null) {
            animationDrawable.stop();
            ivCountDown.setVisibility(View.GONE);
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (btnOtherFollow != null) {
            btnOtherFollow.setVisibility(View.GONE);
        }
        if (tvRightName != null) {
            tvRightName.setText("");
        }
    }

    private void expCardDestroy() {
        if (leftCardDispose != null) {
            leftCardDispose.dispose();
        }
        if (rightCardDispose != null) {
            rightCardDispose.dispose();
        }
        if (llLeftEffect != null) {
            llLeftEffect.setVisibility(View.GONE);
        }
        if (llRightEffect != null) {
            llRightEffect.setVisibility(View.GONE);
        }
        if (tvLeftSecondEffect != null) {
            tvLeftSecondEffect.setText("");
        }
        if (tvRightSecondEffect != null) {
            tvRightSecondEffect.setText("");
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
        dialog.setCancelable(false);
        dialog.show();
    }


    /**
     * PK结束
     */
    private void showPKResult(int status) {
        ((LiveDisplayActivity) context).closeDrawLayout();
        View popView = LayoutInflater.from(context).inflate(R.layout.pop_pk_end, null);
        pkResultPop = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pkResultPop.setBackgroundDrawable(new BitmapDrawable());
        pkResultPop.setOutsideTouchable(true);
        pkResultPop.setFocusable(true);
        pkResultPop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        ImageView leftResult = popView.findViewById(R.id.tv_left_result);
        ImageView rightResult = popView.findViewById(R.id.tv_right_result);
        ImageView mvpTitle = popView.findViewById(R.id.tv_mvp_title);
        CircleImageView ivLeftAvatar = popView.findViewById(R.id.iv_left_avatar);
        CircleImageView ivRightAvatar = popView.findViewById(R.id.iv_right_avatar);
        LinearLayout llMsg = popView.findViewById(R.id.ll_msg);
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
                LogUtils.e("Thread.currentThread().getId() " + Thread.currentThread().getId());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    PopupWindowCompat.showAsDropDown(pkResultPop, pkLayout, offsetX, offsetY, Gravity.START);
                }
            }
        });
        if (null != bean.mvpUser) {
            mvpName.setText(bean.mvpUser.nickname);
            mvpUserId = bean.mvpUser.userId;
        }
        if (status == 0) {
//            leftResult.setText("平");
//            rightResult.setText("平");
            GlideImageLoader.getInstace().displayImage(context, R.drawable.ic_tie_pk_end, leftResult);
            GlideImageLoader.getInstace().displayImage(context, R.drawable.ic_tie_pk_end, rightResult);
            ivLeftAvatar.setBorderColor(Color.parseColor("#979d8c"));
            ivRightAvatar.setBorderColor(Color.parseColor("#979d8c"));

            mvpTitle.setVisibility(View.GONE);
            llMsg.setVisibility(View.GONE);
            GlideImageLoader.getInstace().displayImage(context, leftAvatar, ivLeftAvatar);
            GlideImageLoader.getInstace().displayImage(context, rightAvatar, ivRightAvatar);
        } else if (status == 1) {
//            leftResult.setText("胜");
//            rightResult.setText("败");
//            rightResult.setTextColor(Color.argb(125, 255, 255, 255));
            GlideImageLoader.getInstace().displayImage(context, R.drawable.ic_win_pk_end, leftResult);
            GlideImageLoader.getInstace().displayImage(context, R.drawable.ic_lose_pk_end, rightResult);
            ivLeftAvatar.setBorderColor(Color.parseColor("#f7b927"));
            ivRightAvatar.setBorderColor(Color.parseColor("#979d8c"));

            GlideImageLoader.getInstace().displayImage(context, leftAvatar, ivLeftAvatar);
            GlideImageLoader.getInstace().displayImage(context, rightAvatar, ivRightAvatar);
        } else if (status == 2) {
//            leftResult.setText("败");
//            rightResult.setText("胜");
//            leftResult.setTextColor(Color.argb(125, 255, 255, 255));
            GlideImageLoader.getInstace().displayImage(context, R.drawable.ic_lose_pk_end, leftResult);
            GlideImageLoader.getInstace().displayImage(context, R.drawable.ic_win_pk_end, rightResult);
            ivLeftAvatar.setBorderColor(Color.parseColor("#979d8c"));
            ivRightAvatar.setBorderColor(Color.parseColor("#f7b927"));

            GlideImageLoader.getInstace().displayImage(context, leftAvatar, ivLeftAvatar);
            GlideImageLoader.getInstace().displayImage(context, rightAvatar, ivRightAvatar);
        }
    }

    /**
     * 惩罚
     */
    private void showPunishment(boolean isMvp) {
        ((LiveDisplayActivity) context).closeDrawLayout();
        View popView = LayoutInflater.from(context).inflate(R.layout.pop_mvp, null);
        mvpWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mvpWindow.setBackgroundDrawable(new BitmapDrawable());
        mvpWindow.setOutsideTouchable(true);
//        mvpWindow.setFocusable(false);
//        mvpWindow.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }
//        });
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        popView.measure(width, height);
        pkLayout.measure(width, height);
        int offsetX = Math.abs(popView.getMeasuredWidth() - pkLayout.getMeasuredWidth()) / 2;
        int offsetY = -(popView.getMeasuredHeight() + pkLayout.getMeasuredHeight() - 130);

        ivClose = popView.findViewById(R.id.iv_close_mvp);
        rvPunishment = popView.findViewById(R.id.rv_punishment);
        btnPunishment = popView.findViewById(R.id.btn_punishment);

        initRecy(isMvp);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvpWindow.dismiss();
                isClose = true;
            }
        });

        if (TextUtils.isEmpty(punishment)) {
            pkLayout.post(new Runnable() {
                @Override
                public void run() {
                    LogUtils.e("Thread.currentThread().getId() " + Thread.currentThread().getId());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !mvpWindow.isShowing()) {
                        PopupWindowCompat.showAsDropDown(mvpWindow, pkLayout, offsetX, offsetY, Gravity.START);
                    }
                }
            });
        }

        getPunishWays();
    }

    private void initRecy(boolean isMvp) {
        mSelectedList = new ArrayList<Boolean>();
        rvPunishment.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        rvPunishment.setFocusableInTouchMode(false);
        rvPunishment.setHasFixedSize(true);
        rvPunishment.setLayoutManager(new GridLayoutManager(context, 3));
        rvPunishment.addItemDecoration(new SpacesItemDecoration(10));
        mvpAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return punishWays == null ? 0 : punishWays.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(context).inflate(R.layout.item_punishment, parent, false);
                return new MVPViewHolder(itemView, isMvp);
            }
        };
        rvPunishment.setAdapter(mvpAdapter);
    }

    public void setLeftExpCard(LinearLayout llLeftEffect, TextView tvLeftAddEffect, TextView tvLeftSecondEffect) {
        this.llLeftEffect = llLeftEffect;
        this.tvLeftAddEffect = tvLeftAddEffect;
        this.tvLeftSecondEffect = tvLeftSecondEffect;
    }

    public void setRightExpCard(LinearLayout llRightEffect, TextView tvRightAddEffect, TextView tvRightSecondEffect) {
        this.llRightEffect = llRightEffect;
        this.tvRightAddEffect = tvRightAddEffect;
        this.tvRightSecondEffect = tvRightSecondEffect;
    }


    class MVPViewHolder extends BaseViewHolder {
        TextView tvPunishment;
        private boolean isMvp;

        public MVPViewHolder(View itemView, boolean isMvp) {
            super(itemView);
            tvPunishment = itemView.findViewById(R.id.tv_punishment);
            this.isMvp = isMvp;
        }

        @Override
        public void onBindViewHolder(int position) {
            tvPunishment.setText(punishWays.get(position).getName());
            if (mSelectedList == null || mSelectedList.size() == 0) {
                return;
            }
            if (mSelectedList.get(position)) {
                tvPunishment.setBackgroundResource(R.drawable.shape_item_2);
                tvPunishment.setTextColor(Color.WHITE);
            } else {
                tvPunishment.setBackgroundResource(R.drawable.shape_item_1);
                tvPunishment.setTextColor(Color.argb(255, 42, 46, 54));
            }
            if (isMvp) {
                tvPunishment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < mSelectedList.size(); i++) {
                            mSelectedList.set(i, false);
                        }
                        mSelectedList.set(position, true);
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
    }

    private void startPKAnim() {
        svgaImageView.setLoops(1);
        SVGAParser parser = new SVGAParser(context);
        try {
            parser.parse("svga/start_pk.svga", new SVGAParser.ParseCompletion() {
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
        hashMap.put("wayId", punishWayId);
        hashMap.put("userId", mvpUserId);
        hashMap.put("anchorId", mAnchorId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.PUNISH_WAY, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        String jsonStr = result.toString();
                        ResponseInfo responseInfo = GsonUtils.GsonToBean(jsonStr, ResponseInfo.class);
                        if (responseInfo.getCode() == 200) {
                            //选择惩罚方式
                            pkLayout.setMvpPunishWay(punishWayName, mvpWindow);
                            mvpWindow.dismiss();
                        } else {
                            showToast(responseInfo.getMsg());
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
                            punishWays.clear();
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
