package UI.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

/**
 * Page Object формы авторизации в админке.
 * Полная функциональность: логин, пароль, кнопка «Войти».
 */
public class LoginPage {

    /** Поле логина */
    public final SelenideElement usernameInput = $("#username");

    /** Поле пароля */
    public final SelenideElement passwordInput = $("#password");

    /** Кнопка «Войти» */
    public final SelenideElement submitButton = $("body > div > form > button");

    /** Алерт (ошибка авторизации и т.п.) */
    public final SelenideElement alert = $(".alert");

    /**
     * Ввести логин.
     */
    public LoginPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    /**
     * Ввести пароль.
     */
    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    /**
     * Нажать «Войти».
     */
    public LoginPage clickSubmit() {
        submitButton.click();
        return this;
    }

    /**
     * Полный логин (успешный сценарий → страница админки).
     */
    public AdminPage login(String username, String password) {
        setUsername(username);
        setPassword(password);
        clickSubmit();
        return new AdminPage();
    }

    /**
     * Логин с credentials из Config.
     */
    public AdminPage loginAsAdmin() {
        return login(api.config.Config.getUsername(), api.config.Config.getPassword());
    }

    /**
     * Попытка логина с неверными данными (остаёмся на форме).
     */
    public LoginPage loginExpectingFailure(String username, String password) {
        setUsername(username);
        setPassword(password);
        clickSubmit();
        return this;
    }
}
