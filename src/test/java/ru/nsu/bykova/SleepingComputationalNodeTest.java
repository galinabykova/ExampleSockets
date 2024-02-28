package ru.nsu.bykova;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ru.nsu.bykova.data.SleepingResult;
import ru.nsu.bykova.data.SleepingTask;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

class SleepingComputationalNodeTest {
    // Чтобы протестировать класс SleepingComputationalNode,
    // мне нужно множество сущностей: SleepingBoss, Address, Worker.
    // Если тест упадёт, как мне понять, где проблема:
    // в SleepingComputationalNode или в одной из этих сущностей?
    // Лучше как-нибудь избавиться
    // от выполнения сложной бизнес-логики этих сущностей
    // в SleepingComputationalNodeTest.
    // А если для этих для работы этих сущностей
    // понадобятся базы данных и сеть это вообще ужас.
    // Поэтому при тестировании используют
    // dummy, stub, spy, mock, fake.
    // Правда не все считают, что это хорошая идея.
    // Вот эта статья выглядит внушительно
    // https://habr.com/ru/articles/577424/,
    // но я её не осилила.

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 10})
    void notTaskTest(int nThreads) {
        TestSleepingBoss testSleepingBoss = new TestSleepingBoss(new ConcurrentLinkedQueue<>());
        SleepingComputationalNode sleepingComputationalNode = new SleepingComputationalNode();
        sleepingComputationalNode.calculate(nThreads, testSleepingBoss);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 10})
    void oneTaskTest(int nThreads) {
        var queue = new ConcurrentLinkedQueue<TestSleepingTask>();
        queue.add(new TestSleepingTask(0, new int[] {0}, false));
        TestSleepingBoss testSleepingBoss = new TestSleepingBoss(queue);
        SleepingComputationalNode sleepingComputationalNode = new SleepingComputationalNode();
        sleepingComputationalNode.calculate(nThreads, testSleepingBoss);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 10})
    void smallArrayTaskTest(int nThreads) {
        var queue = new ConcurrentLinkedQueue<TestSleepingTask>();
        queue.add(new TestSleepingTask(0, new int[] {0}, false));
        queue.add(new TestSleepingTask(0, new int[] {0, 1}, false));
        queue.add(new TestSleepingTask(0, new int[] {0, 1, 2}, false));
        TestSleepingBoss testSleepingBoss = new TestSleepingBoss(queue);
        SleepingComputationalNode sleepingComputationalNode = new SleepingComputationalNode();
        sleepingComputationalNode.calculate(nThreads, testSleepingBoss);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 10})
    void bigArrayTaskTest(int nThreads) {
        var queue = new ConcurrentLinkedQueue<TestSleepingTask>();
        queue.add(new TestSleepingTask(0, new int[] {0, 0, 0, 0, 0, 0, 0}, true));
        queue.add(new TestSleepingTask(0, new int[] {0, 0, 0, 0, 0, 0, 0, 1}, true));
        queue.add(new TestSleepingTask(0, new int[] {0, 0, 0, 0, 0, 0, 0, 1, 2}, true));
        TestSleepingBoss testSleepingBoss = new TestSleepingBoss(queue);
        SleepingComputationalNode sleepingComputationalNode = new SleepingComputationalNode();
        sleepingComputationalNode.calculate(nThreads, testSleepingBoss);
    }

    class TestSleepingTask extends SleepingTask implements SleepingCourier {
        boolean expectedResult;

        public TestSleepingTask(long sleepingTime, int[] array, boolean expectedResult) {
            super(sleepingTime, array);
            this.expectedResult = expectedResult;
        }

        @Override
        public void SendResult(SleepingResult sleepingResult) {
            Assertions.assertEquals(expectedResult, sleepingResult.result);
        }
    }

    class TestSleepingBoss implements SleepingBoss {
        ConcurrentLinkedQueue<TestSleepingTask> tasks;

        public TestSleepingBoss(ConcurrentLinkedQueue<TestSleepingTask> tasks) {
            this.tasks = tasks;
        }

        @Override
        public Optional<Package> getSleepingTask() {
            var element = tasks.poll();
            if (element == null) {
                return Optional.empty();
            }
            return Optional.of(new Package(element, element));
        }

        @Override
        public void close() throws IOException {}
    }
}
