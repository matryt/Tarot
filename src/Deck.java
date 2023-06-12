import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

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
            ch.append("\t").append(i).append(") ").append(cards[i].toString()).append("\n");
        }
        return ch.toString();
    }


    public Boolean hasColor(String color) throws IllegalArgumentException {
        for (Carte c: cards) {
            try {
                if (c.couleur == Couleurs.valueOf(color.toUpperCase())) {
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
            if (c.type.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public Boolean isEmpty() {
        for (Carte c: cards) {
            if (c != null) {
                return false;
            }
        }
        return true;
    }

    public Carte getCardByID(String ID) throws NoSuchElementException {
        for (Carte c: cards) {
            if (c.getID().equals(ID)) {
                return c;
            }
        }
        throw new NoSuchElementException("Aucune carte trouvée avec l'ID spécifié.");
    }

    public Carte randCard() {
        if (isEmpty()) {
            throw new NoSuchElementException("Le tableau est vide.");
        }
        Carte c = cards[0];
        while (c==null) {
            shuffle();
            c = cards[0];
        }
        return c;
    }

    public void shuffle() {
        Random rd = new Random();
        for (int i = cards.length - 1; i>0; i--) {
            int j = rd.nextInt(i+1);
            Carte temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
        }
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

            return index(carte1.valeursAvailable, carte1.valeur) > index(carte2.valeursAvailable, carte2.valeur) ? 1 : -1;

        });
    }

    public Carte getByIndex(int index) {
        return cards[index];
    }

    public int index(String [] table, String element) throws NoSuchElementException {
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
}
