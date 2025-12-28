package network;

import GUI.*;
import modele.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.List;



public class ClientNetwork {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ClientFrame frame;
    private int myId;


    public ClientNetwork(String ip, String name, ClientFrame frame) throws IOException {
        this.frame = frame;
        socket = new Socket(ip, Server.PORT);

        out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        out.flush();
        in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

        myId = in.readInt();
        frame.setTitle("Trio - Je suis Joueur " + myId);

        out.writeObject(name);
        out.flush();

        // Thread d'écoute
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

    public void sendAction(String action) {
        try { out.writeObject(action); out.flush(); } catch (IOException e) {}
    }
}

