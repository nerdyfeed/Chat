package ru.nerdyfeed.chat.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;


class EULA {

    private final File b;
    private final boolean c;

    EULA(File file1) {
        this.b = file1;
        this.c = this.a(file1);
    }

    private boolean a(File file1) {
        Date date = new Date();
        FileInputStream fileinputstream;
        boolean flag = false;

        try {
            Properties properties = new Properties();

            fileinputstream = new FileInputStream(file1);
            properties.load(fileinputstream);
            flag = Boolean.parseBoolean(properties.getProperty("eula", "false"));
        } catch (IOException e) {
            System.out.println("Невозможно сохранить файл " + file1);
            this.b();
        } finally {
            System.out.println(date + " [INFO] Проверка файла EULA...");
        }

        return flag;
    }

    boolean a() {
        return this.c;
    }

    void b() {
        FileOutputStream fileoutputstream;

        try {
            Properties properties = new Properties();

            fileoutputstream = new FileOutputStream(this.b);
            properties.setProperty("eula", "false");
            properties.store(fileoutputstream, "By changing the setting below to TRUE you are indicating your agreement to our EULA (site.ru).");
        } catch (IOException e) {
            System.out.println("Невозможно сохранить файл " + e);
        } finally {
            System.out.println("Соглашение не принято!");
        }
    }
}