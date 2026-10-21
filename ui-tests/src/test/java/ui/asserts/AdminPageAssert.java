package ui.asserts;

import io.qameta.allure.Step;
import org.assertj.core.api.AbstractAssert;
import ui.pages.AdminPage;
import static com.codeborne.selenide.Condition.*;

public class AdminPageAssert extends AbstractAssert<AdminPageAssert, AdminPage> {
    public AdminPageAssert(AdminPage actual) { super(actual, AdminPageAssert.class); }
    public static AdminPageAssert assertThat(AdminPage actual) { return new AdminPageAssert(actual); }

    @Step("UI-проверка: форма добавления видна")
    public AdminPageAssert addFormIsVisible() { actual.addProductCard.shouldBe(visible); return this; }

    @Step("UI-проверка: уведомление «Товар успешно добавлен!»")
    public AdminPageAssert productAddedNotificationIsShown() {
        actual.lastToast().shouldBe(text("Товар успешно добавлен!")); return this;
    }

    @Step("UI-проверка: элемент виден")
    public AdminPageAssert elementIsVisible(com.codeborne.selenide.SelenideElement element) {
        element.shouldBe(visible); return this;
    }

    @Step("UI-проверка: элемент содержит «{text}»")
    public AdminPageAssert elementContainsText(com.codeborne.selenide.SelenideElement element, String text) {
        element.shouldHave(text(text)); return this;
    }
}
