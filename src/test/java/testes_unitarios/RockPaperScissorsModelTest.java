package testes_unitarios;

import br.inatel.grupo2.model.rockpaperscissors.RockPaperScissorsModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RockPaperScissorsModelTest {

    private final RockPaperScissorsModel model = new RockPaperScissorsModel();

    @Test
    public void escolhaDoComputadorSempreValida() {
        for (int i = 0; i < 100; i++) {
            String escolha = model.getComputerChoice();
            assertTrue(
                    escolha.equals("pedra") ||
                            escolha.equals("papel") ||
                            escolha.equals("tesoura"),
                    "A escolha do computador precisa ser pedra, papel ou tesoura"
            );
        }
    }

    @Test
    void computadorDeveVencerComPapelContraPedra() {
        String resultado = model.getResult("pedra", "papel");
        assertEquals("Computador", resultado);
    }

    @Test
    void computadorDeveVencerComTesouraContraPapel() {
        String resultado = model.getResult("papel", "tesoura");
        assertEquals("Computador", resultado);
    }

    @Test
    void computadorDeveVencerComPedraContraTesoura() {
        String resultado = model.getResult("tesoura", "pedra");
        assertEquals("Computador", resultado);
    }

    @Test
    public void testarEmpateNoJogo() {
        String resultado = model.getResult("pedra", "pedra");
        assertEquals("Empate", resultado, "Se os dois jogarem a mesma coisa, tem que dar Empate");
    }

    @Test
    public void testarVitoriaDoJogador() {
        String resultado = model.getResult("pedra", "tesoura");
        assertEquals("Jogador", resultado, "A pedra quebra a tesoura, entao o jogador devia ganhar");
    }

    @Test
    public void testarDerrotaDoJogador() {
        String resultado = model.getResult("pedra", "papel");
        assertEquals("Computador", resultado, "O papel embrulha a pedra, entao o computador devia ganhar");
    }
}