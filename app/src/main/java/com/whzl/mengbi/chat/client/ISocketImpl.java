package com.whzl.mengbi.chat.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ISocketImpl extends Socket implements ISocket {

    @Override
    public void connectSocket(final String url, final int port) throws IOException {
        SocketAddress address = new InetSocketAddress(url,port);
        connect(address,10000);
    }

    @Override
    public void shutdownSocket() throws IOException {
        close();
    }

    @Override
    public InputStream getSocketInputStream() throws IOException {
        return getInputStream();
    }

    @Override
    public OutputStream getSocketOutputStream() throws IOException {
        return getOutputStream();
    }

    @Override
    public boolean getIsConnected() {
        return isConnected();
    }

    @Override
    public void closeInputStream() throws IOException {
        shutdownInput();
    }

    @Override
    public void closeOutputStream() throws IOException {
        shutdownOutput();
    }

}
