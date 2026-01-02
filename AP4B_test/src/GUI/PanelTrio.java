package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import modele.*;

public class PanelTrio extends JPanel {

    public PanelTrio(List<Trio> trios) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        setBorder(new EmptyBorder(10, 10, 0, 0));
        setOpaque(false);

        if (trios.isEmpty()) {
            add(Box.createVerticalStrut(40));
            return;
        }

        for (Trio t : trios) {
            JPanel stack = new JPanel(new GridLayout(1, 3, 1, 0));
            stack.setOpaque(false);

            int val = t.getType().getValeur();
            TypeCarte type = TypeCarte.parValeur(val);
            Color c = (type != null) ? type.getCouleur() : Color.WHITE;
            String desc = (type != null) ? type.getDescription() : "" + val;


            for (int i = 0; i < 3; i++) {
                JLabel lbl = new JLabel();

                int w = ClientGamePanel.LARGEUR_CARTE/3;
                int h = ClientGamePanel.HAUTEUR_CARTE/3;

                CardStyler.afficherFace(lbl, val, desc, c, w, h);

                stack.add(lbl);
            }
            add(stack);
        }
    }
}
