package UI.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;

/**
 * Page Object страницы админки (добавление / редактирование товаров).
 */
public class AdminPage {

    public final SelenideElement addProductCard = $(".card");
    public final SelenideElement nameInput = $("[id*='name']");
    public final SelenideElement priceInput = $("[id*='price']");
    public final SelenideElement addButton = $(".card button");
    public final SelenideElement productsTableBody = $("#tbody");
    public final ElementsCollection tableRows = $$("#tbody tr");
    public final SelenideElement storefrontLink = $("[href='/']");
    public final SelenideElement toastContainer = $("#toast-container");

    @Step("Добавить товар: name={name}, price={price}")
    public AdminPage addProduct(String name, String price) {
        nameInput.setValue(name);
        priceInput.setValue(price);
        addButton.click();
        return this;
    }

    @Step("Найти строку таблицы по id={id}")
    public SelenideElement rowById(long id) {
        return $x("//*[@id='tbody']/tr/td[text()=\"" + id + "\"]/..");
    }

    @Step("Поле имени в строке id={id}")
    public SelenideElement nameInputInRow(long id) {
        return rowById(id).$("input[type='text']");
    }

    @Step("Поле цены в строке id={id}")
    public SelenideElement priceInputInRow(long id) {
        return rowById(id).$("input[type='number']");
    }

    @Step("Кнопка update в строке id={id}")
    public SelenideElement updateButtonInRow(long id) {
        return rowById(id).$("button[data-action='update']");
    }

    @Step("Отредактировать товар id={id}: name={newName}, price={newPrice}")
    public AdminPage editProduct(long id, String newName, String newPrice) {
        nameInputInRow(id).setValue(newName);
        priceInputInRow(id).setValue(newPrice);
        updateButtonInRow(id).click();
        return this;
    }

    @Step("Перейти на витрину")
    public MainPage goToStorefront() {
        storefrontLink.click();
        return new MainPage();
    }

    @Step("Получить последнее toast-уведомление")
    public SelenideElement lastToast() {
        return $("#toast-container > div:nth-last-child(1)");
    }
}
