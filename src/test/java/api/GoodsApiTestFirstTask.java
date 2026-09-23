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

import java.util.HashMap;
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

        // Создаем Map для query-параметров (для практики с map)
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", -1);

        // Создаем спеку с базовыми настройками, авторизацией и параметрами
        RequestSpecification getSpec = new RequestSpecBuilder()
                .setBaseUri(Endpoints.getBaseUri())
                .setPort(Endpoints.getPort())
                .setContentType("application/json")
                .setAccept("application/json")
                .setAuth(RestAssured.preemptive().basic(USERNAME, PASSWORD))
                .addQueryParams(queryParams) // Добавляем query-параметры в спецификацию
                .build();

        // гетаем список айтемов
        Response response = given()
                .spec(getSpec)
                .log().all() // Логируем отправляемый ЗАПРОС
                .when()
                .get(Endpoints.GOODS_LIST) // Используем ТОЛЬКО эндпоинт, BaseUri уже внутри спецификации
                .then()
                .log().all() // Логируем ОТВЕТ
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
        var CreationResponse = given().spec(spec)
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

        String ResponseItemID = CreationResponse.jsonPath().getString("data.id");

        // получаем список и проверяем через RestAssured
        var products = given()
                .when()
                .get(Endpoints.getBaseUrl()+Endpoints.GOODS_LIST)
                .then()
                .log().all()          // лог ОТВЕТА (максимальный)
                .statusCode(200)
                .body("goods.find { it.id == "+ ResponseItemID + " }.name", equalTo(uniqueName));

    }

    /// метод создания и проверки существования айтема, с AssertJ проверками
    @Test
    @Tag("API")
    @Tag("TEST")
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
        var CreationResponse = given().spec(spec)
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

        //Вытащим ID записи
        String ResponseItemID = CreationResponse.jsonPath().getString("data.id");

        // получаем список и проверяем через AssertJ
        var products = given()
                .when()
                .get(Endpoints.getBaseUrl()+Endpoints.GOODS_LIST)
                .then()
                .log().all()          // лог ОТВЕТА (максимальный)
                .extract()
                .response();

        Map<String, Object> found = products.jsonPath()
                .getMap("goods.find { it.id == %s }".formatted(ResponseItemID));

        assertThat(found)
                .as("Объект с id=%s должен присутствовать".formatted(ResponseItemID))
                .isNotNull();

        assertThat(found.get("name"))
                .as("name объекта")
                .isEqualTo(uniqueName);

        assertThat(found.get("price"))
                .as("price объекта")
                .isEqualTo(123.0f);   // или 123.0

    }

}
