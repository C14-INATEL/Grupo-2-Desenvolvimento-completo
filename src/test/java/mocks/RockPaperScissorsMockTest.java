package mocks;

import br.inatel.grupo2.controller.rockpaperscissors.RockPaperScissorsViewController;
import br.inatel.grupo2.model.rockpaperscissors.RockPaperScissorsModel;
import br.inatel.grupo2.navigation.GameNavigator;
import javafx.application.Platform;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class RockPaperScissorsViewControllerMockTest {

    @BeforeAll
    static void iniciarFx() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException e) {
            latch.countDown();
        }

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    void deveAtualizarPlacarQuandoJogadorVence() throws Exception {

        TestableController controller = new TestableController();
        MockVitoriaModel model = new MockVitoriaModel();

        fx(() -> {
            montarTela(controller);

            controller.setModel(model);
            controller.initialize();

            chamarPlayRound(controller, "pedra");

            return null;
        });

        Label playerScore = (Label) getField(controller, "playerScoreLabel");
        Label result = (Label) getField(controller, "resultLabel");

        assertEquals("1", playerScore.getText());
        assertTrue(result.getText().contains("venceu"));
    }

    @Test
    void deveResetarPlacar() throws Exception {

        TestableController controller = new TestableController();

        fx(() -> {
            montarTela(controller);

            controller.initialize();
            chamarPlayRound(controller, "pedra");

            controller.reset(); // ✅ usando classe testável

            return null;
        });

        Label playerScore = (Label) getField(controller, "playerScoreLabel");
        Label computerScore = (Label) getField(controller, "computerScoreLabel");
        Label draws = (Label) getField(controller, "drawsLabel");
        Label rounds = (Label) getField(controller, "roundLabel");

        assertEquals("0", playerScore.getText());
        assertEquals("0", computerScore.getText());
        assertEquals("0", draws.getText());
        assertTrue(rounds.getText().contains("0"));
    }

    @Test
    void deveVoltarParaMenuAoClicarEmBack() throws Exception {

        TestableController controller = new TestableController();
        MockNavigator navigator = new MockNavigator();

        fx(() -> {
            controller.setNavigator(navigator);
            controller.back(); // ✅ usando classe testável
            return null;
        });

        assertEquals(1, navigator.menuChamado);
    }

    private static void montarTela(RockPaperScissorsViewController controller) throws Exception {

        setField(controller, "playerChoiceLabel", new Label());
        setField(controller, "computerChoiceLabel", new Label());
        setField(controller, "resultLabel", new Label());
        setField(controller, "playerScoreLabel", new Label());
        setField(controller, "computerScoreLabel", new Label());
        setField(controller, "drawsLabel", new Label());
        setField(controller, "roundLabel", new Label());
    }

    private static void chamarPlayRound(RockPaperScissorsViewController controller, String jogada) throws Exception {
        Method method = RockPaperScissorsViewController.class
                .getDeclaredMethod("playRound", String.class);

        method.setAccessible(true);
        method.invoke(controller, jogada);
    }

    private static void setField(Object alvo, String nome, Object valor) throws Exception {
        Field field = RockPaperScissorsViewController.class.getDeclaredField(nome);
        field.setAccessible(true);
        field.set(alvo, valor);
    }

    private static Object getField(Object alvo, String nome) throws Exception {
        Field field = RockPaperScissorsViewController.class.getDeclaredField(nome);
        field.setAccessible(true);
        return field.get(alvo);
    }

    private static <T> T fx(Acao<T> acao) throws Exception {
        FutureTask<T> task = new FutureTask<>(acao::executar);
        Platform.runLater(task);
        return task.get(5, TimeUnit.SECONDS);
    }

    private static class TestableController extends RockPaperScissorsViewController {

        public void reset() {
            onResetMatch();
        }

        public void back() {
            onBack();
        }
    }

    private static class MockVitoriaModel extends RockPaperScissorsModel {
        @Override
        public String getComputerChoice() {
            return "tesoura";
        }

        @Override
        public String getResult(String player, String computer) {
            return "Jogador";
        }
    }

    private static class MockNavigator implements GameNavigator {

        int menuChamado = 0;

        @Override
        public void showMenuScreen() {
            menuChamado++;
        }

        @Override public void showGameDetailScreen(String g, String i, String d) {}
        @Override public void showRockPaperScissorsScreen() {}
        @Override public void showMinesweeperScreen() {}
        @Override public void showTicTacToeScreen() {}
    }

    @FunctionalInterface
    private interface Acao<T> {
        T executar() throws Exception;
    }
}