package modele;

public enum TypeCarte {
    TYPE1(1, "Type 1"),
    TYPE2(2, "Type 2"),
    TYPE3(3, "Type 3"),
    TYPE4(4, "Type 4"),
    TYPE5(5, "Type 5"),
    TYPE6(6, "Type 6"),
    TYPE7(7, "Type 7"),
    TYPE8(8, "Type 8"),
    TYPE9(9, "Type 9"),
    TYPE10(10, "Type 10"),
    TYPE11(11, "Type 11"),
    TYPE12(12, "Type 12");

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
