import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class server {
    private static AtomicInteger compteurClients = new AtomicInteger(0);
    private static AtomicInteger compteurOperations = new AtomicInteger(0);

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Serveur calculatrice démarré");

            while (true) {
                Socket socket = serverSocket.accept();
                new ClientProcess(socket).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientProcess extends Thread {
        private Socket socket;
        private int numeroClient;

        public ClientProcess(Socket socket) {
            this.socket = socket;
            this.numeroClient = compteurClients.incrementAndGet();
        }

        public void run() {
            try {
                System.out.println("Client n°" + numeroClient + " connecté");

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                out.writeObject("Client n°" + numeroClient);
                out.flush();

                while (true) {
                    try {
                        Operation operation = (Operation) in.readObject();

                        double resultat = calculer(operation);

                        int totalOps = compteurOperations.incrementAndGet();

                        System.out.println("Opération " + totalOps + " - Client " + numeroClient +
                                " : " + operation + " = " + resultat);

                        out.writeObject(resultat);
                        out.flush();

                    } catch (EOFException e) {
                        break;
                    }
                }

                in.close();
                out.close();
                socket.close();
                System.out.println("Client n°" + numeroClient + " déconnecté");

            } catch (Exception e) {
                System.out.println("Erreur client " + numeroClient);
            }
        }

        private double calculer(Operation op) {
            switch (op.operateur) {
                case '+': return op.nombre1 + op.nombre2;
                case '-': return op.nombre1 - op.nombre2;
                case '*': return op.nombre1 * op.nombre2;
                case '/':
                    if (op.nombre2 == 0) return Double.NaN;
                    return op.nombre1 / op.nombre2;
                default: return Double.NaN;
            }
        }
    }
}