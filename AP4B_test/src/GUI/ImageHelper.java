package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageHelper {
    // Cache to store images so we don't reload them every time
    private static final Map<String, ImageIcon> cache = new HashMap<>();

    // Dimensions from your ClientGamePanel
    private static final int W = ClientGamePanel.LARGEUR_CARTE;
    private static final int H = ClientGamePanel.HAUTEUR_CARTE;

    public static ImageIcon getCardIcon(int typeValue) {
        return getIcon(typeValue + ".jpg");
    }

    public static ImageIcon getBackIcon() {
        return getIcon("dos.jpg");
    }

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
                System.err.println("Image not found: " + filename);
                return null;
            }
            ImageIcon originalIcon = new ImageIcon(url);
            Image img = originalIcon.getImage();
            Image newImg = img.getScaledInstance(W, H, Image.SCALE_SMOOTH); // High quality resize

            ImageIcon scaledIcon = new ImageIcon(newImg);
            cache.put(filename, scaledIcon);
            return scaledIcon;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
