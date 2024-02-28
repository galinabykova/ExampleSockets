package ru.nsu.bykova.tcp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.bykova.data.SleepingResult;
import ru.nsu.bykova.data.SleepingTask;
import ru.nsu.bykova.SleepingWorker;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpSleepingWorker implements SleepingWorker {
    String address;
    int port;
    public TcpSleepingWorker(String address, int port) throws IOException {
        this.address = address;
        this.port = port;
    }

    @Override
    public SleepingResult Work(SleepingTask sleepingTask) throws IOException {
        try (var clientSocket = new Socket(address, port)) {
            BufferedWriter out =
                    new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
            mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
            mapper.writeValue(out, sleepingTask);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            return mapper.readValue(in, SleepingResult.class);
        } catch (ConnectException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            return new SleepingResult(false);
        }
    }
}
