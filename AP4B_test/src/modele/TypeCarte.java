package modele;

import java.awt.Color;

public enum TypeCarte {
    TYPE1(1, "Inscriptions Pedagogiques", new Color(135, 206, 250)), // Bleu Ciel
    TYPE2(2, "Median", new Color(255, 182, 193)), // Rose Clair
    TYPE3(3, "SEE", new Color(144, 238, 144)), // Vert Clair
    TYPE4(4, "FISE", new Color(240, 230, 140)), // Kaki / Jaune Pâle
    TYPE5(5, "FISA", new Color(221, 160, 221)), // Prune
    TYPE6(6, "TC", new Color(255, 127, 80)),    // Corail
    TYPE7(7, "Diplome!", new Color(255, 215, 0)), // OR (Gold) - Très important !
    TYPE8(8, "Projets", new Color(210, 180, 140)), // Ocre / Tan
    TYPE9(9, "Stage", new Color(124, 252, 0)),    // Vert Pelouse
    TYPE10(10, "Soutenance", new Color(0, 255, 255)), // Cyan
    TYPE11(11, "Doctorat", new Color(255, 0, 255)),   // Magenta
    TYPE12(12, "Finaux", new Color(255, 69, 0));      // Rouge Orange (Urgent !)

    private final int valeur;
    private final String description;
    private final Color couleur; // Nouvel attribut

    // Constructeur mis à jour
    TypeCarte(int valeur, String description, Color couleur) {
        this.valeur = valeur;
        this.description = description;
        this.couleur = couleur;
    }

    public int getValeur() {
        return valeur;
    }

    public String getDescription() {
        return description;
    }

    public Color getCouleur() {
        return couleur;
    }

    public static TypeCarte parValeur(int v) {
        for (TypeCarte t : values()) {
            if (t.valeur == v) return t;
        }
        return null;
    }
}