package GUI;

import network.ClientNetwork;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

import modele.*;

public class ClientFrame extends JFrame {
    public static final Color BACKGROUND_COLOR = new Color(6, 14, 48);
    public static final Color PANEL_COLOR = new Color(14, 28, 66);
    public static final Color ACTIVE_COLOR = new Color(41, 57, 138);

    private ClientNetwork clientNetwork;
    private ClientGamePanel gamePanel;

    public ClientFrame() {
        setTitle("Trio - Launcher");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new ClientMenuPanel(this));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void startGame(String ip) {
        try {
            String name = askName();
            setSize(1100, 800);
            setLocationRelativeTo(null);
            gamePanel = new ClientGamePanel(this);
            clientNetwork = new ClientNetwork(ip, name, this);
            setContentPane(gamePanel);
            revalidate();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Impossible de se connecter à " + ip);
        }
    }

    public String askName() {
        String nom = JOptionPane.showInputDialog(
                this,
                "Saissez votre nom de joueur :",
                "Joueur"
        );
        if (nom == null || nom.trim().isEmpty()) {
            nom = "Joueur";
        }
        return nom;
    }

    public void updateGame(Partie p, int myId) {
        if (gamePanel != null) gamePanel.refresh(p, myId);
    }

    public void sendAction(String cmd) {
        if (clientNetwork != null) clientNetwork.sendAction(cmd);
    }

    public static JButton createRoundedButton(String text, boolean isPrimary, Color color, int w, int h) {
        JButton btn = new JButton(text) {
            private boolean hovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                if (isPrimary) {
                    g2.setColor(hovered ? new Color(240, 240, 240) : Color.WHITE);
                    g2.fillRoundRect(0, 0, w, h, 20, 20); // 20px radius
                    g2.setColor(color);
                } else {
                    g2.setColor(hovered ? new Color(255, 255, 255, 40) : new Color(255, 255, 255, 20));
                    g2.fillRoundRect(0, 0, w, h, 20, 20);
                    g2.setColor(new Color(255, 255, 255, 100)); // Border color
                    g2.setStroke(new BasicStroke(1));
                    g2.drawRoundRect(0, 0, w - 1, h - 1, 20, 20);
                    g2.setColor(Color.WHITE);
                }

                g2.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                int stringWidth = fm.stringWidth(getText());
                int stringHeight = fm.getAscent();


                String fullText = getText();
                int fullWidth = fm.stringWidth(fullText);

                g2.drawString(fullText, (w - fullWidth) / 2, (h + stringHeight) / 2 - 4);
            }
        };

        btn.setPreferredSize(new Dimension(w, h));
        btn.setMaximumSize(new Dimension(250, 50));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        return btn;
    }
}
