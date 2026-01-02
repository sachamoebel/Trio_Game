package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import modele.*;

public class ClientGamePanel extends JPanel {
    private final ClientFrame frame;
    private CenterCardPanel centerPanel;
    private JPanel playersGrid;
    private JPanel scoresPanel;
    private JLabel lblInfo;
    private JTextArea textAreaScores;

    private String dernierMessageTraite = "";
    private boolean finDePartieAffichee = false;

    static final int LARGEUR_CARTE = 65;
    static final int HAUTEUR_CARTE = 100;
    static final int HAUTEUR_ZONE = 200;

    public ClientGamePanel(ClientFrame frame) {
        this.frame = frame;

        initMainPanel();

        JPanel header = createHeaderSection();
        add(header, BorderLayout.NORTH);

        createScoreSection();

        JPanel board = createGameSection();
        add(board, BorderLayout.CENTER);
    }

    private void initMainPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(ClientFrame.BACKGROUND_COLOR);
    }

    private JPanel createHeaderSection() {
        lblInfo = new JLabel("En attente...", SwingConstants.CENTER);
        lblInfo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblInfo.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, ClientFrame.BACKGROUND_COLOR));
        lblInfo.setOpaque(true);
        lblInfo.setBackground(ClientFrame.BACKGROUND_COLOR);
        lblInfo.setForeground(Color.WHITE);

        JPanel btnContainer = createHeaderButtons();

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(20, 0, 15, 0));
        headerPanel.add(lblInfo, BorderLayout.CENTER);
        headerPanel.add(btnContainer, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createHeaderButtons() {
        JButton btnToggleScore = ClientFrame.createRoundedButton("Afficher scores", false, new Color(0x7C8EF6), 140, 50);
        btnToggleScore.addActionListener(e -> {
            boolean isVisible = scoresPanel.isVisible();
            scoresPanel.setVisible(!isVisible);
            btnToggleScore.setText(isVisible ? "Afficher scores" : "Cacher scores");
        });

        JButton btnRegles = ClientFrame.createRoundedButton("Afficher les règles", true, new Color(0x7C8EF6), 200, 50);
        btnRegles.addActionListener(e -> afficherRegles());

        JPanel btnContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnContainer.setOpaque(false);
        btnContainer.add(btnToggleScore);
        btnContainer.add(btnRegles);

        return btnContainer;
    }

    private void createScoreSection() {
        JPanel contentArea = new JPanel(new BorderLayout(20, 0));
        contentArea.setOpaque(false);

        scoresPanel = new JPanel();
        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.Y_AXIS));
        scoresPanel.setPreferredSize(new Dimension(200, 0));
        scoresPanel.setBackground(ClientFrame.BACKGROUND_COLOR);
        scoresPanel.setVisible(false);
        scoresPanel.setBorder(new EmptyBorder(0, 0, 0, 20));
        scoresPanel.setOpaque(false);

        textAreaScores = new JTextArea();
        textAreaScores.setEditable(false);
        textAreaScores.setFont(new Font("Arial", Font.BOLD, 16));
        textAreaScores.setForeground(Color.white);
        textAreaScores.setBackground(ClientFrame.PANEL_COLOR);
        textAreaScores.setOpaque(false);

        JLabel lbl = new JLabel("SCORES");
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        lbl.setForeground(Color.white);

        RoundedPanel scrollScores = new RoundedPanel(20);
        scrollScores.setBackground(ClientFrame.PANEL_COLOR);
        scrollScores.add(lbl, FlowLayout.LEFT);
        scrollScores.add(textAreaScores);
        scrollScores.setOpaque(false);

        scoresPanel.add(Box.createVerticalStrut(10));
        scoresPanel.add(scrollScores);

        contentArea.add(scoresPanel, BorderLayout.EAST);
        add(scoresPanel, BorderLayout.EAST);
    }

    private JPanel createGameSection() {
        JPanel gameBoard = new JPanel(new BorderLayout(0, 30));

        Border lineBorder = BorderFactory.createMatteBorder(30, 40, 20, 40, ClientFrame.BACKGROUND_COLOR);
        gameBoard.setBorder(lineBorder);
        gameBoard.setBackground(ClientFrame.BACKGROUND_COLOR);
        gameBoard.setOpaque(false);

        centerPanel = new CenterCardPanel(frame);
        centerPanel.setPreferredSize(new Dimension(0, HAUTEUR_ZONE));
        gameBoard.add(centerPanel, BorderLayout.NORTH);

        playersGrid = new JPanel();
        playersGrid.setLayout(new BoxLayout(playersGrid, BoxLayout.Y_AXIS));
        playersGrid.setBackground(ClientFrame.BACKGROUND_COLOR);

        JScrollPane scrollPlayers = new JScrollPane(playersGrid);
        scrollPlayers.setBorder(null);
        scrollPlayers.getVerticalScrollBar().setUnitIncrement(16);

        gameBoard.add(scrollPlayers, BorderLayout.CENTER);

        return gameBoard;
    }

    public void refresh(Partie p, int myId) {
        refreshScores(p);
        refreshInfoLabel(p, myId);

        centerPanel.updateCards(p.getPlateau().getZoneCentre(), p.getIndiceCourant() == myId);

        refreshPlayersGrid(p, myId);

        gererPopups(p);
    }

    private void refreshInfoLabel(Partie p, int myId) {
        String nomJoueurCourant = "Inconnu";
        if(p.getJoueurCourant() != null) {
            nomJoueurCourant = p.getJoueurCourant().getNom();
        }
        lblInfo.setText("Moi: " + myId + " | Tour: " + nomJoueurCourant + " | " + p.getMessage());
    }

    private void refreshPlayersGrid(Partie p, int myId) {
        playersGrid.removeAll();

        List<Joueur> tousLesJoueurs = p.getJoueurs();
        Joueur monJoueur = null;
        List<Joueur> autresJoueurs = new ArrayList<>();

        for (Joueur j : tousLesJoueurs) {
            ZoneJoueur z = p.getPlateau().getZone(j);
            if (z.getJoueur().getId() == p.getIndiceCourant()) {
                monJoueur = j;
            } else {
                autresJoueurs.add(j);
            }
        }

        if (monJoueur != null) {
            ZoneJoueur z = p.getPlateau().getZone(monJoueur);
            ajouterPanelJoueur(z, myId, p.getIndiceCourant());
        }

        for (Joueur j : autresJoueurs) {
            playersGrid.add(Box.createVerticalStrut(20));
            ZoneJoueur z = p.getPlateau().getZone(j);
            ajouterPanelJoueur(z, myId, p.getIndiceCourant());
        }

        playersGrid.revalidate();
        playersGrid.repaint();
    }

    private void ajouterPanelJoueur(ZoneJoueur z, int myId, int currentTurnIndex) {
        PlayersCardPanel pPanel = new PlayersCardPanel(z, myId, currentTurnIndex, frame);
        pPanel.setPreferredSize(new Dimension(0, HAUTEUR_ZONE));
        pPanel.setMinimumSize(new Dimension(0, HAUTEUR_ZONE));
        pPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        playersGrid.add(pPanel);
        playersGrid.add(Box.createVerticalStrut(5));
    }

    private void refreshScores(Partie partie) {
        StringBuilder sb = new StringBuilder();

        for (Joueur joueur : partie.getJoueurs()) {
            sb.append("--- ").append(joueur).append(" ---\n");
            sb.append("Score: ").append(partie.getScore(joueur)).append("\n");

            if (partie.getScore(joueur) > 0) {
                sb.append("  Trios:\n");
                for (Trio trio : partie.getTriosGagnes(joueur)) {
                    sb.append(trio.getType().getDescription()).append("\n");
                }
            }
            sb.append("\n");
        }
        textAreaScores.setText(sb.toString());
    }

    private void afficherRegles() {
        String htmlContent = "<html><body style='width: 350px; font-family: sans-serif;'>" +
                "<h2 style='text-align: center; color: #E6007E;'>RÈGLES DU JEU</h2>" +
                "<p style='text-align: center;'><i>Qui sera le plus rusé pour déduire quels numéros se cachent sous les cartes ?</i></p>" +
                "<hr>" +
                "<h3 style='color: #008080;'>&#10102; DÉVOILEZ 2 CARTES NUMÉRO :</h3>" +
                "<ul>" +
                "<li>Sur la table (au centre),</li>" +
                "<li><b>OU</b> dans la main d'un joueur...</li>" +
                "</ul>" +
                "<p style='margin-left: 20px; color: #D35400;'><b>MAIS ATTENTION :</b><br>" +
                "Seulement le plus <b>PETIT</b> numéro ou le plus <b>GRAND</b> !</p>" +
                "<h3 style='color: #E67E22;'>&#10103; REJOUEZ !</h3>" +
                "<p>Si les 2 cartes dévoilées sont <b>identiques</b>, vous continuez votre tour !</p>" +
                "<h3 style='color: #27AE60;'>&#10104; GAGNEZ LE TRIO</h3>" +
                "<p>Trouvez les <b>3 cartes identiques</b> et gagnez ce trio !</p>" +
                "<hr>" +
                "<p style='text-align: center; background-color: #FFFFE0; padding: 5px; border: 1px solid orange;'>" +
                "<b>VICTOIRE :</b><br>" +
                "Le premier à obtenir <b>3 Trios</b><br>(ou le Trio de 7) gagne la partie !" +
                "</p>" +
                "</body></html>";

        JLabel label = new JLabel(htmlContent);
        JOptionPane.showMessageDialog(frame, label, "Règles Officielles Trio", JOptionPane.PLAIN_MESSAGE);
    }

    private void gererPopups(Partie p) {
        String msg = p.getMessage();

        if (msg == null || msg.equals(dernierMessageTraite)) {
            return;
        }
        dernierMessageTraite = msg;

        if (msg.startsWith("TRIO de")) {
            new Thread(() -> {
                try {
                    String[] mots = msg.split(" ");
                    int valeurCarte = Integer.parseInt(mots[2]);
                    ImageIcon icon = ImageHelper.getCardIcon(valeurCarte);

                    if (icon != null) {
                        Image img = icon.getImage().getScaledInstance(120, 200, Image.SCALE_SMOOTH);
                        icon = new ImageIcon(img);
                    }
                    JOptionPane.showMessageDialog(this, msg, "Trio Validé !", JOptionPane.INFORMATION_MESSAGE, icon);
                } catch (Exception e) {
                    System.err.println("Erreur lecture trio : " + e.getMessage());
                }
            }).start();
        }

        if (p.estTerminee() && !finDePartieAffichee) {
            finDePartieAffichee = true;
            new Thread(() -> {
                JOptionPane.showMessageDialog(this, p.getMessage() + "\n\nFélicitations !", "Partie Terminée", JOptionPane.WARNING_MESSAGE);
            }).start();
        }
    }
}