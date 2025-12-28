package modele;

import java.io.Serializable;

/**
 * Classe représentant une carte du jeu Trio
 */
public class Carte implements Serializable {
    private TypeCarte type;
    boolean visible;
    
    /**
     * Constructeur de la classe Carte
     * @param type Type de la carte (la valeur est déterminée par le type)
     */
    public Carte(TypeCarte type) {
        this.type = type;
        this.visible = false; // Par défaut, la carte est cachée
    }
    
    /**
     * Obtient la valeur de la carte (basée sur son type)
     * @return La valeur entière de la carte
     */
    public int getValeur() {
        return type.getValeur();
    }
    
    /**
     * Retourne la carte (la rend visible)
     */
    public void retourner() {
        this.visible = true;
    }
    
    /**
     * Cache la carte (la rend invisible)
     */
    public void cacher() {
        this.visible = false;
    }
    
    /**
     * Vérifie si la carte est visible
     * @return true si la carte est visible, false sinon
     */
    public boolean estVisible() {
        return this.visible;
    }
    
    /**
     * Compare cette carte avec une autre carte
     * Deux cartes sont égales si elles ont le même type (donc la même valeur)
     * @param autre La carte à comparer
     * @return true si les cartes sont identiques, false sinon
     */
    public boolean equals(Carte autre) {
        if (autre == null) {
            return false;
        }
        return this.type == autre.type;
    }
    
    // ========== GETTERS ==========
    
    public TypeCarte getType() {
        return type;
    }
    
    // ========== SETTERS ==========
    
    public void setType(TypeCarte type) {
        this.type = type;
    }
    
    /**
     * Représentation textuelle de la carte
     * @return Une chaîne décrivant la carte
     */
    @Override
    public String toString() {
        String etat = visible ? "visible" : "cachée";
        return "Carte{" +
                "valeur=" + getValeur() +
                ", type=" + type.getDescription() +
                ", " + etat +
                '}';
    }
}