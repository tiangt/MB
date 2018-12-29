package com.whzl.mengbi.gift;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.events.WeekHeadEvent;
import com.whzl.mengbi.chat.room.message.events.WeekStarEvent;
import com.whzl.mengbi.chat.room.message.messageJson.WeekStarJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.ui.widget.view.WeekStarView;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nobody
 * @date 2018/11/5
 */
public class WeekStarControl {
    RelativeLayout flEnter;
    WeekStarView tvEnter;
    ImageView ivEnter;
    Context context;

    public WeekStarControl(Context context) {
        this.context = context;
    }


    public void setIvEnter(ImageView ivEnter) {
        this.ivEnter = ivEnter;
    }


    public void setRlEnter(RelativeLayout llEnter) {
        this.flEnter = llEnter;
    }

    public void setTvEnter(WeekStarView tvEnter) {
        this.tvEnter = tvEnter;
    }

    private List<WeekHeadEvent> list = new ArrayList<>();

    private boolean isPlay = false;

    public void showEnter(WeekHeadEvent welcomeMsg) {
        list.add(welcomeMsg);
        if (!isPlay && list.size() != 0) {
            startAnimal();
        }
    }

    private void startAnimal() {
        isPlay = true;
        flEnter.setVisibility(View.VISIBLE);
        WeekHeadEvent weekHeadEvent = list.get(0);
        weekHeadEvent.init(tvEnter);
        tvEnter.init();
        GlideImageLoader.getInstace().loadOneTimeGif(context, R.drawable.bg_zhouxing, ivEnter, new GlideImageLoader.GifListener2() {
            @Override
            public void gifPlayComplete() {
                tvEnter.startScroll();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        outAnim();
                    }
                }, 5000);
            }
        });
    }

//    private void initTv(WeekStarEvent weekStarEvent) {
//        if (weekStarEvent.weekStarJson != null) {
//            WeekStarJson.ContextBean bean = weekStarEvent.weekStarJson.context;
//            tvEnter.setText(LevelUtil.getImageResourceSpanByHeight(this.context, R.drawable.ic_week_star_play, 12));
//            tvEnter.append(" 恭喜 ");
//            tvEnter.append(bean.nickName);
//            tvEnter.append(" 在周星礼物 ");
//            tvEnter.append(bean.giftName);
//            tvEnter.append(" 争夺中，上升到第一名");
//        }
//    }

    private void outAnim() {
        list.remove(0);
        tvEnter.stopScroll();
        tvEnter.setText("");
        if (list.size() > 0) {
            startAnimal();
        } else {
            isPlay = false;
            flEnter.setVisibility(View.GONE);
        }
    }

}
