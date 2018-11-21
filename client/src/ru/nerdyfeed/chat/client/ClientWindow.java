package ru.nerdyfeed.chat.client;

import ru.nerdyfeed.chat.network.TCPConnection;
import ru.nerdyfeed.chat.network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {

    private static final String IP_ADDR = "localhost"; // 54.93.87.62
    private static final int PORT = 8189;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }
    private final JTextArea textArea = new JTextArea();
    private final JScrollPane scrollPane = new JScrollPane(textArea);
    private String fieldNickname;
    private String isAdmin;
    private final JTextField fieldInput = new JTextField();

    private TCPConnection connection;

    // Create Window
    private ClientWindow() {
        if (fieldNickname == null) {
            fieldNickname = JOptionPane.showInputDialog(null, "Введите ник: ", "", JOptionPane.QUESTION_MESSAGE);
            while( fieldNickname.isEmpty()) {
                fieldNickname = "Username";
            }
        }

        setSize(WIDTH, HEIGHT);
        if (fieldNickname.equals("admin")) {
            isAdmin = "(as superuser)";
        } else
        {
            isAdmin = "";
        }
        setTitle("RChat" + " | " + fieldNickname + " " + isAdmin);
        setLocationRelativeTo(null);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        scrollPane.setBounds(5,5,300,200);
        fieldInput.addActionListener(this);
        add(scrollPane);
        add(fieldInput, BorderLayout.SOUTH);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
        } catch (IOException e) {
            printMsg("Connection exception" + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText();
        if (msg.equals("")) return;
        fieldInput.setText(null);
        connection.sendString(fieldNickname + ": " + msg);
    }


    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {

        printMsg("Подключились!");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMsg(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMsg("Connection closed");
    }

    @Override
    public void onExсeption(TCPConnection tcpConnection, Exception e) {
        printMsg("Connection exception" + e);
    }

    private synchronized void printMsg(String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                textArea.append(msg + "\n");
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
        });
    }
}
