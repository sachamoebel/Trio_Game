package GUI;

import network.ClientNetwork;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.List;

import modele.*;

public class ClientFrame extends JFrame {
    private ClientNetwork clientNetwork;
    private ClientGamePanel gamePanel;

    public ClientFrame() {
        setTitle("Trio - Launcher");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Au démarrage, on affiche le Menu
        setContentPane(new ClientMenuPanel(this));
        setVisible(true);
    }

    public void startGame(String ip) {
        try {
            String name = askName();
            gamePanel = new ClientGamePanel(this);
            clientNetwork = new ClientNetwork(ip, name, this);

            setContentPane(gamePanel);
            revalidate();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Impossible de se connecter à " + ip);
        }
    }

    public String askName() {
        String nom = JOptionPane.showInputDialog(
                this,
                "Saissez votre nom de joueur :",
                "Joueur"
        );
        if (nom == null || nom.trim().isEmpty()) {
            nom = "Joueur";
        }
        return nom;
    }

    public void updateGame(Partie p, int myId) {
        if (gamePanel != null) gamePanel.refresh(p, myId);
    }

    public void sendAction(String cmd) {
        if (clientNetwork != null) clientNetwork.sendAction(cmd);
    }
}
