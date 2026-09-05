import org.example.First_task;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static java.util.Collections.reverse;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {

    private final Random random = new Random();

    // ==================== 1. Тест реверса (Задача 1) ====================
    @Tag("smoke")
    @RepeatedTest(10)
    void testReverse() {
        String[] input = {"A", "B", "C", "D"};
        String[] actual = First_task.reverse(input);
        String[] expected = input;
        reverse(Arrays.asList(expected));

        assertArrayEquals(expected, actual,
                () -> "reverse: ожидалось " + Arrays.toString(expected) +
                        ", получено " + Arrays.toString(actual));
    }
    // ==================== 2. isEven (boolean) ====================
    @Tag("smoke")
    @Tag("boolean")
    @RepeatedTest(10)
    void testIsEven() {
        int n = random.nextInt(100) + 1;
        boolean actual = First_task.isEven(n);
        boolean expected = n % 2 == 0;

        assertEquals(expected, actual,
                () -> "isEven(" + n + "): ожидалось " + expected + ", получено " + actual);
    }

    // ==================== 3. checkAccess ====================
    @Tag("smoke")
    @RepeatedTest(10)
    void testCheckAccess() {
        int age = random.nextInt(100);
        String actual = First_task.checkAccess(age);
        String expected = age > 18 ? "Allowed" : "Denied";

        assertEquals(expected, actual,
                () -> "checkAccess(" + age + "): ожидалось \"" + expected + "\", получено \"" + actual + "\"");
    }

    // ==================== 4. isPositive (boolean) ====================
    @Tag("boolean")
    @Tag("smoke")
    @RepeatedTest(10)
    void testIsPositive() {
        int n = random.nextInt(201) - 100;
        boolean actual = First_task.isPositive(n);
        boolean expected = n >= 0;

        assertEquals(expected, actual,
                () -> "isPositive(" + n + "): ожидалось " + expected + ", получено " + actual);
    }
    // ==================== 5. Намеренно падающий ассерт (Задача 1) ====================
    @Tag("failing")
    @Test
    void testIntentionallyFailing() {
        String actual = First_task.failingCheckAccess(25);
        String expected = "Allowed"; // специально неверное значение

        assertEquals(expected, actual,
                () -> "НАМЕРЕННОЕ ПАДЕНИЕ checkAccess(25): ожидалось \"" + expected +
                        "\", получено \"" + actual + "\"");
    }
}
