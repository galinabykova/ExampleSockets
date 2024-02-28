package ru.nsu.bykova;

import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.nsu.bykova.data.SleepingResult;
import ru.nsu.bykova.data.SleepingTask;
import ru.nsu.bykova.tcp.TcpSleepingBoss;
import ru.nsu.bykova.tcp.TcpSleepingWorker;
import ru.nsu.bykova.udp.UdpSleepingBoss;
import ru.nsu.bykova.udp.UdpSleepingWorker;

class ClientServerTest {
    static final int PORT = 12345;

    private static Stream<Arguments> provideArguments() throws IOException {
        return Stream.of(
                Arguments.of(new TcpSleepingBoss(PORT), new TcpSleepingWorker("localhost", PORT)),
                Arguments.of(new UdpSleepingBoss(PORT), new UdpSleepingWorker("localhost", PORT)));
    }

    private static Stream<Arguments> provideArgumentsOnlyWorker() throws IOException {
        return Stream.of(
                Arguments.of(new TcpSleepingWorker("localhost", PORT)),
                Arguments.of(new UdpSleepingWorker("localhost", PORT)));
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void simpleFalseTest(SleepingBoss boss, SleepingWorker worker) {
        try {
            Thread serverThread =
                    new Thread(
                            () -> {
                                (new SleepingComputationalNode()).calculate(1, boss);
                            });
            serverThread.start();
            SleepingResult result = worker.Work(new SleepingTask(1, new int[] {0}));
            Assertions.assertFalse(result.result);
            boss.close();
            serverThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void simpleTrueTest(SleepingBoss boss, SleepingWorker worker) {
        try {
            Thread serverThread =
                    new Thread(
                            () -> {
                                (new SleepingComputationalNode()).calculate(1, boss);
                            });
            serverThread.start();
            SleepingResult result =
                    worker.Work(new SleepingTask(1, new int[] {0, 1, 2, 3, 4, 5, 6, 7}));
            Assertions.assertTrue(result.result);
            boss.close();
            serverThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void manyTasksTest(SleepingBoss boss, SleepingWorker worker) {
        final int nThreads = 5;
        final int nTasks = 10;
        try {
            Thread serverThread =
                    new Thread(
                            () -> {
                                (new SleepingComputationalNode()).calculate(nThreads, boss);
                            });
            serverThread.start();
            for (int i = 0; i < nTasks; ++i) {
                SleepingResult result =
                        worker.Work(new SleepingTask(100, new int[] {0, 1, 2, 3, 4, 5, 6, 7}));
                Assertions.assertTrue(result.result);
            }
            boss.close();
            serverThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void manyClientsTest(SleepingBoss boss, SleepingWorker worker) {
        final int nThreads = 5;
        final int nClients = 10;
        try {
            Thread serverThread =
                    new Thread(
                            () -> {
                                (new SleepingComputationalNode()).calculate(nThreads, boss);
                            });
            serverThread.start();
            Thread[] threads = new Thread[nClients];
            boolean[] results = new boolean[nClients];
            for (int i = 0; i < nClients; ++i) {
                final int currentI = i;
                threads[currentI] =
                        new Thread(
                                () -> {
                                    try {
                                        SleepingResult result =
                                                worker.Work(
                                                        new SleepingTask(
                                                                100,
                                                                new int[] {
                                                                    0, 1, 2, 3, 4, 5, 6, 7
                                                                }));
                                        results[currentI] = result.result;
                                    } catch (IOException e) {
                                        synchronized (System.err) {
                                            System.err.println("Thread" + currentI);
                                            e.printStackTrace();
                                        }
                                    }
                                });
                threads[currentI].start();
            }
            for (int i = 0; i < nClients; ++i) {
                threads[i].join();
                Assertions.assertTrue(results[i]);
            }
            boss.close();
            serverThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsOnlyWorker")
    void notServerTest(SleepingWorker worker) {
        Assertions.assertThrows(
                IOException.class, () -> worker.Work(new SleepingTask(1, new int[] {0})));
    }
}
