package UI.asserts;

import UI.pages.LoginPage;
import org.assertj.core.api.AbstractAssert;

import static com.codeborne.selenide.Condition.*;

/**
 * Assert-класс для формы авторизации.
 * Базовые проверки: элемент видно, текстовое поле содержит текст.
 */
public class LoginPageAssert extends AbstractAssert<LoginPageAssert, LoginPage> {

    public LoginPageAssert(LoginPage actual) {
        super(actual, LoginPageAssert.class);
    }

    public static LoginPageAssert assertThat(LoginPage actual) {
        return new LoginPageAssert(actual);
    }

    /** Поле логина видно */
    public LoginPageAssert usernameIsVisible() {
        actual.usernameInput.shouldBe(visible);
        return this;
    }

    /** Поле пароля видно */
    public LoginPageAssert passwordIsVisible() {
        actual.passwordInput.shouldBe(visible);
        return this;
    }

    /** Кнопка «Войти» видна */
    public LoginPageAssert submitIsVisible() {
        actual.submitButton.shouldBe(visible);
        return this;
    }

    /** Поле логина содержит указанный текст */
    public LoginPageAssert usernameContains(String text) {
        actual.usernameInput.shouldHave(value(text));
        return this;
    }

    /** Поле пароля содержит указанный текст */
    public LoginPageAssert passwordContains(String text) {
        actual.passwordInput.shouldHave(value(text));
        return this;
    }

    /** Отображается алерт (ошибка авторизации) */
    public LoginPageAssert alertIsVisible() {
        actual.alert.shouldBe(visible);
        return this;
    }

    /** Все основные элементы формы видны */
    public LoginPageAssert formIsDisplayed() {
        return usernameIsVisible()
                .passwordIsVisible()
                .submitIsVisible();
    }
}
