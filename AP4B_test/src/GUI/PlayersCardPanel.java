package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import modele.*;

public class PlayersCardPanel extends RoundedPanel {
    private final ClientFrame frame;

    public PlayersCardPanel(ZoneJoueur zone, int myId, int currentTurn, ClientFrame frame) {
        super(40);
        this.frame = frame;

        initPanelStyle(zone.getJoueur().getId() == currentTurn);

        Joueur j = zone.getJoueur();
        boolean isMe = (j.getId() == myId);

        add(createHeaderPanel(zone, j, isMe), BorderLayout.NORTH);
        add(createCardsContainer(zone, myId, currentTurn, isMe), BorderLayout.CENTER);
    }

    private void initPanelStyle(boolean isActive) {
        setLayout(new BorderLayout(10, 10));
        setBackground(isActive ? ClientFrame.ACTIVE_COLOR : ClientFrame.PANEL_COLOR);
    }

    private JPanel createHeaderPanel(ZoneJoueur zone, Joueur j, boolean isMe) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel nameLbl = new JLabel(j.getNom() + (isMe ? " (Moi)" : ""));
        nameLbl.setFont(new Font("Arial", Font.BOLD, 16));
        nameLbl.setForeground(Color.WHITE);
        nameLbl.setHorizontalAlignment(SwingConstants.LEFT);
        nameLbl.setBorder(new EmptyBorder(10, 10, 0, 0));

        headerPanel.add(nameLbl, BorderLayout.NORTH);
        headerPanel.add(new PanelTrio(zone.getTriosGagnes()), BorderLayout.CENTER);

        return headerPanel;
    }

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

    private void applyCardStyle(JLabel lbl, Carte c, boolean isMe, boolean isInteractive) {
        int width = ClientGamePanel.LARGEUR_CARTE;
        int height = ClientGamePanel.HAUTEUR_CARTE;
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

    private boolean isCardInteractive(int index, int minIdx, int maxIdx, int myId, int currentTurn, Carte c) {
        boolean isPlayable = (index == minIdx || index == maxIdx);
        return (myId == currentTurn) && !c.estVisible() && isPlayable;
    }

    private int findFirstHiddenIndex(List<Carte> cartes) {
        for (int i = 0; i < cartes.size(); i++) {
            if (!cartes.get(i).estVisible()) return i;
        }
        return -1;
    }

    private int findLastHiddenIndex(List<Carte> cartes) {
        for (int i = cartes.size() - 1; i >= 0; i--) {
            if (!cartes.get(i).estVisible()) return i;
        }
        return -1;
    }
}