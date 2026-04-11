package br.inatel.grupo2.model.rockpaperscissors;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RockPaperScissorsModelTest {

    @Test
    public void testarEmpateNoJogo() {
        RockPaperScissorsModel model = new RockPaperScissorsModel();
        String resultado = model.getResult("pedra", "pedra");
        assertEquals("Empate", resultado, "Se os dois jogarem a mesma coisa, tem que dar Empate");
    }

    @Test
    public void testarVitoriaDoJogador() {
        RockPaperScissorsModel model = new RockPaperScissorsModel();
        String resultado = model.getResult("pedra", "tesoura");
        assertEquals("Jogador", resultado, "A pedra quebra a tesoura, entao o jogador devia ganhar");
    }

    @Test
    public void testarDerrotaDoJogador() {
        RockPaperScissorsModel model = new RockPaperScissorsModel();
        String resultado = model.getResult("pedra", "papel");
        assertEquals("Computador", resultado, "O papel embrulha a pedra, entao o computador devia ganhar");
    }
}
