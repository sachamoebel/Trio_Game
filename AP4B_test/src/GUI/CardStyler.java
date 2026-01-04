package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Classe utilitaire pour appliquer un style visuel aux composants représentant des cartes.
 * Elle gère l'affichage de la face recto (valeur) ou verso (dos) sur des JLabels.
 */
public class CardStyler {

    /**
     * Configure un JLabel pour afficher la face visible d'une carte.
     * Si l'image correspondante est trouvée, elle est redimensionnée et affichée.
     * Sinon, un style par défaut avec du texte et une couleur de fond est appliqué.
     *
     * @param lbl Le JLabel à styliser
     * @param val La valeur numérique de la carte
     * @param description Le texte à afficher si l'image est absente
     * @param couleur La couleur de fond si l'image est absente
     * @param targetWidth La largeur cible du composant
     * @param targetHeight La hauteur cible du composant
     */
    public static void afficherFace(JLabel lbl, int val, String description, Color couleur, int targetWidth, int targetHeight) {
        ImageIcon originalIcon = ImageHelper.getCardIcon(val);

        if (originalIcon != null) {
            Image img = originalIcon.getImage();
            Image resizedImg = img.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

            lbl.setIcon(new ImageIcon(resizedImg));
            lbl.setText("");
            lbl.setOpaque(false);
        } else {
            lbl.setIcon(null);
            lbl.setText(description);
            lbl.setBackground(couleur);
            lbl.setOpaque(true);
        }

        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lbl.setPreferredSize(new Dimension(targetWidth, targetHeight));
    }

    /**
     * Configure un JLabel pour afficher le dos d'une carte.
     *
     * @param lbl Le JLabel à styliser
     * @param couleurFond La couleur de fond à appliquer par défaut
     * @param targetWidth La largeur cible du composant
     * @param targetHeight La hauteur cible du composant
     */
    public static void afficherDos(JLabel lbl, Color couleurFond, int targetWidth, int targetHeight) {
        ImageIcon icon = ImageHelper.getBackIcon();
        if (icon != null) {
            lbl.setIcon(icon);
            lbl.setText("");
        } else {
            lbl.setIcon(null);
            lbl.setText("?");
        }
        lbl.setBackground(couleurFond);
        lbl.setForeground(Color.WHITE);

        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lbl.setPreferredSize(new Dimension(targetWidth, targetHeight));
    }
}