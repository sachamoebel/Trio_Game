package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe utilitaire pour la gestion des images du jeu.
 * Elle permet de charger, redimensionner et mettre en cache les icônes des cartes.
 */
public class ImageHelper {
    // Cache permettant de ne pas recharger et redimensionner plusieurs fois la même image
    private static final Map<String, ImageIcon> cache = new HashMap<>();

    private static final int W = GamePanel.LARGEUR_CARTE;
    private static final int H = GamePanel.HAUTEUR_CARTE;

    /**
     * Récupère l'icône correspondant à la face visible d'une carte
     * @param typeValue La valeur numérique de la carte (utilisée pour le nom du fichier)
     * @return Un ImageIcon redimensionné de la carte, ou null si l'image est introuvable
     */
    public static ImageIcon getCardIcon(int typeValue) {
        return getIcon(typeValue + ".jpg");
    }

    /**
     * Récupère l'icône correspondant au dos des cartes
     * @return Un ImageIcon redimensionné du dos de la carte
     */
    public static ImageIcon getBackIcon() {
        return getIcon("dos.jpg");
    }

    /**
     * Charge une image depuis les ressources, la redimensionne avec une haute qualité
     * et la stocke en cache.
     * @param filename Le nom du fichier image à charger
     * @return L'icône redimensionnée ou null en cas d'erreur de chargement
     */
    private static ImageIcon getIcon(String filename) {
        if (cache.containsKey(filename)) {
            return cache.get(filename);
        }

        try {
            URL url = ImageHelper.class.getClassLoader().getResource("resources/" + filename);
            if (url == null) {
                url = ImageHelper.class.getClassLoader().getResource(filename);
            }

            if (url == null) {
                System.err.println("Image non trouvée : " + filename);
                return null;
            }

            ImageIcon originalIcon = new ImageIcon(url);
            Image img = originalIcon.getImage();

            Image newImg = img.getScaledInstance(W, H, Image.SCALE_SMOOTH);

            ImageIcon scaledIcon = new ImageIcon(newImg);
            cache.put(filename, scaledIcon);

            return scaledIcon;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}