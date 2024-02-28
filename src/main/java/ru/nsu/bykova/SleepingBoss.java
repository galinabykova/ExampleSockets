package ru.nsu.bykova;

import ru.nsu.bykova.data.SleepingTask;

import java.io.Closeable;
import java.util.Optional;

public interface SleepingBoss extends Closeable {
    Optional<Package> getSleepingTask();

    record Package(SleepingTask task, SleepingCourier courier) {}
}
