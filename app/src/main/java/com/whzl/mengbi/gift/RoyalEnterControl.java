package com.whzl.mengbi.gift;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.WelcomeJson;
import com.whzl.mengbi.chat.room.message.messages.WelcomeMsg;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.widget.view.RoyalEnterView;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.UIUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nobody
 * @date 2018/11/5
 */
public class RoyalEnterControl {
    LinearLayout llEnter;
    RoyalEnterView tvEnter;
    ConstraintLayout clEnter;
    private final int screenWidthPixels;
    private ObjectAnimator translationX;
    private ObjectAnimator translationXOut;
    private ValueAnimator showAnim;
    private ValueAnimator hideAnim;

    public RoyalEnterControl() {
        screenWidthPixels = UIUtil.getScreenWidthPixels(BaseApplication.getInstance());
    }

    public void setClEnter(ConstraintLayout clEnter) {
        this.clEnter = clEnter;
    }


    Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setLlEnter(LinearLayout llEnter) {
        this.llEnter = llEnter;
    }

    public void setTvEnter(RoyalEnterView tvEnter) {
        this.tvEnter = tvEnter;
    }

    private List<WelcomeMsg> list = new ArrayList<>();

    private boolean isPlay = false;


    public void showEnter(WelcomeMsg welcomeMsg) {
        list.add(welcomeMsg);
        if (!isPlay && list.size() != 0) {
            startAnimal();
        }

    }

    private void startAnimal() {
        isPlay = true;
        llEnter.setVisibility(View.VISIBLE);
        initTv(list.get(0));
        tvEnter.init();

        showAnim = ValueAnimator.ofFloat(screenWidthPixels, screenWidthPixels / 2 - UIUtil.dip2px(context, 255) / 2);
        showAnim.setInterpolator(new DecelerateInterpolator());
        showAnim.setDuration(1000);
        showAnim.addUpdateListener(animation -> {
            float animatedValue = ((float) animation.getAnimatedValue());
            llEnter.setTranslationX(animatedValue);
            if (animatedValue == screenWidthPixels / 2 - UIUtil.dip2px(context, 255) / 2) {
                tvEnter.startScroll();
                hideTranslateAnim();
            }
        });
        showAnim.start();
    }

    private void initTv(WelcomeMsg welcomeMsg) {
        String prettyNumColor = getPrettyNumColor(welcomeMsg.getmWelcomeJson().getContext().getInfo().getUserBagList());
        String prettyNum = getPrettyNumString(welcomeMsg.getmWelcomeJson().getContext().getInfo().getUserBagList());
        if (welcomeMsg.royalLevel > 0) {
            switch (welcomeMsg.royalLevel) {
                case 1:
                    clEnter.setBackgroundResource(R.drawable.bg_royal_enter_1);
                    break;
                case 2:
                    clEnter.setBackgroundResource(R.drawable.bg_royal_enter_2);
                    break;
                case 3:
                    clEnter.setBackgroundResource(R.drawable.bg_royal_enter_3);
                    break;
                case 4:
                    clEnter.setBackgroundResource(R.drawable.bg_royal_enter_4);
                    break;
                case 5:
                    clEnter.setBackgroundResource(R.drawable.bg_royal_enter_5);
                    break;
                case 6:
                    clEnter.setBackgroundResource(R.drawable.bg_royal_enter_6);
                    break;
                case 7:
                    clEnter.setBackgroundResource(R.drawable.bg_royal_enter_7);
                    break;
                case 8:
                    clEnter.setBackgroundResource(R.drawable.bg_royal_enter_8);
                    break;
                default:
                    break;
            }
            try {
                tvEnter.append(LevelUtil.getRoyalImageResourceSpan(context, welcomeMsg.royalLevel, tvEnter));
            } catch (IOException e) {
                e.printStackTrace();
            }
            tvEnter.append(" ");
        }
//        tvEnter.setMovementMethod(LinkMovementMethod.getInstance());
        if (welcomeMsg.uid != 0) {
            int levelIcon = 0;
            if (welcomeMsg.isAnchor) {
                levelIcon = ResourceMap.getResourceMap().getAnchorLevelIcon(welcomeMsg.userLevel);
            } else {
                levelIcon = ResourceMap.getResourceMap().getUserLevelIcon(welcomeMsg.userLevel);
            }
            tvEnter.append(LevelUtil.getImageResourceSpan(context, levelIcon));
            tvEnter.append(" ");
            if (welcomeMsg.hasGuard) {
//                mHolder.linearLayout.setBackgroundResource(R.drawable.bg_welcome_hasguard);
                tvEnter.append(LevelUtil.getImageResourceSpan(context, R.drawable.guard));
                tvEnter.append(" ");
            }
            if (welcomeMsg.hasVip) {
                tvEnter.append(LevelUtil.getImageResourceSpan(context, R.drawable.ic_vip));
                tvEnter.append(" ");
            }
            if (welcomeMsg.hasSuccubus) {
                tvEnter.append(LevelUtil.getImageResourceSpan(context, R.drawable.ic_succubus));
                tvEnter.append(" ");
            }
            if (null != welcomeMsg.userSpanList) {
                for (SpannableString spanString : welcomeMsg.userSpanList) {
                    tvEnter.append(spanString);
                    tvEnter.append(" ");
                }
            }
            if (!TextUtils.isEmpty(prettyNum)) {
                if ("A".equals(prettyNumColor)) {
                    tvEnter.append(LightSpanString.getAPrettyNumSpan(context, prettyNum, ContextCompat.getColor(context, R.color.a_level_preety_num)));
                } else if ("B".equals(prettyNumColor)) {
                    tvEnter.append(LightSpanString.getBPrettyNumSpan(context, prettyNum, ContextCompat.getColor(context, R.color.b_level_preety_num)));
                } else if ("C".equals(prettyNumColor)) {
                    tvEnter.append(LightSpanString.getPrettyNumSpan(context, prettyNum, ContextCompat.getColor(context, R.color.c_level_preety_num)));
                } else if ("D".equals(prettyNumColor)) {
                    tvEnter.append(LightSpanString.getPrettyNumSpan(context, prettyNum, ContextCompat.getColor(context, R.color.d_level_preety_num)));
                } else if ("E".equals(prettyNumColor)) {
                    tvEnter.append(LightSpanString.getPrettyNumSpan(context, prettyNum, ContextCompat.getColor(context, R.color.e_level_preety_num)));
                } else {
                    tvEnter.append(LightSpanString.getPrettyNumSpan(context, prettyNum, ContextCompat.getColor(context, R.color.e_level_preety_num)));
                }
                tvEnter.append(" ");
            }
            tvEnter.append(welcomeMsg.nickName);
            if (welcomeMsg.royalLevel > 0) {
                tvEnter.append(" 闪亮登场");
            } else {
                tvEnter.append(" 精彩亮相");
            }
        }
    }

