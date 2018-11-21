package ru.nerdyfeed.chat.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

class ServerProperties {
    static int PORT;
    static boolean loaded = false;

    static void load() {
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(new File("./server.properties"))) {
            properties.load(is);
            is.close();
            System.out.println("[INFO] Конф загружен");
            loaded = true;
        } catch (Exception e) {
            System.out.println("[ERROR] Ошибка загрузки!");
        }
        PORT = Integer.parseInt((properties.getProperty("PORT", "8189")));
    }
}
