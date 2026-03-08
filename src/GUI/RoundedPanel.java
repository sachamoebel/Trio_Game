package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Panel personnalisé permettant d'afficher un arrière-plan avec des bords arrondis.
 * Cette classe remplace le dessin standard du JPanel pour offrir une esthétique plus moderne.
 */
public class RoundedPanel extends JPanel {
    /** Le rayon de courbure des angles du panel */
    private final int radius;

    /**
     * Constructeur de RoundedPanel.
     *
     * @param radius Le rayon des arrondis (en pixels). Plus la valeur est grande,
     *               plus les coins sont courbés.
     */
    public RoundedPanel(int radius) {
        super();
        this.radius = radius;
        setOpaque(false);
    }

    /**
     * Redessine le composant en appliquant l'arrondi.
     * Utilise l'anti-aliasing pour garantir des bords lisses et propres.
     *
     * @param g L'objet Graphics utilisé pour le dessin.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.dispose();

        super.paintComponent(g);
    }
}