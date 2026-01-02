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
    private List<Carte> paquet;
    private Joueur joueurCourant;
    private int indiceCourant = 0;
    private Map<Integer, Integer> scores;
    private String message = "Attente...";
    private List<Carte> retourneesCeTour;
    private boolean terminee = false;

    /**
     * Constructeur de la classe Jeu
     */
    public Partie() {
        this.joueurs = new ArrayList<>();
        this.paquet = new ArrayList<>();
        this.scores = new HashMap<>();
        this.retourneesCeTour = new ArrayList<>();
        this.indiceCourant = 0;
    }

    /**
     * Démarre une nouvelle partie
     */
    public void demarrerPartie() {
        // Initialiser et distribuer les cartes
        plateau.distribuerCartes(joueurs.size(), joueurs);

        // Définir le premier joueur
        indiceCourant = 0;
        joueurCourant = joueurs.get(0);
    }

    public void ajouterZoneJoueur(int id) {
        plateau.zonesJoueur.add(new ZoneJoueur(getJoueurById(id)));
    }

    /**
     * Joue un tour avec une carte sélectionnée
     */
    /*
    public String jouerTour(Carte carte) {
        if (carte == null) {
            return "Carte invalide!";
        }

        // Vérifier si on peut retourner cette carte
        if (!plateau.peutRetourner(carte, joueurs)) {
            return "Vous ne pouvez retourner que la première ou dernière carte d'une main, ou n'importe quelle carte du centre!";
        }

        // Retourner la carte
        if (plateau.retournerCarte(carte)) {
            retourneesCeTour.add(carte);
        } else {
            return "Cette carte est déjà retournée!";
        }

        // Vérifier selon le nombre de cartes retournées
        if (retourneesCeTour.size() == 2) {
            // Vérifier si les 2 premières cartes sont identiques
            if (!retourneesCeTour.get(0).equals(retourneesCeTour.get(1))) {
                String msg = "❌ Les deux cartes ne correspondent pas!";
                // NE PAS cacher immédiatement - l'interface graphique le fera après un délai
                // Pour la version console, on cache après que l'utilisateur appuie sur Entrée
                return msg;
            }
            return "✅ Deux cartes identiques! Cherchez la troisième...";
        }

        if (retourneesCeTour.size() == 3) {
            // Vérifier si on a un trio
            if (plateau.verifierTrio(retourneesCeTour.get(0),
                                    retourneesCeTour.get(1),
                                    retourneesCeTour.get(2))) {

                // Trio trouvé!
                TypeCarte typeTrio = retourneesCeTour.get(0).getType();

                plateau.retirerTrio(retourneesCeTour, joueurs);

                // AJOUTER le trio au joueur courant
                Trio trio = new Trio(typeTrio);
                for (Carte c : retourneesCeTour) {
                    trio.ajouterCarte(c);
                }
                joueurCourant.ajouterTrio(trio);

                retourneesCeTour.clear();

                // Vérification spéciale pour le Type 7
                if (typeTrio == TypeCarte.TYPE7) {
                     return "🎉 TRIO SPÉCIAL DE TYPE 7 TROUVÉ! " + joueurCourant.getNom() +
                            " a trouvé le trio " + typeTrio.getDescription() + " et GAGNE LA PARTIE!";
                }

                // Vérification pour les 3 trios
                if (joueurCourant.getTriosGagnes().size() >= 3) {
                    return "🏆 TROIS TRIOS! " + joueurCourant.getNom() +
                           " a trouvé 3 trios (" + joueurCourant.getDescriptionTrios() + ") et GAGNE LA PARTIE!";
                }

                // Le joueur rejoue
                return "🎉 TRIO TROUVÉ! " + joueurCourant.getNom() +
                       " a trouvé le trio " + typeTrio.getDescription() + " et rejoue!";
            } else {
                String msg = "❌ Ce n'est pas un trio!";
                // NE PAS cacher immédiatement - l'interface graphique le fera après un délai
                return msg;
            }
        }

        return "✓ Carte retournée: " + carte.getType().getDescription() + " (valeur: " + carte.getValeur() + ")";
    }*/

    /**
     * Cache toutes les cartes retournées (appelé après un échec)
     */
    public void cacherretourneesCeTour() {
        for (Carte carte : retourneesCeTour) {
            carte.cacher();
        }
        retourneesCeTour.clear();
    }

    /**
     * Cache les cartes retournées ET change de joueur (pour les échecs)
     */
    public void gererEchec() {
        cacherretourneesCeTour();
        changerJoueur();
    }

    /**
     * Change le joueur courant
     */
    public void changerJoueur() {
        indiceCourant = (indiceCourant + 1) % joueurs.size();
        joueurCourant = joueurs.get(indiceCourant);
    }

    public void ajouterJoueur(Joueur joueur) {
        joueurs.add(joueur);
    }

    public void ajouteScore(int id, int score) {
        scores.put(id, score);
    }

    public void ajouteCarteReveleeTour(Carte c) {
        retourneesCeTour.add(c);
    }

    public boolean verifieEtatTour() {
        List<Carte> tour = retourneesCeTour;
        if (tour.size() < 2) return true; // Rien à vérifier

        Carte c1 = tour.get(tour.size() - 2);
        Carte c2 = tour.getLast();

        if (!c1.equals(c2)) {
            message = "Raté ! (" + c1.getValeur() + " est différent de " + c2.getValeur() + ")";
            return false;
        } else if (tour.size() == 3) {
            // TRIO !
            int valeurTrio = c1.getValeur();
            message = "TRIO de " + valeurTrio + " ! Joueur " + joueurCourant.getNom() + " gagne ce trio.";

            ZoneJoueur zoneGagnant = plateau.getZone(joueurCourant);
            zoneGagnant.triosGagnes.add(new Trio(c1.getType()));
            // Mise à jour du score numérique
            scores.put(joueurCourant.getId(), scores.get(joueurCourant.getId()) + 1);

            retirerCartesGagnees();
            retourneesCeTour.clear();

            // Condition de victoire (2 trios pour gagner)
            if (scores.get(joueurCourant.getId()) >= 3) {
                message = "VICTOIRE DU JOUEUR " + joueurCourant.getNom() + " !!!";
                terminee = true;
            }
        } else {
            message = "Bravo ! Encore une ...";
        }
        return true;
    }

    public void finTour() {
        for (Carte c : retourneesCeTour) c.cacher();
        retourneesCeTour.clear();
        changerJoueur();
        message = "Tour du joueur " + joueurCourant;
    }

    private void retirerCartesGagnees() {
        List<Carte> aRetirer = new ArrayList<>(retourneesCeTour);
        plateau.zoneCentre.cartes.removeAll(aRetirer);
        for (ZoneJoueur z : plateau.zonesJoueur) z.cartes.removeAll(aRetirer);
    }

    /**
     * Vérifie si la partie est terminée :
     * 1. Un joueur a obtenu 3 trios.
     * 2. Le trio de TYPE7 a été formé.
     * 3. Plus aucune carte chez les joueurs ni au centre.
     * @return true si la partie est terminée
     */
    public boolean verifierFinPartie() {
        // Condition 1 & 2 : Trio de 7 ou 3 trios
        for (Joueur joueur : joueurs) {
            for (Trio trio : plateau.getZone(joueur).triosGagnes) {
                if (trio.getType() == TypeCarte.TYPE7) {
                    return true;
                }
            }
            if (plateau.getZone(joueur).triosGagnes.size() >= 3) {
                return true;
            }
        }

        // Condition 3 : Plus de cartes disponibles (Fin de partie par épuisement)
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
     * Obtient le gagnant de la partie selon les règles de victoire.
     */
    public Joueur obtenirGagnant() {
        if (joueurs.isEmpty()) {
            return null;
        }

        // 1. Priorité au trio de Type 7
        for (Joueur joueur : joueurs) {
            for (Trio trio : plateau.getZone(joueur).triosGagnes) {
                if (trio.getType() == TypeCarte.TYPE7) {
                    return joueur;
                }
            }
        }

        // 2. Priorité aux 3 trios
        for (Joueur joueur : joueurs) {
            if (plateau.getZone(joueur).triosGagnes.size() >= 3) {
                return joueur;
            }
        }

        // 3. Score le plus élevé (si fin par épuisement)
        Joueur gagnant = joueurs.get(0);
        for (Joueur joueur : joueurs) {
            if (getScore(joueur) > getScore(gagnant)) {
                gagnant = joueur;
            }
        }

        return gagnant;
    }

    // ========== GETTERS ET SETTERS EXISTANTS ==========
    public boolean estTerminee() {
        return terminee;
    }

    public Joueur getJoueurById(int id) {
        for (Joueur joueur : joueurs) {
            if (joueur.getId() == id) {
                return joueur;
            }
        }
        return null;
    }

    public int getIndiceCourant() {
        return indiceCourant;
    }

    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    public int getScore(Joueur joueur) {
        return scores.get(joueur.getId());
    }

    public List<Trio> getTriosGagnes(Joueur joueur) {
        return plateau.getZone(joueur).getTriosGagnes();
    }

    public List<Joueur> getJoueurs() {
        return new ArrayList<>(joueurs);
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNomJoueur(int id, String nomJoueur) {
        Joueur j = getJoueurById(id);
        if (j != null) {
            j.setNom(nomJoueur.trim());
        }
    }

    public List<Carte> getCarteRetournees() {
        return new ArrayList<>(retourneesCeTour);
    }
/*
    public List<Carte> getOrdreAffichage() {
        return ordreAffichage;
    }

    public void setOrdreAffichage(List<Carte> ordreAffichage) {
        this.ordreAffichage = ordreAffichage;
    }

 */
}