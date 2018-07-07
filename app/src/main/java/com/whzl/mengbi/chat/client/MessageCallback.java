package com.whzl.mengbi.chat.client;


public interface MessageCallback {
    void onGetMessage(byte[] messageBytes, short msgID);
    void unregister();
}