    private void hideTranslateAnim() {
        hideAnim = ValueAnimator.ofFloat(screenWidthPixels / 2 - UIUtil.dip2px(context, 255) / 2, -UIUtil.dip2px(context, 255));
        hideAnim.setInterpolator(new AccelerateInterpolator());
        hideAnim.setDuration(1000);
        hideAnim.addUpdateListener(animation -> {
            float animatedValue = ((float) animation.getAnimatedValue());
            llEnter.setTranslationX(animatedValue);
            if (animatedValue == -UIUtil.dip2px(context, 255)) {
                llEnter.setVisibility(View.GONE);
                list.remove(0);
                tvEnter.stopScroll();
                tvEnter.setText("");
                if (list.size() > 0) {
                    startAnimal();
                } else {
                    isPlay = false;
                    llEnter.setVisibility(View.GONE);
                }
            }
        });
        hideAnim.setStartDelay(5000);
        hideAnim.start();
    }

    private void outAnim() {
//        ObjectAnimator translationX = new ObjectAnimator().ofFloat(llEnter, "translationX",
//                0, -UIUtil.dip2px(context, 255));
        if (translationXOut == null) {
            translationXOut = new ObjectAnimator().ofFloat(llEnter, "translationX",
                    screenWidthPixels / 2 - UIUtil.dip2px(context, 255) / 2, -UIUtil.dip2px(context, 255));
            translationXOut.setDuration(1000);
            translationXOut.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    list.remove(0);
                    tvEnter.stopScroll();
                    tvEnter.setText("");
                    if (list.size() > 0) {
                        startAnimal();
                    } else {
                        isPlay = false;
                        llEnter.setVisibility(View.GONE);
                    }
                }
            });
            translationXOut.start();
        }
    }

    public void destroy() {
        isPlay = false;
        llEnter.setVisibility(View.GONE);
        if (tvEnter != null) {
            tvEnter.stopScroll();
            tvEnter.setText("");
        }
        if (showAnim != null) {
            showAnim.removeAllUpdateListeners();
            showAnim.cancel();
            showAnim.end();
            showAnim = null;
        }
        if (hideAnim != null) {
            hideAnim.removeAllUpdateListeners();
            hideAnim.cancel();
            hideAnim.end();
            hideAnim = null;
        }
        list.clear();
    }

    public String getPrettyNumColor(List<WelcomeJson.UserBagItem> userBagList) {
        if (userBagList == null) {
            return "E";
        }
        String string = "E";
        for (WelcomeJson.UserBagItem bagItem : userBagList) {
            if (bagItem.getGoodsType().equals("PRETTY_NUM") && bagItem.getIsEquip().equals("T") && bagItem.goodsColor != null) {
                string = bagItem.goodsColor;
                break;
            }
        }
        return string;
    }

    public String getPrettyNumString(List<WelcomeJson.UserBagItem> userBagList) {
        if (userBagList == null) {
            return "";
        }
        String string = "";
        for (WelcomeJson.UserBagItem bagItem : userBagList) {
            if (bagItem.getGoodsType().equals("PRETTY_NUM") && bagItem.getIsEquip().equals("T")) {
                string = bagItem.getGoodsName();
                break;
            }
        }
        return string;
    }
}
