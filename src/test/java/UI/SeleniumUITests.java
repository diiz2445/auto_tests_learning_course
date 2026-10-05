package UI;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;
import java.util.Random;
import java.util.UUID;

public class SeleniumUITests {
    public static final String USERNAME =
            System.getProperty("api.username", "admin");

    public static final String PASSWORD =
            System.getProperty("api.password", "secret123");

    private static final String BASE_URI =
            System.getProperty("api.base.uri", "http://127.0.0.1");

    private static final int PORT =
            Integer.parseInt(System.getProperty("api.port", "8080"));

    ChromeDriver driver;
    Random random = new Random();

    @BeforeEach
    void setUp()
    {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(BASE_URI+":"+PORT);

    }

    @AfterEach
    void close()
    {
        driver.close();
    }


    @DisplayName("Авторизация")
    private ChromeDriver LoginToAdminPage(ChromeDriver driver)
    {
        driver.findElement(By.cssSelector("[href=\"/admin\"]")).click();

        //Логинимся под учеткой админа
        driver.findElement(By.cssSelector("#username"))
                .sendKeys(USERNAME);
        driver.findElement(By.cssSelector("#password"))
                .sendKeys(PASSWORD);
        driver.findElement(By.cssSelector("body > div > form > button")).click();

        // Без ожидания не успевает подгружаться страница
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(@class,'card')]")
        ));
        return driver;
    }

    @Test
    @Tag("UI")
    @DisplayName("Добавить товар через админку, выйти на витрину и проверить, что товар отображается")
    public void AddingItemAndCheck() throws InterruptedException {
        String ItemName = UUID.randomUUID().toString().replace("-", "");
        String Cost = Integer.toString(random.nextInt(300));

        //логинимся в админку
        driver = LoginToAdminPage(this.driver);

        //Добавление айтема
        driver.findElement(By.xpath("//*[contains(@id,\"name\")]"))
                .sendKeys(ItemName);
        driver.findElement(By.xpath("//*[contains(@id,\"price\")]"))
                .sendKeys(Cost);
        driver.findElement(By.xpath("//*[@class=\"card\"]/button")).click();

        //Проверка добавления
        driver.findElement(By.cssSelector("[href=\"/\"")).click();
        Assertions.assertThat(driver.findElements(
                        By.xpath("//*[@class='product-card'][@data-name='" + ItemName + "'][@data-price='" + Cost + "']")
                ))
                .as("Товар с именем '%s' и ценой '%s' должен быть на странице", ItemName, Cost)
                .isNotEmpty();
    }

    @Test
    @Tag("UI")
    @DisplayName("Попытаться войти в админку с неверным логином и паролем")
    public void FailingLoginToAdminPage() {
        driver.findElement(By.cssSelector("[href=\"/admin\"]")).click();

        //Логинимся под учеткой админа
        driver.findElement(By.cssSelector("#username"))
                .sendKeys("invalid name");
        driver.findElement(By.cssSelector("#password"))
                .sendKeys("invalid pass");
        driver.findElement(By.cssSelector("body > div > form > button")).click();

        //Таймер для ожидания появления уведомления
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

        WebElement formCard = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(@class,\"alert\")]")
        ));
        Assertions.assertThat(driver.findElement(By.xpath("//*[contains(@class,\"alert\")]")))
                .as("Должно появиться уведомление о неверных данных УЗ");
    }

    @Test
    @Tag("UI")
    @DisplayName("Добавить товар в корзину и проверить, что он отображается")
    public void AddItemToCart(){
        WebElement ItemCard = driver.findElement(By.xpath("//*[@id=\"products-list\"]/div"));
        String name = ItemCard.getAttribute("data-name");
        //добавление в корзину
        driver.findElement(By.xpath("//*[@id=\"products-list\"]/div/button")).click();
        //проверка отображения в корзине
        driver.findElement(By.xpath("//*[@id=\"open-cart-btn\"]")).click();
        //Проверим, что по клику открылась корзина
        WebElement Cart = driver.findElement(By.xpath("//*[@id=\"cartModal\"]"));

        Assertions.assertThat(Cart.getCssValue("display"))
                .as("Элемент должен быть видимым (display: block)")
                .isEqualTo("block");

        WebElement ItemInCart = Cart.findElement(By.xpath("//*[@id=\"cart-items\"]/div/div/b[text()=\""+name+"\"]"));
        Cart.findElement(By.xpath("//*[@id=\"cart-items\"]/div/div/b[text()=\""+name+"\"]/../../button")).click();

    }

    @Test
    @Tag("UI")
    @DisplayName("Проверить сохранение товаров в корзине после обновления страницы")
    public void RememberItemsInCartWhenPageRefreshed(){
        WebElement ItemCard = driver.findElement(By.xpath("//*[@id=\"products-list\"]/div"));
        String name = ItemCard.getAttribute("data-name");
        //добавление в корзину
        driver.findElement(By.xpath("//*[@id=\"products-list\"]/div/button")).click();
        //проверка отображения в корзине
        driver.findElement(By.cssSelector("#open-cart-btn")).click();
        //Обновим старинцу
        driver.navigate().refresh();


        driver.findElement(By.cssSelector("#open-cart-btn")).click();
        //Проверим, что по клику открылась корзина
        WebElement Cart = driver.findElement(By.xpath("//*[@id=\"cartModal\"]"));

        Assertions.assertThat(Cart.getCssValue("display"))
                .as("Элемент должен быть видимым (display: block)")
                .isEqualTo("block");

        WebElement ItemInCart = Cart.findElement(By.xpath("//*[@id=\"cart-items\"]/div/div/b[text()=\""+name+"\"]"));
    }
}
