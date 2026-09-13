package api.model;

import java.util.Optional;

public class ProductRequest {

    /** Название товара. Обязательное поле для API. */
    private String name;

    /** Цена товара. Не может быть отрицательной (по спецификации >= 0). */
    private double price;

    private String priceStr;

    /**
     * Пустой конструктор — обязателен для Jackson при десериализации.
     */
    public ProductRequest() {
    }

    /**
     * Конструктор для удобного создания объекта в тестах.
     *
     * @param name  название товара
     * @param price цена товара
     */
    public ProductRequest(String name, Double price) {
        this.name = name;
        this.price = price;
    }
    public ProductRequest(String name, String price) {
        this.name = name;
        this.priceStr = price;
    }

    // =================================================================
    //  Аксессоры (getters / setters)
    // =================================================================
    // Сеттеры использует Jackson при чтении JSON, геттеры — при
    // сериализации Java-объекта обратно в JSON.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // =================================================================
    //  Служебные методы для читаемых сообщений об ошибках в AssertJ
    // =================================================================

    @Override
    public String toString() {
        return "ProductRequest{name='" + name + "', price=" + price + '}';
    }
}
