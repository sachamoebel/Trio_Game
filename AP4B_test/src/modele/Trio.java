package modele;

import java.util.ArrayList;
import java.util.List;
// ==================== FICHIER: Trio.java ====================

/**
 * Classe représentant un trio de trois cartes identiques
 */
public class Trio {
    private List<Carte> cartes;
    private Joueur joueurProprietaire;
    private TypeCarte type;
    
    /**
     * Constructeur de la classe Trio
     */
    public Trio() {
        this.cartes = new ArrayList<>();
    }
    
    /**
     * Constructeur avec joueur propriétaire
     */
    public Trio(Joueur joueur) {
        this();
        this.joueurProprietaire = joueur;
    }
    
    /**
     * Ajoute une carte au trio
     * @param carte La carte à ajouter
     */
    public void ajouterCarte(Carte carte) {
        if (carte != null && cartes.size() < 3) {
            cartes.add(carte);
            if (type == null) {
                type = carte.getType();
            }
        }
    }
    
    /**
     * Vérifie si le trio est complet (3 cartes)
     * @return true si le trio contient 3 cartes
     */
    public boolean estComplet() {
        return cartes.size() == 3;
    }
    
    /**
     * Vérifie si le trio est valide (3 cartes identiques)
     * @return true si les 3 cartes sont identiques
     */
    public boolean est_trio_valide() {
        if (!estComplet()) {
            return false;
        }
        Carte premiere = cartes.get(0);
        return premiere.equals(cartes.get(1)) && premiere.equals(cartes.get(2));
    }
    
    // ========== GETTERS ==========
    
    public List<Carte> getCartes() {
        return new ArrayList<>(cartes);
    }
    
    public Joueur getJoueurProprietaire() {
        return joueurProprietaire;
    }
    
    public TypeCarte getType() {
        return type;
    }
    
    public void setJoueurProprietaire(Joueur joueur) {
        this.joueurProprietaire = joueur;
    }
    
    @Override
    public String toString() {
        return "Trio{" +
                "type=" + type +
                ", cartes=" + cartes.size() +
                ", propriétaire=" + (joueurProprietaire != null ? joueurProprietaire.getNom() : "aucun") +
                '}';
    }
}