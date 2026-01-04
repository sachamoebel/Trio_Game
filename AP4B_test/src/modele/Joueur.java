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

    /**
     * Retourne l'identifiant unique du joueur
     * @return L'identifiant du joueur
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne le nom du joueur
     * @return Le nom du joueur
     */
    public String getNom() {
        return nom;
    }

    /**
     * Modifie le nom du joueur
     * @param nom Le nouveau nom du joueur
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne une représentation textuelle du joueur
     * @return Une chaîne de caractères contenant le nom et l'id du joueur
     */
    @Override
    public String toString() {
        return nom + " (id : " + id + ')';
    }

    /**
     * Compare ce joueur à un autre objet pour vérifier s'ils sont égaux.
     * L'égalité est basée sur le nom du joueur.
     * @param o L'objet à comparer
     * @return true si les objets sont identiques ou ont le même nom, false sinon
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Joueur joueur = (Joueur) o;
        return nom.equals(joueur.nom);
    }

    /**
     * Retourne le code de hachage du joueur, calculé à partir de son nom
     * @return Le code de hachage
     */
    @Override
    public int hashCode() {
        return nom.hashCode();
    }
}