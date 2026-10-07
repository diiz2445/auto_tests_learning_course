package UI.asserts;

import UI.pages.AdminPage;
import org.assertj.core.api.AbstractAssert;

import static com.codeborne.selenide.Condition.*;

/**
 * Assert-класс для страницы админки.
 * Проверки уведомлений и видимости элементов.
 */
public class AdminPageAssert extends AbstractAssert<AdminPageAssert, AdminPage> {

    public AdminPageAssert(AdminPage actual) {
        super(actual, AdminPageAssert.class);
    }

    public static AdminPageAssert assertThat(AdminPage actual) {
        return new AdminPageAssert(actual);
    }

    /** Форма добавления товара видна */
    public AdminPageAssert addFormIsVisible() {
        actual.addProductCard.shouldBe(visible);
        return this;
    }

    /** Уведомление об успешном добавлении товара */
    public AdminPageAssert productAddedNotificationIsShown() {
        actual.lastToast().shouldBe(text("Товар успешно добавлен!"));
        return this;
    }

    /** Элемент виден */
    public AdminPageAssert elementIsVisible(com.codeborne.selenide.SelenideElement element) {
        element.shouldBe(visible);
        return this;
    }

    /** Элемент содержит текст */
    public AdminPageAssert elementContainsText(com.codeborne.selenide.SelenideElement element, String text) {
        element.shouldHave(text(text));
        return this;
    }
}
