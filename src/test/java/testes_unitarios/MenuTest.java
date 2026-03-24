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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MenuTest {

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

    // Aqui comecam os testes unitarios.
    //testes de titulo de menu
    @Test
    void tituloMenu() throws Exception {
        MenuController controller = fx(() -> {
            FXMLLoader loader = new FXMLLoader(GameHubApplication.class.getResource("menu-view.fxml"));
            Parent root = loader.load();
            assertNotNull(root);
            return loader.getController();
        });
        Label titulo = campo(controller, "tituloTela", Label.class);
        assertEquals("GAME HUB", fx(titulo::getText));
    }

    //teste para troca de nick
    @Test
    void trocaNick() throws Exception {
        MenuFake controller = new MenuFake(Optional.of("Usuario"));
        Tela tela = tela(controller);
        fx(() -> {
            controller.initialize();
            controller.trocarNick();
            return null;
        });
        assertEquals("Bem-vindo, Usuario", fx(tela.textoBemVindo::getText));
    }

    //teste para abrir jogo do pedra papel e tesoura
    @Test
    void abreJogo() throws Exception {
        MenuFake controller = new MenuFake(Optional.empty());
        Tela tela = tela(controller);
        fx(() -> {
            controller.initialize();
            tela.listaJogos.getSelectionModel().select("Pedra, Papel e Tesoura");
            controller.iniciarJogo();
            return null;
        });
        assertTrue(controller.abriuJogo);
    }

    //teste para mostrar error caso outro jogo que não existe seja selecionado
    @Test
    void mostraError() throws Exception {
        MenuFake controller = new MenuFake(Optional.empty());
        Tela tela = tela(controller);
        fx(() -> {
            controller.initialize();
            tela.listaJogos.getSelectionModel().select("Campo Minado");
            controller.iniciarJogo();
            return null;
        });
        assertEquals("error", fx(tela.statusLabel::getText));
    }

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

    private record Tela(
            Label tituloTela,
            Label textoBemVindo,
            Label statusLabel,
            ListView<String> listaJogos
    ) {}

    private static class MenuFake extends MenuController {
        private final Optional<String> nick;
        private boolean abriuJogo;

        private MenuFake(Optional<String> nick) {
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

        private void trocarNick() {
            onTrocarNick();
        }

        private void iniciarJogo() {
            onIniciarJogo();
        }
    }

    @FunctionalInterface
    private interface Acao<T> {
        T executar() throws Exception;
    }
}
