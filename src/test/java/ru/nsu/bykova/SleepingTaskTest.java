package ru.nsu.bykova;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.bykova.data.SleepingTask;

public class SleepingTaskTest {
    @Test
    void sleepingTimeLessZero() {
        final int[] array = new int[]{1};
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> {new SleepingTask(-1L, array);});
    }
}
