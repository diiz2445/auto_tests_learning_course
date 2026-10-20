package UI.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

/**
 * Page Object страницы логина в админку.
 */
public class LoginPage {

    public final SelenideElement usernameInput = $("#username");
    public final SelenideElement passwordInput = $("#password");
    public final SelenideElement submitButton = $("body > div > form > button");
    public final SelenideElement alert = $(".alert");

    @Step("Ввести логин: {username}")
    public LoginPage enterUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Ввести пароль")
    public LoginPage enterPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Нажать кнопку входа")
    public LoginPage submit() {
        submitButton.click();
        return this;
    }

    @Step("Войти в админку как администратор")
    public AdminPage loginAsAdmin() {
        String username = api.config.Config.getUsername();
        String password = api.config.Config.getPassword();
        enterUsername(username);
        enterPassword(password);
        submit();
        return new AdminPage();
    }

    @Step("Войти с логином [{username}] и паролем")
    public AdminPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        submit();
        return new AdminPage();
    }
}
