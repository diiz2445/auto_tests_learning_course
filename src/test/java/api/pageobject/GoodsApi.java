package api.pageobject;

import api.builder.RestApiBuilder;
import api.config.Endpoints;
import api.model.Product;
import api.model.ProductRequest;
import api.model.ResultData;
import io.restassured.response.Response;


public class GoodsApi {

    /** Билдер запросов — строит HTTP-запрос с авторизацией и заголовками. */
    private final RestApiBuilder request = new RestApiBuilder();

    // =================================================================
    //  БИЗНЕС-МЕТОДЫ РУЧЕК
    // =================================================================

    /**
     * Добавить новый товар.  <b>POST /goods/add</b>
     *
     * Сервер вернёт {@link ResultData}: сообщение и id созданного товара
     * в {@code data.id}.
     *
     * @param product данные товара (name обязателен, price >= 0)
     * @return ответ сервера
     */
    public Response addProduct(ProductRequest product) {
        return request.doPost(Endpoints.GOODS_ADD, product);
    }

    /**
     * Получить товар по идентификатору.  <b>GET /goods/{id}</b>
     *
     * @param id идентификатор товара
     * @return ответ сервера (200 + {@link Product}, либо 404)
     */
    public Response getProduct(long id) {
        return request.doGet(Endpoints.goodsById(id));
    }

    /**
     * Частично обновить товар.  <b>PATCH /goods/{id}</b>
     *
     * Передаются только те поля, которые нужно изменить. Сервер вернёт
     * обновлённый {@link Product}.
     *
     * @param id      идентификатор товара
     * @param product новые значения полей
     * @return ответ сервера (200 + {@link Product}, либо 400/404)
     */
    public Response updateProduct(long id, ProductRequest product) {
        return request.doPatch(Endpoints.goodsById(id), product);
    }
    public Response updateProductFail(long id, ProductRequest product) {
        return request.doPatch(Endpoints.goodsById(id), product);
    }

    /**
     * Удалить товар.  <b>DELETE /goods/{id}</b>
     *
     * @param id идентификатор товара
     * @return ответ сервера (200 либо 404)
     */
    public Response deleteProduct(long id) {
        return request.doDelete(Endpoints.goodsById(id));
    }


    public Response getAllProducts(int page, int size) {
        return request.doGetWithQueryParams(Endpoints.GOODS_LIST, "page", page, "size", size);
    }

    // =================================================================
    //  ВЫСОКОУРОВНЕВЫЕ СЦЕНАРИИ (композиция ручек)
    // =================================================================
    // Это «составные» бизнес-действия из нескольких ручек. Их тоже можно
    // класть в Page Object: тесты становятся совсем лаконичными, а логика
    // «подготовь данные -> сделай действие -> верни результат» не
    // дублируется.

    /**
     * Сценарий: создать товар и сразу вернуть его из ответа сервера
     * (идём в обход доп. запроса GET — читаем {@link ResultData}).
     *
     * @param name  название товара
     * @param price цена товара
     * @return id созданного товара
     * @throws IllegalStateException если сервер не вернул id
     */
    public Long createProductAndReturnId(String name, double price) {
        Response response = addProduct(new ProductRequest(name, price));
        ResultData result = response.as(ResultData.class);
        Long id = result.getDataId();
        if (id == null) {
            throw new IllegalStateException(
                    "Сервер не вернул id созданного товара. Ответ: " + response.body().asString());
        }
        return id;
    }
}