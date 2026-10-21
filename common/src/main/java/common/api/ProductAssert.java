package common.api;

import common.model.Product;
import io.qameta.allure.Step;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;

public class ProductAssert extends AbstractAssert<ProductAssert, Product> {
    public ProductAssert(Product actual) { super(actual, ProductAssert.class); }
    public static ProductAssert assertThat(Product actual) { return new ProductAssert(actual); }

    @Override
    @Step("API-проверка: товар не null")
    public ProductAssert isNotNull() { super.isNotNull(); return this; }

    @Step("API-проверка: id товара = {expected}")
    public ProductAssert hasId(Long expected) {
        isNotNull();
        info.overridingErrorMessage(String.format("Ожидали id=[%s], получили [%s]", expected, actual.getId()));
        objects.assertEqual(info, actual.getId(), expected);
        return this;
    }

    @Step("API-проверка: название товара = {expected}")
    public ProductAssert hasName(String expected) {
        isNotNull();
        info.overridingErrorMessage(String.format("Ожидали name=[%s], получили [%s]", expected, actual.getName()));
        objects.assertEqual(info, actual.getName(), expected);
        return this;
    }

    @Step("API-проверка: цена = {expected} (offset {offset})")
    public ProductAssert hasPrice(double expected, double offset) {
        isNotNull();
        info.overridingErrorMessage(String.format("Ожидали price=[%s]±[%s], получили [%s]", expected, offset, actual.getPrice()));
        Assertions.assertThat(actual.getPrice()).isCloseTo(expected, Offset.offset(offset));
        return this;
    }

    @Step("API-проверка: цена = {expected}")
    public ProductAssert hasPrice(double expected) { return hasPrice(expected, 0.0); }
}
