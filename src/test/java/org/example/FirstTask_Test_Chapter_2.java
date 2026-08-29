package org.example;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Автотесты с информативными ассертами.
 * - boolean и List методы покрыты
 * - есть намеренно падающий ассерт
 * - каждый тест запускается ≥ 10 раз
 * - фильтрация через @Tag
 */
public class FirstTask_Test_Chapter_2 {

    private final Random random = new Random();

    // ==================== 1. isEven (boolean) ====================
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

    // ==================== 2. checkAccess ====================
    @Tag("smoke")
    @RepeatedTest(10)
    void testCheckAccess() {
        int age = random.nextInt(100);
        String actual = First_task.checkAccess(age);
        String expected = age > 18 ? "Allowed" : "Denied";

        assertEquals(expected, actual,
                () -> "checkAccess(" + age + "): ожидалось \"" + expected + "\", получено \"" + actual + "\"");
    }

    // ==================== 3. isPositive (boolean) ====================
    @Tag("boolean")
    @RepeatedTest(10)
    void testIsPositive() {
        int n = random.nextInt(201) - 100;
        boolean actual = First_task.isPositive(n);
        boolean expected = n >= 0;

        assertEquals(expected, actual,
                () -> "isPositive(" + n + "): ожидалось " + expected + ", получено " + actual);
    }

    // ==================== 4. getGrade ====================
    @Tag("grade")
    @ParameterizedTest
    @ValueSource(ints = {-5, 0, 10, 25, 45, 65, 75, 90, 100})
    void testGetGrade(int score) {
        String actual = First_task.getGrade(score);
        String expected;
        if (score <= 0) expected = "Error";
        else if (score <= 20) expected = "E";
        else if (score <= 40) expected = "D";
        else if (score <= 60) expected = "C";
        else if (score <= 80) expected = "B";
        else expected = "A";

        assertEquals(expected, actual,
                () -> "getGrade(" + score + "): ожидалось \"" + expected + "\", получено \"" + actual + "\"");
    }

    // ==================== 5. blastOff ====================
    @RepeatedTest(10)
    void testBlastOff() {
        int start = random.nextInt(8) + 3;
        String actual = First_task.blastOff(start);

        StringBuilder sb = new StringBuilder();
        for (int i = start; i >= 1; i--) {
            sb.append(i);
            if (i > 1) sb.append(" ");
        }
        sb.append(" Поехали!");
        String expected = sb.toString();

        assertEquals(expected, actual,
                () -> "blastOff(" + start + "): ожидалось \"" + expected + "\", получено \"" + actual + "\"");
    }

    // ==================== 6. sumToN ====================
    @Tag("smoke")
    @RepeatedTest(10)
    void testSumToN() {
        int n = random.nextInt(50) + 1;
        int actual = First_task.sumToN(n);
        int expected = n * (n + 1) / 2;

        assertEquals(expected, actual,
                () -> "sumToN(" + n + "): ожидалось " + expected + ", получено " + actual);
    }

    // ==================== 7. hasBug ====================
    @ParameterizedTest
    @MethodSource("bugMessagesProvider")
    void testHasBug(String[] messages, boolean expected) {
        boolean actual = First_task.hasBug(messages);

        assertEquals(expected, actual,
                () -> "hasBug(" + Arrays.toString(messages) + "): ожидалось " + expected + ", получено " + actual);
    }

    static Stream<Arguments> bugMessagesProvider() {
        return Stream.of(
                Arguments.of(new String[]{"Info", "Warning"}, false),
                Arguments.of(new String[]{"Error", "Bug", "OK"}, true),
                Arguments.of(new String[]{"bug"}, true),
                Arguments.of(new String[]{}, false)
        );
    }

    // ==================== 8. getEvenInRange ====================
    @RepeatedTest(10)
    void testGetEvenInRange() {
        int start = 3;
        int end = 12;
        String actual = First_task.getEvenInRange(start, end);
        String expected = "4 6 8 10 12";

        assertEquals(expected, actual,
                () -> "getEvenInRange(" + start + ", " + end + "): ожидалось \"" + expected +
                        "\", получено \"" + actual + "\"");
    }

    // ==================== 9. findMax ====================
    @ParameterizedTest
    @CsvSource({
            "1, 5, 3, 5",
            "10, 2, 8, 10",
            "-5, -1, -9, -1"
    })
    void testFindMax(int a, int b, int c, int expected) {
        int actual = First_task.findMax(new int[]{a, b, c});

        assertEquals(expected, actual,
                () -> "findMax([" + a + ", " + b + ", " + c + "]): ожидалось " + expected +
                        ", получено " + actual);
    }

    // ==================== 10. reverse ====================
    @RepeatedTest(10)
    void testReverse() {
        String[] input = {"A", "B", "C", "D"};
        String[] actual = First_task.reverse(input);
        String[] expected = {"D", "C", "B", "A"};

        assertArrayEquals(expected, actual,
                () -> "reverse: ожидалось " + Arrays.toString(expected) +
                        ", получено " + Arrays.toString(actual));
    }

    // ==================== 11. calcAverage ====================
    @RepeatedTest(10)
    void testCalcAverage() {
        List<Integer> list = Arrays.asList(
                random.nextInt(20),
                random.nextInt(20),
                random.nextInt(20),
                random.nextInt(20)
        );
        double actual = First_task.calcAverage(list);
        double expected = list.stream().mapToInt(Integer::intValue).average().orElse(0);

        assertEquals(expected, actual, 0.0001,
                () -> "calcAverage(" + list + "): ожидалось " + expected + ", получено " + actual);
    }

    // ==================== 12. removeSpecificName (List) ====================
    @Tag("list")
    @RepeatedTest(10)
    void testRemoveSpecificName() {
        List<String> list = new ArrayList<>(Arrays.asList("Anna", "Bob", "Anna", "Clara", "Bob"));
        List<String> actual = First_task.removeSpecificName(list, "Anna");
        List<String> expected = Arrays.asList("Bob", "Clara", "Bob");

        assertEquals(expected, actual,
                () -> "removeSpecificName: ожидалось " + expected + ", получено " + actual);
    }

    // ==================== Намеренно падающий ассерт (Задача 1) ====================
    @Tag("failing")
    @Test
    void testIntentionallyFailing() {
        String actual = First_task.checkAccess(25);
        String expected = "Access granted"; // специально неверное значение

        assertEquals(expected, actual,
                () -> "НАМЕРЕННОЕ ПАДЕНИЕ checkAccess(25): ожидалось \"" + expected +
                        "\", получено \"" + actual + "\"");
    }
}