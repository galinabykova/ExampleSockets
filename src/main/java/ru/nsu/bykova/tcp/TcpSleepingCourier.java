package ru.nsu.bykova.tcp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import ru.nsu.bykova.SleepingCourier;
import ru.nsu.bykova.data.SleepingResult;

/*
Передаёт результат расчётов через TCP.
 */
public class TcpSleepingCourier implements SleepingCourier {
    private final ObjectMapper mapper;
    private Socket clientSocket;

    public TcpSleepingCourier(Socket socket) {
        clientSocket = socket;
        mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
    }

    @Override
    public void sendResult(SleepingResult result) {
        try {
            var out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            mapper.writeValue(out, result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
            }
        }
    }
}
