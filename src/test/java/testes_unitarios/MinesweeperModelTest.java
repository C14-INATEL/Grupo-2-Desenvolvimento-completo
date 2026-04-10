
import br.inatel.grupo2.model.minesweeper.MinesweeperBoard;
import br.inatel.grupo2.model.minesweeper.MinesweeperCell;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MinesweeperModelTest {

    //  Nenhuma célula deve ter número negativo
    @Test
    public void testarSeNumerosNaoSaoNegativos() {
        MinesweeperBoard board = new MinesweeperBoard();

        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                MinesweeperCell cell = board.getCell(i, j);

                assertTrue(cell.getAdjacentMines() >= 0,
                        "O número de minas adjacentes não pode ser negativo");
            }
        }
    }

    // Máximo de 8 minas ao redor (regra do campo minado)
    @Test
    public void testarLimiteMaximoDeBombasAdjacentes() {
        MinesweeperBoard board = new MinesweeperBoard();

        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                MinesweeperCell cell = board.getCell(i, j);

                assertTrue(cell.getAdjacentMines() <= 8,
                        "Uma célula não pode ter mais de 8 minas adjacentes");
            }
        }
    }

    // Se for bomba, não deve ter número adjacente relevante
    @Test
    public void testarSeBombasNaoPossuemNumero() {
        MinesweeperBoard board = new MinesweeperBoard();

        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                MinesweeperCell cell = board.getCell(i, j);

                if (cell.isMine()) {
                    assertEquals(0, cell.getAdjacentMines(),
                            "Células com mina não devem ter número de minas adjacentes");
                }
            }
        }
    }

    // Revelar célula deve mudar estado corretamente
    @Test
    public void testarRevelarCelula() {
        MinesweeperCell cell = new MinesweeperCell();

        assertFalse(cell.isRevealed(), "A célula deve iniciar não revelada");

        cell.setRevealed(true);

        assertTrue(cell.isRevealed(), "A célula deve estar revelada após setRevealed(true)");
    }
}