package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import modele.*;

/**
 * Serveur de jeu gérant les connexions réseau et la synchronisation de la partie Trio
 */
public class Server {
    private List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    Partie partie = new Partie();
    private boolean enPause = false;
    public int nbJoueursAttendus;

    /** Port de communication du serveur */
    static final int PORT = 12345;

    /**
     * Constructeur du serveur
     * @param n Le nombre de joueurs requis pour commencer la partie
     */
    public Server(int n) {
        this.nbJoueursAttendus = n;
    }

    /**
     * Démarre le serveur, accepte les connexions des clients jusqu'à atteindre
     * le nombre attendu, puis initialise la partie.
     */
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Serveur démarré sur port " + PORT);

            int id = 0;
            while (clients.size() < nbJoueursAttendus) {
                Socket s = serverSocket.accept();
                partie.ajouterJoueur(new Joueur(id));
                partie.ajouterZoneJoueur(id);
                partie.ajouteScore(id, 0);

                ClientHandler h = new ClientHandler(s, id, this);
                clients.add(h);
                new Thread(h).start();

                broadcast("Joueur " + partie.getJoueurById(id) + " connecté (" + clients.size() + "/" + nbJoueursAttendus + ")");
                id++;
            }

            partie.demarrerPartie();
            sendStateToAll();

        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Traite une action de jeu envoyée par un client.
     * Gère la logique de révélation des cartes au centre ou dans la main d'un joueur.
     * @param playerId L'identifiant du joueur qui effectue l'action
     * @param cmd La commande brute reçue du réseau (ex: "CENTRE:2" ou "JOUEUR:1:MIN")
     */
    public synchronized void traiterAction(int playerId, String cmd) {
        if (partie.estTerminee() || enPause || playerId != partie.getJoueurCourant().getId()) return;

        String[] args = cmd.split(":");
        Carte c = null;

        if (args[0].equals("CENTRE")) {
            int idx = Integer.parseInt(args[1]);
            if (idx < partie.getPlateau().getCartesCentre().size()) {
                c = partie.getPlateau().getZoneCentre().reveler(idx);
            }
        } else if (args[0].equals("JOUEUR")) {
            int targetId = Integer.parseInt(args[1]);
            String type = args[2];
            ZoneJoueur z = partie.getPlateau().getZone(partie.getJoueurById(targetId));
            if (z != null) {
                if (type.equals("MIN")) c = z.revelerMin();
                else c = z.revelerMax();
            }
        }

        if (c != null) {
            partie.ajouteCarteReveleeTour(c);
            if (!partie.verifieEtatTour()) {
                lancerPauseEchec();
            }
        }
        partie.verifierFinPartie();
        sendStateToAll();
    }

    /**
     * Lance une pause temporaire après une erreur (cartes différentes).
     * Cela permet aux joueurs de voir les cartes avant qu'elles ne soient recachées.
     */
    private void lancerPauseEchec() {
        enPause = true;
        new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            partie.finTour();
            enPause = false;
            sendStateToAll();
        }).start();
    }

    /**
     * Retire un gestionnaire de client de la liste des connectés
     * @param h Le gestionnaire de client à supprimer
     */
    public void removeClient(ClientHandler h) {
        clients.remove(h);
        broadcast("Joueur " + partie.getJoueurById(h.id) + " a quitté la partie.");
    }

    /**
     * Envoie l'objet Partie (l'état du jeu) à tous les clients connectés.
     * Supprime automatiquement les clients déconnectés.
     */
    private void sendStateToAll() {
        clients.removeIf(c -> !c.send(partie));
    }

    /**
     * Modifie le message d'état général et diffuse la mise à jour à tout le monde
     * @param msg Le message à diffuser
     */
    public synchronized void broadcast(String msg) {
        partie.setMessage(msg);
        sendStateToAll();
    }

}