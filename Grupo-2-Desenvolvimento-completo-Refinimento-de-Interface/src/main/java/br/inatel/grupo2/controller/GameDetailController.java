package br.inatel.grupo2.controller;

import br.inatel.grupo2.app.GameHubApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameDetailController {
    @FXML private Label gameIcon;
    @FXML private Label gameName;
    @FXML private Label gameDescription;

    private String currentGame;

    public void setGame(String name, String icon, String description) {
        this.currentGame = name;
        gameIcon.setText(icon);
        gameName.setText(name);
        gameDescription.setText(description);
    }

    @FXML
    protected void onPlay() {
        if (currentGame == null) return;

        switch (currentGame) {
            case "Pedra, Papel e Tesoura" -> GameHubApplication.showRockPaperScissorsScreen();
            default -> {} // outros jogos futuros
        }
    }

    @FXML
    protected void onBack() {
        GameHubApplication.showMenuScreen();
    }
}
