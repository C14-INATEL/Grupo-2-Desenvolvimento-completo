package br.inatel.grupo2.model.minesweeper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MinesweeperTest {

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
    public void testarEstadoInicialDaNovaCelula() {
        MinesweeperCell celula = new MinesweeperCell();
        assertFalse(celula.isMine(), "As celulas tem que nascer sem nenhuma mina");
        assertFalse(celula.isRevealed(), "A celula inicial nao pode estar clicada/revelada");
        
        celula.setRevealed(true);
        assertTrue(celula.isRevealed(), "Se eu clicar (setRevealed), o status tem que estar ativado");
    }
}
