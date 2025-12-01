import modele.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principale pour jouer au jeu Trio en mode console
 */
public class Main {
    
    private static Scanner scanner = new Scanner(System.in);
    private static Jeu jeu = new Jeu();
    
    public static void main(String[] args) {
        afficherBienvenue();
        
        // Demander le nombre de joueurs
        int nbJoueurs = demanderNombreJoueurs();
        
        // Demander les noms des joueurs
        List<String> nomsJoueurs = new ArrayList<>();
        for (int i = 1; i <= nbJoueurs; i++) {
            System.out.print("Nom du joueur " + i + ": ");
            nomsJoueurs.add(scanner.nextLine());
        }
        
        // Démarrer la partie
        jeu.demarrerPartie(nomsJoueurs);
        
        // Boucle de jeu
        while (!jeu.verifierFinPartie()) {
            jouerUnTour();
        }
        
        // Afficher le résultat final
        afficherResultatFinal();
        
        scanner.close();
    }
    
    /**
     * Affiche le message de bienvenue
     */
    private static void afficherBienvenue() {
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║       BIENVENUE AU JEU TRIO UTBM      ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println();
        System.out.println("Règles: Trouvez des trios de cartes identiques!");
        System.out.println("Chaque trio trouvé = 1 point");
        System.out.println();
    }
    
    /**
     * Demande le nombre de joueurs
     */
    private static int demanderNombreJoueurs() {
        int nbJoueurs = 0;
        while (nbJoueurs < 3 || nbJoueurs > 6) {
            System.out.print("Nombre de joueurs (3, 4, 5 ou 6): ");
            try {
                nbJoueurs = Integer.parseInt(scanner.nextLine());
                if (nbJoueurs < 3 || nbJoueurs > 6) {
                    System.out.println("⚠ Veuillez entrer 3 et 6 joueurs.");
                    return demanderNombreJoueurs();
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠ Veuillez entrer un nombre valide.");
            }
        }
        return nbJoueurs;
    }
    
    /**
     * Joue un tour complet pour le joueur courant
     */
    private static void jouerUnTour() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Tour de: " + jeu.getJoueurCourant().getNom());
        afficherScores();
        System.out.println("=".repeat(50));
        
        afficherCartes();
        
        // Demander au joueur de choisir une carte
        System.out.print("\nChoisissez une carte (numéro): ");
        try {
            int choix = Integer.parseInt(scanner.nextLine());
            
            List<Carte> affichage = jeu.getOrdreAffichage();
            if (choix >= 1 && choix <= affichage.size()) {
                Carte carteChoisie = affichage.get(choix - 1);
                String resultat = jeu.jouerTour(carteChoisie);
                System.out.println("\n>>> " + resultat);
                
                // Pause pour que le joueur voie les cartes
                if (jeu.getCartesRetournees().size() > 0) {
                    System.out.println("\nCartes retournées:");
                    for (Carte c : jeu.getCartesRetournees()) {
                        System.out.println("  - " + c.getType().getDescription());
                    }
                    
                    if (jeu.getCartesRetournees().size() == 2 && 
                        !jeu.getCartesRetournees().get(0).equals(jeu.getCartesRetournees().get(1))) {
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                    }
                }
            } else {
                System.out.println("⚠ Numéro invalide!");
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠ Veuillez entrer un nombre valide.");
        }
    }
    
    /**
     * Affiche toutes les cartes disponibles
     */
    private static void afficherCartes() {
        Joueur joueurActif = jeu.getJoueurCourant();
        List<Joueur> tousJoueurs = jeu.getJoueurs();
        
        System.out.println("\n VOS CARTES:");
        List<Carte> ordre = new ArrayList<>();
        int index = 1;
        List<Carte> mainJoueur = joueurActif.getMain();
        for (int i = 0; i < mainJoueur.size(); i++) {
            Carte carte = mainJoueur.get(i);
            String affichage = carte.estVisible() ?
                "[" + carte.getType().getDescription() + " (" + carte.getValeur() + ")]" :
                "[?????]";

            boolean jouable = (i == 0 || i == mainJoueur.size() - 1);
            String marqueur = jouable ? " ◄ JOUABLE" : " (bloquée)";

            System.out.println("  " + index + ". " + affichage + marqueur);

            ordre.add(carte);
            index++;
        }

        
        // Afficher les cartes des autres joueurs
        for (Joueur joueur : tousJoueurs) {
            if (joueur != joueurActif) {
                System.out.println("\n👤 CARTES DE " + joueur.getNom().toUpperCase() + ":");
                List<Carte> mainAdverse = joueur.getMain();
                for (int i = 0; i < mainAdverse.size(); i++) {
                    Carte carte = mainAdverse.get(i);
                    String affichage = carte.estVisible() ?
                        "[" + carte.getType().getDescription() + " (" + carte.getValeur() + ")]" :
                        "[?????]";

                    boolean jouable = (i == 0 || i == mainAdverse.size() - 1);
                    String marqueur = jouable ? " ◄ JOUABLE" : " (bloquée)";

                    System.out.println("  " + index + ". " + affichage + marqueur);

                    ordre.add(carte);
                    index++;
                }
            }
        }

        System.out.println("\n🎯 CARTES DU CENTRE (toutes jouables):");
        for (Carte carte : jeu.getPlateau().getCartesCentre()) {
            String affichage = carte.estVisible() ?
                "[" + carte.getType().getDescription() + " (" + carte.getValeur() + ")]" :
                "[?????]";
            System.out.println("  " + index + ". " + affichage + " ◄ JOUABLE");

            ordre.add(carte);
            index++;
        }
        jeu.setOrdreAffichage(ordre);

    }
    
    /**
     * Affiche les scores de tous les joueurs
     */
    private static void afficherScores() {
        System.out.println("\n SCORES:");
        for (Joueur joueur : jeu.getJoueurs()) {
            String marqueur = joueur == jeu.getJoueurCourant() ? " ◄" : "";
            System.out.println("  " + joueur.getNom() + ": " + joueur.getScore() + " point(s)" + marqueur);
        }
    }
    
    /**
     * Affiche le résultat final de la partie
     */
    private static void afficherResultatFinal() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           🎉 PARTIE TERMINÉE! 🎉");
        System.out.println("=".repeat(50));
        
        afficherScores();
        
        Joueur gagnant = jeu.obtenirGagnant();
        System.out.println("\n🏆 GAGNANT: " + gagnant.getNom() + 
                          " avec " + gagnant.getScore() + " point(s)!");
        
        System.out.println("\n" + "=".repeat(50));
    }
}