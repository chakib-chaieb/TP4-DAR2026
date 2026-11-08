package etape3;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientChatUDP {

    private static final String HOTE = "localhost";
    private static final int PORT = 1234;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nom d'utilisateur : ");
        String nom = scanner.nextLine().trim();

        try (DatagramSocket socket = new DatagramSocket()) {
            SocketAddress serveur = new InetSocketAddress(HOTE, PORT);

            Thread reception = new Thread(() -> {
                byte[] buffer = new byte[1024];
                while (!socket.isClosed()) {
                    try {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        String message = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                        System.out.println(message);
                    } catch (Exception e) {
                        break;
                    }
                }
            });
            reception.setDaemon(true);
            reception.start();

            envoyer(socket, serveur, "[" + nom + "] a rejoint le chat");

            System.out.println("Tapez vos messages (exit pour quitter)");

            while (true) {
                String saisie = scanner.nextLine();

                if (saisie.equalsIgnoreCase("exit")) {
                    envoyer(socket, serveur, "[" + nom + "] a quitte le chat");
                    break;
                }

                envoyer(socket, serveur, "[" + nom + "] : " + saisie);
            }
        } catch (Exception e) {
            System.err.println("Erreur client : " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void envoyer(DatagramSocket socket, SocketAddress serveur, String message) throws Exception {
        byte[] donnees = message.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(donnees, donnees.length, serveur);
        socket.send(packet);
    }
}
