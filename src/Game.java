import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private final int nPlayers;
    private final Player[] players;
    private Player joueurEnchere;
    private String enchereFaite;
    private final Deck chien;
    private int playerStarting;
    private Deck[] cartesGagnees;
    private static final Scanner s = new Scanner(System.in);
    private Carte[] cartesJouees;

    public Game() {
        Deck myCartes = new Deck(78);
        myCartes.cards = createCards();
        nPlayers = askNumberPlayers();
        players = new Player[nPlayers];
        Deck[] decks = createDecks(myCartes, nPlayers);
        createPlayers(decks);
        chien = decks[nPlayers];
        createTasGagne();

    }

    public void createTasGagne() {
        cartesGagnees = new Deck[nPlayers];
        for (int i=0; i<nPlayers; i++) {
            cartesGagnees[i] = new Deck(78);
        }
    }

    public void shuffle(Carte[] myCartes) {
        Random rd = new Random();
        for (int i = myCartes.length - 1; i>0; i--) {
            int j = rd.nextInt(i+1);
            Carte temp = myCartes[i];
            myCartes[i] = myCartes[j];
            myCartes[j] = temp;
        }
    }

    public Deck[] createDecks(Deck cards, int players) throws IllegalArgumentException {
        if (players < 3 || players > 5) {
            throw new IllegalArgumentException("Trop de participants pour jouer au jeu.");
        }
        int chien;
        if (players == 5) {
            chien = 3;
        }
        else {
            chien = 6;
        }
        int cartesRestantes = cards.cards.length - chien;
        int cartesParJoueur = cartesRestantes/players;
        Deck[] gameDecks = new Deck[players+1];
        for (int i=0;i<players;i++) {
            Deck myDeck = new Deck(cartesParJoueur);
            for (int j=0; j<cartesParJoueur;j++) {
                myDeck.append(randCard(cards.cards));
            }
            myDeck.sort();
            gameDecks[i] = myDeck;
        }
        Deck chienGame = new Deck(chien);
        for (int j=0;j<chien;j++) {
            chienGame.append(randCard(cards.cards));
        }
        gameDecks[players] = chienGame;
        return gameDecks;
    }

    public Carte randCard(Carte[] game) {
        shuffle(game);
        Carte elem = game[0];
        while (elem == null) {
            shuffle(game);
            elem = game[0];
        }
        game[0] = null;
        return elem;
    }


    public Carte[] createCards() {
        Carte[] myCartes = new Carte[78];
        int indice = 0;
        int i;
        String[] types = new String[]{"Atout", "Nombre"};
        int[] figures = new int[]{22, 23, 24, 25};
        String[] couleurs = new String[]{"Coeur", "Carreau", "Pique", "Trèfle"};

        for (i=1; i<11; i++) {
            for (String couleur: couleurs) {
                myCartes[indice] = new Carte(types[1], Valeurs.fromInt(i), couleur);
                indice++;
            }
            myCartes[indice] = new Carte(types[0], Valeurs.fromInt(i), "");
            indice++;
        }

        for (int f: figures) {
            for (String couleur: couleurs) {
                myCartes[indice] = new Carte(types[1], Valeurs.fromInt(f), couleur);
                indice++;
            }
        }

        for (i=11; i<22; i++) {
            myCartes[indice] = new Carte(types[0], Valeurs.fromInt(i), "");
            indice++;
        }

        myCartes[77] = new Carte("Atout", Valeurs.EXCUSE, "");

        for (i=0;i<10;i++) {
            shuffle(myCartes);
        }

        return myCartes;
    }

    public int askNumberPlayers() {
        System.out.println("Combien y-a-t'il de joueurs (entre 3 et 5) ?");
        int n = s.nextInt();
        while (n < 3 || n > 5) {
            System.out.println("Impossible !");
            System.out.println("Combien y-a-t'il de joueurs (entre 3 et 5) ?");
            n = s.nextInt();
        }
        return n;
    }

    public void createPlayers(Deck[] d) {
        for (int i=0;i<nPlayers;i++) {
            System.out.println("Joueur " + (i+1) + ", quel est ton pseudo ?");
            String name = s.next();
            players[i] = new Player(name, d[i]);
        }
    }

    public void afficherCartes() {
        System.out.println("\n");
        for (Player j: players) {
            String sc = j.getName();
            System.out.println("Joueur " + sc +", entrez n'importe quel caractère pour voir vos cartes");
            s.next();
            System.out.println(j.myCards);
        }
    }

    public void mainloop() {
        afficherCartes();
        encheres();
        montrerChien();
        while (!players[0].myCards.isEmpty()) {
            wholeTurn();
        }
        calculerPoints();
    }

    public void encheres() {
        String[] encheresPossibles = new String[]{"passe", "petite", "pousse", "garde", "garde sans chien", "garde contre chien"};
        int numEncheres = 0;
        for (int i = 0; i < 2; i++) {
            for (Player player : players) {
                if (numEncheres == 5 ) {
                    continue;
                }
                System.out.println(player.getName() + ", c'est à vous d'enchérir");
                if (numEncheres > 0) {
                    System.out.println("L'enchère minimale est " + encheresPossibles[numEncheres + 1]);
                }
                StringBuilder c = new StringBuilder();
                c.append("Quelle enchère voulez-vous faire : \n\t- 0 : PASSE\n");
                for (int k = numEncheres + 1; k < 6; k++) {
                    c.append("\t- ").append(k).append(" : ").append(encheresPossibles[k].toUpperCase()).append("\n");
                }
                System.out.println(c);
                int ench = s.nextInt();
                while ((ench != 0 && ench <= numEncheres) || ench > 6) {
                    System.out.println(c);
                    ench = s.nextInt();
                }
                if (ench != 0) {
                    numEncheres = ench;
                    joueurEnchere = player;
                }
            }
        }
        if (numEncheres == 0) {
            throw new IllegalArgumentException("Aucun joueur n'a fait d'enchère");
        }
        else {
            System.out.println(joueurEnchere.getName() + " a fait une " + encheresPossibles[numEncheres]);
            enchereFaite = encheresPossibles[numEncheres];
        }
    }

    public void montrerChien() {
        if (enchereFaite.contains("chien")) {
            System.out.println("Le chien ne doit pas être montré !");
        }
        else {
            System.out.println(joueurEnchere.getName()+", entrez n'importe quel caractère pour voir votre chien !");
            s.next();
            int choix = 0;
            while (choix != -1 ) {
                System.out.println(chien);
                System.out.println("Votre jeu : \n" + joueurEnchere.myCards);
                System.out.println("Quelle carte voulez enlever de votre chien ? (ou -1 pour arrêter) ");
                choix = selectCardChien();
            }

        }
    }

    public int selectCardChien() {
        int choix = s.nextInt();
        if (choix > -1 && choix < joueurEnchere.myCards.length) {
            System.out.println("Avec quelle carte dans votre jeu voulez-vous l'échanger ?");
            int carte = s.nextInt();
            Carte temp = joueurEnchere.myCards.getByIndex(carte);
            joueurEnchere.myCards.cards[carte] = chien.cards[choix];
            chien.cards[choix] = temp;
            joueurEnchere.myCards.sort();
        }

        if (choix == -1 && !chien.chienCorrect()) {
            choix = -2;
            System.out.println("Il y a encore des cartes interdites dans le chien !");
        }
        return choix;
    }

    public boolean typeValable(int carte, Deck myDeck, String typeRequired, int atoutMinimal) {
        String type = myDeck.cards[carte].type;
        int valeur = myDeck.cards[carte].valeur.getValue();
        return (typeRequired.isEmpty()
                || (type.equals(typeRequired))
                || (!(myDeck.hasType(typeRequired))
                    && type.equals("Atout")
                    && ((myDeck.maxAtout() >= atoutMinimal) && (valeur >= atoutMinimal))
                    || (myDeck.maxAtout() < atoutMinimal))
                || (valeur == -1)
        );
    }

    public boolean colorValable(int carte, Deck myDeck, String colorRequired) {
        return ((!(colorRequired.isEmpty())
                && (myDeck.cards[carte].couleur.toString().equals(colorRequired)))
                || (myDeck.hasColor(colorRequired)));
    }

    public boolean indiceValable(int carte, Deck myDeck) {
        return !(carte < 0 || carte >= myDeck.cards.length || myDeck.cards[carte] == null);
    }

    public int turnOnePlayer(int player, String typeRequired, String colorRequired, int atoutMinimal) {
        Deck myDeck = players[player].myCards;
        if (!Objects.equals(colorRequired, "") && !(myDeck.hasColor(colorRequired))) {
            typeRequired = "Atout";
        }
        System.out.println("\n C'est au tour de " + players[player].getName() + " de jouer \nEntrer n'importe quel caractère pour continuer.");
        s.next();
        System.out.println(myDeck);
        System.out.println("Quelle carte voulez-vous jouer ?");
        int carte = s.nextInt();
        while (!(indiceValable(carte, myDeck) && typeValable(carte, myDeck, typeRequired, atoutMinimal) && colorValable(carte, myDeck, colorRequired))) {
            System.out.println("Cette carte n'est pas valable ! Quelle carte voulez-vous jouer ?");
            carte = s.nextInt();
        }
        Carte card = myDeck.cards[carte];
        myDeck.cards[carte] = null;
        cartesJouees[player] = card;
        atoutMinimal = operationsPostTour(players[player], cartesJouees[player], atoutMinimal);
        return atoutMinimal;
    }

    public int firstPlayerTurn() {
        Deck myDeck = players[0].myCards;
        System.out.println("\n C'est au tour de " + players[0].getName() + " de jouer \nEntrer n'importe quel caractère pour continuer.");
        s.next();
        System.out.println(myDeck);
        System.out.println("Quelle carte voulez-vous jouer ?");
        int carte = s.nextInt();
        Carte card = myDeck.cards[carte];
        myDeck.cards[carte] = null;
        cartesJouees[playerStarting] = card;
        return operationsPostTour(players[0], cartesJouees[0], 0);
    }


    public void wholeTurn() {
        cartesJouees = new Carte[nPlayers];
        int atoutMin = firstPlayerTurn();
        Carte card = cartesJouees[playerStarting];
        if (card.type.equals("Atout") && card.valeur.getValue() > atoutMin) {
            atoutMin = card.valeur.getValue();
        }
        String type = card.type;
        String color = card.couleur.toString();
        for (int i=playerStarting + 1; i < players.length; i++) {
            atoutMin = turnOnePlayer(i, type, color, atoutMin);
        }
        for (int i=0; i < playerStarting; i++) {
            atoutMin = turnOnePlayer(i, type, color, atoutMin);
        }
        playerStarting = carteGagnante(cartesJouees);
        addCartesGagnees(playerStarting, cartesJouees);
        System.out.println("C'est " + players[playerStarting].getName() + " qui a gagné !");
    }

    public void addCartesGagnees(int player, Carte[] cartes) {
        for (Carte c: cartes) {
            cartesGagnees[player].append(c);
        }
    }

    public int operationsPostTour(Player player, Carte carte, int atoutMin) {
        if (carte.type.equals("Atout") && carte.valeur.getValue() > atoutMin) {
            atoutMin = carte.valeur.getValue();
        }
        System.out.println(player.getName() + " a joué la carte " + carte);
        return atoutMin;

    }

    public int carteGagnante(Carte[] tour) {
        Carte carte = tour[0];
        int indice=0;
        for (int i=1; i < tour.length; i++) {
            if (carte.compareTo(tour[i]) < 0 && tour[i] != null) {
                carte = tour[i];
                indice = i;
            }
        }
        return indice;
    }

    public void calculerPoints() {
        double points;
        for (Deck d: cartesGagnees) {
            points = d.compterPoints();
            System.out.println("Le joueur " + players[cartesGagnees.length].getName() + " a gagné " + points + " points !");
        }
    }


}
