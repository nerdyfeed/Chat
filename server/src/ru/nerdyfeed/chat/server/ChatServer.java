package ru.nerdyfeed.chat.server;

import ru.nerdyfeed.chat.network.TCPConnection;
import ru.nerdyfeed.chat.network.TCPConnectionListener;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConnectionListener {

    private static EULA n;

    public static void main(String[] args) {
        new ChatServer();

    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private ChatServer() {
        System.out.println("Starting chat server version 0.2");
        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
            System.out.println("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar server.jar\"");
        }
        n = new EULA(new File("eula.txt"));
        if (!n.a()) {
            System.out.println("Необходимо принять соглашение EULA. Откройте файл eula.txt для получения информации.");
            n.b();
        } else {
            System.out.println("Server running!");
            try (ServerSocket serverSocket = new ServerSocket(8189);){
                while (true) {
                    try {
                        new TCPConnection(this, serverSocket.accept());
                    }catch (IOException e) {
                        System.out.println("TCPConnection exception: " + e);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        //sendToAllConnections("Client connected: " + tcpConnection);
        System.out.println("Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        sendToAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        //sendToAllConnections("Client disconnected: " + tcpConnection);
        System.out.println("Client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onExсeption(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void sendToAllConnections(String value) {
        System.out.println(value);
        final int cnt = connections.size();
        for (int i = 0; i < connections.size(); i++) { connections.get(i).sendString(value); }
    }
}
