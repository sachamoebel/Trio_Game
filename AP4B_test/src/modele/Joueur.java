package modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 * Classe représentant un joueur du jeu Trio
 */
public class Joueur {
    private String nom;
    private int score;
    private List<Carte> main;
    private List<Trio> triosGagnes; // NOUVEAU : Liste des trios formés par ce joueur
    
    /**
     * Constructeur de la classe Joueur
     * @param nom Le nom du joueur
     */
    public Joueur(String nom) {
        this.nom = nom;
        this.score = 0;
        this.main = new ArrayList<>();
        this.triosGagnes = new ArrayList<>(); // Initialisation
    }
    
    /**
     * Incrémente le score du joueur de 1 point
     */
    public void incrementerScore() {
        this.score++;
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

    /**
     * Ajoute un trio gagné par ce joueur.
     * @param trio Le trio à ajouter.
     */
    public void ajouterTrio(Trio trio) {
        if (trio != null) {
            this.triosGagnes.add(trio);
        }
    }

    /**
     * Trie la main du joueur par la valeur du TypeCarte (croissant).
     */
    public void trierMain() {
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
    
    // ========== GETTERS ET SETTERS ==========
    
    public String getNom() {
        return nom;
    }
    
    public int getScore() {
        return score;
    }
    
    public List<Carte> getMain() {
        return new ArrayList<>(main); 
    }

    public List<Trio> getTriosGagnes() {
        return new ArrayList<>(triosGagnes);
    }
    
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
                ", trios=" + triosGagnes.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Joueur joueur = (Joueur) o;
        // On compare par nom
        return nom.equals(joueur.nom); 
    }

    @Override
    public int hashCode() {
        return nom.hashCode();
    }
}