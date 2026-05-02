package mocks;

import br.inatel.grupo2.controller.GameDetailController;
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

class GameDetailControllerMockTest {

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
    void deveNavegarParaCampoMinadoAoClicarEmJogar() throws Exception {

        GameDetailController controller = new GameDetailController();
        MockGameNavigator navigator = new MockGameNavigator();

        fx(() -> {
            montarTela(controller);
            controller.setNavigator(navigator);

            controller.setGame("Campo Minado", "💣", "Encontre as minas escondidas!");

            chamarOnPlay(controller);

            return null;
        });

        assertEquals(1, navigator.minesweeperChamado);
        assertEquals(0, navigator.menuChamado);
        assertEquals(0, navigator.rpsChamado);
    }

    @Test
    void naoDeveNavegarQuandoNenhumJogoEstiverConfigurado() throws Exception {

        GameDetailController controller = new GameDetailController();
        MockGameNavigator navigator = new MockGameNavigator();

        fx(() -> {
            montarTela(controller);
            controller.setNavigator(navigator);

            chamarOnPlay(controller);

            return null;
        });

        assertEquals(0, navigator.menuChamado);
        assertEquals(0, navigator.minesweeperChamado);
        assertEquals(0, navigator.rpsChamado);
        assertEquals(0, navigator.tictactoeChamado);
    }

    private static class MockGameNavigator implements GameNavigator {

        int menuChamado = 0;
        int detalhesChamados = 0;
        int rpsChamado = 0;
        int minesweeperChamado = 0;
        int tictactoeChamado = 0;

        @Override
        public void showMenuScreen() {
            menuChamado++;
        }

        @Override
        public void showGameDetailScreen(String gameName, String icon, String description) {
            detalhesChamados++;
        }

        @Override
        public void showRockPaperScissorsScreen() {
            rpsChamado++;
        }

        @Override
        public void showMinesweeperScreen() {
            minesweeperChamado++;
        }

        @Override
        public void showTicTacToeScreen() {
            tictactoeChamado++;
        }
    }

    private static void montarTela(GameDetailController controller) throws Exception {
        setar(controller, "gameIcon", new Label());
        setar(controller, "gameName", new Label());
        setar(controller, "gameDescription", new Label());
    }

    private static void chamarOnPlay(GameDetailController controller) throws Exception {
        Method method = GameDetailController.class.getDeclaredMethod("onPlay");
        method.setAccessible(true);
        method.invoke(controller);
    }

    private static void setar(Object alvo, String nome, Object valor) throws Exception {
        Field field = GameDetailController.class.getDeclaredField(nome);
        field.setAccessible(true);
        field.set(alvo, valor);
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
