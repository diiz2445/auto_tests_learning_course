package api;

import api.config.ApiConfig;
import api.config.Endpoints;
import api.model.Product;
import api.model.ProductList;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static api.config.ApiConfig.PASSWORD;
import static api.config.ApiConfig.USERNAME;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GoodsApiTestFirstTask {
    private final RequestSpecification spec = ApiConfig.baseRequestSpec();

    /// метод, проверяющий эндпоинт с помощью инструкций given(), when(), then()
    /// проверяющий эндпоинт с помощью RequestSpecification
    @Test
    @Tag("API")
    //@Tag("TEST")
    @DisplayName("Ошибка при получении айтемов: отрицательный номер страницы")
    public void doGetWithQueryParams() {

        io.restassured.specification.RequestSpecification request =
                given().spec(spec).log().all();

        request = request.queryParam("page", -1);

        Response response = request
                .when()
                .get(Endpoints.getBaseUri()+Endpoints.GOODS_LIST)
                .then()
                .log().all()          // лог ОТВЕТА (максимальный)
                .extract()
                .response();

        assertThat(response.getStatusCode()).as("код ответа").isEqualTo(400);
    }

    ///GET list с проверками через AssertJ
    @Test
    @Tag("API")
    //@Tag("TEST")
    @DisplayName("Пустое тело ответа в GET list")
    public void doGetWithRestAssured() {

        io.restassured.specification.RequestSpecification request =
                given().spec(spec).log().all();

        Response response = request
                .when()
                .get(Endpoints.getBaseUri()+Endpoints.GOODS_LIST)
                .then()
                .log().all()          // лог ОТВЕТА (максимальный)
                .extract()
                .response();
        String body = response.asString();

        assertThat(response.getStatusCode()).as("код ответа").isEqualTo(200);
        assertThat(body).isEmpty();
    }

    /// метод создания и проверки существования айтема, с restAssured проверками
    @Test
    @Tag("API")
    //@Tag("TEST")
    @DisplayName("Создали товар → он есть в GET /goods/list (RestAssured)")
    void createProductAndCheckInList() {
        String uniqueName = "Product-RA-" + System.currentTimeMillis();

        var builder = new RequestSpecBuilder()
                .setBaseUri(Endpoints.getBaseUri())
                .setPort(Endpoints.getPort())
                .setContentType("application/json")
                .setAccept("application/json")
                .setAuth(RestAssured.preemptive().basic(USERNAME, PASSWORD))
                .build();
        io.restassured.specification.RequestSpecification request = builder.
                given().spec(spec).log().all();


        // создаём товар
        given().spec(spec)
                .body("""
                {"name":"%s","price":123}
                """.formatted(uniqueName))
                .when()
                .log().all()
                .post(Endpoints.getBaseUrl()+Endpoints.GOODS_ADD)
                .then()
                .log().all()          // лог ОТВЕТА (максимальный)
                .statusCode(200)
                .extract()
                .response();

        // получаем список и проверяем через RestAssured
        var products = given()
                .when()
                .get(Endpoints.getBaseUrl()+Endpoints.GOODS_LIST)
                .then()
                .log().all()          // лог ОТВЕТА (максимальный)
                .statusCode(200)
                .body("goods.find { it.id == 10 }.name", equalTo("Product-AJ-1789237569880"));


    }

    /// метод создания и проверки существования айтема, с AssertJ проверками
    @Test
    @Tag("API")
    //@Tag("TEST")
    @DisplayName("Создали товар → он есть в GET /goods/list (AssertJ)")
    void createProductAndCheckInListAssertJ() {
        String uniqueName = "Product-AJ-" + System.currentTimeMillis();

        var builder = new RequestSpecBuilder()
                .setBaseUri(Endpoints.getBaseUri())
                .setPort(Endpoints.getPort())
                .setContentType("application/json")
                .setAccept("application/json")
                .setAuth(RestAssured.preemptive().basic(USERNAME, PASSWORD))
                .build();
        io.restassured.specification.RequestSpecification request = builder.
                given().spec(spec).log().all();


        // создаём товар
        given().spec(spec)
                .body("""
                {"name":"%s","price":123}
                """.formatted(uniqueName))
                .when()
                .log().all()
                .post(Endpoints.getBaseUrl()+Endpoints.GOODS_ADD)
                .then()
                .log().all()          // лог ОТВЕТА (максимальный)
                .statusCode(200)
                .extract()
                .response();

        // получаем список и проверяем через AssertJ
        var products = given()
                .when()
                .get(Endpoints.getBaseUrl()+Endpoints.GOODS_LIST)
                .then()
                .log().all()          // лог ОТВЕТА (максимальный)
                .extract()
                .response();

        Map<String, Object> found = products.jsonPath()
                .getMap("goods.find { it.id == 10 }");

        assertThat(found)
                .as("Объект с id=10 должен присутствовать")
                .isNotNull();

        assertThat(found.get("name"))
                .as("name объекта")
                .isEqualTo("Product-AJ-1789237569880");

        assertThat(found.get("price"))
                .as("price объекта")
                .isEqualTo(123.0f);   // или 123.0

    }

}
