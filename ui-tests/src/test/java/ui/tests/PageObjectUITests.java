package ui.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import common.api.GoodsApi;
import common.config.Config;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ui.asserts.AdminPageAssert;
import ui.asserts.LoginPageAssert;
import ui.asserts.MainPageAssert;
import ui.pages.AdminPage;
import ui.pages.LoginPage;
import ui.pages.MainPage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

@Tag("UI")
public class PageObjectUITests {

    private final Random random = new Random();
    private final GoodsApi goodsApi = new GoodsApi();
    private MainPage mainPage;

    @BeforeAll
    static void setUpAllure() {
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide().screenshots(true).savePageSource(false));
    }

    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1600x900";
        Configuration.timeout = Config.getElementTimeout();
        open(Config.getBaseUrl());
        mainPage = new MainPage();
    }

    @AfterEach
    void tearDown() { closeWebDriver(); }

    @Step("Создать товар через API: name={name}, price={price}")
    private void createProductViaApi(String name, double price) {
        goodsApi.createProductAndReturnId(name, price);
    }

    @Test
    @Tag("smoke")
    @Tag("PageObject")
    @Tag("VebinarUI")
    @DisplayName("2.1 Добавить три единицы товара в корзину и оплатить (сумма ≤ 300)")
    void addThreeUnitsAndPay() {
        String itemName = "PayTest-" + UUID.randomUUID().toString().substring(0, 8);
        createProductViaApi(itemName, 50.0);
        mainPage.refreshPage();

        mainPage.addProductToCart(itemName, 3).openCart();

        MainPageAssert.assertThat(mainPage)
                .cartIsOpen()
                .cartContainsProduct(itemName)
                .totalPriceIsLessThanOrEqualTo(300.0);

        mainPage.makeOrder();
        MainPageAssert.assertThat(mainPage).orderAcceptedNotificationIsShown();
    }

    @Test
    @Tag("PageObject")
    @Tag("VebinarUI")
    @DisplayName("2.2 Добавить несколько разных товаров и проверить общую цену")
    void addDifferentItemsAndCheckTotal() {
        Map<String, Double> products = new LinkedHashMap<>();
        products.put("Multi-A-" + UUID.randomUUID().toString().substring(0, 6), 40.0);
        products.put("Multi-B-" + UUID.randomUUID().toString().substring(0, 6), 75.0);
        double expectedTotal = products.values().stream().mapToDouble(Double::doubleValue).sum();

        products.forEach(this::createProductViaApi);
        mainPage.refreshPage();

        products.forEach((name, price) -> {
            mainPage.productCardByName(name).shouldBe(com.codeborne.selenide.Condition.visible);
            mainPage.addProductToCart(name);
        });
        mainPage.openCart();

        MainPageAssert pageAssert = MainPageAssert.assertThat(mainPage).cartIsOpen();
        products.keySet().forEach(pageAssert::cartContainsProduct);
        pageAssert.totalPriceIsEqualTo(expectedTotal);
    }

    @Test
    @Tag("smoke")
    @Tag("PageObject")
    @Tag("VebinarUI")
    @DisplayName("2.3 Войти в админку и добавить товар. Проверить уведомление")
    void addProductInAdminAndCheckNotification() {
        String itemName = "AdminAdd-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        String cost = Integer.toString(random.nextInt(100) + 1);

        LoginPage loginPage = mainPage.goToAdmin();
        LoginPageAssert.assertThat(loginPage).formIsDisplayed();

        AdminPage adminPage = loginPage.loginAsAdmin();
        AdminPageAssert.assertThat(adminPage).addFormIsVisible();

        adminPage.addProduct(itemName, cost);
        AdminPageAssert.assertThat(adminPage).productAddedNotificationIsShown();
    }

    @Test
    @Tag("PageObject")
    @Tag("VebinarUI")
    @DisplayName("2.4 Войти в админку, отредактировать товар и проверить на витрине")
    void editProductInAdminAndVerifyOnStorefront() {
        String newName = "Edited-" + UUID.randomUUID().toString().substring(0, 8);
        String newPrice = "99";
        long id = goodsApi.createProductAndReturnId("PO-test-" + System.currentTimeMillis(), 123.0);

        LoginPage loginPage = mainPage.goToAdmin();
        LoginPageAssert.assertThat(loginPage).formIsDisplayed();

        AdminPage adminPage = loginPage.loginAsAdmin();
        AdminPageAssert.assertThat(adminPage).addFormIsVisible();

        adminPage.editProduct(id, newName, newPrice);
        MainPage storefront = adminPage.goToStorefront();
        MainPageAssert.assertThat(storefront).productIsDisplayed(newName, newPrice);
    }
}
