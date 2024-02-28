package ru.nsu.bykova.data;

public class SleepingTask {
    // тут публичные поля, чтобы можно было сериализовать в JSON
    // с помощью библиотеки
    public long sleepingTime;
    public int[] array;

    public SleepingTask() {
    }

    public SleepingTask(long sleepingTime, int[] array)
            throws IllegalArgumentException {
        if (sleepingTime < 0) {
            throw new IllegalArgumentException("sleepingTime < 0");
        }
        this.sleepingTime = sleepingTime;
        this.array = array;
    }
}
