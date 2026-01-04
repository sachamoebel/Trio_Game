package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import modele.*;

/**
 * Panel personnalisé destiné à afficher visuellement les trios remportés par un joueur.
 * Chaque trio gagné est représenté par une miniature composée de trois petites cartes.
 */
public class PanelTrio extends JPanel {

    /**
     * Constructeur de PanelTrio.
     * Génère dynamiquement des miniatures pour chaque trio présent dans la liste fournie.
     * Les cartes sont affichées à un tiers de leur taille normale.
     *
     * @param trios La liste des objets Trio que le joueur a gagnés.
     */
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

                int w = GamePanel.LARGEUR_CARTE / 3;
                int h = GamePanel.HAUTEUR_CARTE / 3;

                CardStyler.afficherFace(lbl, val, desc, c, w, h);

                stack.add(lbl);
            }
            add(stack);
        }
    }
}