package etape2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientUDP {

    private static final String HOTE = "localhost";
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            InetAddress serveur = InetAddress.getByName(HOTE);

            System.out.print("Nom d'utilisateur : ");
            String nom = scanner.nextLine().trim();

            System.out.println("Tapez vos messages (exit pour quitter)");

            while (true) {
                String saisie = scanner.nextLine();

                if (saisie.equalsIgnoreCase("exit")) {
                    break;
                }

                String message = "[" + nom + "] : " + saisie;
                byte[] donnees = message.getBytes(StandardCharsets.UTF_8);

                DatagramPacket packet = new DatagramPacket(donnees, donnees.length, serveur, PORT);
                socket.send(packet);
            }
        } catch (Exception e) {
            System.err.println("Erreur client : " + e.getMessage());
        }
    }
}
