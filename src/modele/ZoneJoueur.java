package modele;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Classe représentant la zone de jeu spécifique d'un joueur.
 * Elle contient la main du joueur (cartes) ainsi que les trios qu'il a remportés.
 */
public class ZoneJoueur extends ZoneCarte {
    /** Le joueur propriétaire de cette zone */
    Joueur joueur;
    /** Liste des trios gagnés par ce joueur au cours de la partie */
    List<Trio> triosGagnes = new ArrayList<>();

    /**
     * Constructeur de la classe ZoneJoueur.
     * @param joueur Le joueur associé à cette zone.
     */
    public ZoneJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    /**
     * Retourne (révèle) une carte spécifique dans la main du joueur.
     * @param c La carte à retourner.
     */
    public void retournerCarte(Carte c) {
        c.retourner();
    }

    /**
     * Trie la main du joueur par la valeur des cartes dans l'ordre croissant.
     * Cette étape est cruciale car les règles du Trio imposent de ne révéler
     * que les extrémités de la main (la plus petite ou la plus grande).
     */
    public void trierMain() {
        cartes.sort(Comparator.comparingInt(c -> c.getType().getValeur()));
    }

    /**
     * Révèle la plus petite carte non encore visible dans la main du joueur.
     * @return La carte révélée, ou null s'il n'y a plus de cartes cachées.
     */
    public Carte revelerMin() {
        for (Carte c : cartes){
            if (!c.estVisible()) {
                c.retourner();
                return c;
            }
        }
        return null;
    }

    /**
     * Révèle la plus grande carte non encore visible dans la main du joueur.
     * @return La carte révélée, ou null s'il n'y a plus de cartes cachées.
     */
    public Carte revelerMax() {
        for (int i = cartes.size() - 1; i >= 0; i--) {
            Carte c = cartes.get(i);
            if (!c.estVisible()) {
                c.retourner();
                return c;
            }
        }
        return null;
    }

    /**
     * Retourne l'objet Joueur associé à cette zone.
     * @return Le joueur propriétaire.
     */
    public Joueur getJoueur() {
        return joueur;
    }

    /**
     * Retourne une copie de la liste des trios gagnés par le joueur.
     * @return Une nouvelle liste contenant les trios remportés.
     */
    public List<Trio> getTriosGagnes() {
        return new ArrayList<>(triosGagnes);
    }
}