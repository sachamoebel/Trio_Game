package modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe principale qui gère la logique du jeu Trio
 */
public class Partie implements Serializable {
    private List<Joueur> joueurs;
    private Plateau plateau = new Plateau();
    private Joueur joueurCourant;
    private int indiceCourant = 0;
    private Map<Integer, Integer> scores;
    private String message = "Attente...";
    private List<Carte> retourneesCeTour;
    private boolean terminee = false;

    /**
     * Constructeur de la classe Partie
     */
    public Partie() {
        this.joueurs = new ArrayList<>();
        this.scores = new HashMap<>();
        this.retourneesCeTour = new ArrayList<>();
        this.indiceCourant = 0;
    }

    /**
     * Démarre une nouvelle partie en distribuant les cartes et en désignant le premier joueur
     */
    public void demarrerPartie() {
        plateau.distribuerCartes(joueurs.size(), joueurs);

        indiceCourant = 0;
        joueurCourant = joueurs.get(0);
    }

    /**
     * Ajoute une zone de jeu spécifique pour un joueur sur le plateau
     * @param id L'identifiant du joueur
     */
    public void ajouterZoneJoueur(int id) {
        plateau.zonesJoueur.add(new ZoneJoueur(getJoueurById(id)));
    }

    /**
     * Change le joueur courant vers le joueur suivant dans la liste
     */
    public void changerJoueur() {
        indiceCourant = (indiceCourant + 1) % joueurs.size();
        joueurCourant = joueurs.get(indiceCourant);
    }

    /**
     * Ajoute un nouveau joueur à la liste des participants
     * @param joueur Le joueur à ajouter
     */
    public void ajouterJoueur(Joueur joueur) {
        joueurs.add(joueur);
    }

    /**
     * Définit ou met à jour le score d'un joueur
     * @param id L'identifiant du joueur
     * @param score Le score à lui attribuer
     */
    public void ajouteScore(int id, int score) {
        scores.put(id, score);
    }

    /**
     * Enregistre une carte révélée durant le tour actuel
     * @param c La carte qui vient d'être retournée
     */
    public void ajouteCarteReveleeTour(Carte c) {
        retourneesCeTour.add(c);
    }

    /**
     * Vérifie si les cartes retournées ce tour sont identiques.
     * Gère également la validation d'un Trio et les conditions de victoire immédiate.
     * @return true si le tour peut continuer (cartes identiques ou 1ère carte), false si les cartes diffèrent
     */
    public boolean verifieEtatTour() {
        List<Carte> tour = retourneesCeTour;
        if (tour.size() < 2) return true;

        Carte c1 = tour.get(tour.size() - 2);
        Carte c2 = tour.getLast();

        if (!c1.equals(c2)) {
            message = "Raté ! (" + c1.getValeur() + " est différent de " + c2.getValeur() + ")";
            return false;
        } else if (tour.size() == 3) {
            int valeurTrio = c1.getValeur();
            message = "TRIO de " + valeurTrio + " ! Joueur " + joueurCourant.getNom() + " gagne ce trio.";

            ZoneJoueur zoneGagnant = plateau.getZone(joueurCourant);
            zoneGagnant.triosGagnes.add(new Trio(c1.getType()));
            scores.put(joueurCourant.getId(), scores.get(joueurCourant.getId()) + 1);

            retirerCartesGagnees();
            retourneesCeTour.clear();
        } else {
            message = "Bravo ! Encore une carte ...";
        }
        return true;
    }

    /**
     * Finalise le tour : cache les cartes si nécessaire, vide la liste du tour
     * et passe au joueur suivant.
     */
    public void finTour() {
        for (Carte c : retourneesCeTour) c.cacher();
        retourneesCeTour.clear();
        changerJoueur();
        message = "Tour du joueur " + joueurCourant;
    }

    /**
     * Retire définitivement du plateau les cartes formant un trio gagné
     */
    private void retirerCartesGagnees() {
        List<Carte> aRetirer = new ArrayList<>(retourneesCeTour);
        plateau.zoneCentre.cartes.removeAll(aRetirer);
        for (ZoneJoueur z : plateau.zonesJoueur) z.cartes.removeAll(aRetirer);
    }

    /**
     * Vérifie si la partie est terminée selon les trois règles :
     * 1. Un joueur a obtenu 3 trios.
     * 2. Le trio de TYPE7 a été formé.
     * 3. Plus aucune carte disponible.
     * @return true si la partie est terminée, false sinon
     */
    public boolean verifierFinPartie() {
        for (Joueur joueur : joueurs) {
            for (Trio trio : plateau.getZone(joueur).triosGagnes) {
                if (trio.getType() == TypeCarte.TYPE7) {
                    message = "VICTOIRE DU JOUEUR " + joueurCourant.getNom() + " !!!";
                    terminee = true;
                    return true;
                }
            }
            if (scores.get(joueur.getId()) >= 3) {
                message = "VICTOIRE DU JOUEUR " + joueurCourant.getNom() + " !!!";
                terminee = true;
                return true;
            }
        }

        if (!plateau.getCartesCentre().isEmpty()) {
            return false;
        }

        for (Joueur joueur : joueurs) {
            if (!plateau.getZone(joueur).cartes.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Indique si la partie est actuellement terminée
     * @return true si la partie est finie
     */
    public boolean estTerminee() {
        return terminee;
    }

    /**
     * Recherche un joueur dans la partie grâce à son identifiant
     * @param id L'identifiant recherché
     * @return Le joueur correspondant ou null si non trouvé
     */
    public Joueur getJoueurById(int id) {
        for (Joueur joueur : joueurs) {
            if (joueur.getId() == id) {
                return joueur;
            }
        }
        return null;
    }

    /**
     * Retourne l'indice du joueur dont c'est le tour
     * @return L'indice dans la liste des joueurs
     */
    public int getIndiceCourant() {
        return indiceCourant;
    }

    /**
     * Retourne le joueur qui doit jouer actuellement
     * @return L'objet Joueur courant
     */
    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    /**
     * Retourne le score d'un joueur spécifique
     * @param joueur Le joueur concerné
     * @return Le score numérique associé
     */
    public int getScore(Joueur joueur) {
        return scores.get(joueur.getId());
    }

    /**
     * Récupère la liste des trios remportés par un joueur
     * @param joueur Le joueur concerné
     * @return La liste des trios gagnés
     */
    public List<Trio> getTriosGagnes(Joueur joueur) {
        return plateau.getZone(joueur).getTriosGagnes();
    }

    /**
     * Retourne la liste de tous les joueurs de la partie
     * @return Une nouvelle liste contenant les joueurs
     */
    public List<Joueur> getJoueurs() {
        return new ArrayList<>(joueurs);
    }

    /**
     * Retourne le plateau de jeu
     * @return L'objet Plateau
     */
    public Plateau getPlateau() {
        return plateau;
    }

    /**
     * Retourne le message d'état actuel du jeu
     * @return Le message formaté pour l'affichage
     */
    public String getMessage() {
        return message;
    }

    /**
     * Modifie le message d'état du jeu
     * @param message Le nouveau message à afficher
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Modifie le nom d'un joueur existant
     * @param id L'identifiant du joueur
     * @param nomJoueur Le nouveau nom à appliquer
     */
    public void setNomJoueur(int id, String nomJoueur) {
        Joueur j = getJoueurById(id);
        if (j != null) {
            j.setNom(nomJoueur.trim());
        }
    }
}