package ru.nsu.bykova.tcp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.bykova.data.SleepingResult;
import ru.nsu.bykova.SleepingCourier;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TcpSleepingCourier implements SleepingCourier {
    Socket clientSocket;
    private final ObjectMapper mapper;

    public TcpSleepingCourier(Socket socket) {
        clientSocket = socket;
        mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
    }

    @Override
    public void SendResult(SleepingResult result) {
        try {
            var out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            mapper.writeValue(out, result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                clientSocket.close();
            } catch (IOException e) {
            }
        }
    }
}
