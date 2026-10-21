package ui.asserts;

import io.qameta.allure.Step;
import org.assertj.core.api.AbstractAssert;
import ui.pages.LoginPage;
import static com.codeborne.selenide.Condition.*;

public class LoginPageAssert extends AbstractAssert<LoginPageAssert, LoginPage> {
    public LoginPageAssert(LoginPage actual) { super(actual, LoginPageAssert.class); }
    public static LoginPageAssert assertThat(LoginPage actual) { return new LoginPageAssert(actual); }

    @Step("UI-проверка: поле логина видно")
    public LoginPageAssert usernameIsVisible() { actual.usernameInput.shouldBe(visible); return this; }

    @Step("UI-проверка: поле пароля видно")
    public LoginPageAssert passwordIsVisible() { actual.passwordInput.shouldBe(visible); return this; }

    @Step("UI-проверка: кнопка входа видна")
    public LoginPageAssert submitIsVisible() { actual.submitButton.shouldBe(visible); return this; }

    @Step("UI-проверка: логин содержит «{text}»")
    public LoginPageAssert usernameContains(String text) { actual.usernameInput.shouldHave(value(text)); return this; }

    @Step("UI-проверка: пароль содержит «{text}»")
    public LoginPageAssert passwordContains(String text) { actual.passwordInput.shouldHave(value(text)); return this; }

    @Step("UI-проверка: алерт ошибки виден")
    public LoginPageAssert alertIsVisible() { actual.alert.shouldBe(visible); return this; }

    @Step("UI-проверка: форма логина отображается")
    public LoginPageAssert formIsDisplayed() {
        return usernameIsVisible().passwordIsVisible().submitIsVisible();
    }
}
