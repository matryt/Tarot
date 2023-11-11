public enum Valeurs {
    EXCUSE(-1),
    UN(1),
    DEUX(2),
    TROIS(3),
    QUATRE(4),
    CINQ(5),
    SIX(6),
    SEPT(7),
    HUIT(8),
    NEUF(9),
    DIX(10),
    ONZE(11),
    DOUZE(12),
    TREIZE(13),
    QUATORZE(14),
    QUINZE(15),
    SEIZE(16),
    DIXSEPT(17),
    DIXHUIT(18),
    DIXNEUF(19),
    VINGT(20),
    VINGTETUN(21),
    VALET(22),
    CAVALIER(23),
    DAME(24),
    ROI(25),
    ;
    private int value;

    Valeurs(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    public boolean isNumber() {
        return this.value > -1 && this.value < 22;
    }

    public String getName () {
        return this.name();
    }


    @Override
    public String toString() {
        if (isNumber()) {
            return String.valueOf(value);
        }
        else {
            return name();
        }
    }

    public static Valeurs fromInt(int i) throws IllegalArgumentException {
        for (Valeurs v : Valeurs.values()) {
            if (v.getValue() == i) {
                return v;
            }
        }
        throw new IllegalArgumentException("Aucune valeur correspondante trouvée pour " + i);
    }
}
