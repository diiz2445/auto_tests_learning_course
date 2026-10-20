package UI.asserts;

import UI.pages.MainPage;
import io.qameta.allure.Step;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import static com.codeborne.selenide.Condition.*;

/**
 * Assert-класс для главной страницы (витрина + корзина).
 */
public class MainPageAssert extends AbstractAssert<MainPageAssert, MainPage> {

    public MainPageAssert(MainPage actual) {
        super(actual, MainPageAssert.class);
    }

    public static MainPageAssert assertThat(MainPage actual) {
        return new MainPageAssert(actual);
    }

    @Step("UI-проверка: элемент виден")
    public MainPageAssert elementIsVisible(com.codeborne.selenide.SelenideElement element) {
        element.shouldBe(visible);
        return this;
    }

    @Step("UI-проверка: элемент содержит текст «{text}»")
    public MainPageAssert elementContainsText(com.codeborne.selenide.SelenideElement element, String text) {
        element.shouldHave(text(text));
        return this;
    }

    @Step("UI-проверка: корзина открыта")
    public MainPageAssert cartIsOpen() {
        actual.cartModal.shouldBe(visible);
        return this;
    }

    @Step("UI-проверка: в корзине есть товар «{productName}»")
    public MainPageAssert cartContainsProduct(String productName) {
        actual.cartItems.shouldHave(text(productName));
        return this;
    }

    @Step("UI-проверка: итоговая сумма ≤ {max}")
    public MainPageAssert totalPriceIsLessThanOrEqualTo(double max) {
        double total = actual.getTotalPriceValue();
        Assertions.assertThat(total)
                .as("Общая стоимость не должна превышать %.2f", max)
                .isLessThanOrEqualTo(max);
        return this;
    }

    @Step("UI-проверка: итоговая сумма равна {expected}")
    public MainPageAssert totalPriceIsEqualTo(double expected) {
        double total = actual.getTotalPriceValue();
        Assertions.assertThat(total)
                .as("Итоговая сумма в корзине должна быть %.2f", expected)
                .isEqualTo(expected);
        return this;
    }

    @Step("UI-проверка: показано уведомление «Заказ принят в обработку!»")
    public MainPageAssert orderAcceptedNotificationIsShown() {
        actual.lastToast().shouldBe(text("Заказ принят в обработку!"));
        return this;
    }

    @Step("UI-проверка: на витрине товар «{name}» с ценой {price}")
    public MainPageAssert productIsDisplayed(String name, String price) {
        actual.productCardByName(name)
                .shouldBe(visible)
                .shouldHave(attribute("data-price", price));
        return this;
    }

    @Step("UI-проверка: корзина пуста")
    public MainPageAssert cartIsEmpty() {
        Assertions.assertThat(actual.cartItemElements.size()).isZero();
        return this;
    }
}
