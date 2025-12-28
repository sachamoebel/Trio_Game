import modele.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface graphique principale du jeu Trio
 */
public class TrioGUI extends JFrame {
    private Jeu jeu;
    private JPanel panelPrincipal;
    private JPanel panelJoueurs;
    private JPanel panelCentre;
    private JPanel panelInfo;
    private JLabel labelJoueurCourant;
    private JLabel labelMessage;
    private JTextArea textAreaScores;
    private JButton btnNouvellePartie;
    
    private static final Color COULEUR_FOND = new Color(34, 139, 34);
    private static final Color COULEUR_CARTE_CACHEE = new Color(139, 69, 19);
    private static final Color COULEUR_CARTE_VISIBLE = new Color(255, 250, 205);
    private static final int LARGEUR_CARTE = 100;
    private static final int HAUTEUR_CARTE = 140;
    
    public TrioGUI() {
        super("Jeu TRIO - UTBM");
        jeu = new Jeu();
        
        initialiserInterface();
        demarrerNouvellePartie();
        
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Initialise l'interface graphique
     */
    private void initialiserInterface() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel du haut : informations
        panelInfo = new JPanel(new BorderLayout());
        panelInfo.setBackground(new Color(70, 130, 180));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        labelJoueurCourant = new JLabel("Tour de: ", SwingConstants.CENTER);
        labelJoueurCourant.setFont(new Font("Arial", Font.BOLD, 20));
        labelJoueurCourant.setForeground(Color.WHITE);
        
        labelMessage = new JLabel("Choisissez une carte", SwingConstants.CENTER);
        labelMessage.setFont(new Font("Arial", Font.PLAIN, 16));
        labelMessage.setForeground(Color.YELLOW);
        
        JPanel panelHautGauche = new JPanel(new BorderLayout());
        panelHautGauche.setOpaque(false);
        panelHautGauche.add(labelJoueurCourant, BorderLayout.NORTH);
        panelHautGauche.add(labelMessage, BorderLayout.CENTER);
        
        btnNouvellePartie = new JButton("Nouvelle Partie");
        btnNouvellePartie.addActionListener(e -> demarrerNouvellePartie());
        
        panelInfo.add(panelHautGauche, BorderLayout.CENTER);
        panelInfo.add(btnNouvellePartie, BorderLayout.EAST);
        
        add(panelInfo, BorderLayout.NORTH);
        
        // Panel principal : plateau de jeu
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(COULEUR_FOND);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel des joueurs
        panelJoueurs = new JPanel();
        panelJoueurs.setLayout(new BoxLayout(panelJoueurs, BoxLayout.Y_AXIS));
        panelJoueurs.setBackground(COULEUR_FOND);
        panelJoueurs.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            "Joueurs",
            0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE
        ));
        
        JScrollPane scrollJoueurs = new JScrollPane(panelJoueurs);
        scrollJoueurs.setPreferredSize(new Dimension(900, 400));
        scrollJoueurs.getViewport().setBackground(COULEUR_FOND);
        
        // Panel du centre
        panelCentre = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelCentre.setBackground(COULEUR_FOND);
        panelCentre.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            "Cartes du Centre",
            0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE
        ));
        
        panelPrincipal.add(scrollJoueurs, BorderLayout.CENTER);
        panelPrincipal.add(panelCentre, BorderLayout.SOUTH);
        
        add(panelPrincipal, BorderLayout.CENTER);
        
        // Panel de droite : scores
        textAreaScores = new JTextArea(10, 20);
        textAreaScores.setEditable(false);
        textAreaScores.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollScores = new JScrollPane(textAreaScores);
        scrollScores.setBorder(BorderFactory.createTitledBorder("Scores"));
        
        add(scrollScores, BorderLayout.EAST);
    }
    
    /**
     * Démarre une nouvelle partie
     */
    private void demarrerNouvellePartie() {
        // Demander le nombre de joueurs
        String[] options = {"3", "4", "5", "6"};
        String choix = (String) JOptionPane.showInputDialog(
            this,
            "Nombre de joueurs:",
            "Nouvelle Partie",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            "3"
        );
        
        if (choix == null) return;
        
        int nbJoueurs = Integer.parseInt(choix);
        List<String> nomsJoueurs = new ArrayList<>();
        
        // Demander les noms
        for (int i = 1; i <= nbJoueurs; i++) {
            String nom = JOptionPane.showInputDialog(
                this,
                "Nom du joueur " + i + ":",
                "Joueur " + i
            );
            if (nom == null || nom.trim().isEmpty()) {
                nom = "Joueur " + i;
            }
            nomsJoueurs.add(nom.trim());
        }
        
        // Démarrer la partie
        jeu.demarrerPartie(nomsJoueurs);
        rafraichirInterface();
        labelMessage.setText("Choisissez une carte");
    }
    
    /**
     * Rafraîchit toute l'interface
     */
    private void rafraichirInterface() {
        rafraichirJoueurs();
        rafraichirCentre();
        rafraichirScores();
        
        labelJoueurCourant.setText("Tour de: " + jeu.getJoueurCourant().getNom());
    }
    
    /**
     * Rafraîchit l'affichage des joueurs
     */
    private void rafraichirJoueurs() {
        panelJoueurs.removeAll();
        
        for (Joueur joueur : jeu.getJoueurs()) {
            JPanel panelJoueur = creerPanelJoueur(joueur);
            panelJoueurs.add(panelJoueur);
            panelJoueurs.add(Box.createVerticalStrut(10));
        }
        
        panelJoueurs.revalidate();
        panelJoueurs.repaint();
    }
    
    /**
     * Crée le panel d'un joueur avec ses cartes
     */
    private JPanel creerPanelJoueur(Joueur joueur) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(COULEUR_FOND);
        
        boolean estJoueurActif = joueur.equals(jeu.getJoueurCourant());
        Color couleurBordure = estJoueurActif ? Color.YELLOW : Color.WHITE;
        
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(couleurBordure, 3),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Nom du joueur
        JLabel labelNom = new JLabel(joueur.getNom() + " (" + joueur.getScore() + " trio(s))");
        labelNom.setFont(new Font("Arial", Font.BOLD, 14));
        labelNom.setForeground(Color.WHITE);
        panel.add(labelNom, BorderLayout.NORTH);
        
        // Cartes
        JPanel panelCartes = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelCartes.setBackground(COULEUR_FOND);
        
        List<Carte> main = joueur.getMain();
        
        // Trouver première et dernière carte non retournée
        int premiereNonRetournee = -1;
        int derniereNonRetournee = -1;
        for (int i = 0; i < main.size(); i++) {
            if (!main.get(i).estVisible()) {
                if (premiereNonRetournee == -1) {
                    premiereNonRetournee = i;
                }
                derniereNonRetournee = i;
            }
        }
        
        for (int i = 0; i < main.size(); i++) {
            Carte carte = main.get(i);
            // Une carte est jouable si elle est à une extrémité non retournée (peu importe le joueur)
            boolean jouable = (i == premiereNonRetournee || i == derniereNonRetournee);
            
            JButton btnCarte = creerBoutonCarte(carte, jouable);
            panelCartes.add(btnCarte);
        }
        
        panel.add(panelCartes, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Rafraîchit l'affichage du centre
     */
    private void rafraichirCentre() {
        panelCentre.removeAll();
        
        for (Carte carte : jeu.getPlateau().getCartesCentre()) {
            JButton btnCarte = creerBoutonCarte(carte, true);
            panelCentre.add(btnCarte);
        }
        
        panelCentre.revalidate();
        panelCentre.repaint();
    }
    
    /**
     * Crée un bouton pour une carte
     */
    private JButton creerBoutonCarte(Carte carte, boolean jouable) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(LARGEUR_CARTE, HAUTEUR_CARTE));
        btn.setFocusPainted(false);
        
        if (carte.estVisible()) {
            // Carte visible
            btn.setBackground(COULEUR_CARTE_VISIBLE);
            btn.setText("<html><center><b>" + carte.getType().getDescription() + 
                       "</b><br>Valeur: " + carte.getValeur() + "</center></html>");
            btn.setEnabled(false);
        } else {
            // Carte cachée
            btn.setBackground(COULEUR_CARTE_CACHEE);
            btn.setText("?");
            btn.setFont(new Font("Arial", Font.BOLD, 40));
            btn.setForeground(Color.WHITE);
            btn.setEnabled(jouable);
            
            if (jouable) {
                btn.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                btn.addActionListener(e -> jouerCarte(carte));
            } else {
                btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            }
        }
        
        return btn;
    }
    
    /**
     * Joue une carte
     */
    private void jouerCarte(Carte carte) {
        String resultat = jeu.jouerTour(carte);
        labelMessage.setText(resultat);
        
        rafraichirInterface();
        
        // Si on a retourné 2 cartes qui ne correspondent pas OU 3 cartes qui ne forment pas un trio
        // On attend 2 secondes avant de les cacher et changer de joueur
        if (resultat.contains("ne correspondent pas") || resultat.contains("n'est pas un trio")) {
            // Désactiver tous les boutons pendant l'attente
            desactiverTousBoutons();
            
            Timer timer = new Timer(2000, e -> {
                jeu.gererEchec(); // Cache les cartes ET change de joueur
                rafraichirInterface();
                reactiverBoutons();
            });
            timer.setRepeats(false);
            timer.start();
        }
        
        // Vérifier fin de partie
        if (jeu.verifierFinPartie()) {
            Timer timer = new Timer(500, e -> afficherFinPartie());
            timer.setRepeats(false);
            timer.start();
        }
    }
    
    /**
     * Désactive tous les boutons de cartes
     */
    private void desactiverTousBoutons() {
        desactiverBoutonsPanel(panelJoueurs);
        desactiverBoutonsPanel(panelCentre);
    }
    
    /**
     * Désactive récursivement tous les boutons d'un panel
     */
    private void desactiverBoutonsPanel(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                comp.setEnabled(false);
            } else if (comp instanceof Container) {
                desactiverBoutonsPanel((Container) comp);
            }
        }
    }
    
    /**
     * Réactive les boutons (en rafraîchissant l'interface)
     */
    private void reactiverBoutons() {
        // Le rafraîchissement de l'interface réactivera automatiquement les bons boutons
        rafraichirInterface();
    }
    
    /**
     * Rafraîchit l'affichage des scores
     */
    private void rafraichirScores() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== SCORES ===\n\n");
        
        for (Joueur joueur : jeu.getJoueurs()) {
            sb.append(joueur.getNom()).append(": ")
              .append(joueur.getScore()).append(" trio(s)\n");
            
            if (joueur.getScore() > 0) {
                sb.append("  Trios:\n");
                int num = 1;
                for (Trio trio : joueur.getTriosGagnes()) {
                    sb.append("  ").append(num).append(". ")
                      .append(trio.getType().getDescription()).append("\n");
                    num++;
                }
            }
            sb.append("\n");
        }
        
        textAreaScores.setText(sb.toString());
    }
    
    /**
     * Affiche la fin de partie
     */
    private void afficherFinPartie() {
        Joueur gagnant = jeu.obtenirGagnant();
        
        StringBuilder message = new StringBuilder();
        message.append("🎉 PARTIE TERMINÉE! 🎉\n\n");
        message.append("🏆 GAGNANT: ").append(gagnant.getNom()).append("\n");
        message.append("Score: ").append(gagnant.getScore()).append(" trio(s)\n\n");
        
        message.append("Trios trouvés:\n");
        for (Trio trio : gagnant.getTriosGagnes()) {
            message.append("  • ").append(trio.getType().getDescription()).append("\n");
        }
        
        JOptionPane.showMessageDialog(
            this,
            message.toString(),
            "Fin de partie",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        int reponse = JOptionPane.showConfirmDialog(
            this,
            "Voulez-vous recommencer une partie?",
            "Nouvelle partie?",
            JOptionPane.YES_NO_OPTION
        );
        
        if (reponse == JOptionPane.YES_OPTION) {
            demarrerNouvellePartie();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TrioGUI());
    }
}