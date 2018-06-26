package com.whzl.mengbi.chat.room.message;

import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.whzl.mengbi.bean.message.ChatCommonMesBean;
import com.whzl.mengbi.bean.message.UserMesBean;
import com.whzl.mengbi.chat.ProtoStringAvg;
import com.whzl.mengbi.chat.client.MessageCallback;
import com.whzl.mengbi.eventbus.EventBusBean;
import com.whzl.mengbi.eventbus.EventBusUtils;
import com.whzl.mengbi.eventbus.EventCode;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by qishui on 15/5/20.
 */
public class MessageRouter implements MessageCallback {
    //private Wire wire;
    String tag = "parser";
    private Gson mGson;
    private Context mContext;
    //private SmileyParser smileyParser;
    Map<String, PlaybackStateCompat.Actions> actionsMap = new HashMap<>();
    //Map<Integer, OptAction> optActionMap = new HashMap<>();
    //ResourceMap resourceMap;
    //private ChatAction chatAction;
    //private PrivateChatAction privateChatAction;

    public MessageRouter(Gson gson, Context context) {
        this.mGson = gson;
        this.mContext = context;
        //todo 可能有问题
//        SmileyParser.init(context.getApplicationContext());
//        smileyParser = SmileyParser.getInstance();
//
//        resourceMap = new ResourceMap();

        //initActionMap();
        initOptMap();
        initChatAction();
    }

    private void initActionMap() {
//        actionsMap.put("start_live", new StartPlayAction());
//        actionsMap.put("stop_live", new StopPlayAction());
//        actionsMap.put("WELCOME", new WelComeAction());
//        actionsMap.put("SEND_GIFT", new GiftAction());
//        actionsMap.put("ANCHOR_EXP", new ExpAction());
//        actionsMap.put("LUCK_GIFT", new ZhongjiangAction());
//        actionsMap.put("BROADCAST", new BroadcastAction());
//        actionsMap.put("RUNWAY", new RunWayAction());
//        actionsMap.put("SET_MANAGER", new SetManagerAction());
//        actionsMap.put("ANIMATION", new AnimAction());
    }

    private void initOptMap() {

    }

    private void initChatAction() {

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
            case 1107:
                parseOptMsg(message);
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

            Log.i("Message", "===========");
//            for (ByteString byteString : message.) {
//                String str = new String(byteString.toByteArray(), "UTF-8");
//                Log.i("Message", str);
//            }
            Log.i("Message", "===========");

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
            System.out.println("common>>>>>"+msgInfo);
            ChatCommonMesBean chatMessageBean = GsonUtils.GsonToBean(msgInfo,ChatCommonMesBean.class);
            EventBusBean<ChatCommonMesBean> event = new EventBusBean<>(EventCode.CHAT_COMMON,chatMessageBean);
            EventBusUtils.sendEvent(event);
        } else if (type.equals("private")) {
            System.out.println("private>>>>>"+msgInfo);
        } else if (type.equals("localroom") || type.equals("broadcast")) {
            System.out.println("localroom>>>>>"+msgInfo);
            JSONObject jsonobj = JSON.parseObject(msgInfo);
            Object eventCode = jsonobj.get("eventCode");
            Object msgType = jsonobj.get("msgType");
            if(eventCode!=null){
                if(eventCode.equals("WELCOME")){
                    Object userId = jsonobj.getJSONObject("context").getJSONObject("info").get("userId");
                    if(userId.equals(0)){
                        Object nickname = jsonobj.getJSONObject("context").getJSONObject("info").get("nickname");
                        EventBusBean<Object> event = new EventBusBean<>(EventCode.TOURIST,nickname.toString());
                        EventBusUtils.sendEvent(event);
                    }else{
                        UserMesBean userMesBean = GsonUtils.GsonToBean(msgInfo,UserMesBean.class);
                        EventBusBean<UserMesBean> event = new EventBusBean<>(EventCode.LOGIN_USER,userMesBean);
                        EventBusUtils.sendEvent(event);
                    }
                }else if(eventCode.equals("SEND_SYSTEM_MESSAGE")){
//                    Object  sysmes =  jsonobj.getJSONObject("context").get(message);
//                    System.out.println(">>>>>>>>>>>>SEND_SYSTEM_MESSAGE"+sysmes);
//                    EventBusBean<Object> event = new EventBusBean<>(EventCode.SEND_SYSTEM_MESSAGE,sysmes);
//                    EventBusUtils.sendEvent(event);
                }else{

                }
            }else if (msgType!=null){
                if(msgType.equals("ANIMATION")){
                    System.out.println("ANIMATION>>>>>>>>>>>>"+msgInfo);
                }else{
                      
                }
            }
        }
    }

    private void parseOptMsg(ProtoStringAvg.strAvg  message) {
        String toUidStr = getStrAvgString(message, 4);
        String nickName = getStrAvgString(message, 3);
        String toNickName = getStrAvgString(message, 5);
        String typeStr = getStrAvgString(message, 6);
        int type = -1;
        int toUid = -1;

        try {
            toUid = Integer.valueOf(toUidStr);
            type = Integer.valueOf(typeStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        /*//OptAction actions = optActionMap.get(type);
        if (actions == null) {
            Log.e("test", "不支持的操作格式");
            return;
        }
        actions.performOpt(toUid, nickName, toNickName, mContext);*/
    }
}