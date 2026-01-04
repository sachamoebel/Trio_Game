package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import modele.*;

/**
 * Panel représentant la zone visuelle d'un joueur.
 * Affiche le nom du joueur, les trios qu'il a déjà gagnés, ainsi que sa main actuelle.
 * Gère l'interactivité pour révéler les cartes (uniquement la plus petite ou la plus grande).
 */
public class PlayersCardPanel extends RoundedPanel {
    private final ClientFrame frame;

    /**
     * Constructeur de PlayersCardPanel.
     *
     * @param zone La zone de données du joueur concerné.
     * @param myId L'identifiant du joueur local (celui qui regarde l'écran).
     * @param currentTurn L'identifiant du joueur dont c'est actuellement le tour.
     * @param frame La fenêtre principale pour la transmission des actions réseau.
     */
    public PlayersCardPanel(ZoneJoueur zone, int myId, int currentTurn, ClientFrame frame) {
        super(40);
        this.frame = frame;

        initPanelStyle(zone.getJoueur().getId() == currentTurn);

        Joueur j = zone.getJoueur();
        boolean isMe = (j.getId() == myId);

        add(createHeaderPanel(zone, j, isMe), BorderLayout.NORTH);
        add(createCardsContainer(zone, myId, currentTurn, isMe), BorderLayout.CENTER);
    }

    /**
     * Initialise le style visuel du panel (layout et couleur de fond).
     *
     * @param isActive True si le joueur de cette zone est le joueur courant.
     */
    private void initPanelStyle(boolean isActive) {
        setLayout(new BorderLayout(10, 10));
        setBackground(isActive ? ClientFrame.ACTIVE_COLOR : ClientFrame.PANEL_COLOR);
    }

    /**
     * Crée l'en-tête de la zone contenant le nom du joueur et ses trios gagnés.
     *
     * @param zone La zone du joueur.
     * @param j L'objet Joueur.
     * @param isMe True si le joueur affiché est le joueur local.
     * @return Un JPanel contenant les informations d'identification.
     */
    private JPanel createHeaderPanel(ZoneJoueur zone, Joueur j, boolean isMe) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel nameLbl = new JLabel(j + (isMe ? " (Moi)" : ""));
        nameLbl.setFont(new Font("Arial", Font.BOLD, 16));
        nameLbl.setForeground(Color.WHITE);
        nameLbl.setHorizontalAlignment(SwingConstants.LEFT);
        nameLbl.setBorder(new EmptyBorder(10, 10, 0, 0));

        headerPanel.add(nameLbl, BorderLayout.NORTH);
        headerPanel.add(new PanelTrio(zone.getTriosGagnes()), BorderLayout.CENTER);

