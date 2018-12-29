package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.WeekStarJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.ui.widget.view.WeekStarView;

/**
 * @author nobody
 * @date 2018/12/6
 */
public class WeekStarEvent implements WeekHeadEvent {
    public Context context;
    public WeekStarJson weekStarJson;

    public WeekStarEvent(Context context, WeekStarJson weekStarJson) {
        this.context = context;
        this.weekStarJson = weekStarJson;
    }

    @Override
    public void init(WeekStarView tvEnter) {
        if (weekStarJson != null) {
            WeekStarJson.ContextBean bean = weekStarJson.context;
            tvEnter.setText(LevelUtil.getImageResourceSpanByHeight(this.context, R.drawable.ic_week_star_play, 12));
            tvEnter.append(" 恭喜 ");
            tvEnter.append(bean.nickName);
            tvEnter.append(" 在周星礼物 ");
            tvEnter.append(bean.giftName);
            tvEnter.append(" 争夺中，上升到第一名");
        }
    }
}
