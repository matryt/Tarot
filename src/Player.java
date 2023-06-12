public class Player {
    Deck myCards;
    private final String name;
    private int score;

    public Player(String n, Deck cards) {
        myCards = cards;
        name = n;
    }

    public void addToScore(int n) {
        score += n;
    }

    public String getName() {
        return this.name;
    }
}
