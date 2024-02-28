package ru.nsu.bykova.data;

public class SleepingResult {
    // тут публичные поля, чтобы можно было сериализовать в JSON
    // с помощью библиотеки
    public boolean result;

    public SleepingResult() {}

    public SleepingResult(boolean result) {
        this.result = result;
    }
}
