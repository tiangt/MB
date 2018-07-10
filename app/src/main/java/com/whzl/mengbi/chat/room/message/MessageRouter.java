package com.whzl.mengbi.chat.room.message;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.whzl.mengbi.chat.ProtoStringAvg;
import com.whzl.mengbi.chat.client.MessageCallback;
import com.whzl.mengbi.chat.room.message.messageJson.ChatRoomEventJson;
import com.whzl.mengbi.chat.room.message.messagesActions.Actions;
import com.whzl.mengbi.chat.room.message.messagesActions.AnimAction;
import com.whzl.mengbi.chat.room.message.messagesActions.ChatAction;
import com.whzl.mengbi.chat.room.message.messagesActions.GiftAction;
import com.whzl.mengbi.chat.room.message.messagesActions.LuckGiftAction;
import com.whzl.mengbi.chat.room.message.messagesActions.NoChatAction;
import com.whzl.mengbi.chat.room.message.messagesActions.PrivateChatAction;
import com.whzl.mengbi.chat.room.message.messagesActions.StartPlayAction;
import com.whzl.mengbi.chat.room.message.messagesActions.StopPlayAction;
import com.whzl.mengbi.chat.room.message.messagesActions.SubProgramAction;
import com.whzl.mengbi.chat.room.message.messagesActions.WelComeAction;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class MessageRouter implements MessageCallback {
    String tag = "parser";
    private Gson mGson;
    private Context mContext;
    Map<String, Actions> actionsMap = new HashMap<>();
    private ChatAction chatAction;
    private PrivateChatAction privateChatAction;
    private NoChatAction noChatAction;

    public MessageRouter(Gson gson, Context context) {
        this.mGson = gson;
        this.mContext = context;
        //initActionMap();
        initChatAction();
        EventBus.getDefault().register(context);
    }

    public void unregister() {
        EventBus.getDefault().unregister(mContext);
    }

    private void initActionMap() {
        actionsMap.put("start_live", new StartPlayAction());
        actionsMap.put("stop_live", new StopPlayAction());
        actionsMap.put("WELCOME", new WelComeAction());
        actionsMap.put("SEND_GIFT", new GiftAction());
        actionsMap.put("LUCK_GIFT", new LuckGiftAction());
        //actionsMap.put("BROADCAST", new BroadcastAction());
        actionsMap.put("SubscribeProgram", new SubProgramAction());
        actionsMap.put("ANIMATION", new AnimAction());
    }

    private void initChatAction() {
        chatAction = new ChatAction();
        privateChatAction = new PrivateChatAction();
        noChatAction = new NoChatAction();
    }

    @Override
    public void onGetMessage(byte[] messageBytes, short msgId) {
        ProtoStringAvg.strAvg message = unPack(messageBytes);
        if (message == null) {
            Log.e(tag, "Wire can not unpack!!!!!!");
            return;
        }

        switch (msgId) {
            case 1102:
                parseChatMsg(message);
                break;
            case 1107: //禁言消息
                parseNoChatMsg(message);
                break;
            default:
                Log.e(tag, "unknown message!!!!!!");
                break;
        }
    }

    private ProtoStringAvg.strAvg unPack(byte[] messageBytes) {
        ProtoStringAvg.strAvg message = null;
        try {
            message = ProtoStringAvg.strAvg.parseFrom(messageBytes);
            return message;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    private String getStrAvgString(ProtoStringAvg.strAvg message, int index) {
        String str = null;
        try {
            str = new String (message.getStrs(index).toByteArray(),"UTF-8") ;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    private void parseChatMsg(ProtoStringAvg.strAvg  message) {

        String type = getStrAvgString(message, 2);
        String msgInfo = getStrAvgString(message, 3);

        if (type == null || msgInfo == null) {
            return;
        } else if (type.equals("common")) {
            chatAction.performAction(msgInfo, mContext);
        } else if (type.equals("private")) {
            privateChatAction.performAction(msgInfo, mContext);
        } else if (type.equals("localroom") || type.equals("broadcast")) {
            ChatRoomEventJson eventJson = GsonUtils.GsonToBean(msgInfo, ChatRoomEventJson.class);
            if (null == eventJson) {
                return;
            }
            if (eventJson.getMsgType() != null) {
                handleMsgTypeAction(eventJson.getMsgType(), msgInfo);
            }else if (eventJson.getEventCode() != null) {
                handleEventAction(eventJson.getEventCode(), msgInfo);
            }
        }
    }

    private void handleEventAction(String eventCode, String msgInfo) {
        Actions action = actionsMap.get(eventCode);
        if (null == action) {
            Log.e("chatMsg", "eventCode has no action");
            return;
        }
        action.performAction(msgInfo, mContext);
    }

    private void handleMsgTypeAction(String msgType, String msgInfo) {
        Actions action = actionsMap.get(msgType);
        if (null == action) {
            Log.e("chatMsg", "msgType has no action");
            return;
        }
        action.performAction(msgInfo, mContext);
    }

    private void parseNoChatMsg(ProtoStringAvg.strAvg  message) {
        if (message.getStrsList().size() < 8) {
            return;
        }
        String fromUidStr = getStrAvgString(message, 2);
        String nickName = getStrAvgString(message, 3);
        String toUidStr = getStrAvgString(message, 4);
        String toNickName = getStrAvgString(message, 5);
        String typeStr = getStrAvgString(message, 6);
        String periodStr = getStrAvgString(message, 7);
        int type = -1;
        int fromUid = -1;
        int toUid = -1;
        int iPeriod = 0;

        try {
            fromUid = Integer.valueOf(fromUidStr);
            toUid = Integer.valueOf(toUidStr);
            type = Integer.valueOf(typeStr);
            iPeriod = Integer.valueOf(periodStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }


    }
}