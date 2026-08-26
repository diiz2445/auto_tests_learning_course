import org.example.First_task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BasicMethodsTest {

    private final Random random = new Random();

    @BeforeEach
    void beforeEach() {
        System.out.println("========================Test method start");
    }

    @AfterEach
    void afterEach() {
        System.out.println("Test method end");
        System.out.println("========================");
    }

    // 1. isEven — один раз со случайным числом от 1 до 100
    @Test
    void testIsEven() {
        int n = random.nextInt(100) + 1; // 1..100
        boolean result = First_task.isEven(n);
        System.out.println("isEven(" + n + ") = " + result);
        assertEquals(n % 2 == 0, result);
    }

    // 2. checkAccess — 20 раз со случайными числами от 0 до 99
    @RepeatedTest(20)
    void testCheckAccess() {
        for (int i = 0; i < 20; i++) {
            int age = random.nextInt(100); // 0..99
            String result = First_task.checkAccess(age);
            System.out.println("checkAccess(" + age + ") = " + result);
            assertTrue(result.equals("Allowed") || result.equals("Denied"));
        }
    }

    // 3. getGrade — параметризованный тест с массивом случайных чисел от 0 до 100
    @ParameterizedTest
    @MethodSource("randomScores")
    void testGetGrade(int score) {
        String grade = First_task.getGrade(score);
        System.out.println("getGrade(" + score + ") = " + grade);
        assertNotNull(grade);
    }

    static Stream<Integer> randomScores() {
        Random r = new Random();
        // создаём массив из 10 случайных чисел 0..100
        Integer[] scores = new Integer[10];
        for (int i = 0; i < scores.length; i++) {
            scores[i] = r.nextInt(101);
        }
        return Stream.of(scores);
    }
}