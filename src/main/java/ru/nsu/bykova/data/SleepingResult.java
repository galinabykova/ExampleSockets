package ru.nsu.bykova.data;

/**
Результат вычислений.
 */
public class SleepingResult {
    // тут публичные поля, чтобы можно было сериализовать в JSON
    // с помощью библиотеки
    public boolean result;

    public SleepingResult() {}

    /**
     * Конструктор.
     *
     * @param result результат
     */
    public SleepingResult(boolean result) {
        this.result = result;
    }
}
