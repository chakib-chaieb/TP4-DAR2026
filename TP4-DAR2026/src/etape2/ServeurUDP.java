package etape2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class ServeurUDP {

    private static final int PORT = 1234;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(null)) {
            socket.bind(new InetSocketAddress(PORT));
            System.out.println("Serveur UDP en ecoute sur le port " + PORT);

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                String adresse = packet.getAddress().getHostAddress();
                int port = packet.getPort();

                System.out.println("De " + adresse + ":" + port + " -> " + message);
            }
        } catch (Exception e) {
            System.err.println("Erreur serveur : " + e.getMessage());
        }
    }
}
