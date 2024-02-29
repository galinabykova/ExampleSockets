package ru.nsu.bykova;

import java.io.Closeable;
import java.util.Optional;
import ru.nsu.bykova.data.SleepingTask;

/**
Выдаёт задачи.
 */
public interface SleepingBoss extends Closeable {
    Optional<Package> getSleepingTask();

    /**
     * Пара, которую должен возвращает объект, реализующий интерфейс SleepingBoss.
     * @param task задача
     * @param courier тот, кто должен будет отправить/сохранить результат.
     */
    record Package(SleepingTask task, SleepingCourier courier) {}
}
