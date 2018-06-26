package com.whzl.mengbi.chat.client;


public class MbSocketFactory {
    public ISocket getSocket(){
        return new ISocketImpl();
    }
}
