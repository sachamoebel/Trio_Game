package modele;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ZoneJoueur extends ZoneCarte {
    Joueur joueur;
    List<Trio> triosGagnes = new ArrayList<>();

    public ZoneJoueur(Joueur joueur) { 
        this.joueur = joueur;
    }

    public void retournerCarte(Carte c) {
        c.retourner();
    }

    /**
     * Trie la main du joueur par la valeur du TypeCarte (croissant).
     */
    public void trierMain() {
        cartes.sort(Comparator.comparingInt(c -> c.getType().getValeur()));
    }


    public Carte revelerMin() {
        for (Carte c : cartes){
            if (!c.estVisible()) {
                c.retourner();
                return c;
            }
        }
        return null;
    }

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

    public Joueur getJoueur() {
        return joueur;
    }

    public List<Trio> getTriosGagnes() {
        return new ArrayList<>(triosGagnes);
    }
}