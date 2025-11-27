package modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

// ==================== FICHIER: Joueur.java ====================

/**
 * Classe représentant un joueur du jeu Trio
 */
public class Joueur {
    private String nom;
    private int score;
    private List<Carte> main;
    
    /**
     * Constructeur de la classe Joueur
     * @param nom Le nom du joueur
     */
    public Joueur(String nom) {
        this.nom = nom;
        this.score = 0;
        this.main = new ArrayList<>();
    }
    
    /**
     * Incrémente le score du joueur de 1 point
     */
    public void incrementerScore() {
        this.score++;
    }
    
    /**
     * Obtient le score actuel du joueur
     * @return Le score du joueur
     */
    public int obtenirScore() {
        return this.score;
    }
    
    /**
     * Ajoute une carte à la main du joueur
     * @param carte La carte à ajouter
     */
    public void ajouterCarte(Carte carte) {
        if (carte != null) {
            this.main.add(carte);
        }
    }

    public void trierMain() {
    // 'main' est le champ interne ; on trie directement ce champ
        this.main.sort(Comparator.comparingInt(c -> c.getType().getValeur()));
    }
    
    /**
     * Retire une carte de la main du joueur
     * @param carte La carte à retirer
     * @return true si la carte a été retirée, false sinon
     */
    public boolean retirerCarte(Carte carte) {
        return this.main.remove(carte);
    }
    
    /**
     * Obtient le nombre de cartes dans la main du joueur
     * @return Le nombre de cartes
     */
    public int nombreCartes() {
        return this.main.size();
    }
    
    // ========== GETTERS ==========
    
    public String getNom() {
        return nom;
    }
    
    public int getScore() {
        return score;
    }
    
    public List<Carte> getMain() {
        return new ArrayList<>(main); // Retourne une copie pour protéger l'encapsulation
    }
    
    // ========== SETTERS ==========
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    @Override
    public String toString() {
        return "Joueur{" +
                "nom='" + nom + '\'' +
                ", score=" + score +
                ", cartes=" + main.size() +
                '}';
    }
}