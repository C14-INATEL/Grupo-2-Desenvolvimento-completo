package br.inatel.grupo2.controller.battleships;

import br.inatel.grupo2.features.Battleships;
import br.inatel.grupo2.navigation.GameNavigator;
import br.inatel.grupo2.navigation.JavaFxGameNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class BattleshipsController {
    @FXML private Label statusLabel;
    @FXML private Label attemptsLabel;
    @FXML private Label hitsLabel;
    @FXML private Label remainingLabel;
    @FXML private Label resultLabel;
    @FXML private Button retryButton;
    @FXML private GridPane grid;

    private Battleships game = new Battleships();
    private Button[][] buttons;
    private boolean gameOver;
    private GameNavigator navigator = new JavaFxGameNavigator();

    @FXML
    public void initialize() {
        startGame();
    }

    @FXML
    protected void onNewGame() {
        startGame();
    }

    @FXML
    protected void onBack() {
        navigator.showMenuScreen();
    }

    public void setNavigator(GameNavigator navigator) {
        this.navigator = navigator;
    }

    public void setGame(Battleships game) {
        this.game = game;
    }

    private void startGame() {
        game.criarFrotaPadrao();
        buttons = new Button[game.getBoardSize()][game.getBoardSize()];
        gameOver = false;

        grid.getChildren().clear();
        drawBoard();
        hideRetryButton();
        updateScoreboard("Partida em andamento");
        resultLabel.setText("Escolha uma coordenada para atacar. Encontre todos os navios.");
    }

    private void drawBoard() {
        for (int row = 0; row < game.getBoardSize(); row++) {
            for (int col = 0; col < game.getBoardSize(); col++) {
                Button button = new Button();
                button.setMinSize(40, 40);
                button.setPrefSize(40, 40);
                button.getStyleClass().add("battleships-cell");

                int selectedRow = row;
                int selectedCol = col;
                button.setOnAction(event -> handleAttack(selectedRow, selectedCol));

                buttons[row][col] = button;
                grid.add(button, col, row);
            }
        }
    }

    private void handleAttack(int row, int col) {
        if (gameOver) {
            return;
        }

        Battleships.AttackResult result = game.atacar(row, col);

        switch (result) {
            case HIT -> resultLabel.setText("Acerto confirmado. Continue atacando.");
            case MISS -> resultLabel.setText("Agua. Escolha outra coordenada.");
            case REPEATED -> resultLabel.setText("Essa coordenada ja foi atacada.");
            case OUT_OF_BOUNDS -> resultLabel.setText("Coordenada fora do tabuleiro.");
        }

        refreshBoard();

        if (game.isFleetSunk()) {
            gameOver = true;
            disableBoard();
            updateScoreboard("Vitoria");
            resultLabel.setText("Voce afundou todos os navios!");
            showRetryButton();
            return;
        }

        updateScoreboard("Partida em andamento");
    }

    private void refreshBoard() {
        for (int row = 0; row < game.getBoardSize(); row++) {
            for (int col = 0; col < game.getBoardSize(); col++) {
                Button button = buttons[row][col];

                button.getStyleClass().removeAll(
                        "battleships-cell-hit",
                        "battleships-cell-miss"
                );

                if (!game.wasTried(row, col)) {
                    button.setText("");
                    continue;
                }

                button.setDisable(true);

                if (game.hasShipAt(row, col)) {
                    button.setText("X");
                    button.getStyleClass().add("battleships-cell-hit");
                } else {
                    button.setText("~");
                    button.getStyleClass().add("battleships-cell-miss");
                }
            }
        }
    }

    private void disableBoard() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setDisable(true);
            }
        }
    }

    private void updateScoreboard(String status) {
        int totalShipCells = game.getTotalShipCells();
        int hits = game.getTotalHits();

        statusLabel.setText(status);
        attemptsLabel.setText(String.valueOf(game.getAttempts()));
        hitsLabel.setText(hits + "/" + totalShipCells);
        remainingLabel.setText(String.valueOf(totalShipCells - hits));
    }

    private void showRetryButton() {
        retryButton.setManaged(true);
        retryButton.setVisible(true);
    }

    private void hideRetryButton() {
        retryButton.setManaged(false);
        retryButton.setVisible(false);
    }
}
