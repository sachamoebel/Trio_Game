package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import modele.*;

public class ClientGamePanel extends JPanel {
    private ClientFrame frame;
    private CenterCardPanel centerPanel;
    private JPanel playersGrid;
    private JPanel scoresPanel;
    private JLabel lblInfo;
    private JTextArea textAreaScores;

    // Dimensions pour l'uniformité
    static final int LARGEUR_CARTE = 60;
    static final int HAUTEUR_CARTE = 100;
    static final int HAUTEUR_ZONE = 160; // Hauteur fixe pour toutes les zones (Joueurs et Centre)

    public ClientGamePanel(ClientFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        // --- 1. PANNEAU SCORES (EST) ---
        scoresPanel = new JPanel();
        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.Y_AXIS));
        scoresPanel.setPreferredSize(new Dimension(180, 0));
        scoresPanel.setBackground(new Color(245, 245, 245));
        scoresPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY));

        textAreaScores = new JTextArea();
        textAreaScores.setEditable(false);
        textAreaScores.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textAreaScores.setBackground(new Color(245, 245, 245));

        JButton btnRegles = new JButton("AFFICHER LES RÈGLES"); // Petit clin d'œil à l'image
        btnRegles.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegles.setBackground(new Color(50, 50, 50)); // Fond noir comme la boite
        btnRegles.setForeground(Color.WHITE);
        btnRegles.setFont(new Font("Arial", Font.BOLD, 12));
        btnRegles.setFocusPainted(false);
        btnRegles.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnRegles.addActionListener(e -> afficherRegles());

        JScrollPane scrollScores = new JScrollPane(textAreaScores);
        scrollScores.setBorder(BorderFactory.createTitledBorder("Scores"));
        scrollScores.setOpaque(false);
        scrollScores.getViewport().setOpaque(false);

        scoresPanel.add(Box.createVerticalStrut(10));
        scoresPanel.add(btnRegles);
        scoresPanel.add(Box.createVerticalStrut(10));
        scoresPanel.add(scrollScores);
        add(scoresPanel, BorderLayout.EAST);

        // --- 2. HEADER INFO (NORD) ---
        lblInfo = new JLabel("En attente...", SwingConstants.CENTER);
        lblInfo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblInfo.setBorder(new EmptyBorder(10,0,10,0));
        add(lblInfo, BorderLayout.NORTH);

        // --- 3. ZONE DE JEU (CENTRE) ---
        JPanel gameBoard = new JPanel(new BorderLayout());

        // A. Zone Centre (Fixe en haut)
        centerPanel = new CenterCardPanel(frame);
        // On force la taille pour respecter la règle "Même hauteur que les joueurs"
        centerPanel.setPreferredSize(new Dimension(0, HAUTEUR_ZONE));
        gameBoard.add(centerPanel, BorderLayout.NORTH);

        // B. Zone Joueurs (Scrollable)
        playersGrid = new JPanel();
        // BoxLayout Vertical pour empiler proprement les zones
        playersGrid.setLayout(new BoxLayout(playersGrid, BoxLayout.Y_AXIS));

        JScrollPane scrollPlayers = new JScrollPane(playersGrid);
        scrollPlayers.setBorder(null); // Pas de bordure moche
        scrollPlayers.getVerticalScrollBar().setUnitIncrement(16); // Scroll plus fluide

        gameBoard.add(scrollPlayers, BorderLayout.CENTER);

        add(gameBoard, BorderLayout.CENTER);
    }

    public void refresh(Partie p, int myId) {
        refreshScores(p);

        // Mise à jour infos texte
        String nomJoueurCourant = "Inconnu";
        if(p.getJoueurCourant() != null) {
            nomJoueurCourant = p.getJoueurCourant().getNom();
        }
        lblInfo.setText("Moi: " + myId + " | Tour: " + nomJoueurCourant + " | " + p.getMessage());

        // Mise à jour Centre
        centerPanel.updateCards(p.getPlateau().getZoneCentre(), p.getIndiceCourant() == myId);

        // Mise à jour Joueurs (Logique de tri)
        playersGrid.removeAll();

        List<Joueur> tousLesJoueurs = p.getJoueurs();
        Joueur monJoueur = null;
        List<Joueur> autresJoueurs = new ArrayList<>();

        // 1. Séparer le client des autres
        for (Joueur j : tousLesJoueurs) {
            // On récupère l'ID via la Zone pour comparer avec myId
            ZoneJoueur z = p.getPlateau().getZone(j);
            if (z.getJoueur().getId() == myId) {
                monJoueur = j;
            } else {
                autresJoueurs.add(j);
            }
        }

        // 2. Ajouter MON panneau en PREMIER (tout en haut du scroll)
        if (monJoueur != null) {
            ZoneJoueur z = p.getPlateau().getZone(monJoueur);
            ajouterPanelJoueur(z, myId, p.getIndiceCourant());
            // Petit séparateur visuel
            playersGrid.add(Box.createVerticalStrut(10));
        }

        // 3. Ajouter les AUTRES panneaux ensuite
        for (Joueur j : autresJoueurs) {
            ZoneJoueur z = p.getPlateau().getZone(j);
            ajouterPanelJoueur(z, myId, p.getIndiceCourant());
        }

        playersGrid.revalidate();
        playersGrid.repaint();
    }

    // Méthode utilitaire pour ajouter un joueur avec les contraintes de taille
    private void ajouterPanelJoueur(ZoneJoueur z, int myId, int currentTurnIndex) {
        PlayersCardPanel pPanel = new PlayersCardPanel(z, myId, currentTurnIndex, frame);

        // Contrainte pour que toutes les zones aient la même hauteur
        pPanel.setPreferredSize(new Dimension(0, HAUTEUR_ZONE));
        pPanel.setMinimumSize(new Dimension(0, HAUTEUR_ZONE));

        pPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        playersGrid.add(pPanel);
        playersGrid.add(Box.createVerticalStrut(5)); // Petit espace entre chaque joueur
    }

    private void refreshScores(Partie partie) {
        StringBuilder sb = new StringBuilder();

        for (Joueur joueur : partie.getJoueurs()) {
            sb.append("--- ").append(joueur).append(" ---\n");
            sb.append("Score: ").append(partie.getScore(joueur)).append("\n");

            if (partie.getScore(joueur) > 0) {
                sb.append("  Trios:\n");
                for (Trio trio : partie.getTriosGagnes(joueur)) {
                    sb.append(trio.getType().getDescription()).append(" : ").append(trio.getType().getValeur()).append("\n");
                }
            }
            sb.append("\n");
        }

        textAreaScores.setText(sb.toString());
    }

    private void afficherRegles() {
        // Utilisation de HTML pour styliser comme sur la boîte de jeu
        String htmlContent = "<html><body style='width: 350px; font-family: sans-serif;'>" +

                // En-tête
                "<h2 style='text-align: center; color: #E6007E;'>RÈGLES DU JEU</h2>" +
                "<p style='text-align: center;'><i>Qui sera le plus rusé pour déduire quels numéros se cachent sous les cartes ?</i></p>" +
                "<hr>" +

                // Étape 1 (Bleu turquoise sur la boite)
                "<h3 style='color: #008080;'>&#10102; DÉVOILEZ 2 CARTES NUMÉRO :</h3>" +
                "<ul>" +
                "<li>Sur la table (au centre),</li>" +
                "<li><b>OU</b> dans la main d'un joueur...</li>" +
                "</ul>" +
                "<p style='margin-left: 20px; color: #D35400;'><b>MAIS ATTENTION :</b><br>" +
                "Seulement le plus <b>PETIT</b> numéro ou le plus <b>GRAND</b> !</p>" +

                // Étape 2 (Orange sur la boite)
                "<h3 style='color: #E67E22;'>&#10103; REJOUEZ !</h3>" +
                "<p>Si les 2 cartes dévoilées sont <b>identiques</b>, vous continuez votre tour !</p>" +

                // Étape 3 (Vert sur la boite)
                "<h3 style='color: #27AE60;'>&#10104; GAGNEZ LE TRIO</h3>" +
                "<p>Trouvez les <b>3 cartes identiques</b> et gagnez ce trio !</p>" +

                "<hr>" +

                // Condition de victoire
                "<p style='text-align: center; background-color: #FFFFE0; padding: 5px; border: 1px solid orange;'>" +
                "<b>VICTOIRE :</b><br>" +
                "Le premier à obtenir <b>3 Trios</b><br>(ou le Trio de 7) gagne la partie !" +
                "</p>" +

                "</body></html>";

        // Affichage dans une boîte de dialogue propre
        JLabel label = new JLabel(htmlContent);
        JOptionPane.showMessageDialog(frame, label, "Règles Officielles Trio", JOptionPane.PLAIN_MESSAGE);
    }
}