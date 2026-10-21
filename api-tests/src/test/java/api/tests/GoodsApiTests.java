package api.tests;

import common.api.GoodsApi;
import common.api.ProductAssert;
import common.model.Product;
import common.model.ProductList;
import common.model.ProductRequest;
import common.model.ResultData;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("API")
public class GoodsApiTests {

    private final GoodsApi goodsApi = new GoodsApi();
    private final List<Long> createdIds = new ArrayList<>();

    @AfterEach
    void cleanupCreatedProducts() {
        for (Long id : createdIds) {
            goodsApi.deleteProduct(id);
        }
        createdIds.clear();
    }

    @Step("API-хелпер: создать товар name={name}, price={price}")
    private long createProduct(String name, double price) {
        long id = goodsApi.createProductAndReturnId(name, price);
        createdIds.add(id);
        return id;
    }

    @Step("API-хелпер: уникальное имя {prefix}")
    private String uniqueName(String prefix) {
        return prefix + "-" + System.currentTimeMillis() + "-" + (int) (Math.random() * 1000);
    }

    @Step("API-хелпер: получить все товары")
    private List<Product> fetchAllProducts() {
        List<Product> allItems = new ArrayList<>();
        for (int page = 0; ; page++) {
            Response response = goodsApi.getAllProducts(page, 100);
            if (response.getStatusCode() != 200) break;
            List<Product> goods = response.as(ProductList.class).getGoods();
            if (goods == null || goods.isEmpty()) break;
            allItems.addAll(goods);
            if (goods.size() < 100) break;
        }
        return allItems;
    }

    @Test
    @Tag("smoke")
    @Tag("POSITIVE")
    @DisplayName("POST /goods/add: 200 и id созданного товара")
    void addProductReturnsNewProductId() {
        ProductRequest product = new ProductRequest(uniqueName("Телефон"), 59999.99);
        Response response = goodsApi.addProduct(product);
        assertThat(response.getStatusCode()).as("код ответа").isEqualTo(200);
        ResultData result = response.as(ResultData.class);
        assertThat(result.getMessage()).as("сообщение").isEqualTo("success");
        assertThat(result.getDataId()).as("id").isNotNull();
        createdIds.add(result.getDataId());
    }

    @Test
    @Tag("NEGATIVE")
    @DisplayName("POST /goods/add: пустое имя → 400")
    void addProductWithoutNameIsRejected() {
        Response response = goodsApi.addProduct(new ProductRequest("", 10.0));
        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @Test
    @Tag("NEGATIVE")
    @DisplayName("POST /goods/add: невалидный JSON → 400")
    void addProductWithoutPriceIsRejected() {
        String raw = "{\"name\":\"%s\",\"price\":}".formatted(uniqueName("add400"));
        Response response = goodsApi.addProductRaw(raw);
        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @Test
    @Tag("smoke")
    @DisplayName("GET /goods/{id}: получение созданного товара")
    void getProductReturnsCreatedProduct() {
        String name = uniqueName("Ноутбук");
        long id = createProduct(name, 89999.99);
        Response response = goodsApi.getProduct(id);
        assertThat(response.getStatusCode()).isEqualTo(200);
        Product product = response.as(Product.class);
        ProductAssert.assertThat(product).hasId(id).hasName(name).hasPrice(89999.99, 0.01);
    }

    @Test
    @Tag("NEGATIVE")
    @DisplayName("GET /goods/{id}: удалённый товар → 404")
    void getDeletedProductReturns404() {
        long id = createProduct(uniqueName("Удаляемый"), 100.0);
        goodsApi.deleteProduct(id);
        Response response = goodsApi.getProduct(id);
        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    @Tag("smoke")
    @DisplayName("PATCH /goods/{id}: обновление цены и имени")
    void updateProductPrice() {
        long id = createProduct(uniqueName("Клавиатура"), 3000.0);
        String updatedName = uniqueName("Клавиатура");
        Response response = goodsApi.updateProduct(id, new ProductRequest(updatedName, 2750.5));
        assertThat(response.getStatusCode()).isEqualTo(200);
        Product updated = response.as(Product.class);
        ProductAssert.assertThat(updated).hasId(id).hasName(updatedName).hasPrice(2750.5, 0.01);
    }

    @Test
    @Tag("NEGATIVE")
    @DisplayName("PATCH /goods/{id}: несуществующий id → 404")
    void updateProductNotFound404() {
        long id = createProduct(uniqueName("Клавиатура"), 3000.0);
        Response response = goodsApi.updateProduct(id + 999_999, new ProductRequest(uniqueName("X"), 100.0));
        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    @Tag("NEGATIVE")
    @DisplayName("PATCH /goods/{id}: невалидный JSON → 400")
    void updateProductInvalidBody400() {
        long id = createProduct(uniqueName("Клавиатура"), 3000.0);
        String raw = "{\"name\":\"%s\",\"price\":\"not-a-number\"}".formatted(uniqueName("patch400"));
        Response response = goodsApi.updateProductRaw(id, raw);
        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @Test
    @Tag("smoke")
    @DisplayName("DELETE /goods/{id}: 200, затем GET → 404")
    void deleteProductRemovesIt200() {
        long id = createProduct(uniqueName("Мышь"), 1500.0);
        assertThat(goodsApi.deleteProduct(id).getStatusCode()).isEqualTo(200);
        assertThat(goodsApi.getProduct(id).getStatusCode()).isEqualTo(404);
    }

    @Test
    @Tag("NEGATIVE")
    @DisplayName("DELETE /goods/{id}: несуществующий id → 404")
    void deleteProductNotFound404() {
        long id = createProduct(uniqueName("Мышь"), 1500.0);
        assertThat(goodsApi.deleteProduct(id + 999_999).getStatusCode()).isEqualTo(404);
    }

    @Test
    @Tag("smoke")
    @DisplayName("GET /goods/list: список содержит созданный товар")
    void listContainsCreatedProduct() {
        String name = uniqueName("Монитор");
        createProduct(name, 15000.0);
        List<Product> allProducts = fetchAllProducts();
        assertThat(allProducts).as("список").isNotEmpty();
        assertThat(allProducts.stream().map(Product::getName).toList()).contains(name);
    }

    @Test
    @DisplayName("GET /goods/list: пагинация size ограничивает число элементов")
    void paginationLimitsPageSize() {
        createProduct(uniqueName("Пагинация"), 500.0);
        Response response = goodsApi.getAllProducts(0, 1);
        assertThat(response.getStatusCode()).isEqualTo(200);
        ProductList list = response.as(ProductList.class);
        assertThat(list.getGoods()).as("на странице").hasSizeLessThanOrEqualTo(1);
    }
}
