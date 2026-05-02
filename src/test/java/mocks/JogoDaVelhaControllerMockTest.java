package mocks;

import br.inatel.grupo2.controller.tictactoe.TicTacToeController;
import br.inatel.grupo2.model.tictactoe.TicTacToeModel;
import br.inatel.grupo2.navigation.GameNavigator;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class JogoDaVelhaControllerMockTest {

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
    void deveIncrementarPlacarXQuandoXVence() throws Exception {

        TestableJogoDaVelhaController controller = new TestableJogoDaVelhaController();
        MockVitoriaXModel mockModel = new MockVitoriaXModel();

        fx(() -> {
            montarTela(controller);
            controller.setModel(mockModel);
            controller.initialize();

            chamarPlayHumanMove(controller, 0, 0);

            return null;
        });

        Label xScore = (Label) buscarCampo(controller, "xScoreLabel");
        Label resultado = (Label) buscarCampo(controller, "resultLabel");

        assertEquals("1", xScore.getText());
        assertTrue(resultado.getText().contains("venceu"));
    }

    @Test
    void deveVoltarParaMenuAoClicarEmVoltar() throws Exception {

        TestableJogoDaVelhaController controller = new TestableJogoDaVelhaController();
        MockNavigator navigator = new MockNavigator();

        fx(() -> {
            controller.setNavigator(navigator);
            controller.voltar();
            return null;
        });

        assertEquals(1, navigator.menuChamado);
    }

    private static class TestableJogoDaVelhaController extends TicTacToeController {

        public void voltar() {
            onBack();
        }

        public void novaRodada() {
            onNewRound();
        }

        public void resetarPartida() {
            onResetMatch();
        }
    }

    private static class MockVitoriaXModel extends TicTacToeModel {

        private boolean movimentoFeito = false;

        @Override
        public boolean makeMove(int row, int col) {
            movimentoFeito = true;
            return true;
        }

        @Override
        public boolean isGameOver() {
            return movimentoFeito;
        }

        @Override
        public boolean isDraw() {
            return false;
        }

        @Override
        public char getWinner() {
            return movimentoFeito ? PLAYER_X : EMPTY;
        }

        @Override
        public char getCurrentPlayer() {
            return PLAYER_X;
        }

        @Override
        public char getCell(int row, int col) {
            return EMPTY;
        }
    }

    private static class MockNavigator implements GameNavigator {

        int menuChamado = 0;

        @Override
        public void showMenuScreen() {
            menuChamado++;
        }

        @Override
        public void showGameDetailScreen(String gameName, String icon, String description) {
        }

        @Override
        public void showRockPaperScissorsScreen() {
        }

        @Override
        public void showMinesweeperScreen() {
        }

        @Override
        public void showTicTacToeScreen() {
        }
    }

    private static void montarTela(TicTacToeController controller) throws Exception {
        setar(controller, "subtitleLabel", new Label());
        setar(controller, "turnLabel", new Label());
        setar(controller, "resultLabel", new Label());
        setar(controller, "xScoreLabel", new Label());
        setar(controller, "oScoreLabel", new Label());
        setar(controller, "drawsLabel", new Label());
        setar(controller, "roundLabel", new Label());
        setar(controller, "computerModeButton", new Button());
        setar(controller, "localModeButton", new Button());
        setar(controller, "cell00", new Button());
        setar(controller, "cell01", new Button());
        setar(controller, "cell02", new Button());
        setar(controller, "cell10", new Button());
        setar(controller, "cell11", new Button());
        setar(controller, "cell12", new Button());
        setar(controller, "cell20", new Button());
        setar(controller, "cell21", new Button());
        setar(controller, "cell22", new Button());
    }

    private static void chamarPlayHumanMove(TicTacToeController controller, int row, int col) throws Exception {
        Method method = TicTacToeController.class.getDeclaredMethod("playHumanMove", int.class, int.class);
        method.setAccessible(true);
        method.invoke(controller, row, col);
    }

    private static void setar(Object alvo, String nome, Object valor) throws Exception {
        Field field = TicTacToeController.class.getDeclaredField(nome);
        field.setAccessible(true);
        field.set(alvo, valor);
    }

    private static Object buscarCampo(Object alvo, String nome) throws Exception {
        Field field = TicTacToeController.class.getDeclaredField(nome);
        field.setAccessible(true);
        return field.get(alvo);
    }

    private static <T> T fx(Acao<T> acao) throws Exception {
        FutureTask<T> task = new FutureTask<>(acao::executar);
        Platform.runLater(task);
        return task.get(5, TimeUnit.SECONDS);
    }

    @FunctionalInterface
    private interface Acao<T> {
        T executar() throws Exception;
    }
}
