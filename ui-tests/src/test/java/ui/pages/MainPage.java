package ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import common.config.Config;
import io.qameta.allure.Step;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {
    public final ElementsCollection productCards = $$("#products-list > div, .product-card");
    public final SelenideElement openCartButton = $("#open-cart-btn");
    public final SelenideElement cartModal = $("#cartModal");
    public final SelenideElement cartItems = $("#cart-items");
    public final ElementsCollection cartItemElements = $$(".cart-item");
    public final SelenideElement totalPrice = $("#total-price");
    public final SelenideElement makeOrderButton = $("#makeOrder");
    public final SelenideElement adminLink = $("[href='/admin']");
    public final SelenideElement storefrontLink = $("[href='/']");
    public final SelenideElement toastContainer = $("#toast-container");

    @Step("Открыть главную страницу (витрину)")
    public MainPage open() {
        com.codeborne.selenide.Selenide.open(Config.getBaseUrl());
        return this;
    }

    @Step("Найти карточку товара по имени: {name}")
    public SelenideElement productCardByName(String name) {
        return $x("//*[@id='products-list']/div[@data-name='" + name + "']");
    }

    @Step("Кнопка «В корзину» у товара: {productName}")
    public SelenideElement addToCartButton(String productName) {
        return $x("//*[@id='products-list']/div[@data-name='" + productName + "']/button");
    }

    @Step("Добавить товар в корзину: {productName}")
    public MainPage addProductToCart(String productName) {
        productCardByName(productName).shouldBe(com.codeborne.selenide.Condition.visible);
        addToCartButton(productName).shouldBe(com.codeborne.selenide.Condition.visible).click();
        return this;
    }

    @Step("Добавить товар в корзину {times} раз: {productName}")
    public MainPage addProductToCart(String productName, int times) {
        for (int i = 0; i < times; i++) addProductToCart(productName);
        return this;
    }

    @Step("Открыть модальное окно корзины")
    public MainPage openCart() { openCartButton.click(); return this; }

    @Step("Оформить заказ")
    public MainPage makeOrder() { makeOrderButton.click(); return this; }

    @Step("Перейти в админку")
    public LoginPage goToAdmin() { adminLink.click(); return new LoginPage(); }

    @Step("Текст итоговой суммы")
    public String getTotalPriceText() { return totalPrice.getText().replaceAll("[^0-9.]", ""); }

    @Step("Числовое значение итоговой суммы")
    public double getTotalPriceValue() {
        String text = getTotalPriceText();
        return text.isEmpty() ? 0.0 : Double.parseDouble(text);
    }

    @Step("Последнее toast-уведомление")
    public SelenideElement lastToast() { return $("#toast-container > div:nth-last-child(1)"); }

    @Step("Удалить из корзины: {productName}")
    public MainPage removeFromCart(String productName) {
        $x("//*[@class='cart-item']//b[text()='" + productName + "']/../../button").click();
        return this;
    }

    @Step("Обновить страницу")
    public MainPage refreshPage() {
        refresh();
        $("#products-list").shouldBe(com.codeborne.selenide.Condition.visible);
        return this;
    }
}
