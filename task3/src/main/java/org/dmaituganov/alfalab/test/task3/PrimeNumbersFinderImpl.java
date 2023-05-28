package org.dmaituganov.alfalab.test.task3;

import lombok.NonNull;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;

// Can be replaced with Long or some other implementation
public class PrimeNumbersFinderImpl extends AbstractPrimeNumbersFinder<BigInteger> {
    public PrimeNumbersFinderImpl(
        @NonNull Path resultDirectory,
        @NonNull BigInteger maxNaturalNumber,
        int numberOfThreads
    ) throws IOException {
        super(resultDirectory, BigInteger.ZERO, maxNaturalNumber, numberOfThreads);
    }

    @Override
    protected int compare(@NonNull BigInteger n1, @NonNull BigInteger n2) {
        return n1.compareTo(n2);
    }

    @Override
    protected BigInteger increment(@NonNull BigInteger n) {
        return n.add(BigInteger.ONE);
    }

    @Override
    protected boolean isPrimeNumber(@NonNull BigInteger n) {
        // Constant 100 is obtained from java.math.BigInteger.primeToCertainty method.
        // Certainly param is divided by 2 and can't be greater than 50 for 2^64 (64 bits)
        return n.isProbablePrime(100);
    }
}
