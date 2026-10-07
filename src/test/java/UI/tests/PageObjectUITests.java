package UI.tests;

import UI.asserts.AdminPageAssert;
import UI.asserts.LoginPageAssert;
import UI.asserts.MainPageAssert;
import UI.pages.AdminPage;
import UI.pages.LoginPage;
import UI.pages.MainPage;
import api.GoodsApiAllCodeResponseTest;
import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;

/**
 * UI-тесты 2.1–2.4 с использованием Page Object и PageAssert.
 * Минимум один тест использует API для подготовки тестовых данных.
 */
public class PageObjectUITests {

    private static final String USERNAME = api.config.Config.getUsername();
    private static final String PASSWORD = api.config.Config.getPassword();
    private static final String BASE_URI = api.config.Config.getBaseUri();
    private static final int PORT = api.config.Config.getPort();

    private final Random random = new Random();
    private MainPage mainPage;

    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1600x900";
        Configuration.timeout = api.config.Config.getElementTimeout();
        open(BASE_URI + ":" + PORT);
        mainPage = new MainPage();
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }

    /**
     * Создание товара через API (требование: хотя бы один тест использует API).
     */
    private void createProductViaApi(String name, double price) {
        RequestSpecification spec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setPort(PORT)
                .setContentType("application/json")
                .setAccept("application/json")
                .setAuth(RestAssured.preemptive().basic(USERNAME, PASSWORD))
                .build();

        given().spec(spec)
                .body("{\"name\": \"" + name + "\", \"price\": " + (int) price + "}")
                .when()
                .post("/goods/add")
                .then()
                .statusCode(200);
    }

    // ===================== 2.1 =====================

    @Test
    @Tag("UI")
    @Tag("PageObject")
    @DisplayName("2.1 Добавить три единицы товара в корзину и оплатить (сумма ≤ 300). Проверить уведомление")
    void addThreeUnitsAndPay() {
        // Arrange: товар с ценой ≤ 100 → 3 шт. ≤ 300 (данные через API)
        String itemName = "PayTest-" + UUID.randomUUID().toString().substring(0, 8);
        double price = 50.0; // 3 * 50 = 150 ≤ 300
        createProductViaApi(itemName, price);
        mainPage.refreshPage();

        // Act
        mainPage.addProductToCart(itemName, 3)
                .openCart();

        // Assert через PageAssert
        MainPageAssert.assertThat(mainPage)
                .cartIsOpen()
                .cartContainsProduct(itemName)
                .totalPriceIsLessThanOrEqualTo(300.0);

        mainPage.makeOrder();

        MainPageAssert.assertThat(mainPage)
                .orderAcceptedNotificationIsShown();
    }

    // ===================== 2.2 =====================

    @Test
    @Tag("UI")
    @Tag("PageObject")
    @DisplayName("2.2 Добавить несколько разных товаров и проверить корректность общей цены")
    void addDifferentItemsAndCheckTotal() {
        Map<String, Double> products = new LinkedHashMap<>();
        products.put("Multi-A-" + UUID.randomUUID().toString().substring(0, 6), 40.0);
        products.put("Multi-B-" + UUID.randomUUID().toString().substring(0, 6), 75.0);

        double expectedTotal = products.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        products.forEach(this::createProductViaApi);
        mainPage.refreshPage();

        // Ждём появления каждой карточки и добавляем в корзину
        products.forEach((name, price) -> {
            mainPage.productCardByName(name).shouldBe(com.codeborne.selenide.Condition.visible);
            mainPage.addProductToCart(name);
        });
        mainPage.openCart();

        MainPageAssert pageAssert = MainPageAssert.assertThat(mainPage)
                .cartIsOpen();

        products.keySet().forEach(pageAssert::cartContainsProduct);

        pageAssert.totalPriceIsEqualTo(expectedTotal);
    }

    // ===================== 2.3 =====================

    @Test
    @Tag("UI")
    @Tag("PageObject")
    @DisplayName("2.3 Войти в админку и добавить товар. Проверить уведомление после добавления")
    void addProductInAdminAndCheckNotification() {
        String itemName = "AdminAdd-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        String cost = Integer.toString(random.nextInt(100) + 1);

        LoginPage loginPage = mainPage.goToAdmin();

        LoginPageAssert.assertThat(loginPage)
                .formIsDisplayed();

        AdminPage adminPage = loginPage.loginAsAdmin();

        AdminPageAssert.assertThat(adminPage)
                .addFormIsVisible();

        adminPage.addProduct(itemName, cost);

        AdminPageAssert.assertThat(adminPage)
                .productAddedNotificationIsShown();
    }

    // ===================== 2.4 =====================

    @Test
    @Tag("UI")
    @Tag("PageObject")
    @DisplayName("2.4 Войти в админку, отредактировать товар и проверить изменения на витрине")
    void editProductInAdminAndVerifyOnStorefront() {
        String newName = "Edited-" + UUID.randomUUID().toString().substring(0, 8);
        String newPrice = "99";

        //создаем айтем для теста
        GoodsApiAllCodeResponseTest tests = new GoodsApiAllCodeResponseTest();
        String name = tests.uniqueName("PO-test");
        long id = tests.createProduct(name,123.0 );


        LoginPage loginPage = mainPage.goToAdmin();
        LoginPageAssert.assertThat(loginPage).formIsDisplayed();

        AdminPage adminPage = loginPage.loginAsAdmin();
        AdminPageAssert.assertThat(adminPage).addFormIsVisible();


        // Редактируем товар с id созданного продукта
        adminPage.editProduct(id, newName, newPrice);

        MainPage storefront = adminPage.goToStorefront();

        MainPageAssert.assertThat(storefront)
                .productIsDisplayed(newName, newPrice);
    }
}
