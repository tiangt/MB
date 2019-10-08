package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.UserRedpacketAwardJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;

/**
 * @author nobody
 * @date 2019-09-17
 */
public class UserRedpacketAwardEvent implements RedpacketTotalEvent {

    public final Context context;
    public final UserRedpacketAwardJson json;

    public UserRedpacketAwardEvent(Context context, UserRedpacketAwardJson userRedpacketAwardJson) {
        this.context = context;
        this.json = userRedpacketAwardJson;
    }

    @Override
    public void initTv(TextView textView) {
        textView.setText("");
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.append("恭喜 ");
        textView.append(LightSpanString.getLightString(json.context.userInfoDto.awardNickname, Color.rgb(41,178,249)));
        textView.append(" 抽中 ");
        textView.append(LightSpanString.getLightString(json.context.userInfoDto.nickname, Color.rgb(255,216,3)));
        textView.append(" 发起的");
        if (json.context.gameRedpacketAwardDto.awardType.equals("COIN")) {
            textView.append(LightSpanString.getLightString(json.context.gameRedpacketAwardDto.awardPrice + "萌币", Color.rgb(191,240,61)));
        } else {
            textView.append(LightSpanString.getLightString(json.context.gameRedpacketAwardDto.awardGoodsNum + "个" + json.context.uGameRedpacketDto.awardGoodsName
                    , Color.rgb(191,240,61)));
        }
        textView.append(" 红包（共 ");
        if (json.context.gameRedpacketAwardDto.awardType.equals("COIN")) {
            textView.append(LightSpanString.getLightString(json.context.uGameRedpacketDto.awardTotalPrice + "", Color.rgb(255, 255, 255)));
            textView.append(" 萌币)");
        } else {
            textView.append(LightSpanString.getLightString(json.context.uGameRedpacketDto.awardGoodsNum + ""
                    , Color.rgb(255, 255, 255)));
            textView.append(" 个)");
        }
    }
}
