package modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Classe représentant une zone générique contenant des cartes (ex: le centre du plateau).
 */
public class ZoneCarte implements Serializable {
    /** Liste des cartes présentes dans cette zone */
    protected List<Carte> cartes;

    /**
     * Constructeur de la classe ZoneCarte.
     * Initialise une liste de cartes vide.
     */
    public ZoneCarte() {
        cartes = new ArrayList<>();
    }

    /**
     * Révèle une carte située à un index spécifique en changeant son état de visibilité.
     *
     * @param index L'indice de la carte à révéler dans la liste.
     * @return La carte qui a été retournée.
     */
    public Carte reveler(int index) {
        Carte c = cartes.get(index);
        c.retourner();
        return c;
    }

    /**
     * Retourne une copie de la liste des cartes présentes dans la zone.
     *
     * @return Une nouvelle liste contenant les cartes de cette zone.
     */
    public List<Carte> getCartes() {
        return new ArrayList<>(cartes);
    }
}