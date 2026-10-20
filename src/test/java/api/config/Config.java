package api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Централизованная конфигурация автотестов.
 * Читает параметры из classpath-ресурса {@code config.properties}.
 * Системные свойства (-D...) имеют приоритет над значениями из файла.
 */
public final class Config {

    private static final Properties PROPS = new Properties();

    static {
        load();
        printConfig();
    }

    private Config() {
        throw new AssertionError("Утилитный класс не инстанцируется");
    }

    private static void load() {
        try (InputStream in = Config.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException(
                        "Файл config.properties не найден в classpath (src/test/resources)");
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(
                    "Не удалось прочитать config.properties: " + e.getMessage());
        }
    }

    /**
     * Значение: сначала System property, затем config.properties, затем defaultValue.
     */
    private static String get(String key, String defaultValue) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.isBlank()) {
            return sys;
        }
        return PROPS.getProperty(key, defaultValue);
    }

    // ---------- URL стенда и API ----------

    public static String getBaseUri() {
        return get("api.base.uri", "http://127.0.0.1");
    }

    public static int getPort() {
        return Integer.parseInt(get("api.port", "8080"));
    }

    public static String getBaseUrl() {
        return getBaseUri() + ":" + getPort();
    }

    // ---------- Тайм-аут поиска элементов ----------

    public static long getElementTimeout() {
        return Long.parseLong(get("element.timeout", "3000"));
    }

    // ---------- Режим логирования (без реализации) ----------

    public static String getLoggingMode() {
        return get("logging.mode", "INFO");
    }

    // ---------- Credentials ----------

    public static String getUsername() {
        return get("api.username", "admin");
    }

    public static String getPassword() {
        return get("api.password", "secret123");
    }

    // ---------- Стартовый товар ----------

    public static String getStartProductName() {
        return get("start.product.name", "StartProduct");
    }

    public static double getStartProductPrice() {
        return Double.parseDouble(get("start.product.price", "99.99"));
    }

    /**
     * Вывод всех параметров (кроме credentials) в консоль перед запуском тестов.
     */
    private static void printConfig() {
        System.out.println("========== Конфигурация автотестов ==========");
        System.out.println("URL стенда/API (baseUri) : " + getBaseUri());
        System.out.println("Порт                     : " + getPort());
        System.out.println("Полный URL               : " + getBaseUrl());
        System.out.println("Тайм-аут элементов (мс)  : " + getElementTimeout());
        System.out.println("Режим логирования        : " + getLoggingMode());
        System.out.println("Стартовый товар (имя)    : " + getStartProductName());
        System.out.println("Стартовый товар (цена)   : " + getStartProductPrice());
        System.out.println("==============================================");
    }
}
