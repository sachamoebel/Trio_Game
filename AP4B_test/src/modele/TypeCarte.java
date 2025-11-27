package modele;

// ==================== FICHIER: TypeCarte.java ====================

/**
 * Énumération des types de cartes du jeu
 */
public enum TypeCarte {
    PROJET_INFO(1, "Projet Info"),
    PROJET_MECA(2, "Projet Méca"),
    RAPPORT(3, "Rapport"),
    PRESENTATION(4, "Présentation"),
    CODE(5, "Code Source"),
    GROUPE(6, "Groupe Étudiant");
    
    private final int valeur;
    private final String description;
    
    TypeCarte(int valeur, String description) {
        this.valeur = valeur;
        this.description = description;
    }
    
    public int getValeur() {
        return valeur;
    }
    
    public String getDescription() {
        return description;
    }
}