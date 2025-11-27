package modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe représentant le plateau de jeu avec toutes les cartes
 */
public class Plateau {
    private List<Carte> cartesCentre;   // cartes disponibles au centre (après distribution)
    private List<Trio> triosFormes;

    public Plateau() {
        this.cartesCentre = new ArrayList<>();
        this.triosFormes = new ArrayList<>();
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
     * Ici on remplit cartesCentre avec le jeu complet mélangé (rarement nécessaire si on utilise distribuerCartes)
     */
    public void initialiserCartes() {
        List<Carte> jeu = genererJeuComplet();
        Collections.shuffle(jeu);
        this.cartesCentre = new ArrayList<>(jeu);
    }

    /**
     * Distribue les cartes entre les joueurs et le centre selon tes règles :
     * - 3 joueurs : 9 cartes/joueur, 9 au centre
     * - 4 joueurs : 7 cartes/joueur, 8 au centre
     * - 5 joueurs : 6 cartes/joueur, 6 au centre
     * - 6 joueurs : 5 cartes/joueur, 6 au centre
     *
     * La distribution est ALÉATOIRE et les mains sont triées par ordre croissant (valeur du type).
     *
     * @param nbJoueurs Le nombre de joueurs (3 à 6)
     * @param joueurs La liste des joueurs (taille == nbJoueurs)
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

        // Vérif sécurité : cartesParJoueur * nbJoueurs + cartesAuCentre doit être <= 36
        int totalDistribue = cartesParJoueur * nbJoueurs + cartesAuCentre;
        if (totalDistribue > pioche.size()) {
            throw new IllegalStateException("Le total de cartes à distribuer (" + totalDistribue + ") dépasse le nombre de cartes disponibles (" + pioche.size() + ").");
        }


        // Distribuer aux joueurs (tour par tour pour plus d'équité si souhaité)
        for (int round = 0; round < cartesParJoueur; round++) {
            for (Joueur joueur : joueurs) {
                if (pioche.isEmpty()) break;
                Carte c = pioche.remove(0);
                joueur.ajouterCarte(c);
            }
        }

        // Trier les mains des joueurs par valeur croissante
        for (Joueur joueur : joueurs) {
            joueur.trierMain(); // méthode ajoutée dans Joueur.java (voir plus bas)
        }

        // Prendre les cartes restantes pour le centre (nombre exact)
        cartesCentre.clear();
        for (int i = 0; i < cartesAuCentre && !pioche.isEmpty(); i++) {
            cartesCentre.add(pioche.remove(0));
        }

        // Note : si tu veux garder la pioche restante pour piocher plus tard, laisse pioche,
        // sinon ici toutes les cartes distribuées + centre = totalDistribue, et le reste (s'il y en a) est ignoré.
    }

    /* --- méthodes existantes (inchangées) --- */

    /**
     * Vérifie si une carte peut être retournée selon les règles
     * - première/dernière d'une main
     * - ou n'importe quelle carte du centre
     */
    public boolean peutRetourner(Carte carte, Joueur joueurActif, List<Joueur> tousJoueurs) {
        if (carte == null || carte.estVisible()) {
            return false;
        }

        if (cartesCentre.contains(carte)) {
            return true;
        }

        for (Joueur joueur : tousJoueurs) {
            List<Carte> main = joueur.getMain();
            int index = main.indexOf(carte);

            if (index != -1) {
                return index == 0 || index == main.size() - 1;
            }
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

    public List<Carte> obtenirCartesVisibles() {
        List<Carte> cartesVisibles = new ArrayList<>();
        for (Carte carte : cartesCentre) {
            if (carte.estVisible()) cartesVisibles.add(carte);
        }
        return cartesVisibles;
    }

    public void cacherToutesLesCartes() {
        for (Carte carte : cartesCentre) carte.cacher();
    }

    public boolean aDesCartes() {
        return !cartesCentre.isEmpty();
    }

    public List<Carte> getCartesCentre() {
        return new ArrayList<>(cartesCentre);
    }

    public List<Trio> getTriosFormes() {
        return new ArrayList<>(triosFormes);
    }

    public void ajouterTrio(Trio trio) {
        if (trio != null) triosFormes.add(trio);
    }

    @Override
    public String toString() {
        return "Plateau{ cartes au centre=" + cartesCentre.size() + ", trios formés=" + triosFormes.size() + " }";
    }
}
