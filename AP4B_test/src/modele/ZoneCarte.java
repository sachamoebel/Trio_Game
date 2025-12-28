package modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ZoneCarte implements Serializable {
    protected List<Carte> cartes;

    public ZoneCarte() {
        cartes = new ArrayList<>();
    }

    public Carte reveler(int index) {
        Carte c = cartes.get(index);
        c.retourner();
        return c;
    }

    public List<Carte> getCartes() {
        return new ArrayList<>(cartes);
    }

    public void ajouterCarte(Carte c) { cartes.add(c); }

    public void trierCartes() {
        cartes.sort(Comparator.comparingInt(Carte::getValeur));
    }
}
