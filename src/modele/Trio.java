package modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un trio de trois cartes identiques
 */
public class Trio implements Serializable {
    private TypeCarte type;
    private List<Carte> cartes;
    
    /**
     * Constructeur avec le type de carte du trio
     * @param type Le type des cartes formant ce trio
     */
    public Trio(TypeCarte type) {
        this.type = type;
        this.cartes = new ArrayList<>();
    }

    /**
     * Obtient le type du trio
     * @return Le TypeCarte de ce trio
     */
    public TypeCarte getType() {
        return type;
    }
    
    /**
     * Obtient les cartes du trio
     * @return Une copie de la liste des cartes
     */
    public List<Carte> getCartes() {
        return new ArrayList<>(cartes);
    }

    @Override
    public String toString() {
        return "Trio{" +
                "type=" + type.getDescription() +
                ", cartes=" + cartes.size() + "/3" +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trio trio = (Trio) o;
        return type == trio.type;
    }
    
    @Override
    public int hashCode() {
        return type.hashCode();
    }
}