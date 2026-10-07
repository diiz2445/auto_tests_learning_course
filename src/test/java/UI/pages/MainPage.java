package UI.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

/**
 * Page Object главной страницы (витрина с товарами + корзина).
 * Не менее 5 элементов/коллекций + методы взаимодействия.
 */
public class MainPage {

    // ---------- Элементы (≥ 5) ----------

    /** Список карточек товаров */
    public final ElementsCollection productCards = $$("#products-list > div, .product-card");

    /** Кнопка открытия корзины */
    public final SelenideElement openCartButton = $("#open-cart-btn");

    /** Модальное окно корзины */
    public final SelenideElement cartModal = $("#cartModal");

    /** Контейнер позиций в корзине */
    public final SelenideElement cartItems = $("#cart-items");

    /** Коллекция позиций корзины */
    public final ElementsCollection cartItemElements = $$(".cart-item");

    /** Итоговая сумма в корзине */
    public final SelenideElement totalPrice = $("#total-price");

    /** Кнопка оформления заказа */
    public final SelenideElement makeOrderButton = $("#makeOrder");

    /** Ссылка перехода в админку */
    public final SelenideElement adminLink = $("[href='/admin']");

    /** Ссылка на витрину (главную) */
    public final SelenideElement storefrontLink = $("[href='/']");

    /** Контейнер уведомлений (toast) */
    public final SelenideElement toastContainer = $("#toast-container");

    // ---------- Методы взаимодействия ----------

    /**
     * Открыть главную страницу (витрину).
     */
    public MainPage open() {
        com.codeborne.selenide.Selenide.open(api.config.Config.getBaseUrl());
        return this;
    }

    /**
     * Найти карточку товара по имени.
     */
    public SelenideElement productCardByName(String name) {
        return $x("//*[@id='products-list']/div[@data-name='" + name + "']");
    }

    /**
     * Кнопка "В корзину" у конкретного товара с прямый XPathэом до кнопки.
     */
    public SelenideElement addToCartButton(String productName) {
        return $x("//*[@id='products-list']/div[@data-name='" + productName + "']/button");
    }

    /**
     * Добавить товар в корзину по имени
     * Ждём появления карточки и кнопки, затем жмем на кнопку
     */
    public MainPage addProductToCart(String productName) {
        productCardByName(productName).shouldBe(com.codeborne.selenide.Condition.visible);
        addToCartButton(productName)
                .shouldBe(com.codeborne.selenide.Condition.visible)
                .click();
        return this;
    }

    /**
     * Добавить товар в корзину N раз
     */
    public MainPage addProductToCart(String productName, int times) {
        for (int i = 0; i < times; i++) {
            addProductToCart(productName);
        }
        return this;
    }

    /**
     * Открыть модальное окно корзины.
     */
    public MainPage openCart() {
        openCartButton.click();
        return this;
    }

    /**
     * Оформить заказ (кнопка оплаты).
     */
    public MainPage makeOrder() {
        makeOrderButton.click();
        return this;
    }

    /**
     * Перейти в админку.
     */
    public LoginPage goToAdmin() {
        adminLink.click();
        return new LoginPage();
    }

    /**
     * Получить текстовое значение итоговой суммы (только цифры и точка).
     */
    public String getTotalPriceText() {
        return totalPrice.getText().replaceAll("[^0-9.]", "");
    }

    /**
     * Числовое значение итоговой суммы.
     */
    public double getTotalPriceValue() {
        String text = getTotalPriceText();
        return text.isEmpty() ? 0.0 : Double.parseDouble(text);
    }

    /**
     * Последнее (самое свежее) уведомление toast.
     */
    public SelenideElement lastToast() {
        return $("#toast-container > div:nth-last-child(1)");
    }

    /**
     * Удалить позицию из корзины по имени товара.
     */
    public MainPage removeFromCart(String productName) {
        $x("//*[@class='cart-item']//b[text()='" + productName + "']/../../button").click();
        return this;
    }

    /**
     * Обновить страницу и дождаться списка товаров.
     */
    public MainPage refreshPage() {
        refresh();
        $("#products-list").shouldBe(com.codeborne.selenide.Condition.visible);
        return this;
    }
}
