package ru.nsu.bykova.tcp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import ru.nsu.bykova.SleepingWorker;
import ru.nsu.bykova.data.SleepingResult;
import ru.nsu.bykova.data.SleepingTask;

/**
Передаёт задачу на расчёт серверу и получает результат через TCP.
 */
public class TcpSleepingWorker implements SleepingWorker {
    private String address;
    private int port;

    /**
     * Конструктор.
     *
     * @param address сервера, который будет считать задачу
     * @param port сервера, который будет считать задачу
     * @throws IOException
     */
    public TcpSleepingWorker(String address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public SleepingResult work(SleepingTask sleepingTask) throws IOException {
        try (var clientSocket = new Socket(address, port)) {
            BufferedWriter out =
                    new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
            mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
            mapper.writeValue(out, sleepingTask);
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            return mapper.readValue(in, SleepingResult.class);
        } catch (ConnectException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            return new SleepingResult(false);
        }
    }
}
