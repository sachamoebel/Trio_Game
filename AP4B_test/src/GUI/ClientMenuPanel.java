package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import network.*;

public class ClientMenuPanel extends JPanel {
    private final ClientFrame frame;
    private final Color GRADIENT_TOP = new Color(80, 50, 230);
    private final Color GRADIENT_BOTTOM = new Color(40, 160, 255);
    private final Color BUTTON_TEXT_COLOR = new Color(60, 60, 200);
    private static final int BTN_WIDTH = 250;
    private static final int BTN_HEIGHT = 50;

    public ClientMenuPanel(ClientFrame frame) {
        this.frame = frame;
        initPanelSettings();

        JPanel contentBox = createContentBox();
        add(contentBox);
    }

    private void initPanelSettings() {
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    private JPanel createContentBox() {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setOpaque(false);

        box.add(createTitleLabel());
        box.add(Box.createVerticalStrut(40));

        box.add(createHostButton());
        box.add(Box.createVerticalStrut(15));

        box.add(createJoinButton());
        box.add(Box.createVerticalStrut(35));

        box.add(createCreditsLabel());
        box.add(Box.createVerticalStrut(15));

        return box;
    }

    private JLabel createTitleLabel() {
        JLabel title = new JLabel("TRIO");
        title.setFont(new Font("SansSerif", Font.BOLD, 80));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        return title;
    }

    private JButton createHostButton() {
        JButton btn = ClientFrame.createRoundedButton("Héberger une partie", true, BUTTON_TEXT_COLOR, BTN_WIDTH, BTN_HEIGHT);
        btn.addActionListener(e -> handleHostGame());
        return btn;
    }

    private JButton createJoinButton() {
        JButton btn = ClientFrame.createRoundedButton("Rejoindre une partie", false, BUTTON_TEXT_COLOR, BTN_WIDTH, BTN_HEIGHT);
        btn.addActionListener(e -> handleJoinGame());
        return btn;
    }

    private JLabel createCreditsLabel() {
        JLabel subtitle = new JLabel("Crée par VEURRIER Romain, RICCI Quentin et MOEBEL Sacha.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitle.setForeground(new Color(200, 220, 255));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        return subtitle;
    }

    private void handleHostGame() {
        String nbStr = JOptionPane.showInputDialog(this, "Nombre de joueurs (3-6) :", "3");
        if (nbStr != null) {
            try {
                int nb = Integer.parseInt(nbStr);
                if (nb < 3 || nb > 6) throw new Exception("Nombre de joueurs invalide");

                new Thread(() -> new Server(nb).start()).start();

                try { Thread.sleep(200); } catch (InterruptedException ex) { Thread.currentThread().interrupt(); }

                frame.startGame("localhost");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : Nombre de joueurs invalide (3-6).");
            }
        }
    }

    private void handleJoinGame() {
        String ip = JOptionPane.showInputDialog(this, "Adresse IP du serveur :", "localhost");
        if (ip != null && !ip.trim().isEmpty()) {
            frame.startGame(ip);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGradientBackground(g2);

        int w = getWidth();
        drawDecorativeCard(g2, 100, 150, -15, "7");
        drawDecorativeCard(g2, w - 150, 250, 10, "1");
    }

    private void drawGradientBackground(Graphics2D g2) {
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, GRADIENT_TOP, w, h, GRADIENT_BOTTOM);
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);
    }

    private void drawDecorativeCard(Graphics2D g2, int x, int y, double rotateDeg, String text) {
        Graphics2D gCopy = (Graphics2D) g2.create();
        gCopy.translate(x, y);
        gCopy.rotate(Math.toRadians(rotateDeg));

        gCopy.setColor(new Color(255, 255, 255, 30));
        gCopy.fillRoundRect(0, 0, 100, 160, 15, 15);

        gCopy.setColor(new Color(255, 255, 255, 50));
        gCopy.setFont(new Font("SansSerif", Font.BOLD, 40));
        FontMetrics fm = gCopy.getFontMetrics();
        int tw = fm.stringWidth(text);
        gCopy.drawString(text, (100 - tw) / 2, 95);

        gCopy.dispose();
    }
}