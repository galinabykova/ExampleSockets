package ru.nsu.bykova;

import java.io.IOException;
import ru.nsu.bykova.data.SleepingResult;
import ru.nsu.bykova.data.SleepingTask;

public interface SleepingWorker {
    SleepingResult work(SleepingTask sleepingTask) throws IOException;
}
