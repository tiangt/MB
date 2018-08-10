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
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class ChatRoomPresenterImpl {
    String TAG = "chatroom presenter";
    private MbChatClient client;
    private IConnectCallback connectCallback;
    private ErrorCallback errorCallback;
    private MessageCallback messageCallback;
    private LiveRoomTokenInfo liveRoomTokenInfo;
    private String programId;
    private boolean netErrorNoticed = false;
    private List<String> sendFailMsg = new ArrayList<>();

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
                long userId = Long.parseLong(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, (long) 0).toString());
                if (userId == 0 || !isReconnect) {
                    chatLogin(domain);
                } else {
                    doLiveRoomToken(userId + "", domain);
                }
                Log.d(TAG, "chatLogin finished");
                netErrorNoticed = false;
            }

            @Override
            public void onConnectFailed() {
                Log.e(TAG, "连接失败");
                if (netErrorNoticed) {
                    return;
                }
                //TODO: toast
                updateUI(context, "网络连接异常,请检查网络!");
                netErrorNoticed = true;
                //ToastUtils.showToast("网络连接异常，请检查网络");
            }
        };

        errorCallback = new ErrorCallback() {
            @Override
            public void onError() {
                Log.d(TAG, "网络出现问题");
            }
        };
        if (messageCallback != null) {
            messageCallback.unregister();
        }
        messageCallback = new MessageRouter(context);
        if (client != null) {
            client.closeSocket();
        }
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
        long userId = Long.parseLong(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, (long) 0).toString());
        if (userId > 0) {
            message = new LoginMessage(programId, domain, userId, liveRoomTokenInfo.getData().getToken());
        } else {
            String nickname = SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_NAME, "0").toString();
            if (nickname.equals("0")) {
                nickname = getNickname();
                SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_NAME, nickname);
            }
            message = new LoginMessage(programId, domain, userId, nickname);
        }
        client.send(message);
    }

    private List<ServerAddr> getServerList() {
        List<ServerAddr> list = new ArrayList<>();
        if (liveRoomTokenInfo.getData().getRoomServerList() == null) {
            Log.e(TAG, "LiveRoomTokenInfo getData getRoomServerList=null");
        } else {
            Log.e(TAG, "LiveRoomTokenInfo getData getRoomServerList!=null");
            for (LiveRoomTokenInfo.DataBean.RoomServerListBean lrtb : liveRoomTokenInfo.getData().getRoomServerList()) {
                ServerAddr addr = new ServerAddr(lrtb.getServerDomain(), lrtb.getDataPort());
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
        if (client == null) {
            sendFailMsg.add(message);
            if (sendFailMsg.size() >= 5) {
                sendFailMsg.remove(0);
            }
            return;
        }
        ChatOutMsg msg;
        String userId = SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, (long) 0).toString();
        String nickname = SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_NAME, "0").toString();
        msg = new ChatOutMsg(message, programId, client.getCurrentDomain(), userId, nickname
                , "0", "0", "common");//private
        client.send(msg);
    }

    public void sendPrivateMessage(RoomUserInfo.DataBean roomUser, String message) {
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
        if (client == null) {
            sendFailMsg.add(message);
            if (sendFailMsg.size() >= 5) {
                sendFailMsg.remove(0);
            }
            return;
        }
        ChatOutMsg msg;
        String userId = SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, (long) 0).toString();
        String nickname = SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_NAME, "0").toString();
        msg = new ChatOutMsg(message, programId, client.getCurrentDomain(), userId, nickname
                , roomUser.getUserId() + "", roomUser.getNickname(), "private");//
        client.send(msg);
    }

    public void onChatRoomDestroy() {
        if (null != client) {
            client.closeSocket();
        }
        if (null != messageCallback) {
            messageCallback.unregister();
            messageCallback = null;
        }

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

    private void doLiveRoomToken(String userId, String domain) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userId", userId);
        hashMap.put("programId", programId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.CHECK_ENTERR_ROOM, RequestManager.TYPE_POST_JSON, hashMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                LiveRoomTokenInfo tokenInfo = GsonUtils.GsonToBean(result.toString(), LiveRoomTokenInfo.class);
                if (tokenInfo.getCode() == 200) {
                    liveRoomTokenInfo = tokenInfo;
                    chatLogin(domain);
                } else {
                    chatLogin(domain);
                    Log.e(TAG, "getLiveRoomToken error,code=" + tokenInfo.getCode());
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                chatLogin(domain);
                Log.e(TAG, "getLiveRoomToken error,err=" + errorMsg);
            }
        });
    }

    private void updateUI(final Context context, String msg) {
        LiveDisplayActivity activity = (LiveDisplayActivity) context;
        if (null == activity) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToast(msg);
            }
        });
    }

    private void sendFailedMsg() {
        for (String msg : sendFailMsg) {
            sendMessage(msg);
        }
        sendFailMsg.clear();
    }
}
