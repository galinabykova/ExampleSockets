package ru.nsu.bykova.tcp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.nsu.bykova.SleepingBoss;
import ru.nsu.bykova.data.SleepingTask;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

public class TcpSleepingBoss implements SleepingBoss {
    private final ServerSocket serverSocket;
    private final ObjectMapper mapper;

    public TcpSleepingBoss(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
    }

    @Override
    public Optional<Package> getSleepingTask() {
        try {
            // ждёт пока к нам кто-то подключится и возвращает сокет для общения с новым клиентом
            Socket clientSocket = serverSocket.accept();
            var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            var sleepingTask = mapper.readValue(in, SleepingTask.class);
            return Optional.of(new Package(sleepingTask, new TcpSleepingCourier(clientSocket)));
        } catch (JsonProcessingException e) {
            // вот сюда мы должны провалиться только если есть бага
            e.printStackTrace();
        } catch (IOException e) {
            // сюда мы попадём, если закроют сокет
            // мы ожидаем эту ситуацию
            // поэтому тут я не вывожу stackTrace
        }
        return Optional.empty();
    }
}
