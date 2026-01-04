package network;

import GUI.*;
import modele.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * Classe gérant la connexion réseau côté client.
 * Elle assure la communication avec le serveur, l'envoi des actions du joueur
 * et la réception des mises à jour de l'état de la partie.
 */
public class ClientNetwork {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int myId;

    /**
     * Constructeur de ClientNetwork.
     * Initialise la connexion, configure les flux d'entrée/sortie, récupère l'ID du joueur
     * et lance un thread pour écouter les mises à jour de la partie.
     *
     * @param ip L'adresse IP du serveur
     * @param name Le nom du joueur à transmettre au serveur
     * @param frame La fenêtre principale de l'interface graphique (GUI)
     * @throws IOException Si une erreur de connexion réseau survient
     */
    public ClientNetwork(String ip, String name, ClientFrame frame) throws IOException {
        socket = new Socket(ip, Server.PORT);

        out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        out.flush();
        in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

        myId = in.readInt();
        frame.setTitle("Trio - Je suis Joueur " + myId);

        out.writeObject(name);
        out.flush();

        new Thread(() -> {
            try {
                while (true) {
                    Partie p = (Partie) in.readObject();
                    SwingUtilities.invokeLater(() -> frame.updateGame(p, myId));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Déconnecté du serveur.");
            }
        }).start();
    }

    /**
     * Envoie une commande d'action au serveur.
     * Utilisé pour notifier le serveur lorsqu'une carte est sélectionnée.
     *
     * @param action La chaîne de caractères représentant l'action (ex: "CENTRE:5")
     */
    public void sendAction(String action) {
        try {
            out.writeObject(action);
            out.flush();
        } catch (IOException e) {
        }
    }
}