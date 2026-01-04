package modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe représentant le plateau de jeu avec toutes les cartes encore en jeu.
 * Elle gère la génération du paquet, la distribution initiale et l'accès aux zones.
 */
public class Plateau implements Serializable {
    ZoneCarte zoneCentre = new ZoneCarte();
    List<ZoneJoueur> zonesJoueur =  new ArrayList<>();

    /**
     * Génère le jeu complet nécessaire pour une partie : 12 types de cartes
     * présents en 3 exemplaires chacun, soit un total de 36 cartes.
     * @return Une liste contenant les 36 cartes initiales.
     */
    public List<Carte> genererJeuComplet() {
        List<Carte> jeu = new ArrayList<>();
        for (TypeCarte type : TypeCarte.values()) {
            for (int i = 0; i < 3; i++) {
                jeu.add(new Carte(type));
            }
        }
        return jeu;
    }

    /**
     * Distribue les cartes entre les mains des joueurs et la zone centrale.
     * Le nombre de cartes distribuées dépend du nombre de joueurs (règles officielles).
     * @param nbJoueurs Le nombre de joueurs participant à la partie.
     * @param joueurs La liste des objets Joueur.
     * @throws IllegalArgumentException Si le nombre de joueurs n'est pas entre 3 et 6.
     */
    public void distribuerCartes(int nbJoueurs, List<Joueur> joueurs) {
        if (joueurs == null || joueurs.size() != nbJoueurs) {
            throw new IllegalArgumentException("La liste des joueurs doit contenir exactement " + nbJoueurs + " joueurs.");
        }

        List<Carte> pioche = genererJeuComplet();
        Collections.shuffle(pioche);

        int cartesParJoueur;
        int cartesAuCentre;

        switch (nbJoueurs) {
            case 3:
                cartesParJoueur = 9;
                cartesAuCentre = 9;
                break;
            case 4:
                cartesParJoueur = 7;
                cartesAuCentre = 8;
                break;
            case 5:
                cartesParJoueur = 6;
                cartesAuCentre = 6;
                break;
            case 6:
                cartesParJoueur = 5;
                cartesAuCentre = 6;
                break;
            default:
                throw new IllegalArgumentException("Nombre de joueurs non supporté (doit être entre 3 et 6) : " + nbJoueurs);
        }

        for (ZoneJoueur z : zonesJoueur) {
            for (int i = 0; i < cartesParJoueur; i++) {
                if (pioche.isEmpty()) break;
                Carte c = pioche.remove(0);
                z.cartes.add(c);
                c.cacher();
            }
            z.trierMain();
        }

        zoneCentre.cartes.clear();
        for (int i = 0; i < cartesAuCentre && !pioche.isEmpty(); i++) {
            Carte c = pioche.removeFirst();
            c.cacher();
            zoneCentre.cartes.add(c);
        }
    }

    /**
     * Retourne une copie de la liste des cartes actuellement présentes au centre.
     * @return Une liste de cartes.
     */
    public List<Carte> getCartesCentre() {
        return new ArrayList<>(zoneCentre.cartes);
    }

    /**
     * Récupère la zone de jeu associée à un joueur spécifique.
     * @param joueur Le joueur dont on cherche la zone.
     * @return La ZoneJoueur correspondante ou null si le joueur n'est pas trouvé.
     */
    public ZoneJoueur getZone(Joueur joueur) {
        for (ZoneJoueur z : zonesJoueur) if (z.joueur == joueur) return z;
        return null;
    }

    /**
     * Accède à l'objet ZoneCarte représentant le centre du plateau.
     * @return La zone centrale.
     */
    public ZoneCarte getZoneCentre() {
        return zoneCentre;
    }

    /**
     * Retourne une représentation textuelle de l'état du plateau.
     * @return Une chaîne de caractères indiquant le nombre de cartes au centre.
     */
    @Override
    public String toString() {
        return "Plateau{ cartes au centre=" + zoneCentre.cartes.size() + " }";
    }
}