package org.dmaituganov.alfalab.test.task3;

import lombok.NonNull;
import lombok.ToString;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.dmaituganov.alfalab.test.task3.Task3Main.MAX_NUMBER;


public class BigIntegerIsPrimeTest {
    static final Path TEST_DIR = Path.of("tests/is_prime");

    static boolean rmDir() {
        return TEST_DIR.toFile().delete();
    }

    @ToString(callSuper = true)
    private class FinderStrictIsPrimeCheck extends PrimeNumbersFinderImpl {
        public FinderStrictIsPrimeCheck(@NonNull Path resultDirectory, BigInteger maxNumber, int numberOfThreads) throws IOException {
            super(resultDirectory, maxNumber, numberOfThreads);
        }

        @Override
        protected boolean isPrimeNumber(@NonNull BigInteger bigInteger) {
            int number = bigInteger.intValue();
            if (number < 2) {
                return false;
            }
            if (number < 4) {
                return true;
            }
            // Half operations in the main loop can be prevented with this check.
            if (number % 2 == 0) {
                return false;
            }
            int sqrt = (int) Math.sqrt(number);
            for (long i = 3; i <= sqrt; i+=2) {
                if (number % i == 0) {
                    return false;
                }
            }
            return true;
        }
    }

    private static final int THREADS_NUMBER = 1;
    @Test
    public void checkIsProbablePrime() throws IOException {
        rmDir();
        Path pathToStrict = TEST_DIR.resolve("strict/");
        Path pathToBd = TEST_DIR.resolve("probable/");
        String pathToResult = "Result.txt";
        try (
            var strictFinder = new FinderStrictIsPrimeCheck(pathToStrict, MAX_NUMBER, THREADS_NUMBER);
            var probableFinder = new PrimeNumbersFinderImpl(pathToBd, MAX_NUMBER, THREADS_NUMBER);
        ) {
            strictFinder.findPrimaryNumbers();
            probableFinder.findPrimaryNumbers();
        }
        Path pathToBdResult = pathToBd.resolve(pathToResult);
        Path pathToStrictResult = pathToStrict.resolve(pathToResult);
        long mismatched = Files.mismatch(pathToStrictResult, pathToBdResult);

        if (mismatched >= 0) {
            try (var is = new FileInputStream(pathToStrictResult.toFile())) {
                is.skip(mismatched);
                Scanner scanner = new Scanner(is);
                System.out.println("sctrict");
                System.out.println(scanner.nextInt());
                System.out.println(scanner.nextInt());
            }

            try (var is = new FileInputStream(pathToBdResult.toFile())) {
                is.skip(mismatched);
                Scanner scanner = new Scanner(is);
                System.out.println("probable");
                System.out.println(scanner.nextInt());
                System.out.println(scanner.nextInt());
            }
        }

        Assertions.assertThat(mismatched).isLessThan(0);
    }
}
