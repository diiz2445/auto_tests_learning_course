package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {

        }
    }

}
public class Tasks {

    // Задача 1
    public static boolean isEven(int n) {
        return n % 2 == 0;
    }

    // Задача 2
    public static String checkAccess(int age) {
        return age > 18 ? "Allowed" : "Denied";
    }

    // Задача 3
    public static boolean isPositive(int n) {
        return n >= 0 ? true : false;
    }
    // Задача 4
    public static String getGrade(int score){
        switch (score)
        case score <= 0:
            return "Error";
            break;
        case score<=20:
            return "E";
            break;
        case score<=40:
            return "D";
            break;
        case score<=60:
            return "C";
            break;
        case score<=80:
            return "B";
            break;
        case score<=20:
            return "A";
            break;
    }
    // Задача 5
    public static String blastOff(int start) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i >= 1; i--) {
            sb.append(i);
            if (i > 1) {
                sb.append(" ");
            }
        }
        sb.append(" Поехали!");
        return sb.toString();
    }

    // Задача 6
    public static int sumToN(int n) {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
        }
        return sum;
    }

    // Задача 7
    public static boolean hasBug(String[] messages) {
        for (String msg : messages) {
            if (msg != null && msg.equalsIgnoreCase("Bug")) {
                return true;
            }
        }
        return false;
    }

    // Задача 8
    public static String getEvenInRange(int start, int end) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = start; i <= end; i++) {
            if (i % 2 == 0) {
                if (!first) {
                    sb.append(" ");
                }
                sb.append(i);
                first = false;
            }
        }
        return sb.toString();
    }

    // Задача 9
    public static int findMax(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array is empty or null");
        }
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    // Задача 10
    public static String[] reverse(String[] arr) {
        if (arr == null) {
            return null;
        }
        String[] result = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[arr.length - 1 - i];
        }
        return result;
    }

    // Задача 11
    public static double calcAverage(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List is empty or null");
        }
        double sum = 0;
        for (int num : list) {
            sum += num;
        }
        return sum / list.size();
    }

    // Задача 12
    public static List<String> removeSpecificName(List<String> list, String nameToRemove) {
        List<String> result = new ArrayList<>();
        if (list == null) {
            return result;
        }
        for (String name : list) {
            if (name == null || !name.equals(nameToRemove)) {
                result.add(name);
            }
        }
        return result;
    }
}
