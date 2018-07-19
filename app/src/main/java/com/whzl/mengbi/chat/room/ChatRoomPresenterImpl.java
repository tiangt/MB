package com.whzl.mengbi.chat.room;

import android.content.Context;
import android.util.Log;

import com.whzl.mengbi.chat.client.ErrorCallback;
import com.whzl.mengbi.chat.client.IConnectCallback;
import com.whzl.mengbi.chat.client.MbChatClient;
import com.whzl.mengbi.chat.client.MbSocketFactory;
import com.whzl.mengbi.chat.client.MessageCallback;
import com.whzl.mengbi.chat.client.ServerAddr;
import com.whzl.mengbi.chat.room.message.ChatOutMsg;
import com.whzl.mengbi.chat.room.message.LoginMessage;
import com.whzl.mengbi.chat.room.message.MessageRouter;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.SPUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class ChatRoomPresenterImpl{
    String TAG = "chatroom presenter";
    private MbChatClient client;
    private IConnectCallback connectCallback;
    private ErrorCallback errorCallback;
    private MessageCallback messageCallback;
    private LiveRoomTokenInfo liveRoomTokenInfo;
    private String programId;

    public ChatRoomPresenterImpl(String programId) {
        this.programId = programId;
    }

    public void setupConnection(LiveRoomTokenInfo liveRoomTokenInfo, Context context) {
        this.liveRoomTokenInfo = liveRoomTokenInfo;
        MbSocketFactory socketFactory = new MbSocketFactory();
        connectCallback = new IConnectCallback() {
            @Override
            public void onConnectSuccess(String domain, boolean isReconnect) {
                Log.d(TAG, "连接成功");
                if(domain!=null){
                    chatLogin(domain);
                    Log.d(TAG,"chatLogin finished");
                }
                //TODO: if isReconnect == true, get token info
            }

            @Override
            public void onConnectFailed() {
                Log.d(TAG, "连接失败");
                //TODO: toast
                //ToastUtils.showToast("网络连接异常，请检查网络");
            }
        };

        errorCallback = new ErrorCallback() {
            @Override
            public void onError() {
                Log.d(TAG, "网络出现问题");
            }
        };

        messageCallback = new MessageRouter(context);

        client = new MbChatClient(socketFactory);
        client.setErrorCallback(errorCallback);
        client.setConnectCallback(connectCallback);
        client.setmMessageCallback(messageCallback);

        client.connectWithServerList(getServerList());
    }

    /**
     * 登录聊天服务器
     *
     * @param domain
     */
    public void chatLogin(String domain) {
        LoginMessage message;
        int userId = Integer.parseInt(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0).toString());
        if(userId > 0) {
            message = new LoginMessage(programId, domain, userId, liveRoomTokenInfo.getData().getToken());
        }else{
            String nickname = SPUtils.get(BaseApplication.getInstance(),SpConfig.KEY_USER_NAME,"0").toString();
            if (nickname.equals("0")) {
                nickname = getNickname();
                SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_NAME, nickname);
            }
            message = new LoginMessage(programId,domain,userId,nickname);
        }
        client.send(message);
    }

    private List<ServerAddr> getServerList() {
        List<ServerAddr> list = new ArrayList<>();
        if (liveRoomTokenInfo.getData().getRoomServerList() == null) {
            Log.e(TAG, "LiveRoomTokenInfo getData getRoomServerList=null");
        }else {
            Log.e(TAG, "LiveRoomTokenInfo getData getRoomServerList!=null");
            for (LiveRoomTokenInfo.DataBean.RoomServerListBean lrtb: liveRoomTokenInfo.getData().getRoomServerList()) {
                ServerAddr addr = new ServerAddr(lrtb.getServerDomain(),lrtb.getDataPort());
                list.add(addr);
            }
        }
        return list;
    }

    public void sendMessage(String message) {
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
        String userId = SPUtils.get(BaseApplication.getInstance(),SpConfig.KEY_USER_ID,0).toString();
        String nickname = SPUtils.get(BaseApplication.getInstance(),SpConfig.KEY_USER_NAME,"0").toString();
        msg = new ChatOutMsg(message, programId,client.getCurrentDomain(),userId,nickname
                ,"0","0","common");
        client.send(msg);
    }

    public void onChatRoomDestroy() {
        client.closeSocket();
        messageCallback.unregister();
    }

    public void sendBroadCast(String content) {
        Log.e(TAG, "sendBroadCast 发送广播:" + content);
        //iChatRoom.sendBroadCast(content);
    }

    public void onBackPressed() {
    }

    private String getNickname() {
        Random random = new Random(new Date().getTime() / 1000);
        return "中国萌友" + (1000000 + random.nextInt(100000));
    }

}
