package ru.nsu.bykova.udp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Optional;
import ru.nsu.bykova.SleepingBoss;
import ru.nsu.bykova.data.SleepingTask;

/**
Принимает задачи от клиентов через протокол UDP.
 */
public class UdpSleepingBoss implements SleepingBoss {
    // мы не будет думать о том, что будет, если объект не влезет в один пакет
    // потому что вы можете организовать код так, чтобы вам не надо было
    // об этом думать
    static final int PACKET_LIMIT = 65507;
    final ObjectMapper mapper;
    DatagramSocket datagramSocket;

    /**
     * конструктор
     *
     * @param port порт, на котором будем ждать исключений.
     * @throws IOException если порт занят.
     */
    public UdpSleepingBoss(int port) throws IOException {
        datagramSocket = new DatagramSocket(port);
        mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
    }

    @Override
    public void close() throws IOException {
        datagramSocket.close();
    }

    @Override
    public Optional<Package> getSleepingTask() {
        try {
            DatagramPacket pack = new DatagramPacket(new byte[PACKET_LIMIT], PACKET_LIMIT);
            datagramSocket.receive(pack);
            var task = mapper.readValue(pack.getData(), SleepingTask.class);
            return Optional.of(
                    new Package(task, new UdpSleepingCourier(pack.getAddress(), pack.getPort())));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
