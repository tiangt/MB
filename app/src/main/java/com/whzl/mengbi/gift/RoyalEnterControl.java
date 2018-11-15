package com.whzl.mengbi.gift;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.WelcomeJson;
import com.whzl.mengbi.chat.room.message.messages.WelcomeMsg;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.widget.view.RoyalEnterView;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.RxTimerUtil;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.glide.GlideImageLoader;

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
    ImageView ivEnter;
    ConstraintLayout clEnter;
    private final int screenWidthPixels;

    public RoyalEnterControl() {
        screenWidthPixels = UIUtil.getScreenWidthPixels(BaseApplication.getInstance());
    }

    public void setClEnter(ConstraintLayout clEnter) {
        this.clEnter = clEnter;
    }

    public void setIvEnter(ImageView ivEnter) {
        this.ivEnter = ivEnter;
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
        String imageUrl = ImageUrl.getImageUrl(list.get(0).getCarId(), "jpg");
        GlideImageLoader.getInstace().displayImage(context, imageUrl, ivEnter);

//        ObjectAnimator translationX = new ObjectAnimator().ofFloat(llEnter, "translationX",
//                -UIUtil.dip2px(context, 255), 0);
        ObjectAnimator translationX = new ObjectAnimator().ofFloat(llEnter, "translationX",
                screenWidthPixels, screenWidthPixels / 2 - UIUtil.dip2px(context, 255) / 2);
        translationX.setDuration(1000);  //设置动画时间
        translationX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                tvEnter.startScroll();
                RxTimerUtil.timer(5000, new RxTimerUtil.IRxNext() {
                    @Override
                    public void doNext(long number) {
                        outAnim();
                    }
                });
            }
        });
        translationX.start();
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
                tvEnter.append(LevelUtil.getImageResourceSpan(context, R.drawable.ic_vip_chat));
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
                    tvEnter.append(LightSpanString.getPrettyNumBgSpan(context, "靓", Color.parseColor("#8bc1fe"), Color.parseColor("#ffffff")));
                    tvEnter.append(LightSpanString.getPrettyNumSpan(context, prettyNum, Color.parseColor("#8bc1fe"), Color.parseColor("#8bc1fe")));
                } else if ("B".equals(prettyNumColor)) {
                    tvEnter.append(LightSpanString.getPrettyNumBgSpan(context, "靓", Color.parseColor("#fe7a2a"), Color.parseColor("#ffffff")));
                    tvEnter.append(LightSpanString.getPrettyNumSpan(context, prettyNum, Color.parseColor("#fe7a2a"), Color.parseColor("#fe7a2a")));
                } else if ("C".equals(prettyNumColor)) {
                    tvEnter.append(LightSpanString.getPrettyNumBgSpan(context, "靓", Color.parseColor("#fe3c7c"), Color.parseColor("#ffffff")));
                    tvEnter.append(LightSpanString.getPrettyNumSpan(context, prettyNum, Color.parseColor("#fe3c7c"), Color.parseColor("#fe3c7c")));
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

    private void outAnim() {
//        ObjectAnimator translationX = new ObjectAnimator().ofFloat(llEnter, "translationX",
//                0, -UIUtil.dip2px(context, 255));
        ObjectAnimator translationX = new ObjectAnimator().ofFloat(llEnter, "translationX",
                screenWidthPixels / 2 - UIUtil.dip2px(context, 255) / 2, -UIUtil.dip2px(context, 255));
        translationX.setDuration(1000);
        translationX.addListener(new AnimatorListenerAdapter() {
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
        translationX.start();
    }

    public String getPrettyNumColor(List<WelcomeJson.UserBagItem> userBagList) {
        if (userBagList == null) {
            return "A";
        }
        String string = "A";
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
