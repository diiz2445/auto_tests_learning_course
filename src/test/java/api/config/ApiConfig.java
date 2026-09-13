package api.config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public final class ApiConfig {

    public static final String USERNAME =
            System.getProperty("api.username", "admin");

    public static final String PASSWORD =
            System.getProperty("api.password", "secret123");

    /** Флаг: была ли уже выполнена настройка RestAssured. */
    private static boolean configured;

    private ApiConfig() {
        throw new AssertionError("Утилитный класс не инстанцируется");
    }

    private static synchronized void initIfNeeded() {
        if (configured) {
            return;
        }
        // Базовый адрес берём из того же класса Endpoints — ЕДИНАЯ точка правды.
        RestAssured.baseURI = Endpoints.getBaseUri();
        RestAssured.port = Endpoints.getPort();
        // Устанавливаем таймауты, чтобы тесты не «висели» бесконечно,
        // если сервер недоступен.
        RestAssured.config = RestAssured.config()
                .httpClient(io.restassured.config.HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 10_000)
                        .setParam("http.socket.timeout", 10_000));
        configured = true;
    }

    public static RequestSpecification baseRequestSpec() {
        initIfNeeded();
        return new RequestSpecBuilder()
                .setBaseUri(Endpoints.getBaseUri())
                .setPort(Endpoints.getPort())
                .setContentType("application/json")
                .setAccept("application/json")
                .setAuth(RestAssured.preemptive().basic(USERNAME, PASSWORD))
                .build();
    }

    /**
     * Имя пользователя для Basic Auth.
     *
     * @return логин из системного свойства {@code api.username}
     */
    public static String getUsername() {
        return USERNAME;
    }

    /**
     * Пароль для Basic Auth.
     *
     * @return пароль из системного свойства {@code api.password}
     */
    public static String getPassword() {
        return PASSWORD;
    }
}
