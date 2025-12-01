package modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe représentant le plateau de jeu avec toutes les cartes encore en jeu.
 */
public class Plateau {
    private List<Carte> cartesCentre;   // cartes disponibles au centre
    // private List<Trio> triosFormes; // RETIRÉ
    
    public Plateau() {
        this.cartesCentre = new ArrayList<>();
        // this.triosFormes = new ArrayList<>(); // RETIRÉ
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
     * Initialise les cartes (utile si tu veux juste remplir le plateau sans distribuer)
     */
    public void initialiserCartes() {
        // Aucune action spécifique n'est nécessaire ici, la distribution est faite par distribuerCartes
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
        for (int round = 0; round < cartesParJoueur; round++) {
            for (Joueur joueur : joueurs) {
                if (pioche.isEmpty()) break;
                Carte c = pioche.remove(0);
                joueur.ajouterCarte(c);
            }
        }

        // Tri et initialisation des cartes face cachée
        for (Joueur joueur : joueurs) {
            joueur.trierMain(); 
            for(Carte c : joueur.getMain()) {
                c.cacher();
            }
        }

        // Cartes au centre
        cartesCentre.clear();
        for (int i = 0; i < cartesAuCentre && !pioche.isEmpty(); i++) {
            Carte c = pioche.remove(0);
            c.cacher(); // Cartes du centre face cachée au départ
            cartesCentre.add(c);
        }
    }

    /**
     * Vérifie si une carte peut être retournée selon les règles
     */
    public boolean peutRetourner(Carte carte, List<Joueur> tousJoueurs) {
        if (carte == null || carte.estVisible()) {
            return false;
        }

        // Vérifier le centre
        if (cartesCentre.contains(carte)) {
            return true;
        }

        // Vérifier les mains des joueurs
        for (Joueur joueur : tousJoueurs) {
            List<Carte> main = joueur.getMain();
            if (!main.contains(carte)) {
                continue;
            }
            
            int index = main.indexOf(carte);
            return index == 0 || index == main.size() - 1;
        }

        return false;
    }

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
    public void retirerTrio(List<Carte> trio, List<Joueur> joueurs) {
        if (trio == null || trio.size() != 3) return;
        for (Carte carte : trio) {
            cartesCentre.remove(carte);
            if (joueurs != null) {
                for (Joueur joueur : joueurs) {
                    joueur.retirerCarte(carte);
                }
            }
        }
    }

    // --- Getters ---

    public boolean aDesCartes() {
        return !cartesCentre.isEmpty();
    }

    public List<Carte> getCartesCentre() {
        return new ArrayList<>(cartesCentre);
    }

    // REMOVAL: getTriosFormes et ajouterTrio sont maintenant dans Jeu.java (via Joueur.java)
    
    @Override
    public String toString() {
        return "Plateau{ cartes au centre=" + cartesCentre.size() + " }";
    }
}