package testes_unitarios;

import br.inatel.grupo2.model.rockpaperscissors.RockPaperScissorsModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RockPaperScissorsModelTest {

    private RockPaperScissorsModel model = new RockPaperScissorsModel();

    // 1. erro para entrada inválida
    @Test
    void deveLancarErroParaEntradaInvalida() {
        assertThrows(IllegalArgumentException.class, () -> {
            model.getResult("abc", "pedra");
        });
    }

    // 2. erro para entrada null do jogador
    @Test
    void deveLancarErroQuandoPlayerForNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            model.getResult(null, "pedra");
        });
    }

    // 3. escolha do computador várias vezes
    @Test
    public void escolhaDoComputadorSempreValida() {
        for (int i = 0; i < 100; i++) {
            String escolha = model.getComputerChoice();

            assertTrue(
                    escolha.equals("pedra") ||
                            escolha.equals("papel") ||
                            escolha.equals("tesoura")
            );
        }
    }

    // 4. computador vence
    @Test
    void computadorDeveVencerComPapelContraPedra() {
        String resultado = model.getResult("pedra", "papel");
        assertEquals("Computador", resultado);
    }
}