        return headerPanel;
    }

    /**
     * Crée le conteneur pour les cartes de la main du joueur.
     * Identifie quelles cartes sont "jouables" (MIN ou MAX).
     *
     * @param zone La zone du joueur.
     * @param myId ID du joueur local.
     * @param currentTurn ID du joueur dont c'est le tour.
     * @param isMe True si c'est la main du joueur local.
     * @return Un JPanel contenant les cartes.
     */
    private JPanel createCardsContainer(ZoneJoueur zone, int myId, int currentTurn, boolean isMe) {
        JPanel cardsBox = new JPanel(new GridLayout());
        cardsBox.setOpaque(false);

        JPanel cardsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        cardsRow.setBorder(new EmptyBorder(0, 0, 5, 0));
        cardsRow.setOpaque(false);

        List<Carte> cartes = zone.getCartes();
        int firstHiddenIndex = findFirstHiddenIndex(cartes);
        int lastHiddenIndex = findLastHiddenIndex(cartes);

        for (int i = 0; i < cartes.size(); i++) {
            Carte c = cartes.get(i);
            boolean isInteractive = isCardInteractive(i, firstHiddenIndex, lastHiddenIndex, myId, currentTurn, c);
            boolean isPlayableMin = (i == firstHiddenIndex);

            cardsRow.add(createCardLabel(c, isMe, isInteractive, isPlayableMin, zone.getJoueur().getId()));
        }

        cardsBox.add(cardsRow);
        return cardsBox;
    }

    /**
     * Crée le label visuel d'une carte et définit son comportement au clic.
     *
     * @param c La carte à afficher.
     * @param isMe True si c'est la carte du joueur local.
     * @param isInteractive True si la carte peut être cliquée.
     * @param isMin True s'il s'agit de la carte la plus petite (MIN).
     * @param playerId L'ID du propriétaire de la carte.
     * @return Un JLabel configuré.
     */
    private JLabel createCardLabel(Carte c, boolean isMe, boolean isInteractive, boolean isMin, int playerId) {
        JLabel lbl = new JLabel("", SwingConstants.CENTER);
        lbl.setOpaque(true);

        applyCardStyle(lbl, c, isMe, isInteractive);

        if (isInteractive) {
            setupInteractivity(lbl, playerId, isMin);
        } else {
            lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }

        return lbl;
    }

    /**
     * Applique le style (image ou dos) au label d'une carte selon sa visibilité.
     *
     * @param lbl Le label à styliser.
     * @param c La carte.
     * @param isMe True si le joueur local possède la carte (voit ses propres cartes).
     * @param isInteractive True si la carte est jouable.
     */
    private void applyCardStyle(JLabel lbl, Carte c, boolean isMe, boolean isInteractive) {
        int width = GamePanel.LARGEUR_CARTE;
        int height = GamePanel.HAUTEUR_CARTE;
        boolean showFace = c.estVisible() || isMe;

        if (showFace) {
            if (c.estVisible()) {
                width += 6;
                height += 10;
            }
            Color bg = c.estVisible() ? c.getType().getCouleur().darker() : c.getType().getCouleur();
            CardStyler.afficherFace(lbl, c.getType().getValeur(), c.getType().getDescription(), bg, width, height);
        } else {
            Color bgDos = isInteractive ? new Color(255, 251, 53) : new Color(200, 50, 50);
            CardStyler.afficherDos(lbl, bgDos, width, height);
        }

        lbl.setPreferredSize(new Dimension(width, height));
    }

    /**
     * Ajoute les écouteurs de souris pour envoyer l'action MIN ou MAX au serveur.
     *
     * @param lbl Le label cliquable.
     * @param playerId ID du joueur ciblé par l'action.
     * @param isMin True si l'action est de révéler la carte MIN, false pour MAX.
     */
    private void setupInteractivity(JLabel lbl, int playerId, boolean isMin) {
        lbl.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
        lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        String command = isMin ? "MIN" : "MAX";

        lbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.sendAction("JOUEUR:" + playerId + ":" + command);
            }
        });
    }

    /**
     * Détermine si une carte spécifique remplit les conditions d'interaction.
     *
     * @param index Position de la carte dans la main.
     * @param minIdx Index de la première carte cachée.
     * @param maxIdx Index de la dernière carte cachée.
     * @param myId ID du joueur local.
     * @param currentTurn ID du joueur dont c'est le tour.
     * @param c La carte.
     * @return True si la carte peut être révélée.
     */
    private boolean isCardInteractive(int index, int minIdx, int maxIdx, int myId, int currentTurn, Carte c) {
        boolean isPlayable = (index == minIdx || index == maxIdx);
        return (myId == currentTurn) && !c.estVisible() && isPlayable;
    }

    /**
     * Recherche l'index de la première carte non révélée (à gauche).
     *
     * @param cartes Liste des cartes du joueur.
     * @return L'index trouvé ou -1.
     */
    private int findFirstHiddenIndex(List<Carte> cartes) {
        for (int i = 0; i < cartes.size(); i++) {
            if (!cartes.get(i).estVisible()) return i;
        }
        return -1;
    }

    /**
     * Recherche l'index de la dernière carte non révélée (à droite).
     *
     * @param cartes Liste des cartes du joueur.
     * @return L'index trouvé ou -1.
     */
    private int findLastHiddenIndex(List<Carte> cartes) {
        for (int i = cartes.size() - 1; i >= 0; i--) {
            if (!cartes.get(i).estVisible()) return i;
        }
        return -1;
    }
}