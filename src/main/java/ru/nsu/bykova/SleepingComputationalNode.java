package ru.nsu.bykova;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import ru.nsu.bykova.data.SleepingResult;

/**
Распределяет задачи между потоками.
 */
public class SleepingComputationalNode {
    /**
     * Дожидается новой задачи и отправляет её в пул.
     *
     * @param threadCount число потоков
     * @param sleepingBoss выдаёт задачи
     */
    public void calculate(int threadCount, SleepingBoss sleepingBoss) {
        if (threadCount < 0) {
            throw new IllegalArgumentException("thread count < 0");
        }
        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        Optional<SleepingBoss.Package> lastTask = sleepingBoss.getSleepingTask();
        while (lastTask.isPresent()) {
            final Optional<SleepingBoss.Package> currentTask = lastTask;
            service.submit(
                    () -> {
                        TrueSleepingWorker sleepingWorker = new TrueSleepingWorker();
                        SleepingResult result = sleepingWorker.work(currentTask.get().task());
                        currentTask.get().courier().sendResult(result);
                    });
            lastTask = sleepingBoss.getSleepingTask();
        }
        service.shutdown();
        try {
            service.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
