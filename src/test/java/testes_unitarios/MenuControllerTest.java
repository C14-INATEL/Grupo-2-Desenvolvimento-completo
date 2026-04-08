package testes_unitarios;

import br.inatel.grupo2.app.GameHubApplication;
import br.inatel.grupo2.controller.MenuController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class MenuControllerTest {

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

    // --- TESTES DE UI ---

    @Test
    void tituloMenuDeveEstarCorreto() throws Exception {
        MenuController controller = fx(() -> {
            FXMLLoader loader = new FXMLLoader(GameHubApplication.class.getResource("menu-view.fxml"));
            Parent root = loader.load();
            assertNotNull(root);
            return loader.getController();
        });
        Label titulo = campo(controller, "tituloTela", Label.class);
        assertEquals("GAME HUB", fx(titulo::getText));
    }

    // --- TESTES DE LÓGICA (COM MENU FAKE) ---

    @Test
    void deveTrocarNickComSucesso() throws Exception {
        MenuFake controller = new MenuFake(Optional.of("Pedroca"));
        Tela tela = tela(controller);
        fx(() -> {
            controller.initialize();
            controller.acionarTrocarNick();
            return null;
        });
        assertEquals("Bem-vindo, Pedroca", fx(tela.textoBemVindo::getText));
    }

    @Test
    void deveAbrirJogoPedraPapelTesoura() throws Exception {
        MenuFake controller = new MenuFake(Optional.empty());
        Tela tela = tela(controller);
        fx(() -> {
            controller.initialize();
            tela.listaJogos.getSelectionModel().select("Pedra, Papel e Tesoura");
            controller.acionarIniciarJogo();
            return null;
        });
        assertTrue(controller.abriuJogo, "O jogo deveria ter sido aberto");
    }

    @Test
    void deveMostrarErroParaJogoInexistente() throws Exception {
        MenuFake controller = new MenuFake(Optional.empty());
        Tela tela = tela(controller);
        fx(() -> {
            controller.initialize();
            tela.listaJogos.getSelectionModel().select("Campo Minado");
            controller.acionarIniciarJogo();
            return null;
        });
        assertEquals("error", fx(tela.statusLabel::getText));
    }

    @Test
    void deveCarregarListaDeJogosNaInicializacao() throws Exception {
        MenuFake controller = new MenuFake(Optional.empty());
        Tela tela = tela(controller);
        fx(() -> {
            controller.initialize();
            return null;
        });

        int totalJogos = fx(() -> tela.listaJogos.getItems().size());
        assertTrue(totalJogos > 0, "A lista de jogos não pode estar vazia");
    }

    // --- MÉTODOS AUXILIARES E REFLEXÃO ---

    private static Tela tela(MenuController controller) throws Exception {
        return fx(() -> {
            Label tituloTela = new Label("GAME HUB");
            Label textoBemVindo = new Label();
            Label statusLabel = new Label();
            ListView<String> listaJogos = new ListView<>();

            setar(controller, "tituloTela", tituloTela);
            setar(controller, "textoBemVindo", textoBemVindo);
            setar(controller, "statusLabel", statusLabel);
            setar(controller, "listaJogos", listaJogos);

            return new Tela(tituloTela, textoBemVindo, statusLabel, listaJogos);
        });
    }

    private static void setar(Object alvo, String nome, Object valor) throws Exception {
        Class<?> atual = alvo.getClass();
        while (atual != null) {
            try {
                Field field = atual.getDeclaredField(nome);
                field.setAccessible(true);
                field.set(alvo, valor);
                return;
            } catch (NoSuchFieldException e) {
                atual = atual.getSuperclass();
            }
        }
        throw new NoSuchFieldException(nome);
    }

    private static <T> T campo(Object alvo, String nome, Class<T> tipo) throws Exception {
        Field field = alvo.getClass().getDeclaredField(nome);
        field.setAccessible(true);
        return tipo.cast(field.get(alvo));
    }

    private static <T> T fx(Acao<T> acao) throws Exception {
        FutureTask<T> task = new FutureTask<>(acao::executar);
        Platform.runLater(task);
        return task.get(5, TimeUnit.SECONDS);
    }

    // --- CLASSES DE SUPORTE ---

    private record Tela(
            Label tituloTela,
            Label textoBemVindo,
            Label statusLabel,
            ListView<String> listaJogos
    ) {}

    private static class MenuFake extends MenuController {
        private final Optional<String> nick;
        public boolean abriuJogo = false;

        public MenuFake(Optional<String> nick) {
            this.nick = nick;
        }

        @Override
        protected void abrirPedraPapelTesoura() {
            abriuJogo = true;
        }

        @Override
        protected Optional<String> solicitarNovoNick() {
            return nick;
        }

        // Métodos para expor as ações protegidas do Controller original
        public void acionarTrocarNick() {
            super.onTrocarNick();
        }

        public void acionarIniciarJogo() {
            super.onIniciarJogo();
        }
    }

    @FunctionalInterface
    private interface Acao<T> {
        T executar() throws Exception;
    }
}