package ru.nsu.bykova;

import java.io.Closeable;
import java.util.Optional;
import ru.nsu.bykova.data.SleepingTask;

/**
Выдаёт задачи.
 */
public interface SleepingBoss extends Closeable {
    /**
     * Выдаёт следующую задачу. Может блокироваться до поступления новых задач.
     *
     * @return следующую задачу. Возвращает empty, если задач больше нет.
     */
    Optional<Package> getSleepingTask();

    /**
     * Пара, которую должен возвращает объект, реализующий интерфейс SleepingBoss.
     *
     * @param task задача
     * @param courier тот, кто должен будет отправить/сохранить результат.
     */
    record Package(SleepingTask task, SleepingCourier courier) {}
}
