package modele;

import java.io.Serializable;

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
        return nom + " (id : " + id + ')';
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