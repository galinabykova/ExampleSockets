package ru.nsu.bykova.udp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import ru.nsu.bykova.SleepingCourier;
import ru.nsu.bykova.data.SleepingResult;

/**
Передаёт результат расчётов через UDP.
 */
public class UdpSleepingCourier implements SleepingCourier {
    final ObjectMapper mapper;
    InetAddress address;
    int port;

    public UdpSleepingCourier(InetAddress address, int port) {
        this.address = address;
        this.port = port;
        mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
    }

    @Override
    public void sendResult(SleepingResult sleepingResult) {
        try (DatagramSocket datagramSocketResult = new DatagramSocket()) {
            var bytes = mapper.writeValueAsBytes(sleepingResult);
            DatagramPacket packet =
                    new DatagramPacket(
                            bytes,
                            bytes.length,
                            InetAddress.getByAddress(address.getAddress()),
                            port);
            datagramSocketResult.send(packet);
        } catch (IOException e) {
        }
    }
}
