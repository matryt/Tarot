import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class Deck implements Cloneable{
    int length;
    Carte[] cards;
    public Deck(int number) {
        cards = new Carte[number];
        length = number;
    }

    public int firstNullIndex() throws NoSuchElementException {
        for (int i=0; i<length; i++) {
            if (cards[i] == null) {
                return i;
            }
        }
        throw new NoSuchElementException("Il n'y a aucune case vide dans ce tableau");
    }

    public void append(Carte elem) throws ArrayIndexOutOfBoundsException {
        try {
            int index = firstNullIndex();
            cards[index] = elem;
        }
        catch (NoSuchElementException e) {
            throw new ArrayIndexOutOfBoundsException("Le tableau dans lequel vous souhaitez ajouter un élément est déjà plein !");
        }
    }

    @Override
    public String toString() {
        StringBuilder ch= new StringBuilder();
        for (int i=0;i<cards.length;i++) {
            ch.append("\t").append(i).append(") ");
            if (cards[i] != null) {
                ch.append(cards[i].toString());
            }
            else {
                ch.append("Aucune");
            }
            ch.append("\n");
        }
        return ch.toString();
    }


    public boolean hasColor(String color) throws IllegalArgumentException {
        for (Carte c: cards) {
            try {
                if (c != null && c.couleur == Couleurs.valueOf(color.toUpperCase())) {
                    return true;
                }
            }
            catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("La couleur passée en paramètre est inconnue !");
            }
        }
        return false;
    }

    public Boolean hasType(String type) {
        for (Carte c: cards) {
            if (c != null && c.type.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public int maxAtout() {
        int max = 0;
        for (Carte c: cards) {
            if (c != null && c.type.equals("Atout") && !(c.valeur.getName().equals("Excuse")) && c.valeur.getValue() > max) {
                max = c.valeur.getValue();
            }
        }
        return max;
    }

    public Boolean isEmpty() {
        for (Carte c: cards) {
            if (c != null) {
                return false;
            }
        }
        return true;
    }

    public boolean chienCorrect() {
        for (Carte c: cards) {
            if (c.type.equals("Atout") || c.valeur.getName().contains("Roi")) {
                return false;
            }
        }
        return true;
    }

    public void sort() {
        Arrays.sort(cards, (carte1, carte2) -> {
            // Comparaison par type
            if (!(carte1.type.equals(carte2.type))) {
                return index(carte1.typesAvailable, carte1.type) < index(carte2.typesAvailable, carte2.type) ? 1 : -1;
            }

            if (carte1.couleur != carte2.couleur) {
                return carte1.couleur.compareTo(carte2.couleur);
            }

            return carte1.valeur.getValue() > carte2.valeur.getValue() ? 1 : -1;

        });
    }

    public Carte getByIndex(int index) {
        return cards[index];
    }

    public int index(String @NotNull [] table, String element) throws NoSuchElementException {
        for (int i=0; i<table.length; i++) {
            if (table[i].equals(element)) {
                return i;
            }
        }
        throw new NoSuchElementException("L'élément passé en paramètre n'existe pas !");
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();    // renvoie une copie superficielle
    }

    public Carte getCarteHaute() {
        int i = 0;
        Carte myCard = cards[0];
        while (myCard != null && !((myCard.type.equals("Atout")
                    && (myCard.valeur.getValue() == 21 || myCard.valeur.getValue() == 1))
                || (myCard.type.equals("Nombre")
                && myCard.valeur.getValue() >= 22
                && myCard.valeur.getValue() <= 25))
                && !myCard.estComptee()) {
            i += 1;
            myCard = cards[i];
        }
        if (myCard != null) {
            myCard.compter();
        }
        return myCard;
    }

    public boolean carteHauteExiste() {
        int i = 0;
        Carte myCard = cards[0];
        while (myCard != null && !((myCard.type.equals("Atout")
                && (myCard.valeur.getValue() == 21 || myCard.valeur.getValue() == 1))
                || (myCard.type.equals("Nombre")
                && myCard.valeur.getValue() >= 22
                && myCard.valeur.getValue() <= 25))
                && !myCard.estComptee()) {
            i += 1;
            myCard = cards[i];
        }
        return myCard != null;
    }

    public Carte getCarteBasse() {
        int i = 0;
        Carte myCard = cards[0];
        while (myCard != null && (myCard.type.equals("Nombre")
                && myCard.valeur.getValue() >= 1
                && myCard.valeur.getValue() <= 21)
                && !myCard.estComptee()) {
            i += 1;
            myCard = cards[i];
        }
        if (myCard != null) {
            myCard.compter();
        }
        return myCard;
    }

    public boolean carteBasseExiste() {
        int i = 0;
        Carte myCard = cards[0];
        while (myCard != null && (myCard.type.equals("Nombre")
                && myCard.valeur.getValue() >= 1
                && myCard.valeur.getValue() <= 21)
                && !myCard.estComptee()) {
            i += 1;
            myCard = cards[i];
        }
        return myCard != null;
    }

    public double compterPoints() {
        HashMap<String, Integer> pointsParCarte = new HashMap<String, Integer>();
        pointsParCarte.put("Atout", 5);
        pointsParCarte.put("Roi", 5);
        pointsParCarte.put("Dame", 4);
        pointsParCarte.put("Cavalier", 3);
        pointsParCarte.put("Valet", 2);
        double points = 0.0;
        while (carteHauteExiste()) {
            Carte carteHaute = getCarteHaute();
            Carte carteBasse = getCarteBasse();
            points += pointsParCarte.get(carteHaute.type);
        }
        while (carteBasseExiste()) {
            Carte carteBasse = getCarteBasse();
            points += 0.5;
        }
        return points;
    }
}
