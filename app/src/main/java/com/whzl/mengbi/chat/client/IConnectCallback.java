package com.whzl.mengbi.chat.client;


public interface IConnectCallback {
    void onConnectSuccess(String domain, boolean isReconnect);
    void onConnectFailed();
}
