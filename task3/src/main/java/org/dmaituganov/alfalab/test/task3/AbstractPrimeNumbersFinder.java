package org.dmaituganov.alfalab.test.task3;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Getter(AccessLevel.PACKAGE)
@ToString(onlyExplicitlyIncluded = true)
public abstract class AbstractPrimeNumbersFinder<N extends Number> implements Closeable {
    protected static final String SHARED_FILE_NAME = "Result.txt";


    @NonNull private final ForkJoinPool pool;

    @NonNull private final Path resultDirectory;
    @NonNull private final FileWriter sharedWriter;
             private final List<ThreadContext<N>> threads;

    @ToString.Include
    @NonNull protected final N stopAt;
    @Getter(AccessLevel.NONE)
    @NonNull protected final AtomicReference<N> currentNaturalNumber;


    public AbstractPrimeNumbersFinder(
        @NonNull Path resultDirectory,
        @NonNull N minNaturalNumber,
        @NonNull N maxNaturalNumber,
        int numberOfThreads
    ) throws IOException {
        this.pool = new ForkJoinPool(numberOfThreads);
        this.resultDirectory = resultDirectory;
        this.stopAt = increment(maxNaturalNumber);
        this.sharedWriter = new FileWriter(prepareSharedFile());
        this.threads = new ArrayList<>(numberOfThreads);
        this.currentNaturalNumber = new AtomicReference<>(minNaturalNumber);

        for (int i = 1; i <= numberOfThreads; i++) {
            this.threads.add(new ThreadContext<>(this, i));
        }
    }

    protected abstract int compare(@NonNull N n1, @NonNull N n2);
    protected abstract N increment(@NonNull N n);
    protected abstract boolean isPrimeNumber(@NonNull N number);

    @ToString.Include
    public int numberOfThreads() {
        return this.threads.size();
    }

    protected File prepareSharedFile() {
        File dir = this.resultDirectory.toFile();
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return this.resultDirectory.resolve(SHARED_FILE_NAME).toFile();
        } catch (Exception e) {
            throw new RuntimeException("Unable to prepare shared file", e);
        }
    }

    /** Returns Null if maximal natural number has been reached. */
    @Nullable
    protected N getNextNaturalNumber() {
        if (compare(currentNaturalNumber.get(), stopAt) >= 0) {
            return null;
        }

        N result = currentNaturalNumber
            .updateAndGet(n -> compare(n, stopAt) >= 0 ? n : increment(n));
        if (compare(result, stopAt) < 0) {
            return result;
        }
        return null;
    }

    public void findPrimaryNumbers() {
        long currentTime = System.currentTimeMillis();
        this.threads.forEach(pool::execute);
        this.threads.forEach(ForkJoinTask::join);
        log.info("{} completed in {}ms", this, System.currentTimeMillis() - currentTime);
    }

    @Override
    public void close() throws IOException {
        this.sharedWriter.close();
    }
}
