package mocks;

import br.inatel.grupo2.controller.MenuController;
import br.inatel.grupo2.navigation.GameNavigator;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MenuControllerMockTest {

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
    void deveAbrirDetalhesDoCampoMinadoAoClicarEmJogar() throws Exception {
        TestableMenuController controller = new TestableMenuController();
        MockGameNavigator navigator = new MockGameNavigator();

        fx(() -> {
            Tela tela = montarTela(controller);
            controller.setNavigator(navigator);
            controller.initialize();

            tela.listaJogos().getSelectionModel().select("\uD83D\uDCA3  Campo Minado");
            controller.iniciarJogo();
            return null;
        });

        assertEquals(1, navigator.detalhesChamados);
        assertEquals("Campo Minado", navigator.ultimoNomeJogo);
        assertEquals("\uD83D\uDCA3", navigator.ultimoIcone);
        assertTrue(navigator.ultimaDescricao.contains("minas"));
    }

    @Test
    void naoDeveNavegarQuandoNenhumJogoEstiverSelecionado() throws Exception {
        TestableMenuController controller = new TestableMenuController();
        MockGameNavigator navigator = new MockGameNavigator();
        Tela tela = fx(() -> {
            Tela telaMontada = montarTela(controller);
            controller.setNavigator(navigator);
            controller.initialize();

            telaMontada.listaJogos().getSelectionModel().clearSelection();
            controller.iniciarJogo();
            return telaMontada;
        });

        assertEquals(0, navigator.detalhesChamados);
        assertEquals("Selecione um jogo para continuar.", fx(tela.statusLabel()::getText));
    }

    private static Tela montarTela(MenuController controller) throws Exception {
        Label tituloTela = new Label("GAME HUB");
        Label textoBemVindo = new Label();
        Label statusLabel = new Label();
        ListView<String> listaJogos = new ListView<>();
        Label nickLabel = new Label();
        Label profileInitials = new Label();
        StackPane profileCircle = new StackPane();
        VBox settingsPanel = new VBox();
        Pane settingsOverlay = new Pane();

        setar(controller, "tituloTela", tituloTela);
        setar(controller, "textoBemVindo", textoBemVindo);
        setar(controller, "statusLabel", statusLabel);
        setar(controller, "listaJogos", listaJogos);
        setar(controller, "nickLabel", nickLabel);
        setar(controller, "profileInitials", profileInitials);
        setar(controller, "profileCircle", profileCircle);
        setar(controller, "settingsPanel", settingsPanel);
        setar(controller, "settingsOverlay", settingsOverlay);

        return new Tela(statusLabel, listaJogos);
    }

    private static void setar(Object alvo, String nome, Object valor) throws Exception {
        Field field = alvo.getClass().getSuperclass().getDeclaredField(nome);
        field.setAccessible(true);
        field.set(alvo, valor);
    }

    private static <T> T fx(Acao<T> acao) throws Exception {
        FutureTask<T> task = new FutureTask<>(acao::executar);
        Platform.runLater(task);
        return task.get(5, TimeUnit.SECONDS);
    }

    private record Tela(Label statusLabel, ListView<String> listaJogos) {}

    private static class TestableMenuController extends MenuController {
        private void iniciarJogo() {
            onIniciarJogo();
        }
    }

    private static class MockGameNavigator implements GameNavigator {
        private int detalhesChamados;
        private String ultimoNomeJogo;
        private String ultimoIcone;
        private String ultimaDescricao;

        @Override
        public void showMenuScreen() {
        }

        @Override
        public void showGameDetailScreen(String gameName, String icon, String description) {
            detalhesChamados++;
            ultimoNomeJogo = gameName;
            ultimoIcone = icon;
            ultimaDescricao = description;
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

    @FunctionalInterface
    private interface Acao<T> {
        T executar() throws Exception;
    }
}
