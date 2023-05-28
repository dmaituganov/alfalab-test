package org.dmaituganov.alfalab.test.task3;

import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Scanner;

import static org.dmaituganov.alfalab.test.task3.Task3Main.MAX_NUMBER;

public class PrimeNumbersFinderTest {
    private static final Path TEST_DIR = Path.of("tests/compare_results");

    private Path threadsDirPath(int t) {
        return TEST_DIR.resolve(String.valueOf(t));
    }

    private Path threadsResultPath(int t) {
        return threadsDirPath(t).resolve("Result.txt");
    }


    private boolean rmDir() {
        return TEST_DIR.toFile().delete();
    }

    @Test
    public void compareMultiThreadedResults() throws IOException {
        rmDir();
        boolean mismatched = false;
        for (int threads = 1; threads <= 8; threads++) {
            try (var finder = new PrimeNumbersFinderImpl(threadsDirPath(threads), MAX_NUMBER, threads)) {
                finder.findPrimaryNumbers();
            }
            if (threads > 1) {
                mismatched |= !Utils.areFinderResultsEqual(threadsResultPath(threads - 1), threadsResultPath(threads));
            }
        }
        Assertions.assertThat(mismatched).isFalse();
    }

    private static class ResultIterator implements Iterable<Long>, Closeable {
        private final Scanner resultScanner;
        private final Iterator<Long> iterator;

        public ResultIterator(Path file) throws FileNotFoundException {
            this.resultScanner = new Scanner(file.toFile());
            this.iterator = new Iterator<>() {
                @Override
                public boolean hasNext() {
                    return resultScanner.hasNext();
                }

                @Override
                public Long next() {
                    return resultScanner.nextLong();
                }
            };
        }

        @NotNull
        @Override
        public Iterator<Long> iterator() {
            return this.iterator;
        }

        @Override
        public void close() {
            this.resultScanner.close();
        }
    }
}
