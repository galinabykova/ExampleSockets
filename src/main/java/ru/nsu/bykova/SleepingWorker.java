package ru.nsu.bykova;

import ru.nsu.bykova.data.SleepingResult;
import ru.nsu.bykova.data.SleepingTask;
import java.io.IOException;

public interface SleepingWorker {
    SleepingResult Work(SleepingTask sleepingTask) throws IOException;
}
