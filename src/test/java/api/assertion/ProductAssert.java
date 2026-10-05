package api.assertion;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import api.model.Product;


public class ProductAssert extends AbstractAssert<ProductAssert, Product> {

    /**
     * Конструктор, вызываемый из фабричного метода.
     *
     * @param actual проверяемый товар (может быть null — это валидно,
     *               просто упадут проверки isNotNull и т.п.)
     */
    public ProductAssert(Product actual) {
        super(actual, ProductAssert.class);
    }
    public static ProductAssert assertThat(Product actual) {
        return new ProductAssert(actual);
    }
    @Override
    public ProductAssert isNotNull() {
        super.isNotNull();
        return this;
    }
    public ProductAssert hasId(Long expected) {
        isNotNull();
        // info.overridingErrorMessage(...) — подменяем стандартное сообщение
        // AssertJ своим, более понятным. ВАЖНО: в этой версии AssertJ метод
        // принимает ТОЛЬКО готовую строку (без формат-аргументов), поэтому
        // строку форматируем сами через String.format.
        info.overridingErrorMessage(
                String.format("Ожидали id товара = [%s], но он = [%s]", expected, actual.getId()));
        objects.assertEqual(info, actual.getId(), expected);
        return this;
    }
    public ProductAssert hasName(String expected) {
        isNotNull();
        info.overridingErrorMessage(
                String.format("Ожидали название товара = [%s], но получили [%s]",
                        expected, actual.getName()));
        objects.assertEqual(info, actual.getName(), expected);
        return this;
    }
    public ProductAssert hasPrice(double expected, double offset) {
        isNotNull();
        info.overridingErrorMessage(
                String.format("Ожидали цену товара = [%s] (погрешность [%s]), но получили [%s]",
                        expected, offset, actual.getPrice()));
        Assertions.assertThat(actual.getPrice())
                .isCloseTo(expected, Offset.offset(offset));
        return this;
    }
    public ProductAssert hasPrice(double expected) {
        return hasPrice(expected, 0.0);
    }
}