package modele;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe principale qui gère la logique du jeu Trio
 */
public class Jeu {
    private Plateau plateau;
    private List<Joueur> joueurs;
    private Joueur joueurCourant;
    private int indiceCourant;
    private List<Carte> cartesRetournees;
    private List<Carte> ordreAffichage = new ArrayList<>();
    
    /**
     * Constructeur de la classe Jeu
     */
    public Jeu() {
        this.plateau = new Plateau();
        this.joueurs = new ArrayList<>();
        this.cartesRetournees = new ArrayList<>();
        this.indiceCourant = 0;
    }
    
    /**
     * Démarre une nouvelle partie
     */
    public void demarrerPartie(List<String> nomsJoueurs) {
        // Créer les joueurs
        joueurs.clear();
        for (String nom : nomsJoueurs) {
            joueurs.add(new Joueur(nom));
        }
        
        // Initialiser et distribuer les cartes
        plateau.initialiserCartes();
        plateau.distribuerCartes(joueurs.size(), joueurs);
        
        // Définir le premier joueur
        indiceCourant = 0;
        joueurCourant = joueurs.get(0);
        
        System.out.println("=== PARTIE DÉMARRÉE ===");
    }
    
    /**
     * Joue un tour avec une carte sélectionnée
     */
    public String jouerTour(Carte carte) {
        if (carte == null) {
            return "Carte invalide!";
        }
        
        // Vérifier si on peut retourner cette carte
        if (!plateau.peutRetourner(carte, joueurs)) {
            return "Vous ne pouvez retourner que la première ou dernière carte d'une main, ou n'importe quelle carte du centre!";
        }
        
        // Retourner la carte
        if (plateau.retournerCarte(carte)) {
            cartesRetournees.add(carte);
        } else {
            return "Cette carte est déjà retournée!";
        }
        
        // Vérifier selon le nombre de cartes retournées
        if (cartesRetournees.size() == 2) {
            // Vérifier si les 2 premières cartes sont identiques
            if (!cartesRetournees.get(0).equals(cartesRetournees.get(1))) {
                String msg = "Les deux cartes ne correspondent pas!";
                cacherCartesRetournees();
                changerJoueur();
                return msg;
            }
            return "Deux cartes identiques! Cherchez la troisième...";
        }
        
        if (cartesRetournees.size() == 3) {
            // Vérifier si on a un trio
            if (plateau.verifierTrio(cartesRetournees.get(0), 
                                    cartesRetournees.get(1), 
                                    cartesRetournees.get(2))) {
                
                // Trio trouvé!
                TypeCarte typeTrio = cartesRetournees.get(0).getType(); 

                plateau.retirerTrio(cartesRetournees, joueurs);
                
                // AJOUTER le trio au joueur courant
                Trio trio = new Trio(typeTrio);
                for (Carte c : cartesRetournees) {
                    trio.ajouterCarte(c);
                }
                joueurCourant.ajouterTrio(trio);
                
                cartesRetournees.clear();

                // Vérification spéciale pour le Type 7
                if (typeTrio == TypeCarte.TYPE7) {
                     return "🎉 TRIO SPÉCIAL DE TYPE 7 TROUVÉ! " + joueurCourant.getNom() + 
                            " a trouvé le trio " + typeTrio.getDescription() + " et GAGNE LA PARTIE!";
                }

                // Vérification pour les 3 trios
                if (joueurCourant.getTriosGagnes().size() >= 3) {
                    return "🏆 TROIS TRIOS! " + joueurCourant.getNom() + 
                           " a trouvé 3 trios (" + joueurCourant.getDescriptionTrios() + ") et GAGNE LA PARTIE!";
                }

                // Le joueur rejoue
                return "🎉 TRIO TROUVÉ! " + joueurCourant.getNom() + 
                       " a trouvé le trio " + typeTrio.getDescription() + " et rejoue!";
            } else {
                String msg = "❌ Ce n'est pas un trio!";
                cacherCartesRetournees();
                changerJoueur();
                return msg;
            }
        }
        
        return "✓ Carte retournée: " + carte.getType().getDescription() + " (valeur: " + carte.getValeur() + ")";
    }
    
    /**
     * Cache toutes les cartes retournées
     */
    private void cacherCartesRetournees() {
        for (Carte carte : cartesRetournees) {
            carte.cacher();
        }
        cartesRetournees.clear();
    }
    
    /**
     * Change le joueur courant
     */
    public void changerJoueur() {
        indiceCourant = (indiceCourant + 1) % joueurs.size();
        joueurCourant = joueurs.get(indiceCourant);
    }
    
    /**
     * Vérifie si la partie est terminée :
     * 1. Un joueur a obtenu 3 trios.
     * 2. Le trio de TYPE7 a été formé.
     * 3. Plus aucune carte chez les joueurs ni au centre.
     * @return true si la partie est terminée
     */
    public boolean verifierFinPartie() {
        // Condition 1 & 2 : Trio de 7 ou 3 trios
        for (Joueur joueur : joueurs) {
            for (Trio trio : joueur.getTriosGagnes()) {
                if (trio.getType() == TypeCarte.TYPE7) {
                    return true;
                }
            }
            if (joueur.getTriosGagnes().size() >= 3) {
                return true;
            }
        }
        
        // Condition 3 : Plus de cartes disponibles (Fin de partie par épuisement)
        if (plateau.getCartesCentre().size() > 0) {
            return false;
        }
        
        for (Joueur joueur : joueurs) {
            if (joueur.nombreCartes() > 0) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Obtient le gagnant de la partie selon les règles de victoire.
     */
    public Joueur obtenirGagnant() {
        if (joueurs.isEmpty()) {
            return null;
        }
        
        // 1. Priorité au trio de Type 7
        for (Joueur joueur : joueurs) {
            for (Trio trio : joueur.getTriosGagnes()) {
                if (trio.getType() == TypeCarte.TYPE7) {
                    return joueur;
                }
            }
        }
        
        // 2. Priorité aux 3 trios
        for (Joueur joueur : joueurs) {
            if (joueur.getTriosGagnes().size() >= 3) {
                return joueur; 
            }
        }
        
        // 3. Score le plus élevé (si fin par épuisement)
        Joueur gagnant = joueurs.get(0);
        for (Joueur joueur : joueurs) {
            if (joueur.getScore() > gagnant.getScore()) {
                gagnant = joueur;
            }
        }
        
        return gagnant;
    }
    
    // ========== GETTERS ET SETTERS EXISTANTS ==========
    
    public Joueur getJoueurCourant() {
        return joueurCourant;
    }
    
    public List<Joueur> getJoueurs() {
        return new ArrayList<>(joueurs);
    }
    
    public Plateau getPlateau() {
        return plateau;
    }
    
    public List<Carte> getCartesRetournees() {
        return new ArrayList<>(cartesRetournees);
    }

    public List<Carte> getOrdreAffichage() {
        return ordreAffichage;
    }

    public void setOrdreAffichage(List<Carte> ordreAffichage) {
        this.ordreAffichage = ordreAffichage;
    }
}