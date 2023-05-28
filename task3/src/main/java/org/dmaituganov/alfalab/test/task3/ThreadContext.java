package org.dmaituganov.alfalab.test.task3;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
class ThreadContext<T extends Number> extends RecursiveAction {
    private static final String THREAD_FILE_PATTERN = "Thread%d.txt";
    private static final int QUEUE_SIZE = 500;

    private final AbstractPrimeNumbersFinder<T> finder;
    private final int thread;
    private final File resultFile;

    // ReadWriteLock is used for inter-thread interaction with this not thread-safe queue.
    private final ArrayDeque<T> primeNumbersQueue = new ArrayDeque<>(QUEUE_SIZE);
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public ThreadContext(AbstractPrimeNumbersFinder<T> finder, int threadNumber) {
        this.finder = finder;
        this.thread = threadNumber;
        String threadFile = THREAD_FILE_PATTERN.formatted(threadNumber);
        this.resultFile = finder.getResultDirectory().resolve(threadFile).toFile();
    }

    private void notifyToWrite() {
        synchronized (this.resultFile) {
            this.resultFile.notify();
        }
    }
    private void waitToWrite() throws InterruptedException {
        synchronized (this.resultFile) {
            this.resultFile.wait(1);
        }
    }

    @NonNull
    private T leastNumberInProgress() {
        this.rwLock.readLock().lock();
        T result;
        try {
            result = primeNumbersQueue.peekFirst();
        } finally {
            this.rwLock.readLock().unlock();
        }
        if (result == null) {
            return finder.stopAt;
        }
        return result;
    }

    // This operation must be atomic,
    // because other threads can check the received number against the numbers in the local queue before it is set:
    // thread1 gets number
    // thread2 gets number
    // thread2 saves number to thread2.queue
    // thread2 compares thread1.queue with thread2.queue
    // thread1 saves number to thread1.queue

    @Nullable
    private T getAndAddNextNaturalNumber() {
        this.rwLock.writeLock().lock();
        try {
            T number = finder.getNextNaturalNumber();
            if (number != null) {
                this.primeNumbersQueue.addLast(number);
            }
            return number;
        } finally {
            this.rwLock.writeLock().unlock();
        }
    }
    private void wasNotPrime() {
        this.rwLock.writeLock().lock();
        try {
            this.primeNumbersQueue.pollLast();
        } finally {
            this.rwLock.writeLock().unlock();
        }
    }
    private void numberWritten() {
        this.rwLock.writeLock().lock();
        try {
            this.primeNumbersQueue.pollFirst();
        } finally {
            this.rwLock.writeLock().unlock();
        }
    }

    private record ContextLeastNumber<T extends Number>(ThreadContext<T> context, T number) {}
    protected void tryWrite(FileWriter writer) throws IOException {
        if (this.primeNumbersQueue.isEmpty()) {
            return;
        }
        Optional<ContextLeastNumber<T>> min = finder.getThreads().stream()
            .filter(c -> c != this)
            .map(c -> new ContextLeastNumber<>(c, c.leastNumberInProgress()))
            .min((c1, c2) -> finder.compare(c1.number(), c2.number()));
        T leastPrimeNumberOfOtherThreads = min.map(ContextLeastNumber::number).orElse(finder.stopAt);
        while (finder.compare(this.leastNumberInProgress(), leastPrimeNumberOfOtherThreads) < 0) {
            String str = this.primeNumbersQueue.getFirst() + " ";
            finder.getSharedWriter().write(str);
            this.numberWritten();
            writer.write(str);
        }
        min.ifPresent(c -> {
            log.trace("Notify thread " + c.context.thread);
            c.context.notifyToWrite();
        });
    }

    @Override
    protected void compute() {
        try (FileWriter writer = new FileWriter(resultFile)) {
            while (true) {
                tryWrite(writer);

                if (this.primeNumbersQueue.size() >= QUEUE_SIZE) {
                    log.debug("Queue of thread {} is full, is waiting to be unlocked", this.thread);
                    this.waitToWrite();
                    log.debug("Thread {} unlocked", this.thread);
                    continue;
                }

                T currentNumber = getAndAddNextNaturalNumber();
                if (this.primeNumbersQueue.isEmpty()) {
                    break;
                }
                if (currentNumber == null) {
                    log.debug("Queue of natural numbers is empty, thread {} is waiting to be unlocked", this.thread);
                    this.waitToWrite();
                    log.debug("Thread {} unlocked", this.thread);
                    continue;
                }

                if (finder.isPrimeNumber(currentNumber)) {
                    log.trace("Prime number " + currentNumber);
                } else {
                    wasNotPrime();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }
}
