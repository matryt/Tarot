import java.util.NoSuchElementException;
import java.util.Objects;

public class Carte implements Comparable<Carte> {
    String type;
    String valeur;
    Couleurs couleur;
    String ID;
    String [] typesAvailable = new String[] {"Atout", "Nombre"};
    String [] valeursAvailable = {"Excuse", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10","Valet", "Cavalier", "Dame", "Roi", "11", "12",
    "13", "14", "15", "16", "17", "18", "19", "20", "21"};


    public Carte(String type, String valeur, String couleur) {
        this.type = type;
        this.valeur = valeur;
        switch (couleur) {
            case "coeur" -> this.couleur = Couleurs.COEUR;
            case "carreau" -> this.couleur = Couleurs.CARREAU;
            case "pique" -> this.couleur = Couleurs.PIQUE;
            case "trefle" -> this.couleur = Couleurs.TREFLE;
            default -> this.couleur = Couleurs.AUCUNE;
        }
        ID = type.charAt(0) + valeur.substring(0, 2);
        if (!(couleur.equals(""))) {
            ID = ID + couleur.substring(0, 2);
        }
    }

    public String description() throws UnsupportedOperationException{
        switch (this.type) {
            case "Atout" -> {
                if (valeur.equals("Excuse")) {
                    return "ID " + this.ID + " - " + unfill(this.valeur);
                }
                if (valeur.equals("01")) {
                    return "ID " + this.ID + " - Petit";
                }
                return "ID " + this.ID + " - " + unfill(this.valeur) + " d'" + this.type;
            }
            case "Nombre" -> {
                return "ID " + this.ID + " - " + unfill(this.valeur) + " de " + this.couleur;
            }
            default -> throw new UnsupportedOperationException("Ce type est inconnu");
        }

    }

    public Boolean greater(Carte other) throws IllegalArgumentException {
        if (!Objects.equals(this.type, other.type) && (this.type.equals("Atout") || other.type.equals("Atout"))) {
            return (index(typesAvailable, this.type) < index(typesAvailable, other.type));
        }
        if (this.couleur == other.couleur) {
            if (!Objects.equals(this.type, other.type)) {
                return (index(typesAvailable, this.type) < index(typesAvailable, other.type));
            }
            return (index(valeursAvailable, this.valeur) > index(valeursAvailable, other.valeur));
        }
        throw new IllegalArgumentException("Les cartes doivent être de la même couleur pour être comparées.");
    }

    @Override
    public int compareTo(Carte other) {
        if (this.equals(other)) {
            return 0;
        }

        // Comparaison par type
        if (!this.type.equals(other.type)) {
            return index(typesAvailable, this.type) - index(typesAvailable, other.type);
        }

        // Comparaison par couleur
        if (this.couleur != other.couleur) {
            return this.couleur.compareTo(other.couleur);
        }

        // Comparaison par valeur
        return index(valeursAvailable, this.valeur) - index(valeursAvailable, other.valeur);
    }

    @Override
    public String toString() throws UnsupportedOperationException{
        switch (this.type) {
            case "Atout" -> {
                if (valeur.equals("Excuse")) {
                    return unfill(this.valeur);
                }
                if (valeur.equals("01")) {
                    return "Petit";
                }
                return unfill(this.valeur) + " d'" + this.type;
            }
            case "Nombre" -> {
                return unfill(this.valeur) + " de " + this.couleur;
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
