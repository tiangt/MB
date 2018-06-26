package com.whzl.mengbi.chat.client;


public class ServerAddr {
    private String addr;
    private int port;

    public ServerAddr(String addr, int port) {
        this.addr = addr;
        this.port = port;
    }

    public String getAddr() {
        return addr;
    }

    public int getPort() {
        return port;
    }
}
