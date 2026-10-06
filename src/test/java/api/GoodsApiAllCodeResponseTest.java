package api;

import api.config.ApiConfig;
import api.config.Endpoints;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import api.model.*;
import api.pageobject.GoodsApi;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static api.assertion.ProductAssert.assertThat;

public class GoodsApiAllCodeResponseTest {
    // Page Object — единственная «точка входа» в API из тестов.
    private final GoodsApi goodsApi = new GoodsApi();
    private final RequestSpecification spec = ApiConfig.baseRequestSpec();
    private final List<Long> createdIds = new ArrayList<>();

    @AfterEach
    void cleanupCreatedProducts() {
        for (Long id : createdIds) {
            goodsApi.deleteProduct(id);
        }
        createdIds.clear();
    }

    //локальное создание айтема
    private long createProduct(String name, double price) {
        long id = goodsApi.createProductAndReturnId(name, price);
        createdIds.add(id);
        return id;
    }
    private String uniqueName(String prefix) {
        return prefix + "-" + System.currentTimeMillis() + "-" + (int) (Math.random() * 1000);
    }
    //Получение всех айтемов
    private List<Product> fetchAllProducts() {
        List<Product> allItems = new ArrayList<>();
        for (int page = 0; ; page++) {
            Response response = goodsApi.getAllProducts(page, 100);
            if (response.getStatusCode() != 200) {
                break;
            }
            List<Product> goods = response.as(ProductList.class).getGoods();
            if (goods == null || goods.isEmpty()) {
                break;
            }
            allItems.addAll(goods);
            // вернулось меньше запрошенного размера
            if (goods.size() < 100) {
                break;
            }
        }
        return allItems;
    }
    private String GenerateRandomStr(int len)
    {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<len;i++)
            builder.append((char)random.nextInt(1,100));
        return builder.toString();
    }

    @Test
    @Tag("API")
    @Tag("POSITIVE")
    @DisplayName("Create item: code 200; body ID")
    void addProductReturnsNewProductId() {
        ProductRequest product = new ProductRequest(uniqueName("Телефон"), 59999.99);

        // Единственный «HTTP-шаг» — бизнес-метод ручки add.
        Response response = goodsApi.addProduct(product);

        assertThat(response.getStatusCode()).as("код ответа").isEqualTo(200);

        // Тело ответа — ResultData: сообщение + id созданного товара.
        ResultData result = response.as(ResultData.class);
        assertThat(result.getMessage()).as("сообщение сервера").isEqualTo("success");
        assertThat(result.getDataId()).as("id созданного товара").isNotNull();

        // Запоминаем id, чтобы очистить его в @AfterEach.
        createdIds.add(result.getDataId());
    }

    @Test
    @Tag("API")
    @Tag("NEGATIVE")
    @DisplayName("Create item with empty string name: code 400")
    void addProductWithoutNameIsRejected() {
        // name пустой, а это обязательное поле — сервер должен ответить 400.
        ProductRequest product = new ProductRequest("", 10.0);

        Response response = goodsApi.addProduct(product);

        // Негативная проверка: ожидаем ошибку валидации.
        assertThat(response.getStatusCode()).isEqualTo(400);
    }
    @Test
    @Tag("API")
    
    @Tag("NEGATIVE")
    @DisplayName("Create item with empty price: code 400")
    void addProductWithoutPriceIsRejected() {
        // price пустой, а это обязательное поле — сервер должен ответить 400.
        given().spec(spec)
                .body("""
                {"name":"%s","price":}
                """.formatted(uniqueName("add404")))
                .when()
                .log().all()
                .post(Endpoints.getBaseUrl()+Endpoints.GOODS_ADD)
                .then()
                .log().all()          // лог ОТВЕТА (максимальный)
                .statusCode(400)
                .extract()
                .response();

    }

    //Get item by id Returns code 200
    @Test
    @Tag("API")
    @DisplayName("Получение созданного товара возвращает его данные")
    void getProductReturnsCreatedProduct() {
        String name = uniqueName("Ноутбук");
        long id = createProduct(name, 89999.99);

        Response response = goodsApi.getProduct(id);

        assertThat(response.getStatusCode()).isEqualTo(200);

        Product product = response.as(Product.class);
        assertThat(product)
                .hasId(id)
                .hasName(name)
                .hasPrice(89999.99, 0.01);
    }

    @Test
    @Tag("API")
    @DisplayName("Получение удалённого товара -> 404")
    void getDeletedProductReturns404() {
        long id = createProduct(uniqueName("Удаляемый"), 100.0);
        goodsApi.deleteProduct(id); // удаляем, чтобы получить детерминированный 404

        Response response = goodsApi.getProduct(id);

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    //Patch item with code 200
    @Test
    @Tag("API")
    @DisplayName("Обновление цены товара через PATCH")
    void updateProductPrice() {
        long id = createProduct(uniqueName("Клавиатура"), 3000.0);

        // Меняем цену — это и есть «частичное» обновление.
        // PATCH в этом сервисе требует уникальное name, поэтому генерируем новое.
        String updatedName = uniqueName("Клавиатура");
        ProductRequest update = new ProductRequest(updatedName, 2750.5);
        Response response = goodsApi.updateProduct(id, update);

        assertThat(response.getStatusCode()).isEqualTo(200);

        Product updated = response.as(Product.class);
        assertThat(updated)
                .hasId(id)
                .hasName(updatedName)
                .hasPrice(2750.5, 0.01);
    }

    //Patch item with code 404
    @Test
    @Tag("API")
    @DisplayName("Обновление цены товара через PATCH")
    void updateProductPriceInvalide404() {
        long id = createProduct(uniqueName("Клавиатура"), 3000.0);

        // PATCH в этом сервисе требует уникальное name, поэтому генерируем новое.
        String updatedName = uniqueName("Клавиатура");
        ProductRequest update = new ProductRequest(updatedName, "as");
        Response response = goodsApi.updateProduct(id+1, update);

        assertThat(response.getStatusCode()).isEqualTo(404);

    }

    //Patch item with code 400
    @Test
    @Tag("API")
    @DisplayName("Обновление цены товара через PATCH")
    void updateProductPriceInvalide400() {
        long id = createProduct(uniqueName("Клавиатура"), 3000.0);
        // PATCH в этом сервисе требует уникальное name, поэтому генерируем новое.
        String updatedName = uniqueName("Клавиатура");

        given().spec(spec)
                .body("""
                {"name":"%s","price":-123}
                """.formatted(updatedName))
                .when()
                .log().all()
                .patch(Endpoints.getBaseUrl()+"/goods/%s".formatted(id))
                .then()
                .log().all()          // лог ОТВЕТА (максимальный)
                .statusCode(400)
                .extract()
                .response();
    }

    @Test
    @Tag("API")
    @DisplayName("Удаление товара, после чего GET возвращает 404")
    void deleteProductRemovesIt200() {
        long id = createProduct(uniqueName("Мышь"), 1500.0);

        // Удаляем созданный товар.
        Response deleteResponse = goodsApi.deleteProduct(id);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(200);

        // Проверяем, что товара больше нет.
        Response getAfterDelete = goodsApi.getProduct(id);
        assertThat(getAfterDelete.getStatusCode()).isEqualTo(404);
    }
    @Test
    @Tag("API")
    @DisplayName("Удаление товара с ошибкой 404")
    void deleteProductRemovesIt404() {
        long id = createProduct(uniqueName("Мышь"), 1500.0);

        // Удаляем созданный товар.
        Response deleteResponse = goodsApi.deleteProduct(id+1);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(404);

    }
    
    @Test
    @Tag("API")
    @DisplayName("Список товаров содержит только что созданный товар")
    void listContainsCreatedProduct() {
        String name = uniqueName("Монитор");
        createProduct(name, 15000.0);

        // Собираем все товары по всем страницам (см. fetchAllProducts).
        List<Product> allProducts = fetchAllProducts();
        assertThat(allProducts).as("список товаров").isNotEmpty();

        // ...и проверяем, что наш товар присутствует в списке.
        List<String> names = allProducts.stream().map(Product::getName).toList();
        assertThat(names).contains(name);
    }

    @Test
    @Tag("API")
    @DisplayName("Пагинация: size ограничивает число элементов на странице")
    void paginationLimitsPageSize() {
        // Гарантируем, что в списке есть хотя бы один товар — чтобы тест
        // не зависел от порядка запуска и состояния БД.
        createProduct(uniqueName("Пагинация"), 500.0);

        // Запрашиваем первую страницу размером 1.
        Response response = goodsApi.getAllProducts(0, 1);

        assertThat(response.getStatusCode()).isEqualTo(200);

        // Сервер должен вернуть НЕ БОЛЬШЕ 1 товара (срез по размеру страницы).
        ProductList list = response.as(ProductList.class);
        assertThat(list.getGoods()).as("товаров на странице").hasSizeLessThanOrEqualTo(1);
    }
}
