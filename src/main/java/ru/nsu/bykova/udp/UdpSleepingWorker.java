package ru.nsu.bykova.udp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.nsu.bykova.SleepingWorker;
import ru.nsu.bykova.data.SleepingResult;
import ru.nsu.bykova.data.SleepingTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class UdpSleepingWorker implements SleepingWorker {
    final ObjectMapper mapper;
    String address;
    int port;


    public UdpSleepingWorker(String address, int port) throws IOException {
        this.address = address;
        this.port = port;
        mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
    }
    @Override
    public SleepingResult Work(SleepingTask sleepingTask) throws IOException {
        try (DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.setSoTimeout(10000);
            var bytes = mapper.writeValueAsBytes(sleepingTask);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length,
                    InetAddress.getByName(address), port);
            datagramSocket.send(packet);

            DatagramPacket pack = new DatagramPacket(new byte[16], 16);
            datagramSocket.receive(pack);
            var resultA = mapper.readValue(pack.getData(), SleepingResult.class);
            return resultA;
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SleepingResult(false);
    }
}
