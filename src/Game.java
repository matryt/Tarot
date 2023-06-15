import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private int nPlayers;
    private Deck myCartes;
    private Player[] players;
    private Player joueurEnchere;
    private String enchereFaite;
    private Deck chien;
    private int playerStarting;

    public Game(Scanner s) {
        myCartes = new Deck(78);
        myCartes.cards = createCards();
        nPlayers = askNumberPlayers(s);
        players = new Player[nPlayers];
        Deck[] decks = createDecks(myCartes, nPlayers);
        createPlayers(s, decks);
        chien = decks[nPlayers];


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

    public String zfill(String str, int width) {
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < width) {
            sb.insert(0, '0');
        }
        return sb.toString();
    }


    public Carte[] createCards() {
        Carte[] myCartes = new Carte[78];
        int indice = 0;
        int i;
        for (i = 1; i <= 21; i++) {
            myCartes[indice] = new Carte("Atout", zfill(Integer.toString(i), 2), "");
            indice++;
        }
        for (i = 1; i <= 10; i++) {
            for (String coul: new String[]{"coeur", "carreau", "pique", "trefle"}) {
                myCartes[indice] = new Carte("Nombre", zfill(Integer.toString(i), 2), coul);
                indice++;
            }
        }
        for (String fig: new String[]{"Valet", "Cavalier", "Dame", "Roi"}) {
            for (String coul : new String[]{"coeur", "carreau", "pique", "trefle"}) {
                myCartes[indice] = new Carte("Nombre", fig, coul);
                indice++;
            }
        }

        myCartes[77] = new Carte("Atout", "Excuse", "");

        for (i=0;i<10;i++) {
            shuffle(myCartes);
        }

        return myCartes;
    }

    public int askNumberPlayers(Scanner s) {
        System.out.println("Combien y-a-t'il de joueurs (entre 3 et 5) ?");
        int n = s.nextInt();
        while (n < 3 || n > 5) {
            System.out.println("Impossible !");
            System.out.println("Combien y-a-t'il de joueurs (entre 3 et 5) ?");
            n = s.nextInt();
        }
        return n;
    }

    public void createPlayers(Scanner s, Deck[] d) {
        for (int i=0;i<nPlayers;i++) {
            System.out.println("Joueur " + i + ", quel est ton pseudo ?");
            String name = s.next();
            players[i] = new Player(name, d[i]);
        }
    }

    public void afficherCartes(Scanner s) {
        System.out.println("\n");
        for (Player j: players) {
            String sc = j.getName();
            System.out.println("Joueur " + sc +", entrez n'importe quel caractère pour voir vos cartes");
            s.next();
            System.out.println(j.myCards);
        }
    }

    public void mainloop(Scanner s) {
        afficherCartes(s);
        encheres(s);
        montrerChien(s);
        while (!players[0].myCards.isEmpty()) {
            wholeTurn(s);
        }
    }

    public void encheres(Scanner s) {
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

    public void montrerChien(Scanner s) {
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
                choix = s.nextInt();
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


            }

        }
    }

    public Carte turn(int player, Scanner s, String typeRequired, String colorRequired, int atoutMinimal) {
        Deck myDeck = players[player].myCards;
        if (!Objects.equals(colorRequired, "") && !(myDeck.hasColor(colorRequired))) {
            typeRequired = "Atout";
        }
        System.out.println("\n C'est au tour de " + players[player].getName() + " de jouer \nEntrer n'importe quel caractère pour continuer.");
        s.next();
        System.out.println(myDeck);
        System.out.println("Quelle carte voulez-vous jouer ?");
        int carte = s.nextInt();
        while (carte < 0 || carte >= myDeck.cards.length || myDeck.cards[carte] == null) {
            System.out.println("Quelle carte voulez-vous jouer ?");
            carte = s.nextInt();
        }

        while ((!(typeRequired.equals(""))
                        && (!(myDeck.cards[carte].type.equals(typeRequired))
                                || (Integer.parseInt(myDeck.cards[carte].valeur) < atoutMinimal
                                    && myDeck.maxAtout() >= atoutMinimal)
                            )
                        && (myDeck.hasType(typeRequired))
                        && !(myDeck.cards[carte].valeur.equals("Excuse"))
        )
                || myDeck.cards[carte] == null) {
            System.out.println("Vous devez mettre une carte de type " + typeRequired + " et de valeur min " + atoutMinimal);
            System.out.println("Quelle carte voulez-vous jouer ?");
            carte = s.nextInt();
        }

        while ((!(colorRequired.equals(""))
                    && !(myDeck.cards[carte].couleur.toString().equals(colorRequired))
                    && (myDeck.hasColor(colorRequired)))
                    && !(myDeck.cards[carte].valeur.equals("Excuse"))
                || myDeck.cards[carte] == null) {
            System.out.println("Vous devez mettre une carte de la couleur "+colorRequired);
            System.out.println("Quelle carte voulez-vous jouer ?");
            carte = s.nextInt();
        }
        Carte card = myDeck.cards[carte];
        myDeck.cards[carte] = null;
        return card;
    }


    public void wholeTurn(Scanner s) {
        Carte[] cartesJouees = new Carte[nPlayers];
        int atoutMin = 0;
        cartesJouees[playerStarting] = turn(playerStarting, s,"","", atoutMin);
        Carte card = cartesJouees[playerStarting];
        if (card.type.equals("Atout") && Integer.parseInt(card.valeur) > atoutMin) {
            atoutMin = Integer.parseInt(card.valeur);
        }
        System.out.println(players[playerStarting].getName() + " a joué la carte " + cartesJouees[playerStarting]);
        String type="";
        String color="";
        if (Objects.equals(cartesJouees[playerStarting].type, "Atout")) {
            type = "Atout";
        }
        else {
            color = String.valueOf(cartesJouees[playerStarting].couleur);
        }
        for (int i=playerStarting + 1; i < players.length; i++) {
            cartesJouees[i] = turn(i, s, type, color, atoutMin);
            card = cartesJouees[i];
            if (card.type.equals("Atout") && Integer.parseInt(card.valeur) > atoutMin) {
                atoutMin = Integer.parseInt(card.valeur);
            }
            System.out.println(players[i].getName() + " a joué la carte " + cartesJouees[i]);
        }
        for (int i=0; i < playerStarting; i++) {
            cartesJouees[i] = turn(i, s, type, color, atoutMin);
            card = cartesJouees[i];
            if (card.type.equals("Atout") && Integer.parseInt(card.valeur) > atoutMin) {
                atoutMin = Integer.parseInt(card.valeur);
            }
            System.out.println(players[i].getName() + " a joué la carte " + cartesJouees[i]);
        }
        playerStarting = carteGagnante(cartesJouees);
        System.out.println("C'est " + players[playerStarting].getName() + " qui a gagné !");
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


}
