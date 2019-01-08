package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;
import android.graphics.Color;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.BetsEndJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.widget.view.WeekStarView;

/**
 * @author nobody
 * @date 2018/12/6
 */
public class BetsEndEvent implements WeekHeadEvent {
    public Context context;
    public BetsEndJson headLineJson;

    public BetsEndEvent(Context context, BetsEndJson headLineJson) {
        this.context = context;
        this.headLineJson = headLineJson;
    }

    @Override
    public void init(WeekStarView tvEnter) {
        if (headLineJson != null) {
            BetsEndJson.ContextBean bean = headLineJson.context;
            tvEnter.setText(LevelUtil.getImageResourceSpanByHeight(this.context, R.drawable.ic_happy_ball_live, 12));
            tvEnter.append(LightSpanString.getLightString(bean.period, Color.parseColor("#FFFFCB00")));
            tvEnter.append(LightSpanString.getLightString(" 期欢乐球 ", Color.parseColor("#ffffff")));
            int ceil = (int) Math.ceil(bean.frozenCountDownSecond / 60);
            tvEnter.append(LightSpanString.getLightString(String.valueOf(ceil), Color.parseColor("#FFFFCB00")));
            tvEnter.append(LightSpanString.getLightString(" 分钟后开奖，百万大奖花落谁家，尽请期待！", Color.parseColor("#ffffff")));
        }
    }
}
