package ru.nerdyfeed.chat.server;

import java.io.*;
import java.util.Date;
import java.util.Properties;

class ServerProperties {
    static int PORT;
    static boolean loaded = false;

    ServerProperties() throws Exception {
        Properties p = new Properties();
        OutputStream os = new FileOutputStream("server.properties");
        InputStream is = new FileInputStream("server.properties");
            p.setProperty("PORT", "8189");
            p.setProperty("IP", "localhost");
            p.store(os, null);
    }

    static void load() {
        Properties properties = new Properties();
        Date date = new Date();
        try (InputStream is = new FileInputStream(new File("server.properties"))) {
            properties.load(is);
            properties.setProperty("PORT", "8189");
            is.close();
            System.out.println(date + " [INFO] Конфигурация загружена");
            loaded = true;
        } catch (Exception e) {
            System.out.println("[ERROR] Ошибка загрузки!");
        }
        PORT = Integer.parseInt((properties.getProperty("PORT", "8189")));
    }
}
