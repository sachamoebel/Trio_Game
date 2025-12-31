package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

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
        PanelTrio pnlTrios = new PanelTrio(zone.getTriosGagnes());
        add(pnlTrios, BorderLayout.NORTH);

        // 2. Zone des cartes en main
        JPanel cardsBox = new JPanel(new GridLayout());
        cardsBox.setOpaque(false);

        JPanel cardsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        cardsRow.setOpaque(false);

        List<Carte> cartes = zone.getCartes();

        // --- CORRECTION : CALCULER LES INDICES CLIQUABLES ---
        int indexFirstHidden = -1;
        int indexLastHidden = -1;

        // Trouver le premier caché (Nouveau MIN)
        for (int i = 0; i < cartes.size(); i++) {
            if (!cartes.get(i).estVisible()) {
                indexFirstHidden = i;
                break;
            }
        }

        // Trouver le dernier caché (Nouveau MAX)
        for (int i = cartes.size() - 1; i >= 0; i--) {
            if (!cartes.get(i).estVisible()) {
                indexLastHidden = i;
                break;
            }
        }
        // ----------------------------------------------------

        for (int i = 0; i < cartes.size(); i++) {
            Carte c = cartes.get(i);

            // Une carte est "Jouable" si elle correspond aux nouveaux indices calculés
            boolean isPlayableMin = (i == indexFirstHidden);
            boolean isPlayableMax = (i == indexLastHidden);

            // Est-elle interactive ? (Mon tour + Carte Cachée + Est une extrémité cachée)
            boolean isInteractive = (myId == currentTurn) && !c.estVisible() && (isPlayableMin || isPlayableMax);

            Color couleurCarte = (c.getType() != null) ? c.getType().getCouleur() : Color.WHITE;
            String descriptionCarte = c.getType().getDescription();

            JLabel lbl = new JLabel("", SwingConstants.CENTER);
            lbl.setPreferredSize(new Dimension(ClientGamePanel.LARGEUR_CARTE, ClientGamePanel.HAUTEUR_CARTE));
            lbl.setOpaque(true);

            if (c.estVisible()) {
                // CARTE RÉVÉLÉE (Déjà jouée)
                lbl.setText(descriptionCarte);
                lbl.setBackground(couleurCarte.darker());
                lbl.setPreferredSize(new Dimension(ClientGamePanel.LARGEUR_CARTE + 5, ClientGamePanel.HAUTEUR_CARTE + 10));
                lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            } else if (isInteractive) {
                if (isMe) {
                    lbl.setText(descriptionCarte);
                    lbl.setBackground(couleurCarte);
                } else {
                    lbl.setText("?");
                    lbl.setBackground(new Color(255, 251, 53));
                    lbl.setForeground(Color.WHITE);
                }

                // Bordure Bleue + Main
                lbl.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                // On détermine quelle commande envoyer (MIN ou MAX) selon l'index trouvé
                // Note : Si il ne reste qu'une carte, elle est à la fois Min et Max, "MIN" fonctionne par défaut.
                String commandType = isPlayableMin ? "MIN" : "MAX";

                lbl.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        frame.sendAction("JOUEUR:" + j.getId() + ":" + commandType);
                    }
                });

            } else {
                lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                if (isMe) {
                    lbl.setText(descriptionCarte);
                    lbl.setBackground(couleurCarte);
                } else {
                    lbl.setText("?");
                    lbl.setBackground(new Color(200, 50, 50));
                    lbl.setForeground(Color.WHITE);
                }
            }
            cardsRow.add(lbl);
        }

        cardsBox.add(cardsRow);
        add(cardsBox, BorderLayout.CENTER);
    }
}