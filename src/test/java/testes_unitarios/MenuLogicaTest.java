package testes_unitarios;

import br.inatel.grupo2.controller.MenuController;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MenuLogicaTest {

    // 1. Teste de Quantidade
    @Test
    void listaDeveTerQuatroJogosIniciais() throws Exception {
        MenuController controller = new MenuController();


        Field field = MenuController.class.getDeclaredField("jogos");
        field.setAccessible(true);
        ObservableList<String> lista = (ObservableList<String>) field.get(controller);

        assertEquals(4, lista.size(), "A lista deveria comecar com exatamente 4 jogos.");
    }

    // 2. Teste de Filtro de Nomes
    @Test
    void validaRegraDeNomeInvalido() {
        String nomeComEspacos = "   ";

        Optional<String> resultado = Optional.of(nomeComEspacos)
                .map(String::trim)
                .filter(nome -> !nome.isEmpty());

        assertTrue(resultado.isEmpty(), "Nomes compostos apenas por espaços devem ser descartados.");
    }

    // 3. Teste de Nomes dos Jogos
    @Test
    void verificaSeOsJogosCorretosEstaoNaLista() throws Exception {
        MenuController controller = new MenuController();

        Field field = MenuController.class.getDeclaredField("jogos");
        field.setAccessible(true);
        ObservableList<String> lista = (ObservableList<String>) field.get(controller);

        // Verifica se os nomes batem exatamente com o que está no Controller
        assertTrue(lista.contains("\uD83E\uDEA8  Pedra, Papel e Tesoura"));
        assertTrue(lista.contains("\uD83D\uDCA3  Campo Minado"));
        assertTrue(lista.contains("\u274C  Jogo da Velha"));
        assertTrue(lista.contains("\u2693  Batalha Naval"));
    }

    // 4. Teste extra: Validação de nome válido
    @Test
    void validaRegraDeNomeValido() {
        String nomeValido = "  Xadrez  ";

        Optional<String> resultado = Optional.of(nomeValido)
                .map(String::trim)
                .filter(nome -> !nome.isEmpty());

        assertTrue(resultado.isPresent());
        assertEquals("Xadrez", resultado.get(), "O nome deve ser limpo (sem espaços nas bordas) e aceito.");
    }
}
