package com.whzl.mengbi.util;

import com.whzl.mengbi.chat.room.message.messageJson.FromJson;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.dialog.PrivateChatListFragment;

import java.util.List;

/**
 * author: yaobo wu
 * date:   On 2018/7/19
 */
public class UserIdentity {
    public static final int ANCHOR = 10; //运管
    public static final int OPTR_MANAGER = 40; //运管
    public static final int ROOM_MANAGER = 41; //房管

    public static boolean getCanChatPaivate(RoomUserInfo.DataBean user) {
        if (user == null) {
            return false;
        }
        boolean canChatPrivate = false;
        int identityId = user.getIdentityId();
        if (identityId == UserIdentity.ANCHOR
                || identityId == UserIdentity.OPTR_MANAGER) {
            canChatPrivate = true;
        }
        List<RoomUserInfo.LevelMapBean> levelMap = user.getLevelList();
        for (int i = 0; i < levelMap.size(); i++) {
            if ("USER_LEVEL".equals(levelMap.get(i).getLevelType())) {
                int levelValue = levelMap.get(i).getLevelValue();
                if (levelValue > 5) {
                    canChatPrivate = true;
                }
            }
            if ("ROYAL_LEVEL".equals(levelMap.get(i).getLevelType())) {
                int levelValue = levelMap.get(i).getLevelValue();
                if (levelValue > 0) {
                    canChatPrivate = true;
                }
            }
        }

        if (user.getGoodsList() != null) {
            for (int i = 0; i < user.getGoodsList().size(); i++) {
                if ("GUARD".equals(user.getGoodsList().get(i).getGoodsType())) {
                    canChatPrivate = true;
                }
                if ("VIP".equals(user.getGoodsList().get(i).getGoodsType())) {
                    canChatPrivate = true;
                }
            }
        }
        return canChatPrivate;
    }
}
