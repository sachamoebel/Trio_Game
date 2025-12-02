package modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 * Classe représentant un joueur du jeu Trio
 */
public class Joueur {
    private String nom;
    private List<Carte> main;
    private List<Trio> triosGagnes; // Liste des trios formés par ce joueur
    
    /**
     * Constructeur de la classe Joueur
     * @param nom Le nom du joueur
     */
    public Joueur(String nom) {
        this.nom = nom;
        this.main = new ArrayList<>();
        this.triosGagnes = new ArrayList<>();
    }
    
    /**
     * Obtient le score du joueur (nombre de trios gagnés)
     * @return Le nombre de trios gagnés
     */
    public int getScore() {
        return triosGagnes.size();
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
    
    /**
     * Obtient une description détaillée des trios gagnés
     * @return String décrivant tous les trios
     */
    public String getDescriptionTrios() {
        if (triosGagnes.isEmpty()) {
            return "Aucun trio";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < triosGagnes.size(); i++) {
            Trio trio = triosGagnes.get(i);
            sb.append("Trio ").append(i + 1).append(": ")
              .append(trio.getType().getDescription());
            if (i < triosGagnes.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    
    // ========== GETTERS ET SETTERS ==========
    
    public String getNom() {
        return nom;
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
    
    @Override
    public String toString() {
        return "Joueur{" +
                "nom='" + nom + '\'' +
                ", score=" + getScore() +
                ", cartes=" + main.size() +
                ", trios=" + getDescriptionTrios() +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Joueur joueur = (Joueur) o;
        return nom.equals(joueur.nom); 
    }
    
    @Override
    public int hashCode() {
        return nom.hashCode();
    }
}