package com.whzl.mengbi.chat.room;

import android.app.Activity;
import android.os.Handler;

import java.util.List;

public interface IChatRoom {
    /**
     * 获取当前activity对应的presenter
     * @return 直播间对应的presenter
     */
    IChatRoomPresenter getPresenter();

    /**
     * 返回当前activity
     * @return 当前activity
     */
    Activity getContext();
    void closeKeyboard();
    void popKeyboard();
    void popBroadcastKeyboard();
    void goneDummyView();
    void popUpSelectTo();
    void setChatToNickName(String chatTo);
    void dissMissSelectToPop();

    void setToPubTab();
    void setToPriTab();

    //ChatInputView getChatInputView();
    void setShowConnectingPic(boolean isShow);
    void setShowNoShowPic(boolean isShow);

    void startPlayVideo(List<String> rtmpSource);
    void stopVideo();
    Handler mGetMainHandler();

    void setRenQi(String renqiStr);
    void showBottomMsg(String nickName, String content);
   //PubChatView getPubChatView();
    //PriChatView getPriChatView();
    void clearBottomMsg();

    void showGiftView(int targetId, String targetName);
    boolean hideGiftView();
    void getUserCombineGift();
    //void receiveStarGiftEvent(StarGiftEvent event);
    // receiveTexturePackerEvent(TexturePackerEvent event);
    //void receiveNormalGiftEvent(NormalGiftEvent event);

    int getCurrentTabIndex();
    void updateAudience();
    void showBroadcastMsg(String nickName, String content, int programId);
    void showTopRunway();
    void hideTopRunway();
    void chatToUser(String nickName, int uid);
    void privateChatToUser(String nickName, int uid);
    void updateChatToList();
    void getPubMsg();
    void getPriMsg();
    void fullSreen();
    void backSmallScreen();
    boolean isInFullScreen();
    void showWarning(String message);

    void sendBroadCast(String content);
    //void receiveGiftRunwayEvent(GiftRunwayEvent event);
}
