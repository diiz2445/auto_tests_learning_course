package ui.asserts;

import io.qameta.allure.Step;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import ui.pages.MainPage;
import static com.codeborne.selenide.Condition.*;

public class MainPageAssert extends AbstractAssert<MainPageAssert, MainPage> {
    public MainPageAssert(MainPage actual) { super(actual, MainPageAssert.class); }
    public static MainPageAssert assertThat(MainPage actual) { return new MainPageAssert(actual); }

    @Step("UI-проверка: элемент виден")
    public MainPageAssert elementIsVisible(com.codeborne.selenide.SelenideElement element) {
        element.shouldBe(visible); return this;
    }

    @Step("UI-проверка: элемент содержит «{text}»")
    public MainPageAssert elementContainsText(com.codeborne.selenide.SelenideElement element, String text) {
        element.shouldHave(text(text)); return this;
    }

    @Step("UI-проверка: корзина открыта")
    public MainPageAssert cartIsOpen() { actual.cartModal.shouldBe(visible); return this; }

    @Step("UI-проверка: в корзине «{productName}»")
    public MainPageAssert cartContainsProduct(String productName) {
        actual.cartItems.shouldHave(text(productName)); return this;
    }

    @Step("UI-проверка: итоговая сумма ≤ {max}")
    public MainPageAssert totalPriceIsLessThanOrEqualTo(double max) {
        Assertions.assertThat(actual.getTotalPriceValue())
                .as("Общая стоимость ≤ %.2f", max).isLessThanOrEqualTo(max);
        return this;
    }

    @Step("UI-проверка: итоговая сумма = {expected}")
    public MainPageAssert totalPriceIsEqualTo(double expected) {
        Assertions.assertThat(actual.getTotalPriceValue())
                .as("Итоговая сумма = %.2f", expected).isEqualTo(expected);
        return this;
    }

    @Step("UI-проверка: уведомление «Заказ принят в обработку!»")
    public MainPageAssert orderAcceptedNotificationIsShown() {
        actual.lastToast().shouldBe(text("Заказ принят в обработку!")); return this;
    }

    @Step("UI-проверка: на витрине «{name}» цена {price}")
    public MainPageAssert productIsDisplayed(String name, String price) {
        actual.productCardByName(name).shouldBe(visible).shouldHave(attribute("data-price", price));
        return this;
    }

    @Step("UI-проверка: корзина пуста")
    public MainPageAssert cartIsEmpty() {
        Assertions.assertThat(actual.cartItemElements.size()).isZero(); return this;
    }
}
