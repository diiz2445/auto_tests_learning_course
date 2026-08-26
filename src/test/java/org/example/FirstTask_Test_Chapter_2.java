package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
        import java.util.stream.Stream;

public class FirstTask_Test_Chapter_2 {

    private final Random random = new Random();

    // ==================== 1. isEven — @Test ====================
    @Test
    void testIsEven() {
        int n = random.nextInt(100) + 1; // 1..100
        boolean result = First_task.isEven(n);
        boolean expected = n % 2 == 0;

        if (result == expected) {
            System.out.println("TEST PASSED: isEven(" + n + ") = " + result);
        } else {
            System.out.println("TEST FAILED: isEven(" + n + ") = " + result + ", expected " + expected);
        }
    }

    // ==================== 2. checkAccess — @RepeatedTest(20) ====================
    @RepeatedTest(20)
    void testCheckAccess() {
        int age = random.nextInt(100); // 0..99
        String result = First_task.checkAccess(age);
        String expected = age > 18 ? "Allowed" : "Denied";

        if (result.equals(expected)) {
            System.out.println("TEST PASSED: checkAccess(" + age + ") = " + result);
        } else {
            System.out.println("TEST FAILED: checkAccess(" + age + ") = " + result + ", expected " + expected);
        }
    }

    // ==================== 3. isPositive — @Test ====================
    @Test
    void testIsPositive() {
        int n = random.nextInt(201) - 100; // -100..100
        boolean result = First_task.isPositive(n);
        boolean expected = n >= 0;

        if (result == expected) {
            System.out.println("TEST PASSED: isPositive(" + n + ") = " + result);
        } else {
            System.out.println("TEST FAILED: isPositive(" + n + ") = " + result + ", expected " + expected);
        }
    }

    // ==================== 4. getGrade — @ParameterizedTest ====================
    @ParameterizedTest
    @ValueSource(ints = {-5, 0, 10, 25, 45, 65, 75, 90, 100})
    void testGetGrade(int score) {
        String result = First_task.getGrade(score);
        String expected;
        if (score <= 0) expected = "Error";
        else if (score <= 20) expected = "E";
        else if (score <= 40) expected = "D";
        else if (score <= 60) expected = "C";
        else if (score <= 80) expected = "B";
        else expected = "A";

        if (result.equals(expected)) {
            System.out.println("TEST PASSED: getGrade(" + score + ") = " + result);
        } else {
            System.out.println("TEST FAILED: getGrade(" + score + ") = " + result + ", expected " + expected);
        }
    }

    // ==================== 5. blastOff — @Test ====================
    @Test
    void testBlastOff() {
        int start = random.nextInt(8) + 3; // 3..10
        String result = First_task.blastOff(start);

        StringBuilder expected = new StringBuilder();
        for (int i = start; i >= 1; i--) {
            expected.append(i);
            if (i > 1) expected.append(" ");
        }
        expected.append(" Поехали!");

        if (result.equals(expected.toString())) {
            System.out.println("TEST PASSED: blastOff(" + start + ") = " + result);
        } else {
            System.out.println("TEST FAILED: blastOff(" + start + ") = " + result);
        }
    }

    // ==================== 6. sumToN — @RepeatedTest ====================
    @RepeatedTest(5)
    void testSumToN() {
        int n = random.nextInt(50) + 1;
        int result = First_task.sumToN(n);
        int expected = n * (n + 1) / 2;

        if (result == expected) {
            System.out.println("TEST PASSED: sumToN(" + n + ") = " + result);
        } else {
            System.out.println("TEST FAILED: sumToN(" + n + ") = " + result + ", expected " + expected);
        }
    }

    // ==================== 7. hasBug — @ParameterizedTest ====================
    @ParameterizedTest
    @MethodSource("bugMessagesProvider")
    void testHasBug(String[] messages, boolean expected) {
        boolean result = First_task.hasBug(messages);

        if (result == expected) {
            System.out.println("TEST PASSED: hasBug = " + result);
        } else {
            System.out.println("TEST FAILED: hasBug = " + result + ", expected " + expected);
        }
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> bugMessagesProvider() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(new String[]{"Info", "Warning"}, false),
                org.junit.jupiter.params.provider.Arguments.of(new String[]{"Error", "Bug", "OK"}, true),
                org.junit.jupiter.params.provider.Arguments.of(new String[]{"bug"}, true),
                org.junit.jupiter.params.provider.Arguments.of(new String[]{}, false)
        );
    }

    // ==================== 8. getEvenInRange — @Test ====================
    @Test
    void testGetEvenInRange() {
        int start = 3;
        int end = 12;
        String result = First_task.getEvenInRange(start, end);
        String expected = "4 6 8 10 12";

        if (result.equals(expected)) {
            System.out.println("TEST PASSED: getEvenInRange(" + start + ", " + end + ") = " + result);
        } else {
            System.out.println("TEST FAILED: getEvenInRange = " + result + ", expected " + expected);
        }
    }

    // ==================== 9. findMax — @ParameterizedTest + CsvSource ====================
    @ParameterizedTest
    @CsvSource({
            "1, 5, 3, 5",
            "10, 2, 8, 10",
            "-5, -1, -9, -1"
    })
    void testFindMax(int a, int b, int c, int expected) {
        int result = First_task.findMax(new int[]{a, b, c});

        if (result == expected) {
            System.out.println("TEST PASSED: findMax = " + result);
        } else {
            System.out.println("TEST FAILED: findMax = " + result + ", expected " + expected);
        }
    }

    // ==================== 10. reverse — @Test ====================
    @Test
    void testReverse() {
        String[] input = {"A", "B", "C", "D"};
        String[] result = First_task.reverse(input);
        String[] expected = {"D", "C", "B", "A"};

        if (Arrays.equals(result, expected)) {
            System.out.println("TEST PASSED: reverse = " + Arrays.toString(result));
        } else {
            System.out.println("TEST FAILED: reverse = " + Arrays.toString(result));
        }
    }

    // ==================== 11. calcAverage — @RepeatedTest ====================
    @RepeatedTest(3)
    void testCalcAverage() {
        List<Integer> list = Arrays.asList(
                random.nextInt(20),
                random.nextInt(20),
                random.nextInt(20),
                random.nextInt(20)
        );
        double result = First_task.calcAverage(list);
        double expected = list.stream().mapToInt(Integer::intValue).average().orElse(0);

        if (Math.abs(result - expected) < 0.0001) {
            System.out.println("TEST PASSED: calcAverage = " + result);
        } else {
            System.out.println("TEST FAILED: calcAverage = " + result + ", expected " + expected);
        }
    }

    // ==================== 12. removeSpecificName — @Test ====================
    @Test
    void testRemoveSpecificName() {
        List<String> list = new ArrayList<>(Arrays.asList("Anna", "Bob", "Anna", "Clara", "Bob"));
        List<String> result = First_task.removeSpecificName(list, "Anna");
        List<String> expected = Arrays.asList("Bob", "Clara", "Bob");

        if (result.equals(expected)) {
            System.out.println("TEST PASSED: removeSpecificName = " + result);
        } else {
            System.out.println("TEST FAILED: removeSpecificName = " + result + ", expected " + expected);
        }
    }
}