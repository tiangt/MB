package com.whzl.mengbi.chat.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public interface ISocket {
    void connectSocket(String url, int port) throws IOException;
    void shutdownSocket() throws IOException;
    InputStream getSocketInputStream() throws IOException;
    OutputStream getSocketOutputStream() throws IOException;
    boolean getIsConnected();
    void closeInputStream() throws IOException;
    void closeOutputStream() throws IOException;
}
