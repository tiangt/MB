package com.whzl.mengbi.chat.client;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.List;


public class  MbChatClient {
    private ISocket socket;
    private IConnectCallback mConnectCallback;
    private MessageCallback mMessageCallback;
    private ErrorCallback errorCallback;
    private Thread listenThread;
    String tag = "androidclient";
    private String currentDomain;
    private MbSocketFactory socketFactory;

    private InputStream in;
    private OutputStream out;

    private volatile boolean isStoped;

    private List<ServerAddr> serverList;

    public MbChatClient(MbSocketFactory socketFactory){
        this.socketFactory = socketFactory;
    }

    public void setConnectCallback(IConnectCallback connectCallback){
        this.mConnectCallback = connectCallback;
    }

    public void setmMessageCallback(MessageCallback mMessageCallback) {
        this.mMessageCallback = mMessageCallback;
    }

    public void setErrorCallback(ErrorCallback errorCallback) {
        this.errorCallback = errorCallback;
    }

    public String getCurrentDomain() {
        return currentDomain;
    }

    public void connectWithServerList(final List<ServerAddr> serverList){
        if(serverList == null || serverList.size() ==0){
            Log.e("server","server list is null");
            return;
        }
        this.serverList = serverList;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(ServerAddr addr:serverList) {
                    socket = socketFactory.getSocket();
                    try {
                        socket.connectSocket(addr.getAddr(),addr.getPort());
                        Log.i(tag,"connectSocket");
                    } catch (IOException e) {
                        e.printStackTrace();
                        continue;
                    }

                    if(socket.getIsConnected()){
                        try {
                            in = socket.getSocketInputStream();
                            Log.i(tag,"getSocketInputStream");
                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                socket.shutdownSocket();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                                continue;
                            }
                            continue;
                        }

                        try {
                            out = socket.getSocketOutputStream();
                            Log.i(tag,"getSocketOutputStream");
                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                socket.shutdownSocket();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                                continue;
                            }
                            continue;
                        }

                        if(in != null && out!=null){
                            currentDomain = addr.getAddr();
                            startListen();
                            mConnectCallback.onConnectSuccess(currentDomain);
                            break;
                        }
                    }

                    mConnectCallback.onConnectFailed();
                }
            }
        }).start();
    }

    public void send(ByteableMessage msg){
        if(!socket.getIsConnected()){
            Log.e(tag,"server not connected");
            return;
        }
        try {
            out.write(msg.getMessageBytes());
        } catch (IOException e) {
            errorCallback.onError();
            e.printStackTrace();
            //todo 是否需要重发
        }
    }

    public void closeSocket(){
        isStoped = true;
        if(socket == null){
            return;
        }
        try {
            socket.shutdownSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitFinish(){
        if(listenThread == null){
            return;
        }
        try {
            listenThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startListen(){
        listenThread =  new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(tag,"开始监听网络消息");
                int maxBodyLen = 100000;
                while (true){
                    byte[] headBuff = new byte[8];
                    PackageHeader headPack = readHeader(headBuff);
                    if(headPack != null){
                        int bodyLen = headPack.getBodyLen();
                        if(bodyLen > maxBodyLen){
                            Log.e(tag,"body too big");
                            break;
                        }
                        byte[] bodyBuff = new byte[bodyLen];
                        if(readExactLength(in,bodyBuff)){
                            mMessageCallback.onGetMessage(bodyBuff,headPack.getMsgId());
                        }else{
                            break;
                        }
                    }else{
                        break;
                    }
                }
            }
        });
        listenThread.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listenThread.join();
                    Log.i(tag,"网络监听线程返回");
                    if(!isStoped){
                        connectWithServerList(serverList);
                        Log.e(tag,"重新连接聊天服务器");
                    }
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private PackageHeader readHeader(byte[] headerBuffer){
        if(!readExactLength(in, headerBuffer)){
            return null;
        }
        ByteBuffer headByteBuffer = ByteBuffer.wrap(headerBuffer);
        short msgVer = headByteBuffer.getShort();
        short msgId = headByteBuffer.getShort();
        int bodyLen = headByteBuffer.getInt();

        return new PackageHeader(msgVer,msgId,bodyLen);
    }

    private boolean readExactLength(InputStream inputStream, byte[] buffer){
        int desireLen = buffer.length;
        int redLenSum = 0;
        if(desireLen <= 0){
            return false;
        }

        while (desireLen != redLenSum){
            int readLen = 0;
            try {
                readLen = inputStream.read(buffer,redLenSum,desireLen - redLenSum);
                if(readLen == -1 || readLen == 0){
                    return false;
                }
            } catch (IOException e) {
                errorCallback.onError();
                try {
                    socket.shutdownSocket();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return false;
            }
            redLenSum += readLen;
        }

        if(desireLen == redLenSum){
            return true;
        }else{
            return false;
        }
    }

    class PackageHeader{
        short msgVersion;
        short msgId;
        int bodyLen;

        public PackageHeader(short msgVersion,short msgId,int bodyLen) {
            this.msgVersion = msgVersion;
            this.msgId = msgId;
            this.bodyLen = bodyLen;
        }

        public short getMsgVersion() {
            return msgVersion;
        }

        public short getMsgId() {
            return msgId;
        }

        public int getBodyLen() {
            return bodyLen;
        }
    }
}
