package common.api;

import common.config.Config;
import common.config.Endpoints;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public final class ApiConfig {
    private static volatile boolean configured;
    private ApiConfig() { throw new AssertionError("Утилитный класс не инстанцируется"); }

    private static synchronized void initIfNeeded() {
        if (configured) return;
        RestAssured.baseURI = Endpoints.getBaseUri();
        RestAssured.port = Endpoints.getPort();
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
                .setAuth(RestAssured.preemptive().basic(Config.getUsername(), Config.getPassword()))
                .addFilter(new AllureRestAssured())
                .build();
    }
}
