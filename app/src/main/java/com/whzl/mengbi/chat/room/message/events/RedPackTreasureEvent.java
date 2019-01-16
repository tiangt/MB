package com.whzl.mengbi.chat.room.message.events;

import com.whzl.mengbi.chat.room.message.messageJson.RedPackJson;

/**
 * @author nobody
 * @date 2019/1/16
 */
public class RedPackTreasureEvent {

    public RedPackJson treasureNum;

    public RedPackTreasureEvent(RedPackJson programTreasureNum) {
        treasureNum = programTreasureNum;
    }
}
