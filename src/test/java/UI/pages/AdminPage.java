package UI.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

/**
 * Page Object страницы админки (добавление / редактирование товаров).
 * Нужен для сценариев 2.3 и 2.4.
 */
public class AdminPage {

    /** Карточка формы добавления товара */
    public final SelenideElement addProductCard = $(".card");

    /** Поле имени нового товара */
    public final SelenideElement nameInput = $("[id*='name']");

    /** Поле цены нового товара */
    public final SelenideElement priceInput = $("[id*='price']");

    /** Кнопка добавления товара */
    public final SelenideElement addButton = $(".card button");

    /** Таблица товаров (tbody) */
    public final SelenideElement productsTableBody = $("#tbody");

    /** Строки таблицы товаров */
    public final ElementsCollection tableRows = $$("#tbody tr");

    /** Ссылка на витрину */
    public final SelenideElement storefrontLink = $("[href='/']");

    /** Контейнер toast-уведомлений */
    public final SelenideElement toastContainer = $("#toast-container");

    /**
     * Заполнить и отправить форму добавления товара.
     */
    public AdminPage addProduct(String name, String price) {
        nameInput.setValue(name);
        priceInput.setValue(price);
        addButton.click();
        return this;
    }

    /**
     * Найти строку таблицы по id (первая колонка — id).
     */
    public SelenideElement rowById(long id) {
        return $x("//*[@id='tbody']/tr/td[text()=\"" + id + "\"]/..");
        //*[@id='tbody']/tr/td[text()="38"]/../
    }

    /**
     * Поле имени в строке с указанным id.
     */
    public SelenideElement nameInputInRow(long id) {
        return rowById(id).$("input[type='text']");
    }

    /**
     * Поле цены в строке с указанным id.
     */
    public SelenideElement priceInputInRow(long id) {
        return rowById(id).$("input[type='number']");
    }

    /**
     * Кнопка update в строке.
     */
    public SelenideElement updateButtonInRow(long id) {
        return rowById(id).$("button[data-action='update']");
    }

    /**
     * Отредактировать товар с id=1 (или другим) и сохранить.
     */
    public AdminPage editProduct(long id, String newName, String newPrice) {
        nameInputInRow(id).setValue(newName);
        priceInputInRow(id).setValue(newPrice);
        updateButtonInRow(id).click();
        return this;
    }

    /**
     * Перейти на витрину.
     */
    public MainPage goToStorefront() {
        storefrontLink.click();
        return new MainPage();
    }

    /**
     * Последнее toast-уведомление.
     */
    public SelenideElement lastToast() {
        return $("#toast-container > div:nth-last-child(1)");
    }
}
