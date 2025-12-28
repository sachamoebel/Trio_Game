package modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un trio de trois cartes identiques
 */
public class Trio implements Serializable {
    private TypeCarte type;
    private List<Carte> cartes;
    
    /**
     * Constructeur avec le type de carte du trio
     * @param type Le type des cartes formant ce trio
     */
    public Trio(TypeCarte type) {
        this.type = type;
        this.cartes = new ArrayList<>();
    }
    
    /**
     * Ajoute une carte au trio
     * @param carte La carte à ajouter
     * @throws IllegalArgumentException si la carte n'est pas du bon type ou si le trio est déjà complet
     */
    public void ajouterCarte(Carte carte) {
        if (carte == null) {
            throw new IllegalArgumentException("La carte ne peut pas être nulle");
        }
        
        if (!carte.getType().equals(this.type)) {
            throw new IllegalArgumentException("La carte doit être du type " + this.type.getDescription());
        }
        
        if (cartes.size() >= 3) {
            throw new IllegalStateException("Le trio est déjà complet (3 cartes)");
        }
        
        this.cartes.add(carte);
    }
    
    /**
     * Vérifie si le trio est complet (3 cartes)
     * @return true si le trio contient 3 cartes
     */
    public boolean estComplet() {
        return cartes.size() == 3;
    }
    
    /**
     * Obtient le type du trio
     * @return Le TypeCarte de ce trio
     */
    public TypeCarte getType() {
        return type;
    }
    
    /**
     * Obtient les cartes du trio
     * @return Une copie de la liste des cartes
     */
    public List<Carte> getCartes() {
        return new ArrayList<>(cartes);
    }
    
    /**
     * Obtient le nombre de cartes dans le trio
     * @return Le nombre de cartes (0 à 3)
     */
    public int getNombreCartes() {
        return cartes.size();
    }
    
    @Override
    public String toString() {
        return "Trio{" +
                "type=" + type.getDescription() +
                ", cartes=" + cartes.size() + "/3" +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trio trio = (Trio) o;
        return type == trio.type;
    }
    
    @Override
    public int hashCode() {
        return type.hashCode();
    }
}