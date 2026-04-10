package testes_unitarios;

import br.inatel.grupo2.model.rockpaperscissors.RockPaperScissorsModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RockPaperScissorsModelTest {

    private RockPaperScissorsModel model = new RockPaperScissorsModel();

    // 1. escolha do computador várias vezes
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

    // 2. computador vence quando joga papel contra pedra
    @Test
    void computadorDeveVencerComPapelContraPedra() {
        String resultado = model.getResult("pedra", "papel");
        assertEquals("Computador", resultado);
    }

    // 3. computador vence quando joga tesoura contra papel
    @Test
    void computadorDeveVencerComTesouraContraPapel() {
        String resultado = model.getResult("papel", "tesoura");
        assertEquals("Computador", resultado);
    }

    // 4. computador vence quando joga tesoura contra papel
    @Test
    void computadorDeveVencerComPedraContraTesoura() {
        String resultado = model.getResult("tesoura", "pedra");
        assertEquals("Computador", resultado);
    }
}