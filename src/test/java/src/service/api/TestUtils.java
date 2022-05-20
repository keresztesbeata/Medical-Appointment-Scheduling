package src.service.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class TestUtils {

    public static String generateRandomName(int length) {
        return (new Random())
                .ints(length, 26,51 )
                .asLongStream()
                .mapToObj(value -> String.valueOf(Character.toChars((int) (value % 255))))
                .collect(Collectors.joining());
    }

    public static String generateRandomNumericalCode() {
        return (new Random(0))
                .ints(6,1,9)
                .asLongStream()
                .mapToObj(value -> String.valueOf((char) (value + (int)'0')))
                .collect(Collectors.joining());
    }

    public static int getRandomInt() {
        return (new Random()).nextInt(Integer.MAX_VALUE);
    }

    public static int getRandomInt(int range) {
        return (new Random()).nextInt(range);
    }

    public static double getRandomDouble() {
        return (new Random()).nextDouble();
    }

    public static double getRandomDouble(int range) {
        return (new Random()).nextDouble(range);
    }

    public static LocalDateTime generateRandomDate() {
        LocalDate start = LocalDate.of(1989, Month.OCTOBER, 14);
        LocalDate end = LocalDate.now();
        long startEpochDay = start.toEpochDay();
        long endEpochDay = end.toEpochDay();
        long randomDay = ThreadLocalRandom
                .current()
                .nextLong(startEpochDay, endEpochDay);

        return LocalDateTime.of(LocalDate.ofEpochDay(randomDay), LocalTime.MIN);
    }
}
