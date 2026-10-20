package UI;

import api.GoodsApiAllCodeResponseTest;
import com.codeborne.selenide.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static api.assertion.ProductAssert.assertThat;

import java.util.Random;
import java.util.UUID;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class SelenideUITests {

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
        Configuration.browserSize = "1600x900";
        Configuration.timeout = 3000; // 3 секунды ожидания по умолчанию

        open(BASE_URI + ":" + PORT);
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }

    /**
     * Логин в админку
     */
    private void loginToAdminPage() {
        $("[href='/admin']").click();

        $("#username").setValue(USERNAME);
        $("#password").setValue(PASSWORD);
        $("body > div > form > button").click();

        // Ждём появления формы добавления товара
        $(".card").shouldBe(visible);
    }

    @Test
    @Tag("UI")
    @DisplayName("Добавить товар через админку, выйти на витрину и проверить, что товар отображается")
    void addingItemAndCheck() {
        String itemName = UUID.randomUUID().toString().replace("-", "");
        String cost = Integer.toString(random.nextInt(300));

        loginToAdminPage();

        // Добавление товара
        $("[id*='name']").setValue(itemName);
        $("[id*='price']").setValue(cost);
        $(".card button").click();

        // Переход на витрину
        $("[href='/']").click();

        // Проверка, что товар появился
        $$(".product-card")
                .findBy(attribute("data-name", itemName))
                .shouldHave(attribute("data-price", cost));
    }

    @Test
    @Tag("UI")
    @DisplayName("Попытаться войти в админку с неверным логином и паролем")
    void failingLoginToAdminPage() {
        $("[href='/admin']").click();

        $("#username").setValue("invalid name");
        $("#password").setValue("invalid pass");
        $("body > div > form > button").click();

        // Проверяем появление алерта
        $(".alert").shouldBe(visible);
    }

    @Test
    @Tag("UI")
    @DisplayName("Добавить товар в корзину и проверить, что он отображается")
    void addItemToCart() {
        SelenideElement itemCard = $x("//*[@id=\"products-list\"]/div").shouldBe(visible);
        String name = itemCard.getAttribute("data-name");

        // Добавляем в корзину
        $x("//*[@id=\"products-list\"]/div/button").click();

        // Открываем корзину
        $("#open-cart-btn").click();

        // Проверяем, что модалка корзины открылась
        $("#cartModal").shouldBe(visible);

        // Проверяем, что товар есть в корзине
        $x("//*[@id=\"cart-items\"]/div/div/b")
                .shouldHave(text(name));

        // Удаляем товар из корзины
        $x("//*[@class=\"cart-item\"]/div/b[text()=\""+name+"\"]/../../button")
                .click();
    }

    @Test
    @Tag("UI")
    @DisplayName("Проверить сохранение товаров в корзине после обновления страницы")
    void rememberItemsInCartWhenPageRefreshed() {
        SelenideElement itemCard = $("#products-list > div").shouldBe(visible);
        String name = itemCard.getAttribute("data-name");

        refresh();
        // Добавляем в корзину
        $("#products-list > div button").click();

        // Открываем корзину
        $("#open-cart-btn").click();
        $("#cartModal").shouldBe(visible);

        // Обновляем страницу
        refresh();

        // Снова открываем корзину
        $("#open-cart-btn").click();

        // Проверяем, что товар есть в корзине
        $x("//*[@id=\"cart-items\"]/div/div/b")
                .shouldHave(text(name));

        // Удаляем товар из корзины
        $x("//*[@class=\"cart-item\"]/div/b[text()=\""+name+"\"]/../../button")
                .click();
    }

    @Test
    @Tag("UI")
    @DisplayName("1.1 Перетащить элемент в корзину с помощью Drag-and-Drop.")
    void ItemToCartDND() {
        //создаем айтем для теста
        GoodsApiAllCodeResponseTest tests = new GoodsApiAllCodeResponseTest();
        String name = tests.uniqueName("DND-test");
        long id = tests.createProduct(name,123.0 );

        refresh();

        SelenideElement itemCard = $x("//*[@id=\"products-list\"]/div[@data-name=\""+name+"\"]").shouldBe(visible);
        SelenideElement CartButton = $("#open-cart-btn").shouldBe(visible);

        actions().clickAndHold(itemCard)
                .pause(100)
                .moveToElement(CartButton)
                .release()
                .pause(1000)
                .perform();

        // Открываем корзину
        $("#open-cart-btn").click();

        // Проверяем, что товар есть в корзине
        $x("//*[@id=\"cart-items\"]/div/div/b")
                .shouldHave(text(name));

        // Удаляем товар из корзины
        $x("//*[@class=\"cart-item\"]/div/b[text()=\""+name+"\"]/../../button")
                .click();
    }

    @Test
    @Tag("UI")
    @DisplayName("1.2 Удалить добавленный элемент из корзины и проверить, что он там больше не отображается.")
    void DeleteItemFromCart()
    {
        //Добавление айтема в корзину
        SelenideElement itemCard = $x("//*[@id=\"products-list\"]/div").shouldBe(visible);
        String name = itemCard.getAttribute("data-name");

        // Добавляем в корзину
        $x("//*[@id=\"products-list\"]/div/button").click();

        // Открываем корзину
        $("#open-cart-btn").click();
        // Проверяем, что модалка корзины открылась
        $("#cartModal").shouldBe(visible);
        // Проверяем, что товар есть в корзине
        $x("//*[@id=\"cart-items\"]/div/div/b")
                .shouldHave(text(name));

        // Удаляем товар из корзины
        $x("//*[@class=\"cart-item\"]/div/b[text()=\""+name+"\"]/../../button")
                .click();

        // Проверяем, что корзина пустая
        assertThat($$x("//*[@id=\"cart-items\"]/div/div/b").size()).isZero();
    }
}