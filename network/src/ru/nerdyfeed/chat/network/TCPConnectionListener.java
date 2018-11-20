package ru.nerdyfeed.chat.network;

public interface TCPConnectionListener {

    void onConnectionReady(TCPConnection tcpConnection);
    void onReceiveString(TCPConnection tcpConnection, String value);
    void onDisconnect(TCPConnection tcpConnection);
    void onExсeption(TCPConnection tcpConnection, Exception e);
}
