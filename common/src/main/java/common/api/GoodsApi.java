package common.api;

import common.config.Endpoints;
import common.model.ProductRequest;
import common.model.ResultData;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class GoodsApi {
    private final RestApiBuilder request = new RestApiBuilder();

    @Step("Бизнес: добавить товар")
    public Response addProduct(ProductRequest product) {
        return request.doPost(Endpoints.GOODS_ADD, product);
    }

    @Step("Бизнес: добавить товар (raw JSON)")
    public Response addProductRaw(String rawJson) {
        return request.doPostRaw(Endpoints.GOODS_ADD, rawJson);
    }

    @Step("Бизнес: получить товар id={id}")
    public Response getProduct(long id) {
        return request.doGet(Endpoints.goodsById(id));
    }

    @Step("Бизнес: обновить товар id={id}")
    public Response updateProduct(long id, ProductRequest product) {
        return request.doPatch(Endpoints.goodsById(id), product);
    }

    @Step("Бизнес: обновить товар id={id} (raw JSON)")
    public Response updateProductRaw(long id, String rawJson) {
        return request.doPatchRaw(Endpoints.goodsById(id), rawJson);
    }

    @Step("Бизнес: удалить товар id={id}")
    public Response deleteProduct(long id) {
        return request.doDelete(Endpoints.goodsById(id));
    }

    @Step("Бизнес: получить список товаров page={page}, size={size}")
    public Response getAllProducts(int page, int size) {
        return request.doGetWithQueryParams(Endpoints.GOODS_LIST, "page", page, "size", size);
    }

    @Step("Бизнес: создать товар и вернуть id")
    public Long createProductAndReturnId(String name, double price) {
        Response response = addProduct(new ProductRequest(name, price));
        ResultData result = response.as(ResultData.class);
        Long id = result.getDataId();
        if (id == null) {
            throw new IllegalStateException("Сервер не вернул id. Ответ: " + response.body().asString());
        }
        return id;
    }
}
