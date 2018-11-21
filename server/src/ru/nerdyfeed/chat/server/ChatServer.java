package ru.nerdyfeed.chat.server;

import ru.nerdyfeed.chat.network.TCPConnection;
import ru.nerdyfeed.chat.network.TCPConnectionListener;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Date;

public class ChatServer implements TCPConnectionListener {
    private Date date = new Date();
    private String INFO;

    {
        INFO = date + " [INFO] ";
    }

    public static void main(String[] args) {
        new ChatServer();

    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private ChatServer() {
        System.out.println(INFO + "Запуск сервера RChat версии " + getVersion());
        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
            System.out.println("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar server.jar\"");
        }
        EULA n = new EULA(new File("eula.txt"));
        if (!n.a()) {
            System.out.println("Необходимо принять соглашение EULA. Откройте файл eula.txt для получения информации.");
            n.b();
        } else {
            ServerProperties.load();
            if (ServerProperties.loaded) {
                System.out.println(INFO + "Сервер запущен!");
                try (ServerSocket serverSocket = new ServerSocket(ServerProperties.PORT)){
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
    }

    private String getVersion() {
        return "0.3";
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
        switch(value) {
            case "admin: stop":
                sendToAllConnections("Server was stopped by Administrator");
                System.exit(0);
                break;
            case "admin: reload":
                //TODO
                break;
            case "admin: bd":
                //TODO sendToAllConnections("[ВНИМАНИЕ!] " + value);
                break;
        }
        /*if (value.equals("admin: stop")) {
            sendToAllConnections("Server was stopped by admin");
            System.exit(0);
        }*/
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
        for (TCPConnection connection : connections) {
            connection.sendString(value);
        }
    }
}
