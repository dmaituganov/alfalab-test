package org.dmaituganov.alfalab.test.task3;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Utils {
    static boolean areFinderResultsEqual(Path p1, Path p2) throws IOException {
        long mismatched = Files.mismatch(p1, p2);

        if (mismatched < 0) {
            return true;
        }
        System.out.println("Files mismatched.");
        try (var is = new FileInputStream(p1.toFile())) {
            is.skip(mismatched);
            Scanner scanner = new Scanner(is);
            System.out.printf("\t%s:%n", p1);
            System.out.println(scanner.nextInt());
            System.out.println(scanner.nextInt());
        }

        try (var is = new FileInputStream(p2.toFile())) {
            is.skip(mismatched);
            Scanner scanner = new Scanner(is);
            System.out.printf("\t%s:", p2);
            System.out.println(scanner.nextInt());
            System.out.println(scanner.nextInt());
        }
        return false;
    }
}
