package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;
import android.graphics.Color;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.HeadLineJson;
import com.whzl.mengbi.chat.room.message.messageJson.WeekStarJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.widget.view.WeekStarView;

/**
 * @author nobody
 * @date 2018/12/6
 */
public class HeadLineEvent implements WeekHeadEvent {
    public Context context;
    public HeadLineJson headLineJson;

    public HeadLineEvent(Context context, HeadLineJson headLineJson) {
        this.context = context;
        this.headLineJson = headLineJson;
    }

    @Override
    public void init(WeekStarView tvEnter) {
        if (headLineJson != null) {
            HeadLineJson.ContextBean bean = headLineJson.context;
            tvEnter.setText(LevelUtil.getImageResourceSpanByHeight(this.context, R.drawable.ic_live_headline, 12));
            tvEnter.append(" 恭喜 ");
            tvEnter.append(LightSpanString.getLightString(bean.nickname, Color.parseColor("#ffd800")));
            tvEnter.append(" 在头条争夺中勇夺第 ");
            tvEnter.append(LightSpanString.getLightString(bean.rank + " ", Color.parseColor("#ffd800")));
            tvEnter.append("名");
        }
    }
}
