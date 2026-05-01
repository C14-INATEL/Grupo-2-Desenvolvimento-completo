package mocks;

import br.inatel.grupo2.controller.minesweeper.MinesweeperController;
import br.inatel.grupo2.model.minesweeper.MinesweeperBoard;
import br.inatel.grupo2.model.minesweeper.MinesweeperBoardFactory;
import br.inatel.grupo2.model.minesweeper.MinesweeperCell;
import br.inatel.grupo2.navigation.GameNavigator;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class MinesweeperControllerMockTest {

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
    void deveVoltarParaMenuAoClicarEmVoltar() throws Exception {

        TestableMinesweeperController controller =
                new TestableMinesweeperController();

        MockNavigator navigator = new MockNavigator();

        fx(() -> {
            controller.setNavigator(navigator);
            controller.voltar();
            return null;
        });

        assertEquals(1, navigator.menuChamado);
    }

    @Test
    void deveCriarNovoTabuleiroAoIniciarJogo() throws Exception {

        TestableMinesweeperController controller =
                new TestableMinesweeperController();

        MockBoardFactory factory = new MockBoardFactory();

        fx(() -> {
            montarTela(controller);

            controller.setBoardFactory(factory);
            controller.initialize();

            return null;
        });

        assertEquals(1, factory.criacoes);
    }

    @Test
    void deveCriarNovoTabuleiroAoClicarEmNovoJogo() throws Exception {

        TestableMinesweeperController controller =
                new TestableMinesweeperController();

        MockBoardFactory factory = new MockBoardFactory();

        fx(() -> {
            montarTela(controller);

            controller.setBoardFactory(factory);
            controller.initialize(); // 1ª criação

            controller.novoJogo();   // 2ª criação

            return null;
        });

        assertEquals(2, factory.criacoes);
    }

    @Test
    void deveMostrarFimDeJogoAoClicarEmMina() throws Exception {

        TestableMinesweeperController controller =
                new TestableMinesweeperController();

        MockMineBoardFactory factory = new MockMineBoardFactory();

        fx(() -> {
            montarTela(controller);

            controller.setBoardFactory(factory);
            controller.initialize();

            Method metodo = MinesweeperController.class
                    .getDeclaredMethod("handleClick", int.class, int.class);

            metodo.setAccessible(true);
            metodo.invoke(controller, 0, 0);

            return null;
        });

        Label status = (Label) buscarCampo(controller, "statusLabel");
        Button retry = (Button) buscarCampo(controller, "retryButton");

        assertEquals("Fim de jogo", status.getText());
        assertTrue(retry.isVisible());
    }

    private static void montarTela(MinesweeperController controller) throws Exception {

        setar(controller, "statusLabel", new Label());
        setar(controller, "minesLabel", new Label());
        setar(controller, "revealedLabel", new Label());
        setar(controller, "resultLabel", new Label());
        setar(controller, "retryButton", new Button());
        setar(controller, "grid", new GridPane());
    }

    private static void setar(Object alvo, String nome, Object valor) throws Exception {
        Field field = MinesweeperController.class.getDeclaredField(nome);
        field.setAccessible(true);
        field.set(alvo, valor);
    }

    private static Object buscarCampo(Object alvo, String nome) throws Exception {
        Field field = MinesweeperController.class.getDeclaredField(nome);
        field.setAccessible(true);
        return field.get(alvo);
    }

    private static <T> T fx(Acao<T> acao) throws Exception {
        FutureTask<T> task = new FutureTask<>(acao::executar);
        Platform.runLater(task);
        return task.get(5, TimeUnit.SECONDS);
    }

    private static class TestableMinesweeperController
            extends MinesweeperController {

        public void voltar() {
            onBack();
        }

        public void novoJogo() {
            onNewGame();
        }
    }

    private static class MockNavigator implements GameNavigator {

        int menuChamado = 0;

        @Override
        public void showMenuScreen() {
            menuChamado++;
        }

        @Override
        public void showGameDetailScreen(String gameName, String icon, String description) {}

        @Override
        public void showRockPaperScissorsScreen() {}

        @Override
        public void showMinesweeperScreen() {}

        @Override
        public void showTicTacToeScreen() {}
    }

    private static class MockBoardFactory implements MinesweeperBoardFactory {

        int criacoes = 0;

        @Override
        public MinesweeperBoard createBoard() {
            criacoes++;
            return new MinesweeperBoard();
        }
    }

    private static class MockMineBoardFactory implements MinesweeperBoardFactory {

        @Override
        public MinesweeperBoard createBoard() {
            return new MineBoard();
        }
    }

    private static class MineBoard extends MinesweeperBoard {

        private final MinesweeperCell[][] fakeBoard = new MinesweeperCell[1][1];

        public MineBoard() {
            fakeBoard[0][0] = new MinesweeperCell();
            fakeBoard[0][0].setMine(true);
        }

        @Override
        public MinesweeperCell getCell(int row, int col) {
            return fakeBoard[row][col];
        }

        @Override
        public int getRows() {
            return 1;
        }

        @Override
        public int getCols() {
            return 1;
        }

        @Override
        public int getMines() {
            return 1;
        }
    }

    @FunctionalInterface
    private interface Acao<T> {
        T executar() throws Exception;
    }
}