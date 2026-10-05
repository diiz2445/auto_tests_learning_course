package api.model;

import java.util.List;


public class ProductList {

    /** Товары на запрошенной странице. */
    private List<Product> goods;

    /** Пустой конструктор — обязателен для Jackson. */
    public ProductList() {
    }

    // =================================================================
    //  Аксессоры (getters / setters)
    // =================================================================

    public List<Product> getGoods() {
        return goods;
    }

    public void setGoods(List<Product> goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "ProductList{goods.size=" + (goods == null ? 0 : goods.size()) + '}';
    }
}