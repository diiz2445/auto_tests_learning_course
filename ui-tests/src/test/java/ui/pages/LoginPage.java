package ui.pages;

import com.codeborne.selenide.SelenideElement;
import common.config.Config;
import io.qameta.allure.Step;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    public final SelenideElement usernameInput = $("#username");
    public final SelenideElement passwordInput = $("#password");
    public final SelenideElement submitButton = $("body > div > form > button");
    public final SelenideElement alert = $(".alert");

    @Step("Ввести логин: {username}")
    public LoginPage enterUsername(String username) { usernameInput.setValue(username); return this; }

    @Step("Ввести пароль")
    public LoginPage enterPassword(String password) { passwordInput.setValue(password); return this; }

    @Step("Нажать кнопку входа")
    public LoginPage submit() { submitButton.click(); return this; }

    @Step("Войти как администратор")
    public AdminPage loginAsAdmin() { return login(Config.getUsername(), Config.getPassword()); }

    @Step("Войти с логином [{username}]")
    public AdminPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        submit();
        return new AdminPage();
    }
}
