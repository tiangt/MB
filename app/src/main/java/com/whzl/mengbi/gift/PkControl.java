package com.whzl.mengbi.gift;

import android.view.View;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.PkJson;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.model.entity.PkInfoBean;
import com.whzl.mengbi.ui.widget.view.PkLayout;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

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
    private PkInfoBean.userInfoBean myPkInfo;
    private PkInfoBean.userInfoBean otherPkInfo;

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

    public PkControl(PkLayout pkLayout) {
        this.pkLayout = pkLayout;
    }


    public void init() {
        switch (bean.busiCode) {
            case "PK_ACCEPT_REQUEST": //接收PK请求
                pkLayout.setVisibility(View.VISIBLE);
                startCountDown(5);
                pkLayout.timer("PK进行中 ", bean.pkSurPlusSecond);
                pkLayout.setStateImg(R.drawable.ic_vs);
                if (bean.launchUserProgramId == mProgramId) {
                    pkLayout.setLeftName(bean.launchPkUserInfo.nickname);
                    pkLayout.setRightName(bean.pkUserInfo.nickname);
                    String jpg = ImageUrl.getAvatarUrl(bean.launchPkUserInfo.userId, "jpg", bean.launchPkUserInfo.lastUpdateTime);
                    pkLayout.setLeftImg(jpg);
                    String jpg2 = ImageUrl.getAvatarUrl(bean.pkUserInfo.userId, "jpg", bean.pkUserInfo.lastUpdateTime);
                    pkLayout.setRightImg(jpg2);
                } else if (bean.pkUserProgramId == mProgramId) {
                    pkLayout.setLeftName(bean.pkUserInfo.nickname);
                    pkLayout.setRightName(bean.launchPkUserInfo.nickname);
                    String jpg = ImageUrl.getAvatarUrl(bean.pkUserInfo.userId, "jpg", bean.pkUserInfo.lastUpdateTime);
                    pkLayout.setLeftImg(jpg);
                    String jpg2 = ImageUrl.getAvatarUrl(bean.launchPkUserInfo.userId, "jpg", bean.launchPkUserInfo.lastUpdateTime);
                    pkLayout.setRightImg(jpg2);
                }
                break;
            case "PK_SCORE"://PK分数
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
                        pkLayout.setTied();
                    } else if (bean.launchPkUserScore > bean.pkUserScore) {
                        pkLayout.setLeftWin();
                    } else if (bean.launchPkUserScore < bean.pkUserScore) {
                        pkLayout.setRightWin();
                    }
                } else if (bean.pkUserId == mAnchorId) {
                    if (bean.launchPkUserScore == bean.pkUserScore) {
                        pkLayout.setTied();
                    } else if (bean.pkUserScore > bean.launchPkUserScore) {
                        pkLayout.setLeftWin();
                    } else if (bean.pkUserScore < bean.launchPkUserScore) {
                        pkLayout.setRightWin();
                    }
                }
                break;
            case "PK_TIE_START"://平局时间开始
                pkLayout.setVisibility(View.VISIBLE);
                pkLayout.timer("平局 ", bean.pkTieSurplusSecond);
                pkLayout.setStateImg(R.drawable.ic_vs);
                break;
            case "PK_PUNISH_START"://惩罚时间开始
                pkLayout.setVisibility(View.VISIBLE);
                pkLayout.timer("惩罚时刻 ", bean.pkPunishSurplusSecond);
                pkLayout.setStateImg(R.drawable.ic_ko);
                break;
            case "PK_TIE_FINISH"://平局时间结束
                pkLayout.setVisibility(View.GONE);
                break;
            case "PK_PUNISH_FINISH"://惩罚时间结束
                pkLayout.setVisibility(View.GONE);
                break;
        }
        pkLayout.setListener(new PkLayout.TimeDwonListener() {
            @Override
            public void onTimeDownListener() {
                startCountDown(10);
            }
        });
    }

    public void initNet(PkInfoBean bean) {
        if ("T".equals(bean.pkStatus) || "T".equals(bean.punishStatus) || "T".equals(bean.tieStatus)) {
            pkLayout.setVisibility(View.VISIBLE);
            if (mProgramId == bean.launchPkUserProgramId) {
                myPkInfo = bean.launchUserInfo;
                otherPkInfo = bean.pkUserInfo;
                pkLayout.setLeftScore(bean.launchPkUserScore);
                pkLayout.setRightScore(bean.pkUserScore);
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
                pkLayout.setLeftScore(bean.pkUserScore);
                pkLayout.setRightScore(bean.launchPkUserScore);
                if (bean.pkUserScore == 0 && bean.launchPkUserScore == 0) {
                    pkLayout.setProgress(50);
                } else {
                    double a = bean.pkUserScore;
                    double b = bean.launchPkUserScore + bean.pkUserScore;
                    double v = (a / b) * 100;
                    pkLayout.setProgress((int) v);
                }
            }
            pkLayout.setLeftImg(myPkInfo.avatar);
            pkLayout.setRightImg(otherPkInfo.avatar);
            pkLayout.setLeftName(myPkInfo.nickname);
            pkLayout.setRightName(otherPkInfo.nickname);
            if ("T".equals(bean.pkStatus)) {
                pkLayout.setStateImg(R.drawable.ic_vs);
                pkLayout.timer("PK进行中 ", bean.pkSurPlusSecond);
                if (pkLayout.getProgressBar().getProgress() > 50) {
                    pkLayout.setLeftLead();
                } else if (pkLayout.getProgressBar().getProgress() < 50) {
                    pkLayout.setRightLead();
                }
            }
            if ("T".equals(bean.punishStatus)) {
                pkLayout.setStateImg(R.drawable.ic_ko);
                pkLayout.timer("惩罚时刻 ", bean.punishSurPlusSecond);
                if (pkLayout.getProgressBar().getProgress() > 50) {
                    pkLayout.setLeftWin();
                } else if (pkLayout.getProgressBar().getProgress() < 50) {
                    pkLayout.setRightWin();
                }
            }
            if ("T".equals(bean.tieStatus)) {
                pkLayout.setStateImg(R.drawable.ic_vs);
                pkLayout.timer("平局 ", bean.tieSurPlusSecond);
                pkLayout.setTied();
            }
        }
    }

    private void startCountDown(int count) {
        tvCountDown.setVisibility(View.VISIBLE);
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    tvCountDown.setText(String.valueOf(count-1  - aLong));
                    if (aLong == count) {
                        disposable.dispose();
                        tvCountDown.setVisibility(View.GONE);
                    }
                });
    }

    public void destroy() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
