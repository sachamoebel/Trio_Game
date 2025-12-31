package GUI;

import javax.swing.*;
import java.awt.*;

import modele.*;

public class CenterCardPanel extends JPanel {
    private ClientFrame frame;

    public CenterCardPanel(ClientFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(0, 300));
        setBorder(BorderFactory.createTitledBorder("Zone Centrale (Cartes restantes)"));
        setBackground(new Color(0, 89, 0, 77));
    }

    public void updateCards(ZoneCarte zone, boolean isMyTurn) {
        removeAll();
        // On affiche les cartes
        for (int i = 0; i < zone.getCartes().size(); i++) {
            Carte c = zone.getCartes().get(i);
            Color couleurCarte = (c.getType() != null) ? c.getType().getCouleur() : Color.WHITE;
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(ClientGamePanel.LARGEUR_CARTE, ClientGamePanel.HAUTEUR_CARTE));

            if (c.estVisible()) {
                btn.setText(c.getType().getDescription());
                btn.setBackground(couleurCarte);
            } else {
                btn.setText("?");
                btn.setBackground(new Color(100, 100, 200));
                btn.setForeground(Color.WHITE);
                if (isMyTurn) {
                    int index = i;
                    btn.addActionListener(e -> frame.sendAction("CENTRE:" + index));
                }
            }
            add(btn);
        }
        revalidate(); repaint();
    }
}