package UI.asserts;

import UI.pages.MainPage;
import org.assertj.core.api.AbstractAssert;

import static com.codeborne.selenide.Condition.*;
import org.assertj.core.api.Assertions;

/**
 * Assert-класс для главной страницы (витрина + корзина).
 * Все проверки сценариев 2.1 / 2.2 организованы здесь.
 */
public class MainPageAssert extends AbstractAssert<MainPageAssert, MainPage> {

    public MainPageAssert(MainPage actual) {
        super(actual, MainPageAssert.class);
    }

    public static MainPageAssert assertThat(MainPage actual) {
        return new MainPageAssert(actual);
    }

    /** Элемент виден */
    public MainPageAssert elementIsVisible(com.codeborne.selenide.SelenideElement element) {
        element.shouldBe(visible);
        return this;
    }

    /** Текстовое поле / элемент содержит текст */
    public MainPageAssert elementContainsText(com.codeborne.selenide.SelenideElement element, String text) {
        element.shouldHave(text(text));
        return this;
    }

    /** Корзина открыта */
    public MainPageAssert cartIsOpen() {
        actual.cartModal.shouldBe(visible);
        return this;
    }

    /** В корзине есть товар с указанным именем */
    public MainPageAssert cartContainsProduct(String productName) {
        actual.cartItems.shouldHave(text(productName));
        return this;
    }

    /** Итоговая сумма ≤ max */
    public MainPageAssert totalPriceIsLessThanOrEqualTo(double max) {
        double total = actual.getTotalPriceValue();
        Assertions.assertThat(total)
                .as("Общая стоимость не должна превышать %.2f", max)
                .isLessThanOrEqualTo(max);
        return this;
    }

    /** Итоговая сумма равна expected */
    public MainPageAssert totalPriceIsEqualTo(double expected) {
        double total = actual.getTotalPriceValue();
        Assertions.assertThat(total)
                .as("Итоговая сумма в корзине должна быть %.2f", expected)
                .isEqualTo(expected);
        return this;
    }

    /** Уведомление об обработке заказа */
    public MainPageAssert orderAcceptedNotificationIsShown() {
        actual.lastToast().shouldBe(text("Заказ принят в обработку!"));
        return this;
    }

    /** На витрине отображается товар с заданным именем и ценой */
    public MainPageAssert productIsDisplayed(String name, String price) {
        actual.productCardByName(name)
                .shouldBe(visible)
                .shouldHave(attribute("data-price", price));
        return this;
    }

    /** Корзина пуста */
    public MainPageAssert cartIsEmpty() {
        Assertions.assertThat(actual.cartItemElements.size()).isZero();
        return this;
    }
}
