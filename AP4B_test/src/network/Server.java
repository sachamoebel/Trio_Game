package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import modele.*;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    Partie partie = new Partie();
    private boolean enPause = false;
    public int nbJoueursAttendus;


    static final int PORT = 12345;


    public Server(int n) {
        this.nbJoueursAttendus = n;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
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


    public synchronized void traiterAction(int playerId, String cmd) {
        if (partie.estTerminee() || enPause || playerId != partie.getJoueurCourant().getId()) return;

        String[] args = cmd.split(":");
        Carte c = null;

        // Logique de révélation
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
            sendStateToAll();
        }

        partie.verifierFinPartie();
    }

    private void lancerPauseEchec() {
        enPause = true;
        new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            partie.finTour();
            enPause = false;
            sendStateToAll();
        }).start();
    }

    public void removeClient(ClientHandler h) {
        clients.remove(h);
        broadcast("Joueur " + partie.getJoueurById(h.id) + " a quitté la partie.");
    }

    private void sendStateToAll() {
        clients.removeIf(c -> !c.send(partie));
    }

    public synchronized void broadcast(String msg) {
        partie.setMessage(msg);
        sendStateToAll();
    }

}