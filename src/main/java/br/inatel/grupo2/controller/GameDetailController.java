package br.inatel.grupo2.controller;

import br.inatel.grupo2.navigation.GameNavigator;
import br.inatel.grupo2.navigation.JavaFxGameNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameDetailController {
    @FXML private Label gameIcon;
    @FXML private Label gameName;
    @FXML private Label gameDescription;

    private String currentGame;
    private GameNavigator navigator = new JavaFxGameNavigator();

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
            case "Pedra, Papel e Tesoura" -> navigator.showRockPaperScissorsScreen();
            case "Campo Minado" -> navigator.showMinesweeperScreen();
            case "Jogo da Velha" -> navigator.showTicTacToeScreen();
            default -> {} // outros jogos futuros
        }
    }

    @FXML
    protected void onBack() {
        navigator.showMenuScreen();
    }

    public void setNavigator(GameNavigator navigator) {
        this.navigator = navigator;
    }
}
