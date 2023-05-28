package org.dmaituganov.alfalab.test.task3;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;

public class Task3Main {
    static final String DIR_PATH_PATTERN = "task3/t%d_result/";
    // Maximal value of Long is 2^63. For this reason there is BigInteger is used
    static final BigInteger MAX_NUMBER = BigInteger.valueOf(1_000_000);

    public static void main(String[] args) throws IOException {
        for (int threads = 1; threads <= 6; threads++) {
            for (int retry = 1; retry <= 4; retry++) {
                try (var finder = new PrimeNumbersFinderImpl(Path.of(DIR_PATH_PATTERN.formatted(threads)), MAX_NUMBER, threads)) {
                    finder.findPrimaryNumbers();
                }
            }
        }
    }
}
