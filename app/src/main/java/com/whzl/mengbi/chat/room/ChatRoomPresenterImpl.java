package com.whzl.mengbi.chat.room;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.whzl.mengbi.R;
import com.whzl.mengbi.application.BaseAppliaction;

import com.whzl.mengbi.bean.VisitorUserBean;
import com.whzl.mengbi.chat.client.ErrorCallback;
import com.whzl.mengbi.chat.client.IConnectCallback;
import com.whzl.mengbi.chat.client.MbChatClient;
import com.whzl.mengbi.chat.client.MbSocketFactory;
import com.whzl.mengbi.chat.client.MessageCallback;
import com.whzl.mengbi.chat.client.ServerAddr;

import com.whzl.mengbi.chat.room.message.ChatOutMsg;
import com.whzl.mengbi.chat.room.message.LoginMessage;

import com.whzl.mengbi.chat.room.message.MessageRouter;
import com.whzl.mengbi.chat.room.responses.EnterUserInfo;
import com.whzl.mengbi.chat.room.responses.GuardInfo;
import com.whzl.mengbi.util.SPUtils;

import java.util.ArrayList;
import java.util.List;


public class ChatRoomPresenterImpl{
    String TAG = "chatroom presenter";
    private MbChatClient client;
    private IConnectCallback connectCallback;
    private ErrorCallback errorCallback;
    private MessageCallback messageCallback;
    //private IChatRoom iChatRoom;
    private Gson gson;
    //private ChatToStatus chatToStatus;
    private List<GuardInfo> guardInfoList;
    private boolean canIPriChat;

    //private String token = ChatRoomInfo.getCheckEnterData() == null ? "" : ChatRoomInfo.getCheckEnterData().getToken();

//    public ChatRoomPresenterImpl(IChatRoom iChatRoom) {
//        this.iChatRoom = iChatRoom;
//        gson = new MbJsonParserImpl();
//        chatToStatus = new ChatToStatus(iChatRoom);
//
//        EventBus.getDefault().register(this);
//    }

    public boolean canIprivateChat(EnterUserInfo userInfo) {
//        if (!EnvironmentBridge.getUserInfoBridge().isLogin()) {
//            return false;
//        }
//
//        if (EnvironmentBridge.getUserInfoBridge().isVip()) {
//            return true;
//        }

        if (userInfo.getData().getIsGuard() == 1) {
            return true;
        }

        for (EnterUserInfo.DataEntity.LevelListEntity item : userInfo.getData().getLevelList()) {
            if (item.getLevelType().equals("ROYAL_LEVEL")) {
                return true;
            }
        }

        for (EnterUserInfo.DataEntity.LevelListEntity item : userInfo.getData().getLevelList()) {
            if (item.getLevelType().equals("USER_LEVEL")) {
                int levelValue = item.getLevelValue();
                if (levelValue > 5) {
                    return true;
                }
            }
        }

//        if (AudienceView.identity == 40 || AudienceView.identity == 41) {
//            return true;
//        }
        return false;
    }

    public void setupConnection(int programId,String token,List<ServerAddr> serverAddrList) {
        MbSocketFactory socketFactory = new MbSocketFactory();
        connectCallback = new IConnectCallback() {
            @Override
            public void onConnectSuccess(String domain) {
                Log.d(TAG, "连接成功");
                if(domain!=null){
                    chatLogin(domain,programId,token);
                    Log.d(TAG,"chatLogin");
                }

            }

            @Override
            public void onConnectFailed() {
                Log.d(TAG, "连接失败");
            }
        };

        errorCallback = new ErrorCallback() {
            @Override
            public void onError() {
                Log.d(TAG, "网络出现问题");
            }
        };

        messageCallback = new MessageRouter(gson,BaseAppliaction.getInstance().getApplicationContext());

        client = new MbChatClient(socketFactory);
        client.setErrorCallback(errorCallback);
        client.setConnectCallback(connectCallback);
        client.setmMessageCallback(messageCallback);

        client.connectWithServerList(serverAddrList);
    }

    /**
     * 登录聊天服务器
     *
     * @param domain
     */
    public void chatLogin(String domain,int programId,String token) {
        LoginMessage message;
        message = new LoginMessage(programId+"",
                domain, BaseAppliaction.getInstance().getUserId(),token);
        client.send(message);
    }

