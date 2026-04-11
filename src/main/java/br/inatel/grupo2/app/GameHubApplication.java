package br.inatel.grupo2.app;

import br.inatel.grupo2.controller.GameDetailController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameHubApplication extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setMinWidth(720);
        primaryStage.setMinHeight(520);
        showMenuScreen();
    }

    public static void showMenuScreen() {
        showScreen("menu-view.fxml", "Game Hub");
    }

    public static void showRockPaperScissorsScreen() {
        showScreen("rock-paper-scissors-view.fxml", "Pedra, Papel e Tesoura");
    }

    public static void showMinesweeperScreen() {
        showScreen("minesweeper-view.fxml", "Campo Minado");
    }

    public static void showGameDetailScreen(String gameName, String icon, String description) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GameHubApplication.class.getResource("game-detail-view.fxml"));
            Parent root = fxmlLoader.load();

            GameDetailController controller = fxmlLoader.getController();
            controller.setGame(gameName, icon, description);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(GameHubApplication.class.getResource("app.css").toExternalForm());

            primaryStage.setTitle(gameName);
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
            primaryStage.show();
        } catch (IOException e) {
            throw new IllegalStateException("Nao foi possivel carregar a tela de detalhes do jogo", e);
        }
    }

    private static void showScreen(String resourceName, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GameHubApplication.class.getResource(resourceName));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            scene.getStylesheets().add(GameHubApplication.class.getResource("app.css").toExternalForm());

            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
            primaryStage.show();
        } catch (IOException e) {
            throw new IllegalStateException("Nao foi possivel carregar a tela " + resourceName, e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
