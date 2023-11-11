import org.jetbrains.annotations.NotNull;


import java.util.NoSuchElementException;

public class Carte implements Comparable<Carte> {
    String type;
    Valeurs valeur;
    Couleurs couleur;
    String ID;
    String [] typesAvailable = new String[] {"Atout", "Nombre"};


    public Carte(String type, Valeurs valeur, String couleur) {
        this.type = type;
        this.valeur = valeur;
        switch (couleur) {
            case "Coeur" -> this.couleur = Couleurs.COEUR;
            case "Carreau" -> this.couleur = Couleurs.CARREAU;
            case "Pique" -> this.couleur = Couleurs.PIQUE;
            case "Trèfle" -> this.couleur = Couleurs.TREFLE;
            default -> this.couleur = Couleurs.AUCUNE;
        }
        ID = type.charAt(0) + valeur.toString();
        if (!(couleur.isEmpty())) {
            ID = ID + couleur.substring(0, 2);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        Carte other = (Carte) obj;
        return this.compareTo(other) > 0;
    }

    @Override
    public int compareTo(@NotNull Carte other) {

        // Comparaison par type
        if (!this.type.equals(other.type)) {
            return - index(typesAvailable, this.type) + index(typesAvailable, other.type);
        }

        // Comparaison par couleur
        if (this.couleur != other.couleur) {
            return this.couleur.compareTo(other.couleur);
        }

        // Comparaison par valeur
        return this.valeur.getValue() - other.valeur.getValue();
    }

    @Override
    public String toString() throws UnsupportedOperationException{
        String val = this.valeur.toString();
        switch (this.type) {
            case "Atout" -> {
                if (val.equals("Excuse")) {
                    return val;
                }
                if (val.equals("01")) {
                    return "Petit";
                }
                return unfill(val) + " d'" + this.type;
            }
            case "Nombre" -> {
                return unfill(val) + " de " + this.couleur;
            }
            default -> throw new UnsupportedOperationException("Ce type est inconnu");
        }

    }


    public int index(String [] table, String element) throws NoSuchElementException {
        for (int i=0; i<table.length; i++) {
            if (table[i].equals(element)) {
                return i;
            }
        }
        throw new NoSuchElementException("L'élément passé en paramètre n'existe pas !");
    }

    public static String unfill(String str) {
        StringBuilder sb = new StringBuilder(str);
        while (sb.substring(0, 1).equals("0")) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    public String getID() {
        return ID;
    }
}
