import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            System.out.println("Connecté au serveur");

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in);

            String bienvenue = (String) in.readObject();
            System.out.println("Serveur: " + bienvenue);

            while (true) {
                System.out.println("\n--- Nouvelle opération ---");
                System.out.print("Premier nombre: ");
                double n1 = scanner.nextDouble();

                System.out.print("Opérateur (+, -, *, /): ");
                char op = scanner.next().charAt(0);

                System.out.print("Deuxième nombre: ");
                double n2 = scanner.nextDouble();

                Operation operation = new Operation(n1, n2, op);
                out.writeObject(operation);
                out.flush();

                Object reponse = in.readObject();

                if (reponse instanceof Double) {
                    double resultat = (Double) reponse;
                    if (Double.isNaN(resultat)) {
                        System.out.println("Erreur: opération impossible");
                    } else {
                        System.out.println("Résultat: " + resultat);
                    }
                }

                System.out.print("Continuer? (o/n): ");
                String choix = scanner.next();
                if (choix.equalsIgnoreCase("n")) break;
            }

            scanner.close();
            socket.close();
            System.out.println("Déconnecté");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}