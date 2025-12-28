package GUI;

import javax.swing.*;
import java.awt.*;

import modele.*;

public class PlayersCardPanel extends JPanel {
    public PlayersCardPanel(ZoneJoueur zone, int myId, int currentTurn, ClientFrame frame) {
        setLayout(new BorderLayout());
        Joueur j = zone.getJoueur();
        boolean isMe = (j.getId() == myId);
        boolean isActive = (j.getId() == currentTurn);

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(isActive ? Color.GREEN : Color.GRAY, 2),
                j.getNom() + (isMe ? " (Moi)" : "")
        ));
        setBackground(isMe ? new Color(240, 255, 240) : Color.WHITE);

        // 1. Zone des trios gagnés
        // On l'ajoute en haut du panel joueur
        PanelTrio pnlTrios = new PanelTrio(zone.getTriosGagnes());
        add(pnlTrios, BorderLayout.NORTH);

        // 2. Zone des cartes en main
        JPanel cardsBox = new JPanel();
        cardsBox.setOpaque(false);

        for (Carte c : zone.getCartes()) {
            Color couleurCarte = (c.getType() != null) ? c.getType().getCouleur() : Color.WHITE;
            String descriptionCarte = c.getType().getDescription();

            JLabel lbl = new JLabel("", SwingConstants.CENTER);
            lbl.setPreferredSize(new Dimension(ClientGamePanel.LARGEUR_CARTE, ClientGamePanel.HAUTEUR_CARTE));
            lbl.setOpaque(true);
            lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            if (c.estVisible()) {
                lbl.setText(descriptionCarte);
                lbl.setBackground(couleurCarte.darker());
                lbl.setPreferredSize(new Dimension(ClientGamePanel.LARGEUR_CARTE+5, ClientGamePanel.HAUTEUR_CARTE+10));
            } else if (isMe) {
                lbl.setText(descriptionCarte);
                lbl.setBackground(couleurCarte);
            } else {
                lbl.setText("?");
                lbl.setBackground(new Color(200, 50, 50));
                lbl.setForeground(Color.WHITE);
            }
            cardsBox.add(lbl);
        }
        add(cardsBox, BorderLayout.NORTH);

        // 3. Boutons d'action (inchangé)
        if (myId == currentTurn && !zone.getCartes().isEmpty()) {
            JPanel btnPanel = new JPanel(new GridLayout(1, 2));
            JButton btnMin = new JButton("Min");
            JButton btnMax = new JButton("Max");

            btnMin.addActionListener(e -> frame.sendAction("JOUEUR:" + j.getId() + ":MIN"));
            btnMax.addActionListener(e -> frame.sendAction("JOUEUR:" + j.getId() + ":MAX"));

            btnPanel.add(btnMin); btnPanel.add(btnMax);
            add(btnPanel, BorderLayout.SOUTH);
        }
    }
}


