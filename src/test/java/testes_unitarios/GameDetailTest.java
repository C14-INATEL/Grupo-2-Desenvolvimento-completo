package testes_unitarios;

import br.inatel.grupo2.controller.GameDetailController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameDetailTest {

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
    void testarCarregamentoDeDados() throws Exception {
        GameDetailController controller = new GameDetailController();
        
        fx(() -> {
            Label gameIcon = new Label();
            Label gameName = new Label();
            Label gameDescription = new Label();

            setar(controller, "gameIcon", gameIcon);
            setar(controller, "gameName", gameName);
            setar(controller, "gameDescription", gameDescription);

            controller.setGame("Teste Jogo", "🎮", "Descricao Teste");
            return null;
        });

        Label nomeLabel = campo(controller, "gameName", Label.class);
        Label iconLabel = campo(controller, "gameIcon", Label.class);
        
        assertEquals("Teste Jogo", fx(nomeLabel::getText));
        assertEquals("🎮", fx(iconLabel::getText));
    }

    // Métodos auxiliares idênticos ao MenuTest para lidar com JavaFX e Reflection
    private static void setar(Object alvo, String nome, Object valor) throws Exception {
        Field field = alvo.getClass().getDeclaredField(nome);
        field.setAccessible(true);
        field.set(alvo, valor);
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

    @FunctionalInterface
    private interface Acao<T> {
        T executar() throws Exception;
    }
}
