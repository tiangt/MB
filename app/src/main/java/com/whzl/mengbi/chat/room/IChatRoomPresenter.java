package com.whzl.mengbi.chat.room;

import android.widget.LinearLayout;


/**
 * Created by qishui on 15/5/25.
 */
public interface IChatRoomPresenter {
    /**
     * 设置聊天连接
     */
    void setupConnection(String token);

    /**
     * 断开聊天服务器
     */
    void disconnectChat();

    /**
     * 发送消息
     * @param message
     */
    void sendMessage(String message);

    /**
     * 直播间销毁时的操作
     */
    void onChatRoomDestroy();

    /**
     * 点击返回按钮的操作
     */
    void onBackPressed();

    /**
     * 填充聊天对象菜单列表
     * @param chatToLayout
     */
    void fillChatToList(LinearLayout chatToLayout);

    /**
     * 返回聊天对象状态，公聊私聊和当前聊天对象ID
     * @return
     */
    //ChatToStatus getChatToStatus();

    /**
     * 查询自己是否能私聊
     * @return 是否能私聊
     */
    //boolean canIprivateChat(EnterUserInfo userInfo);

    void sendBroadCast(String content);

}
