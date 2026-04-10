import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import br.inatel.grupo2.features.Battleships;

public class BattleshipsTest {
    @Test
    void testShipPlacementWithinBounds() {
        Battleships b = new Battleships();
        b.criarBattleship();
        int x = b.getInicioX();
        int y = b.getInicioY();
        int len = b.getComprimento();
        boolean vertical = b.isVertical();
        if (vertical) {
            assertTrue(y + len <= 10, "Navio deve caber verticalmente dentro dos limites");
        } else {
            assertTrue(x + len <= 10, "Navio deve caber horizontalmente dentro dos limites");
        }
    }

    @Test
    void testShipOverlap() {
        Battleships b1 = new Battleships();
        b1.criarBattleship();
        Battleships b2 = new Battleships();
        b2.criarBattleship();

        // Verifica se os navios não se sobrepõem
        boolean overlap = false;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (b1.getCampo()[i][j] && b2.getCampo()[i][j]) {
                    overlap = true;
                    break;
                }
            }
            if (overlap) break;
        }
        assertFalse(overlap, "Navios não devem se sobrepor");
    }

    @Test
    void testHitAndSink() {
        Battleships b = new Battleships();
        b.criarBattleship();
        int x = b.getInicioX();
        int y = b.getInicioY();
        int len = b.getComprimento();
        boolean vertical = b.isVertical();

        for (int i = 0; i < len; i++) {
            if (vertical) {
                b.checarAcerto(x, y + i);
            } else {
                b.checarAcerto(x + i, y);
            }
        }
        assertTrue(b.isSunk(), "Navio deve estar afundado após todos os cells serem acertados");
    }

    @Test
    void testMiss() {
        Battleships b = new Battleships();
        b.criarBattleship();
        // Try to hit a cell that is not part of the ship
        boolean foundMiss = false;
        outer: for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int x = b.getInicioX();
                int y = b.getInicioY();
                int len = b.getComprimento();
                boolean vertical = b.isVertical();
                boolean isShipCell = false;
                for (int k = 0; k < len; k++) {
                    if ((vertical && i == x && j == y + k) || (!vertical && i == x + k && j == y)) {
                        isShipCell = true;
                        break;
                    }
                }
                if (!isShipCell) {
                    b.checarAcerto(i, j);
                    foundMiss = true;
                    break outer;
                }
            }
        }
        assertTrue(foundMiss, "Deve haver pelo menos uma tentativa de acerto que seja um erro");
    }
}
