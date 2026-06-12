package testes_unitarios;

import br.inatel.grupo2.model.minesweeper.MinesweeperBoard;
import br.inatel.grupo2.model.minesweeper.MinesweeperCell;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MinesweeperModelTest {

    @Test
    public void testarQuantidadeDeBombasNoTabuleiro() {
        MinesweeperBoard board = new MinesweeperBoard();
        int qtdBombas = 0;

        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                if (board.getCell(i, j).isMine()) {
                    qtdBombas++;
                }
            }
        }

        assertEquals(10, qtdBombas, "O jogo tem que comecar sempre com 10 minas espalhadas");
    }

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

    @Test
    public void testarEstadoInicialEDeclinioDaCelula() {
        MinesweeperCell celula = new MinesweeperCell();

        assertFalse(celula.isMine(), "As celulas tem que nascer sem nenhuma mina");
        assertFalse(celula.isRevealed(), "A celula inicial nao pode estar clicada/revelada");

        celula.setRevealed(true);
        assertTrue(celula.isRevealed(), "Se eu clicar (setRevealed), o status tem que estar ativado");
    }
}