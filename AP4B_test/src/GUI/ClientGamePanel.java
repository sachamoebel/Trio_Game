package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import modele.*;

public class ClientGamePanel extends JPanel {
    private ClientFrame frame;
    private CenterCardPanel centerPanel;
    private JPanel playersGrid;
    private JPanel scoresPanel;
    private JLabel lblInfo;
    private JTextArea textAreaScores;

    static final int LARGEUR_CARTE = 60;
    static final int HAUTEUR_CARTE = 100;

    public ClientGamePanel(ClientFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout()); // Main layout

        scoresPanel = new JPanel();
        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.Y_AXIS));
        scoresPanel.setPreferredSize(new Dimension(180, 0));
        scoresPanel.setBackground(new Color(245, 245, 245));
        scoresPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY));

        textAreaScores = new JTextArea();
        textAreaScores.setEditable(false);
        textAreaScores.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textAreaScores.setBackground(new Color(245, 245, 245));

        JScrollPane scrollScores = new JScrollPane(textAreaScores);
        scrollScores.setBorder(BorderFactory.createTitledBorder("Scores"));
        scrollScores.setOpaque(false);
        scrollScores.getViewport().setOpaque(false);

        scoresPanel.add(scrollScores);
        add(scoresPanel, BorderLayout.EAST);


        JPanel gameBoard = new JPanel(new BorderLayout());

        lblInfo = new JLabel("En attente...", SwingConstants.CENTER);
        lblInfo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblInfo.setBorder(new EmptyBorder(10,0,10,0));
        gameBoard.add(lblInfo, BorderLayout.NORTH);

        centerPanel = new CenterCardPanel(frame);
        gameBoard.add(centerPanel, BorderLayout.NORTH);

        playersGrid = new JPanel(new GridLayout(0, 1, 5, 5));
        playersGrid.setPreferredSize(new Dimension(0, 400));
        gameBoard.add(playersGrid, BorderLayout.CENTER);

        add(gameBoard, BorderLayout.CENTER);
    }

    public void refresh(Partie p, int myId) {
        refreshScores(p);

        String nomJoueurCourant = "Inconnu";
        if(p.getJoueurCourant() != null) {
            nomJoueurCourant = p.getJoueurCourant().getNom();
        }

        lblInfo.setText("Moi: " + myId + " | Tour: " + nomJoueurCourant + " | " + p.getMessage());


        centerPanel.updateCards(p.getPlateau().getZoneCentre(), p.getIndiceCourant() == myId);

        // Update Players Grid
        playersGrid.removeAll();
        for (Joueur j : p.getJoueurs()) {
            ZoneJoueur z = p.getPlateau().getZone(j);
            PlayersCardPanel pPanel = new PlayersCardPanel(z, myId, p.getIndiceCourant(), frame);
            playersGrid.add(pPanel);
        }
        playersGrid.revalidate();
        playersGrid.repaint();
    }

    private void refreshScores(Partie partie) {
        StringBuilder sb = new StringBuilder();

        for (Joueur joueur : partie.getJoueurs()) {
            sb.append("--- ").append(joueur.getNom()).append(" ---\n");
            sb.append("Score: ").append(partie.getScore(joueur)).append("\n");

            if (partie.getScore(joueur) > 0) {
                sb.append("Trios:\n");
                for (Trio trio : partie.getTriosGagnes(joueur)) {
                    sb.append(trio.getType().getDescription()).append(" : ").append(trio.getType().getValeur()).append("\n");
                }
            }
            sb.append("\n");
        }

        textAreaScores.setText(sb.toString());
    }
}