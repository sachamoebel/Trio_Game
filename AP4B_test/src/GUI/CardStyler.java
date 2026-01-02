package GUI;

import javax.swing.*;
import java.awt.*;

public class CardStyler {

    /**
     * Renders a card face and forces the image to fit the specified width/height.
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
    }
}