    private List<ServerAddr> getServerList() {
        List<ServerAddr> list = new ArrayList<>();
//        if (ChatRoomInfo.getCheckEnterData() == null) {
//            Log.e(TAG, "CheckEnterRoom getServerList getCheckEnterData=null");
//        }else {
//            Log.e(TAG, "CheckEnterRoom getServerList getCheckEnterData!=null");
//            for (CheckEnterRoom.RoomServerListItem item : ChatRoomInfo.getCheckEnterData().getRoomServerList()) {
//                ServerAddr addr = new ServerAddr(item.getServerDomain(), item.getDataPort());
//                list.add(addr);
//            }
//        }
        return list;
    }


    public void disconnectChat() {
        client.closeSocket();
    }


    public void sendMessage(String message,int ProgramId ) {
//        if (!EnvironmentBridge.getUserInfoBridge().isLogin()) {
//            if (message.length() > 5) {
//                iChatRoom.showWarning("游客最多发言5个字");
//                return;
//            }
//        }
//
//        if (message.length() > 50) {
//            iChatRoom.showWarning("最多发言50个字");
//            return;
//        }
//
//        //如果私聊先判断有没有权限
//        if (chatToStatus.getChatType() == ChatToStatus.ChatType.PRIVATE_CHAT) {
//            if (!ChatRoomInfo.isCanIPri()) {//没有私聊权限的话
//                Toast.makeText((Context) iChatRoom, "财富LV5以上用户才能私聊哦，快快升级吧", Toast.LENGTH_LONG).show();
//                return;
//            }
//        }
//
        ChatOutMsg msg;
//        String toNickname = chatToStatus.getToUser().getNickName();
//        if (toNickname.equals("所有人")) {
//            toNickname = "";
//        }

//        int toId = chatToStatus.getToUser().getUid();
//        if (chatToStatus.getChatType() == ChatToStatus.ChatType.PRIVATE_CHAT) {
//            msg = new ChatOutMsg(message, ChatRoomInfo.getProgramId() + "",
//                    client.getCurrentDomain(), EnvironmentBridge.getUserInfoBridge().getUserId() + "",
//                    EnvironmentBridge.getUserInfoBridge().getNickName(), toId + "", toNickname, "private");
//        } else {
//            msg = new ChatOutMsg(message, ChatRoomInfo.getProgramId() + "",
//                    client.getCurrentDomain(), EnvironmentBridge.getUserInfoBridge().getUserId() + "",
//                    EnvironmentBridge.getUserInfoBridge().getNickName(), toId + "", toNickname, "common");
//        }

        msg = new ChatOutMsg(message,ProgramId+"",client.getCurrentDomain(),VisitorUserBean.getInstace().getData().getUserId()+"",
                VisitorUserBean.getInstace().getData().getNickname(),"0","0","common");
        client.send(msg);
    }


    public void onChatRoomDestroy() {

    }


    public void sendBroadCast(String content) {
        Log.e(TAG, "sendBroadCast 发送广播:" + content);
        //iChatRoom.sendBroadCast(content);
    }



    public void onBackPressed() {
    }


    public void fillChatToList(LinearLayout chatToLayout) {
//        chatToLayout.removeAllViews();
//        for (final ChatToUser user : chatToStatus.getUserList()) {
//            View chatToItem = LayoutInflater.from(iChatRoom.getContext()).
//                    inflate(R.layout.chattolist_item, chatToLayout, false);
//            TextView text = (TextView) chatToItem.findViewById(R.id.chattolist_text);
//            text.setText(user.getNickName());
//            chatToItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (user.getUid() == 0) {//所有人
//                        chatToStatus.setChatType(ChatToStatus.ChatType.PUBLIC_CHAT);
//                        iChatRoom.getChatInputView().changToPub();
//                    }
//
//                    iChatRoom.setChatToNickName(user.getNickName());
//                    chatToStatus.setToUser(user);
//                    iChatRoom.dissMissSelectToPop();
//                }
//            });
//            chatToLayout.addView(chatToItem);
//        }
    }

//    @Override
//    public ChatToStatus getChatToStatus() {
//        return chatToStatus;
//    }

    
}
