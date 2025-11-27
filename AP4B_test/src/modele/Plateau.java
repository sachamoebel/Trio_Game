package modele;

import java.util.ArrayList;
import java.util.List;
 
// ==================== FICHIER: Plateau.java ====================

/**
 * Classe représentant le plateau de jeu avec toutes les cartes
 */
public class Plateau {
    private List<Carte> cartesCentre;
    private List<Trio> triosFormes;
    
    /**
     * Constructeur de la classe Plateau
     */
    public Plateau() {
        this.cartesCentre = new ArrayList<>();
        this.triosFormes = new ArrayList<>();
    }
    
    /**
     * Initialise toutes les cartes du jeu
     * Crée 6 types de cartes avec 3 exemplaires de chaque (18 cartes au total)
     * Toutes les cartes d'un même type ont la même valeur
     */
    public void initialiserCartes() {
        cartesCentre.clear();
        
        // Créer 3 exemplaires de chaque type de carte
        TypeCarte[] types = TypeCarte.values();
        for (TypeCarte type : types) {
            for (int i = 0; i < 3; i++) {
                Carte carte = new Carte(type);
                cartesCentre.add(carte);
            }
        }
    }
    
    /**
     * Distribue les cartes entre les joueurs et le centre
     * @param nbJoueurs Le nombre de joueurs (2 ou 3)
     * @param joueurs La liste des joueurs
     */
    public void distribuerCartes(int nbJoueurs, List<Joueur> joueurs) {
        if (joueurs == null || joueurs.size() != nbJoueurs) {
            return;
        }
        
        int cartesParJoueur = (nbJoueurs == 2) ? 7 : 6;
        int cartesCentre = (nbJoueurs == 2) ? 4 : 6;
        
        int index = 0;
        
        // Distribuer les cartes aux joueurs
        for (Joueur joueur : joueurs) {
            for (int i = 0; i < cartesParJoueur && index < this.cartesCentre.size(); i++) {
                Carte carte = this.cartesCentre.get(index);
                joueur.ajouterCarte(carte);
                index++;
            }
        }
        
        // Garder les cartes restantes au centre
        List<Carte> nouvCartesCentre = new ArrayList<>();
        for (int i = index; i < index + cartesCentre && i < this.cartesCentre.size(); i++) {
            nouvCartesCentre.add(this.cartesCentre.get(i));
        }
        this.cartesCentre = nouvCartesCentre;
    }
    
    /**
     * Vérifie si une carte peut être retournée selon les règles
     * On peut retourner seulement:
     * - La première ou dernière carte de sa propre main
     * - La première ou dernière carte de la main adverse
     * - N'importe quelle carte du centre
     * @param carte La carte à vérifier
     * @param joueurActif Le joueur qui veut retourner la carte
     * @param tousJoueurs La liste de tous les joueurs
     * @return true si la carte peut être retournée
     */
    public boolean peutRetourner(Carte carte, Joueur joueurActif, List<Joueur> tousJoueurs) {
        if (carte == null || carte.estVisible()) {
            return false;
        }
        
        // Vérifier si la carte est au centre (toujours autorisé)
        if (cartesCentre.contains(carte)) {
            return true;
        }
        
        // Vérifier dans les mains des joueurs
        for (Joueur joueur : tousJoueurs) {
            List<Carte> main = joueur.getMain();
            int index = main.indexOf(carte);
            
            if (index != -1) {
                // La carte est dans la main de ce joueur
                // Autorisé si c'est la première (index 0) ou la dernière (index = size-1)
                return index == 0 || index == main.size() - 1;
            }
        }
        
        return false;
    }
    
    /**
     * Retourne une carte (la rend visible)
     * @param carte La carte à retourner
     * @return true si la carte a été retournée, false sinon
     */
    public boolean retournerCarte(Carte carte) {
        if (carte != null && !carte.estVisible()) {
            carte.retourner();
            return true;
        }
        return false;
    }
    
    /**
     * Vérifie si trois cartes forment un trio valide
     * @param c1 Première carte
     * @param c2 Deuxième carte
     * @param c3 Troisième carte
     * @return true si les trois cartes sont identiques, false sinon
     */
    public boolean verifierTrio(Carte c1, Carte c2, Carte c3) {
        if (c1 == null || c2 == null || c3 == null) {
            return false;
        }
        return c1.equals(c2) && c2.equals(c3);
    }
    
    /**
     * Retire un trio de cartes du plateau et des mains des joueurs
     * @param trio La liste des trois cartes du trio
     * @param joueurs La liste des joueurs
     */
    public void retirerTrio(List<Carte> trio, List<Joueur> joueurs) {
        if (trio == null || trio.size() != 3) {
            return;
        }
        
        // Retirer du centre
        for (Carte carte : trio) {
            cartesCentre.remove(carte);
            
            // Retirer des mains des joueurs
            if (joueurs != null) {
                for (Joueur joueur : joueurs) {
                    joueur.retirerCarte(carte);
                }
            }
        }
    }
    
    /**
     * Obtient toutes les cartes actuellement visibles
     * @return Liste des cartes visibles
     */
    public List<Carte> obtenirCartesVisibles() {
        List<Carte> cartesVisibles = new ArrayList<>();
        for (Carte carte : cartesCentre) {
            if (carte.estVisible()) {
                cartesVisibles.add(carte);
            }
        }
        return cartesVisibles;
    }
    
    /**
     * Cache toutes les cartes visibles
     */
    public void cacherToutesLesCartes() {
        for (Carte carte : cartesCentre) {
            carte.cacher();
        }
    }
    
    /**
     * Vérifie s'il reste des cartes sur le plateau
     * @return true s'il reste des cartes, false sinon
     */
    public boolean aDesCartes() {
        return !cartesCentre.isEmpty();
    }
    
    // ========== GETTERS ==========
    
    public List<Carte> getCartesCentre() {
        return new ArrayList<>(cartesCentre);
    }
    
    public List<Trio> getTriosFormes() {
        return new ArrayList<>(triosFormes);
    }
    
    public void ajouterTrio(Trio trio) {
        if (trio != null) {
            triosFormes.add(trio);
        }
    }
    
    @Override
    public String toString() {
        return "Plateau{" +
                "cartes au centre=" + cartesCentre.size() +
                ", trios formés=" + triosFormes.size() +
                '}';
    }
}