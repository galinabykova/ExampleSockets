package ru.nsu.bykova.udp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import ru.nsu.bykova.SleepingWorker;
import ru.nsu.bykova.data.SleepingResult;
import ru.nsu.bykova.data.SleepingTask;

/**
Передаёт задачу на расчёт серверу и получает результат через UDP.
 */
public class UdpSleepingWorker implements SleepingWorker {
    private final ObjectMapper mapper;
    private String address;
    private int port;

    /**
     * конструктор
     *
     * @param address адрес сервера для расчёта
     * @param port порт сервера для расчёта
     */
    public UdpSleepingWorker(String address, int port) {
        this.address = address;
        this.port = port;
        mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
    }

    @Override
    public SleepingResult work(SleepingTask sleepingTask) throws IOException {
        try (DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.setSoTimeout(10000);
            var bytes = mapper.writeValueAsBytes(sleepingTask);
            DatagramPacket packet =
                    new DatagramPacket(bytes, bytes.length, InetAddress.getByName(address), port);
            datagramSocket.send(packet);

            DatagramPacket pack = new DatagramPacket(new byte[16], 16);
            datagramSocket.receive(pack);
            var resultA = mapper.readValue(pack.getData(), SleepingResult.class);
            return resultA;
        } catch (SocketTimeoutException e) {
            // Вот тут есть неприятная деталь: если сервер будет считать задачу
            // слишком долго, мы попадём сюда и не получим результат.
            // Если будете использовать UDP для отправки задач (без этого можно обойтись)
            // Нужно будет чего-то отправлять клиенту, чтобы он знал, что сервер жив.
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SleepingResult(false);
    }
}
