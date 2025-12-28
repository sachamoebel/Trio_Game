package modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe représentant le plateau de jeu avec toutes les cartes encore en jeu.
 */
public class Plateau implements Serializable {
    ZoneCarte zoneCentre = new ZoneCarte();
    List<ZoneJoueur> zonesJoueur =  new ArrayList<>();

    public ZoneJoueur getZone(Joueur joueur) {
        for (ZoneJoueur z : zonesJoueur) if (z.joueur == joueur) return z;
        return null;
    }

    /**
     * Génère le jeu complet : 12 types x 3 exemplaires = 36 cartes
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
     * Distribue les cartes entre les joueurs et le centre.
     */
    public void distribuerCartes(int nbJoueurs, List<Joueur> joueurs) {
        if (joueurs == null || joueurs.size() != nbJoueurs) {
            throw new IllegalArgumentException("La liste des joueurs doit contenir exactement " + nbJoueurs + " joueurs.");
        }

        // Générer et mélanger le jeu complet (36 cartes)
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

        // Distribution aux joueurs
        for (ZoneJoueur z : zonesJoueur) {
            for (int i = 0; i < cartesParJoueur; i++) {
                if (pioche.isEmpty()) break;
                Carte c = pioche.remove(0);
                z.cartes.add(c);
                c.cacher();
            }
            z.trierMain();
        }

        // Cartes au centre
        zoneCentre.cartes.clear();
        for (int i = 0; i < cartesAuCentre && !pioche.isEmpty(); i++) {
            Carte c = pioche.removeFirst();
            c.cacher(); // Cartes du centre face cachée au départ
            zoneCentre.cartes.add(c);
        }
    }

    /**
     * Vérifie si une carte peut être retournée selon les règles :
     * - N'importe quelle carte du centre (non retournée)
     * - La première carte NON RETOURNÉE de la main
     * - La dernière carte NON RETOURNÉE de la main
     */
    /*
    public boolean peutRetourner(Carte carte, List<Joueur> tousJoueurs) {
        if (carte == null || carte.estVisible()) {
            return false;
        }

        // Vérifier le centre
        if (zoneCentre.cartes.contains(carte)) {
            return true;
        }

        // Vérifier les mains des joueurs
        for (Joueur joueur : tousJoueurs) {
            List<Carte> main = joueur.getMain();
            if (!main.contains(carte)) {
                continue;
            }
            
            int index = main.indexOf(carte);
            
            // Trouver la première carte NON RETOURNÉE
            int premiereNonRetournee = -1;
            for (int i = 0; i < main.size(); i++) {
                if (!main.get(i).estVisible()) {
                    premiereNonRetournee = i;
                    break;
                }
            }
            
            // Trouver la dernière carte NON RETOURNÉE
            int derniereNonRetournee = -1;
            for (int i = main.size() - 1; i >= 0; i--) {
                if (!main.get(i).estVisible()) {
                    derniereNonRetournee = i;
                    break;
                }
            }
            
            // La carte est jouable si c'est la première OU la dernière non retournée
            return index == premiereNonRetournee || index == derniereNonRetournee;
        }

        return false;
    }*/

    public boolean retournerCarte(Carte carte) {
        if (carte != null && !carte.estVisible()) {
            carte.retourner();
            return true;
        }
        return false;
    }

    public boolean verifierTrio(Carte c1, Carte c2, Carte c3) {
        if (c1 == null || c2 == null || c3 == null) return false;
        return c1.equals(c2) && c2.equals(c3);
    }

    /**
     * Retire les cartes du trio des mains des joueurs et/ou du centre.
     */
    /*
    public void retirerTrio(List<Carte> trio, List<Joueur> joueurs) {
        if (trio == null || trio.size() != 3) return;
        for (Carte carte : trio) {
            zoneCentre.cartes.remove(carte);
            if (joueurs != null) {
                for (Joueur joueur : joueurs) {
                    joueur.retirerCarte(carte);
                }
            }
        }
    }
*/
    // --- Getters ---

    public boolean aDesCartes() {
        return !zoneCentre.cartes.isEmpty();
    }

    public List<Carte> getCartesCentre() {
        return new ArrayList<>(zoneCentre.cartes);
    }

    public ZoneCarte getZoneCentre() {
        return zoneCentre;
    }
    
    @Override
    public String toString() {
        return "Plateau{ cartes au centre=" + zoneCentre.cartes.size() + " }";
    }
}