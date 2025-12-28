package network;

import java.io.*;
import java.net.Socket;
import modele.*;

public class ClientHandler implements Runnable {
    private Socket s;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    int id;
    private Server server;

    public ClientHandler(Socket s, int id, Server server) {
        this.s = s;
        this.id = id;
        this.server = server;
        try {
            out = new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));
            out.flush();
            out.writeInt(id);
            out.flush();

            ObjectInputStream tempIn = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
            String nomRecu = (String) tempIn.readObject();
            server.partie.setNomJoueur(id, nomRecu);

            this.in = tempIn;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Si le client se déconnecte, on envoie expections EOFException ou IOException.
                String cmd = (String) in.readObject();
                server.traiterAction(id, cmd);
            }
        } catch (Exception e) {
            // Client déconnecté
            System.out.println("Joueur " + id + " déconnecté." + e.getMessage());
            server.removeClient(this); // Clean up
        } finally {
            try { s.close(); } catch (IOException e) {}
        }
    }

    // Renvoie FALSE si l'envoi échoue (pour que le Server puisse retirer le client)
    public boolean send(Partie p) {
        try {
            synchronized (out) {
                out.reset();
                out.writeObject(p);
                out.flush();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
