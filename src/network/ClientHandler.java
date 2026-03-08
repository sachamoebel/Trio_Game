package network;

import java.io.*;
import java.net.Socket;
import modele.*;

/**
 * Gestionnaire de client côté serveur.
 * Cette classe s'occupe de la communication avec un joueur spécifique
 * et s'exécute dans son propre thread.
 */
public class ClientHandler implements Runnable {
    private Socket s;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    int id;
    private Server server;

    /**
     * Constructeur du gestionnaire de client.
     * Initialise les flux de données, envoie l'ID au client et récupère son nom de joueur.
     *
     * @param s Le socket associé à la connexion du client
     * @param id L'identifiant unique attribué à ce joueur
     * @param server L'instance du serveur de jeu
     */
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

    /**
     * Boucle principale d'exécution du thread.
     * Écoute en permanence les commandes envoyées par le client et les transmet au serveur.
     */
    @Override
    public void run() {
        try {
            while (true) {
                String cmd = (String) in.readObject();
                server.traiterAction(id, cmd);
            }
        } catch (Exception e) {
            System.out.println("Joueur " + id + " déconnecté. " + e.getMessage());
            server.removeClient(this);
        } finally {
            try { s.close(); } catch (IOException e) {}
        }
    }

    /**
     * Envoie l'état actuel de la partie au client pour mettre à jour son interface.
     * Utilise reset() pour forcer la sérialisation des modifications de l'objet Partie.
     *
     * @param p L'instance de la partie à envoyer
     * @return true si l'envoi a réussi, false si une erreur est survenue (client déconnecté)
     */
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