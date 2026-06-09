package testes_unitarios;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import br.inatel.grupo2.features.Battleships;

import java.util.Random;

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
        Battleships b = new Battleships(new SequenceRandom(
                new int[]{
                        2, 0, 0, // primeiro navio: comprimento 3, x 0, y 0
                        2, 0, 0, // tentativa com sobreposicao: comprimento 3, x 0, y 0
                        1, 5, 5  // segunda tentativa: comprimento 2, x 5, y 5
                },
                new boolean[]{
                        false,
                        false,
                        false
                }
        ));

        b.criarBattleship();
        int celulasOcupadasDepoisDoPrimeiroNavio = contarCelulasOcupadas(b);

        b.criarBattleship();
        int celulasOcupadasDepoisDoSegundoNavio = contarCelulasOcupadas(b);

        assertEquals(3, celulasOcupadasDepoisDoPrimeiroNavio);
        assertEquals(5, celulasOcupadasDepoisDoSegundoNavio, "Navios nao devem se sobrepor no mesmo tabuleiro");
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


    @Test
    void testRepeatedHits() {
        Battleships b = new Battleships();
        b.criarBattleship();
        int x = b.getInicioX();
        int y = b.getInicioY();
        // Capture System.out
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        java.io.PrintStream originalOut = System.out;
        System.setOut(new java.io.PrintStream(outContent));
        try {
            b.checarAcerto(x, y); // First hit
            outContent.reset(); // Clear output
            b.checarAcerto(x, y); // Second hit (should print 'Já tentou essa posição!')
        } finally {
            System.setOut(originalOut);
        }
        String output = outContent.toString();
        assertTrue(output.contains("Já tentou essa posição!"), "Deve avisar que já tentou essa posição");
    }

    @Test
    void testOutOfBoundsHit() {
        Battleships b = new Battleships();
        b.criarBattleship();
        // Capture System.out
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        java.io.PrintStream originalOut = System.out;
        System.setOut(new java.io.PrintStream(outContent));
        try {
            b.checarAcerto(-1, 0); // Out of bounds
            b.checarAcerto(0, -1); // Out of bounds
            b.checarAcerto(10, 0); // Out of bounds
            b.checarAcerto(0, 10); // Out of bounds
        } finally {
            System.setOut(originalOut);
        }
        String output = outContent.toString();
        int count = output.split("Coordenada fora do tabuleiro!").length - 1;
        assertEquals(4, count, "Deve avisar que a coordenada está fora do tabuleiro para cada tentativa inválida");
    }

    private static int contarCelulasOcupadas(Battleships b) {
        int ocupadas = 0;
        boolean[][] campo = b.getCampo();

        for (int i = 0; i < campo.length; i++) {
            for (int j = 0; j < campo[i].length; j++) {
                if (campo[i][j]) {
                    ocupadas++;
                }
            }
        }

        return ocupadas;
    }

    private static class SequenceRandom extends Random {
        private final int[] ints;
        private final boolean[] booleans;
        private int intIndex;
        private int booleanIndex;

        private SequenceRandom(int[] ints, boolean[] booleans) {
            this.ints = ints;
            this.booleans = booleans;
        }

        @Override
        public int nextInt(int bound) {
            return ints[intIndex++];
        }

        @Override
        public boolean nextBoolean() {
            return booleans[booleanIndex++];
        }
    }
}
