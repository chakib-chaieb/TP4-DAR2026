package etape3;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Set;

public class ServeurChatUDP {

    private static final int PORT = 1234;

    public static void main(String[] args) {
        Set<SocketAddress> clients = new LinkedHashSet<>();

        try (DatagramSocket socket = new DatagramSocket(null)) {
            socket.bind(new InetSocketAddress(PORT));
            System.out.println("Serveur de chat UDP en ecoute sur le port " + PORT);

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                SocketAddress expediteur = packet.getSocketAddress();
                String message = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);

                if (clients.add(expediteur)) {
                    System.out.println("Nouveau client : " + expediteur);
                }

                System.out.println("De " + expediteur + " -> " + message);

                byte[] donnees = message.getBytes(StandardCharsets.UTF_8);

                for (SocketAddress client : clients) {
                    if (!client.equals(expediteur)) {
                        DatagramPacket envoi = new DatagramPacket(donnees, donnees.length, client);
                        socket.send(envoi);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur serveur : " + e.getMessage());
        }
    }
}
