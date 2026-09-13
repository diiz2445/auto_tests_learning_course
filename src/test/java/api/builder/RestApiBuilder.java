package api.builder;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
import api.config.ApiConfig;
import api.config.Endpoints;


public class RestApiBuilder {

    // Базовый каркас запроса берём из ApiConfig (адрес, порт, auth, JSON).
    private final RequestSpecification spec = ApiConfig.baseRequestSpec();


    /**
     * Возвращает «сырую» спецификацию запроса — если нужно докрутить
     * что-то нестандартное (дополнительный query-параметр, заголовок,
     * отключение логирования и т.п.).
     *
     * @return спецификация с базовыми настройками
     */
    public RequestSpecification getSpec() {
        return spec;
    }

    // =================================================================
    //  ГОТОВЫЕ МЕТОДЫ ДЛЯ ОТПРАВКИ ЗАПРОСОВ
    // =================================================================
    // Каждый метод принимает URL и опциональное тело, строит запрос
    // на базе `spec` и возвращает ответ RestAssured ({@link Response}).
    // Именно эти методы использует Page Object (см. GoodsApi).
    //
    // МАКСИМАЛЬНОЕ ЛОГИРОВАНИЕ:
    //   - .log().all()   в фазе given()  — печатает ЗАПРОС полностью:
    //     метод, URL, заголовки, тело, авторизацию;
    //   - .log().all()   в фазе then()   — печатает ОТВЕТ полностью:
    //     статус-код, заголовки, тело.
    // Это «максимум» из возможного: и что отправили, и что вернулось.
    // Удобно для отладки и вебинара, но в проде обычно ограничиваются
    // log().ifValidationFails() (логировать только при падении проверки),
    // чтобы не раздувать вывод.

    /**
     * Выполнить GET-запрос.
     *
     * @param url полный адрес (из {@link Endpoints})
     * @return ответ сервера
     */
    public Response doGet(String url) {
        return given()
                .spec(spec)
                .log().all()          // лог ЗАПРОСА (максимальный)
                .when()
                .get(url)
                .then()
                .log().all()          // лог ОТВЕТА (максимальный)
                .extract()
                .response();
    }

    /**
     * Выполнить GET-запрос с query-параметрами. URL передаётся БЕЗ
     * параметров — они добавляются типизированно через RestAssured
     * (это правильнее, чем склеивать строку: значения экранируются
     * автоматически).
     *
     * @param url    адрес без параметров (из {@link Endpoints})
     * @param params пары «имя параметра -> значение»: { "page", 0, "size", 10 }
     * @return ответ сервера
     */
    public Response doGetWithQueryParams(String url, Object... params) {
        // Перебираем пары «ключ-значение» и навешиваем каждую на запрос.
        io.restassured.specification.RequestSpecification request =
                given().spec(spec).log().all();
        for (int i = 0; i + 1 < params.length; i += 2) {
            request = request.queryParam(params[i].toString(), params[i + 1]);
        }
        return request
                .when()
                .get(url)
                .then()
                .log().all()          // лог ОТВЕТА (максимальный)
                .extract()
                .response();
    }

    /**
     * Выполнить POST-запрос с JSON-телом.
     *
     * @param url  полный адрес (из {@link Endpoints})
     * @param body объект, который будет сериализован в JSON (Jackson)
     * @return ответ сервера
     */
    public Response doPost(String url, Object body) {
        return given()
                .spec(spec)
                .body(body)
                .log().all()          // лог ЗАПРОСА (максимальный)
                .when()
                .post(url)
                .then()
                .log().all()          // лог ОТВЕТА (максимальный)
                .extract()
                .response();
    }

    /**
     * Выполнить PATCH-запрос с JSON-телом.
     *
     * @param url  полный адрес (из {@link Endpoints})
     * @param body объект, который будет сериализован в JSON (Jackson)
     * @return ответ сервера
     */
    public Response doPatch(String url, Object body) {
        return given()
                .spec(spec)
                .body(body)
                .log().all()          // лог ЗАПРОСА (максимальный)
                .when()
                .patch(url)
                .then()
                .log().all()          // лог ОТВЕТА (максимальный)
                .extract()
                .response();
    }

    /**
     * Выполнить DELETE-запрос.
     *
     * @param url полный адрес (из {@link Endpoints})
     * @return ответ сервера
     */
    public Response doDelete(String url) {
        return given()
                .spec(spec)
                .log().all()          // лог ЗАПРОСА (максимальный)
                .when()
                .delete(url)
                .then()
                .log().all()          // лог ОТВЕТА (максимальный)
                .extract()
                .response();
    }
}
