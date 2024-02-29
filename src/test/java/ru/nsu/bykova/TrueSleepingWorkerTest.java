package ru.nsu.bykova;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.bykova.data.SleepingTask;

/**
 * Тесты для TrueSleepingWorker.
 */
class TrueSleepingWorkerTest {
    private TrueSleepingWorker trueSleepingWorker;

    @BeforeEach
    void createWorker() {
        trueSleepingWorker = new TrueSleepingWorker();
    }

    @Test
    void smallArrayTest() {
        int[] smallArray = new int[] {0, 1, 2};
        SleepingTask sleepingTask = new SleepingTask(1, smallArray);
        Assertions.assertFalse(trueSleepingWorker.work(sleepingTask).result);
    }

    @Test
    void bigArrayTest() {
        int[] bigArray = new int[] {0, 1, 2, 3, 4, 5, 6};
        SleepingTask sleepingTask = new SleepingTask(1, bigArray);
        Assertions.assertTrue(trueSleepingWorker.work(sleepingTask).result);
    }

    @Test
    void emptyArrayTest() {
        int[] emptyArray = new int[] {};
        SleepingTask sleepingTask = new SleepingTask(1, emptyArray);
        Assertions.assertFalse(trueSleepingWorker.work(sleepingTask).result);
    }

    @Test
    void zeroSleepingTimeTest() {
        int[] array = new int[] {0, 1, 2};
        SleepingTask sleepingTask = new SleepingTask(0, array);
        Assertions.assertFalse(trueSleepingWorker.work(sleepingTask).result);
    }
}
