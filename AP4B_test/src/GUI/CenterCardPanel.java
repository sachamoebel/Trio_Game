package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import modele.*;

public class CenterCardPanel extends RoundedPanel {
    private final ClientFrame frame;

    public CenterCardPanel(ClientFrame frame) {
        super(40);
        this.frame = frame;
        setPreferredSize(new Dimension(0, 160));
        setBackground(ClientFrame.PANEL_COLOR);
    }

    public void updateCards(ZoneCarte zone, boolean isMyTurn) {
        removeAll();
        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createCardsContainer(zone, isMyTurn), BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JPanel createHeaderPanel() {
        JLabel nameLbl = new JLabel("Zone Centrale (Cartes restantes)");
        nameLbl.setFont(new Font("Arial", Font.BOLD, 16));
        nameLbl.setForeground(Color.WHITE);
        nameLbl.setBorder(new EmptyBorder(10, 15, 5, 0));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(nameLbl, BorderLayout.WEST);
        return headerPanel;
    }

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

    private JButton createCardButton(Carte c, int index, boolean isMyTurn) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(ClientGamePanel.LARGEUR_CARTE, ClientGamePanel.HAUTEUR_CARTE));

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

    private void setupInteractivity(JButton btn, int index) {
        btn.setBorder(BorderFactory.createLineBorder(Color.BLUE, 4));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> frame.sendAction("CENTRE:" + index));
    }
}