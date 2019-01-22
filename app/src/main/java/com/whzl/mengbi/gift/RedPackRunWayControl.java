package com.whzl.mengbi.gift;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.whzl.mengbi.chat.room.message.events.RedPackTreasureEvent;
import com.whzl.mengbi.chat.room.message.messageJson.RedPackJson;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.config.AppConfig;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.UIUtil;

import java.util.ArrayList;

/**
 * @author nobody
 * @date 2019/1/16
 */
public class RedPackRunWayControl {

    private boolean isShow;
    private ArrayList<RedPackTreasureEvent> giftQueue = new ArrayList<>();

    private TextView tvRedPack;
    private final int screenWidthPixels;
    private ValueAnimator showAnim;
    private ValueAnimator hideAnim;
    private Context mContext;

    public RedPackRunWayControl(Context context, TextView textView) {
        mContext = context;
        tvRedPack = textView;
        screenWidthPixels = UIUtil.getScreenWidthPixels(BaseApplication.getInstance());
    }

    public void load(RedPackTreasureEvent redPackTreasureEvent) {
        if (redPackTreasureEvent == null || redPackTreasureEvent.treasureNum == null || redPackTreasureEvent.treasureNum.context == null) {
            return;
        }
        if (redPackTreasureEvent.treasureNum.context.busiCodeName.equals(AppConfig.OFFICIAL_SEND_REDPACKET) || redPackTreasureEvent
                .treasureNum.context.busiCodeName.equals(AppConfig.PROGRAM_TREASURE_SEND_REDPACKET) ||
                (redPackTreasureEvent.treasureNum.context.busiCodeName.equals(AppConfig.USER_SEND_REDPACKET) && redPackTreasureEvent.treasureNum
                        .context.messageSubType.equals("broadcast"))) {
            if (!isShow) {
                show(redPackTreasureEvent);
                return;
            }
            giftQueue.add(redPackTreasureEvent);
        }
    }

    private void show(RedPackTreasureEvent redPackTreasureEvent) {
        isShow = true;
        tvRedPack.setTranslationX(screenWidthPixels);
        tvRedPack.setText("");
        RedPackJson.ContextBean context = redPackTreasureEvent.treasureNum.context;
        switch (redPackTreasureEvent.treasureNum.context.busiCodeName) {
            case AppConfig.USER_SEND_REDPACKET:
                tvRedPack.append(LightSpanString.getLightString(context.sendObjectNickname.length() > 4 ?
                                context.sendObjectNickname.substring(0, 4) + "..." : context.sendObjectNickname,
                        Color.parseColor("#FFF8CF2C")));
                tvRedPack.append(LightSpanString.getLightString("在", Color.parseColor("#ffffff")));
                tvRedPack.append(LightSpanString.getLightString(context.founderUserNickname.length() > 4 ?
                                context.founderUserNickname.substring(0, 4) + "..." : context.founderUserNickname,
                        Color.parseColor("#FFF8CF2C")));
                tvRedPack.append(LightSpanString.getLightString("的直播间发了一个", Color.parseColor("#ffffff")));
                tvRedPack.append(LightSpanString.getLightString(context.redPacketType.equals("RANDOM") ? "手气红包," : "普通红包,", Color.parseColor("#FFF8CF2C")));
                tvRedPack.append(LightSpanString.getLightString(context.leftSeconds + "秒", Color.parseColor("#FFF8CF2C")));
                tvRedPack.append(LightSpanString.getLightString("后开抢,速度围观哦！", Color.parseColor("#ffffff")));
                break;
            case AppConfig.PROGRAM_TREASURE_SEND_REDPACKET:
                tvRedPack.append(LightSpanString.getLightString(context.sendObjectNickname, Color.parseColor("#FFFFE68E")));
                tvRedPack.append(LightSpanString.getLightString(" 发了一个", Color.parseColor("#ffffff")));
                tvRedPack.append(LightSpanString.getLightString("红包", Color.parseColor("#FFF8CF2C")));
                tvRedPack.append(LightSpanString.getLightString(context.leftSeconds + "秒 ", Color.parseColor("#FFF8CF2C")));
                tvRedPack.append(LightSpanString.getLightString("后开抢,速度围观哦！", Color.parseColor("#ffffff")));
                break;
            case AppConfig.OFFICIAL_SEND_REDPACKET:
                tvRedPack.append(LightSpanString.getLightString(" 萌比直播官方", Color.parseColor("#FF81ECFF")));
                tvRedPack.append(LightSpanString.getLightString(" 发了一个", Color.parseColor("#ffffff")));
                tvRedPack.append(LightSpanString.getLightString("红包", Color.parseColor("#FFF8CF2C")));
                tvRedPack.append(LightSpanString.getLightString(context.leftSeconds + "秒 ", Color.parseColor("#FFF8CF2C")));
                tvRedPack.append(LightSpanString.getLightString("后开抢,速度围观哦！", Color.parseColor("#ffffff")));
                break;
        }
        showTranslateAnim();
    }

    private void showTranslateAnim() {
        tvRedPack.setVisibility(View.VISIBLE);
        showAnim = ValueAnimator.ofFloat(screenWidthPixels, 0);
        showAnim.setInterpolator(new DecelerateInterpolator());
        showAnim.setDuration(1000);
        showAnim.addUpdateListener(animation -> {
            float animatedValue = ((float) animation.getAnimatedValue());
            tvRedPack.setTranslationX(animatedValue);
            if (animatedValue == 0) {
                hideTranslateAnim();
            }
        });
        showAnim.start();
    }

    private void hideTranslateAnim() {
        hideAnim = ValueAnimator.ofFloat(0, -screenWidthPixels);
        hideAnim.setInterpolator(new AccelerateInterpolator());
        hideAnim.setDuration(1000);
        hideAnim.addUpdateListener(animation -> {
            float animatedValue = ((float) animation.getAnimatedValue());
            tvRedPack.setTranslationX(animatedValue);
            if (animatedValue == -screenWidthPixels) {
                tvRedPack.setVisibility(View.GONE);
                if (giftQueue.size() > 0) {
                    show(giftQueue.get(0));
                    giftQueue.remove(0);
                } else {
                    isShow = false;
                }
            }
        });
        hideAnim.setStartDelay(3000);
        hideAnim.start();
    }

    public void destroy() {
        tvRedPack.setVisibility(View.GONE);
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
        giftQueue.clear();
    }
}
