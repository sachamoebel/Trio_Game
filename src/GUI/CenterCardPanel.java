package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import modele.*;

/**
 * Panel personnalisé représentant la zone centrale du plateau de jeu.
 * Ce panel affiche les cartes communes que tous les joueurs peuvent tenter de révéler.
 */
public class CenterCardPanel extends RoundedPanel {
    private final ClientFrame frame;

    /**
     * Constructeur du panel de la zone centrale.
     *
     * @param frame La fenêtre principale permettant de communiquer les actions au serveur.
     */
    public CenterCardPanel(ClientFrame frame) {
        super(40);
        this.frame = frame;
        setPreferredSize(new Dimension(0, 160));
        setBackground(ClientFrame.PANEL_COLOR);
    }

    /**
     * Met à jour l'affichage des cartes présentes dans la zone centrale.
     *
     * @param zone La zone de cartes contenant les données à afficher.
     * @param isMyTurn Indicateur permettant de savoir si le joueur local peut interagir.
     */
    public void updateCards(ZoneCarte zone, boolean isMyTurn) {
        removeAll();
        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createCardsContainer(zone, isMyTurn), BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    /**
     * Crée le panel d'en-tête contenant le titre de la zone.
     *
     * @return Un JPanel contenant le label de titre.
     */
    private JPanel createHeaderPanel() {
        JLabel nameLbl = new JLabel("Zone centrale (Cartes restantes)");
        nameLbl.setFont(new Font("Arial", Font.BOLD, 16));
        nameLbl.setForeground(Color.WHITE);
        nameLbl.setBorder(new EmptyBorder(10, 15, 5, 0));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(nameLbl, BorderLayout.WEST);
        return headerPanel;
    }

    /**
     * Crée le conteneur principal pour les boutons de cartes.
     *
     * @param zone La zone contenant la liste des cartes.
     * @param isMyTurn Indicateur de tour pour l'interactivité.
     * @return Un JPanel contenant tous les boutons de cartes centrés.
     */
    private JPanel createCardsContainer(ZoneCarte zone, boolean isMyTurn) {
        JPanel cardsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        cardsRow.setOpaque(false);

        List<Carte> cartes = zone.getCartes();
        for (int i = 0; i < cartes.size(); i++) {
            cardsRow.add(createCardButton(cartes.get(i), i, isMyTurn));
        }

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(cardsRow);
        return wrapper;
    }

    /**
     * Crée un bouton représentant une carte spécifique.
     *
     * @param c La carte à représenter.
     * @param index L'index de la carte dans la zone (pour l'action réseau).
     * @param isMyTurn Définit si le bouton doit être cliquable ou non.
     * @return Un JButton configuré selon l'état de la carte.
     */
    private JButton createCardButton(Carte c, int index, boolean isMyTurn) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(GamePanel.LARGEUR_CARTE, GamePanel.HAUTEUR_CARTE));

        if (c.estVisible()) {
            applyFaceStyle(btn, c);
        } else {
            applyBackStyle(btn);
            if (isMyTurn) {
                setupInteractivity(btn, index);
            }
        }
        return btn;
    }

    /**
     * Applique le style visuel de la face recto d'une carte sur un bouton.
     *
     * @param btn Le bouton à styliser.
     * @param c La carte dont on veut afficher la valeur.
     */
    private void applyFaceStyle(JButton btn, Carte c) {
        int val = c.getType().getValeur();
        ImageIcon icon = ImageHelper.getCardIcon(val);

        if (icon != null) {
            btn.setIcon(icon);
        } else {
            btn.setText(c.getType().getDescription());
            btn.setBackground(c.getType().getCouleur());
        }
    }

    /**
     * Applique le style visuel du dos d'une carte sur un bouton.
     *
     * @param btn Le bouton à styliser.
     */
    private void applyBackStyle(JButton btn) {
        ImageIcon icon = ImageHelper.getBackIcon();

        if (icon != null) {
            btn.setIcon(icon);
        } else {
            btn.setText("?");
            btn.setBackground(new Color(100, 100, 200));
        }
        btn.setForeground(Color.WHITE);
    }

    /**
     * Configure les écouteurs d'événements et les effets visuels pour l'interaction.
     *
     * @param btn Le bouton à rendre interactif.
     * @param index L'index à envoyer au serveur lors du clic.
     */
    private void setupInteractivity(JButton btn, int index) {
        btn.setBorder(BorderFactory.createLineBorder(Color.BLUE, 4));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> frame.sendAction("CENTRE:" + index));
    }
}