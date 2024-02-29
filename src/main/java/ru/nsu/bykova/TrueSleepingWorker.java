package ru.nsu.bykova;

import ru.nsu.bykova.data.SleepingResult;
import ru.nsu.bykova.data.SleepingTask;

/**
Вычисляет задачу там, где его вызывают.
 */
public class TrueSleepingWorker implements SleepingWorker {
    @Override
    public SleepingResult work(SleepingTask sleepingTask) {
        try {
            // здесь я предполагаю, что время сна неотрицательно
            // иначе пользователь бы не смог создать объект SleepingTask
            Thread.sleep(sleepingTask.sleepingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new SleepingResult(sleepingTask.array.length > 5);
    }
}
