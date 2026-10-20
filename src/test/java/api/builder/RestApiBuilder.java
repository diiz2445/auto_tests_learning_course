package api.builder;

import api.config.ApiConfig;
import api.config.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestApiBuilder {

    private final RequestSpecification spec = ApiConfig.baseRequestSpec();

    public RequestSpecification getSpec() {
        return spec;
    }

    @Step("API GET {url}")
    public Response doGet(String url) {
        return given()
                .spec(spec)
                .log().all()
                .when()
                .get(url)
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Step("API GET {url} с query-параметрами")
    public Response doGetWithQueryParams(String url, Object... params) {
        io.restassured.specification.RequestSpecification request =
                given().spec(spec).log().all();
        for (int i = 0; i + 1 < params.length; i += 2) {
            request = request.queryParam(params[i].toString(), params[i + 1]);
        }
        return request
                .when()
                .get(url)
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Step("API POST {url}")
    public Response doPost(String url, Object body) {
        return given()
                .spec(spec)
                .body(body)
                .log().all()
                .when()
                .post(url)
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Step("API PATCH {url}")
    public Response doPatch(String url, Object body) {
        return given()
                .spec(spec)
                .body(body)
                .log().all()
                .when()
                .patch(url)
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Step("API DELETE {url}")
    public Response doDelete(String url) {
        return given()
                .spec(spec)
                .log().all()
                .when()
                .delete(url)
                .then()
                .log().all()
                .extract()
                .response();
    }
}
