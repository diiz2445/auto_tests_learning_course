package UI;

import com.codeborne.selenide.*;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import java.util.*;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class TestsAfterVebinar {

    public static final String USERNAME =
            System.getProperty("api.username", "admin");

    public static final String PASSWORD =
            System.getProperty("api.password", "secret123");

    private static final String BASE_URI =
            System.getProperty("api.base.uri", "http://127.0.0.1");

    private static final int PORT =
            Integer.parseInt(System.getProperty("api.port", "8080"));

    private final Random random = new Random();

    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 3000;

        open(BASE_URI + ":" + PORT);
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }

    private void loginToAdminPage() {
        $("[href='/admin']").click();
        $("#username").setValue(USERNAME);
        $("#password").setValue(PASSWORD);
        $("body > div > form > button").click();
        $(".card").shouldBe(visible);
    }

    /**
     * Создание товара через API (для генерации тестовых данных).
     */
    private Map<String, Object> createProductViaApi(String name, double price) {
        //String uniqueName = "Product-RA-" + System.currentTimeMillis();
        final String BASE_URI =
                System.getProperty("api.base.uri", "http://127.0.0.1");
        final int PORT =
                Integer.parseInt(System.getProperty("api.port", "8080"));

        RequestSpecification getSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setPort(PORT)
                .setContentType("application/json")
                .setAccept("application/json")
                .setAuth(RestAssured.preemptive().basic(USERNAME, PASSWORD))
                .build();


        // создаём товар
        var CreationResponse = given().spec(getSpec)
                .body("{\"name\": \""+name+"\", \"price\": "+(int)price+"}")
                .when()
                .log().all()
                .post("/goods/add")
                .then()
                .log().all()          // лог ОТВЕТА (максимальный)
                .statusCode(200)
                .extract()
                .response();

        return Map.of("name",name,"price",123);
    }

    // ===================== 3.1 =====================

    @Test
    @Tag("VebinarUI")
    @Tag("testing")
    @DisplayName("3.1 Добавить три единицы товара в корзину и оплатить (сумма ≤ 300). Проверить уведомление об обработке заказа")
    void addThreeUnitsAndPay() {
        // Arrange: товар с ценой ≤ 100, чтобы 3 шт. ≤ 300
        String itemName = "PayTest-" + UUID.randomUUID().toString().substring(0, 8);
        double price = 50.0; // 3 * 50 = 150 ≤ 300
        createProductViaApi(itemName, price); // API (требование 3.5)

        refresh(); // подтянуть товар на витрину


        // Act: 3 раза добавить в корзину
        for (int i = 0; i < 3; i++) {
            $x("//*[@id=\"products-list\"]/div[@data-name=\""+itemName+"\"]/button").click();
        }

        $("#open-cart-btn").click();
        $("#cartModal").shouldBe(visible);

        // Проверка, что сумма не превышает 300 (по UI)
        // Предположение: в корзине есть элемент с итогом, например #cart-total или .cart-total
        String totalText = $("#total-price").shouldBe(visible).getText()
                .replaceAll("[^0-9.]", "");
        double total = Double.parseDouble(totalText.isEmpty() ? "0" : totalText);
        assertThat(total)
                .as("Общая стоимость не должна превышать 300")
                .isLessThanOrEqualTo(300.0);

        // Оплата
        $("#makeOrder").click();


        // Assert: уведомление об обработке заказа
        $("#toast-container > div:nth-last-child(1)").shouldBe(text("Заказ принят в обработку!"));
    }

    // ===================== 3.2 =====================

    @Test
    @Tag("VebinarUI")

    @DisplayName("3.2 Добавить несколько разных товаров в корзину и проверить корректность общей цены")
    void addDifferentItemsAndCheckTotal() {
        // Arrange: словарь name → price
        Map<String, Double> products = new LinkedHashMap<>();
        products.put("Multi-A-" + UUID.randomUUID().toString().substring(0, 6), 40.0);
        products.put("Multi-B-" + UUID.randomUUID().toString().substring(0, 6), 75.0);

        double expectedTotal = products.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        // Создаём товары через API
        products.forEach(this::createProductViaApi);
        refresh();

        // Act: добавляем каждый товар в корзину
        products.forEach((name, price) -> {
            $x("//*[@id=\"products-list\"]/div[@data-name=\""+name+"\"]/button").click();

        });
        screenshot("abc");

        $("#open-cart-btn").click();
        $("#cartModal").shouldBe(visible);

        // Assert: все товары есть в корзине
        products.keySet().forEach(name ->
                $("#cart-items").shouldHave(text(name))
        );

        // Проверка итоговой суммы
        String totalText = $("#total-price").shouldBe(visible).getText()
                .replaceAll("[^0-9.]", "");
        double actualTotal = Double.parseDouble(totalText.isEmpty() ? "0" : totalText);

        assertThat(actualTotal)
                .as("Итоговая сумма в корзине должна быть %.2f", expectedTotal)
                .isEqualTo(expectedTotal);
    }

    // ===================== 3.3 =====================

    @Test
    @Tag("VebinarUI")
    @Tag("testing")
    @DisplayName("3.3 Войти в админку и добавить товар. Проверить уведомление после добавления")
    void addProductInAdminAndCheckNotification() {
        String itemName = "AdminAdd-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        String cost = Integer.toString(random.nextInt(100) + 1);

        loginToAdminPage();

        $("[id*='name']").setValue(itemName);
        $("[id*='price']").setValue(cost);
        $(".card button").click();

        // Уведомление после добавления
        $("#toast-container > div:nth-last-child(1)").shouldBe(text("Заказ принят в обработку!"));

    }

    // ===================== 3.4 =====================

    @Test
    @Tag("VebinarUI")
    @DisplayName("3.4 Войти в админку, отредактировать товар и проверить изменения на витрине")
    void editProductInAdminAndVerifyOnStorefront() {
        String newName = "Edited-" + UUID.randomUUID().toString().substring(0, 8);
        String newPrice = "99";

        loginToAdminPage();

        $x("//*[@id=\"tbody\"]/tr/td[text()=\"1\"]/../td/input[@type=\"text\"]").setValue(newName);
        $x("//*[@id=\"tbody\"]/tr/td[text()=\"1\"]/../td/input[@type=\"number\"]").setValue(newPrice);

        $x("//*[@id=\"tbody\"]/tr/td[text()=\"1\"]/../td/button[@data-action=\"update\"]").click();

        // Возврат на витрину
        $("[href='/']").click();

        // Проверка, что изменения применились
        $$(".product-card")
                .findBy(attribute("data-name", newName))
                .shouldBe(visible)
                .shouldHave(attribute("data-price", newPrice));

    }
}