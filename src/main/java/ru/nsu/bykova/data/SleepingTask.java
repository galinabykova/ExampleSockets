package ru.nsu.bykova.data;

/**
Информация, необходимая для расчёта одной задачи.
 */
public class SleepingTask {
    // тут публичные поля, чтобы можно было сериализовать в JSON
    // с помощью библиотеки
    public long sleepingTime;
    public int[] array;

    public SleepingTask() {}

    /**
     * Конструктор.
     *
     * @param sleepingTime время сна
     * @param array массив, по которому рассчитывается задача
     * @throws IllegalArgumentException если sleepingTime меньше 0
     */
    public SleepingTask(long sleepingTime, int[] array) throws IllegalArgumentException {
        if (sleepingTime < 0) {
            throw new IllegalArgumentException("sleepingTime < 0");
        }
        this.sleepingTime = sleepingTime;
        this.array = array;
    }
}
