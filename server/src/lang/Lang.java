package lang;
// TODO
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Lang {

    static String START;

    public static void load() {
        Properties lang = new Properties();
        try (InputStream is = new FileInputStream(new File("ru_RU.lang"))) {
            lang.load(is);
            is.close();
            System.out.println("[INFO] Язык загружен");
        } catch (Exception e) {
            System.out.println("[ERROR] Ошибка загрузки!");
        }
        START = lang.getProperty("SERVER_START", "Starting chat server version ");
        // PORT = Integer.parseInt(properties.getProperty("PORT", "8189"));
    }
}
