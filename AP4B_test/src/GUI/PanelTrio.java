package GUI;

import modele.Trio;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelTrio extends JPanel {
    public PanelTrio(List<Trio> trios) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        setOpaque(false);

        if (trios.isEmpty()) {
            // Espace vide pour garder la hauteur constante
            add(Box.createVerticalStrut(40));
            return;
        }

        for (Trio t : trios) {
            // Pour chaque trio gagné, on dessine un petit groupe de 3 cartes
            JPanel stack = new JPanel(new GridLayout(1, 3, 1, 0));
            stack.setOpaque(false);
            stack.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            String text = t.getType().getDescription();
            Color couleurCarte = (t.getType() != null) ? t.getType().getCouleur() : Color.YELLOW;

            for (int i = 0; i < 3; i++) {
                JLabel lbl = new JLabel(text, SwingConstants.CENTER);
                lbl.setFont(new Font("Arial", Font.BOLD, 10));
                lbl.setPreferredSize(new Dimension(20, 30));
                lbl.setOpaque(true);
                lbl.setBackground(couleurCarte); // Vert victoire
                lbl.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                stack.add(lbl);
            }
            add(stack);
        }
    }
}