package ru.nsu.bykova;

import java.io.Closeable;
import java.util.Optional;
import ru.nsu.bykova.data.SleepingTask;

public interface SleepingBoss extends Closeable {
    Optional<Package> getSleepingTask();

    record Package(SleepingTask task, SleepingCourier courier) {}
}
