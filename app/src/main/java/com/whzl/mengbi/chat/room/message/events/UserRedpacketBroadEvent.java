package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.whzl.mengbi.chat.room.message.messageJson.UserRedpacketJson;
import com.whzl.mengbi.chat.room.util.LightSpanString;

/**
 * @author nobody
 * @date 2019-09-17
 */
public class UserRedpacketBroadEvent implements RedpacketTotalEvent {

    public final UserRedpacketJson json;
    public final Context context;

    public UserRedpacketBroadEvent(Context context, UserRedpacketJson userRedpacketJson) {
        this.context = context;
        this.json = userRedpacketJson;
    }

    @Override
    public void initTv(TextView textView) {
        textView.setText("");
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.append("土豪金主 ");
        textView.append(LightSpanString.getLightString(json.context.userInfo.nickname, Color.rgb(41, 178, 249)));
        textView.append(" 在 ");
        textView.append(LightSpanString.getLightString(json.context.anchorInfo.nickname, Color.rgb(255, 216, 3)));
        textView.append(" 直播间发起了 ");
        if (json.context.uGameRedpacketDto.awardType.equals("COIN")) {
            textView.append(LightSpanString.getLightString(json.context.uGameRedpacketDto.awardTotalPrice + "萌币", Color.rgb(191, 240, 61)));
        } else {
            textView.append(LightSpanString.getLightString(json.context.uGameRedpacketDto.awardPeopleNum + "个" +
                    json.context.uGameRedpacketDto.awardGoodsName, Color.rgb(191, 240, 61)));
            textView.append("（价值");
            textView.append(LightSpanString.getLightString(json.context.uGameRedpacketDto.awardTotalPrice + "", Color.rgb(191, 240, 61)));
            textView.append("萌币）");
        }
        textView.append("红包抽奖，赶紧围观~");

    }
}
