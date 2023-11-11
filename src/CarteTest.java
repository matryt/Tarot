import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CarteTest {
    private Carte c1 = new Carte("Number", Valeurs.ROI, "coeur");

    @Test
    public void TestEquals() {
        Carte c2 = new Carte("Number", Valeurs.ROI, "coeur");
        assertEquals(c1, c2);
    }
}
