package modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 * Classe représentant un joueur du jeu Trio
 */
public class Joueur implements Serializable {
    private int id;
    private String nom;

    /**
     * Constructeur de la classe Joueur
     * @param id L'identifiant du joueur
     */
    public Joueur(int id) {
        this.id = id;
        this.nom = "Joueur " + id;
    }
    
    /**
     * Ajoute un trio gagné par ce joueur.
     * @return true si la carte a été retirée, false sinon

    public boolean retirerCarte(Carte carte) {
        return this.main.remove(carte);
    }
    
    /**
     * Obtient le nombre de cartes dans la main du joueur
     * @return Le nombre de cartes

    public int nombreCartes() {
        return this.main.size();
    }


    // obsolète
    /**
     * Obtient une description détaillée des trios gagnés
     * @return String décrivant tous les trios

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
         */
    
    // ========== GETTERS ET SETTERS ==========

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    @Override
    public String toString() {
        return "Joueur{" +
                "nom='" + nom + '\'' +
                ", id=" + id +
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