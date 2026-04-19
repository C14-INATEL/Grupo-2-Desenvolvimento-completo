package br.inatel.grupo2.controller.tictactoe;

import br.inatel.grupo2.model.tictactoe.TicTacToeModel;
import br.inatel.grupo2.navigation.GameNavigator;
import br.inatel.grupo2.navigation.JavaFxGameNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TicTacToeController {
    @FXML private Label subtitleLabel;
    @FXML private Label turnLabel;
    @FXML private Label resultLabel;
    @FXML private Label xScoreLabel;
    @FXML private Label oScoreLabel;
    @FXML private Label drawsLabel;
    @FXML private Label roundLabel;
    @FXML private Button computerModeButton;
    @FXML private Button localModeButton;
    @FXML private Button cell00;
    @FXML private Button cell01;
    @FXML private Button cell02;
    @FXML private Button cell10;
    @FXML private Button cell11;
    @FXML private Button cell12;
    @FXML private Button cell20;
    @FXML private Button cell21;
    @FXML private Button cell22;

    private TicTacToeModel model = new TicTacToeModel();
    private GameNavigator navigator = new JavaFxGameNavigator();

    private Button[][] cells;
    private boolean computerMode = true;
    private int xScore;
    private int oScore;
    private int draws;
    private int rounds;

    @FXML
    public void initialize() {
        cells = new Button[][]{
                {cell00, cell01, cell02},
                {cell10, cell11, cell12},
                {cell20, cell21, cell22}
        };

        updateModeButtons();
        updateBoard();
        updateScoreboard();
        updateTurnLabel();
        resultLabel.setText("Escolha uma casa para comecar.");
    }

    @FXML
    protected void onChooseComputerMode() {
        computerMode = true;
        resetMatch();
    }

    @FXML
    protected void onChooseLocalMode() {
        computerMode = false;
        resetMatch();
    }

    @FXML
    protected void onCell00() {
        playHumanMove(0, 0);
    }

    @FXML
    protected void onCell01() {
        playHumanMove(0, 1);
    }

    @FXML
    protected void onCell02() {
        playHumanMove(0, 2);
    }

    @FXML
    protected void onCell10() {
        playHumanMove(1, 0);
    }

    @FXML
    protected void onCell11() {
        playHumanMove(1, 1);
    }

    @FXML
    protected void onCell12() {
        playHumanMove(1, 2);
    }

    @FXML
    protected void onCell20() {
        playHumanMove(2, 0);
    }

    @FXML
    protected void onCell21() {
        playHumanMove(2, 1);
    }

    @FXML
    protected void onCell22() {
        playHumanMove(2, 2);
    }

    @FXML
    protected void onNewRound() {
        model.reset();
        updateBoard();
        updateTurnLabel();
        resultLabel.setText("Nova rodada iniciada. X comeca.");
    }

    @FXML
    protected void onResetMatch() {
        resetMatch();
    }

    @FXML
    protected void onBack() {
        navigator.showMenuScreen();
    }

    public void setNavigator(GameNavigator navigator) {
        this.navigator = navigator;
    }

    public void setModel(TicTacToeModel model) {
        this.model = model;
    }

    private void playHumanMove(int row, int col) {
        if (computerMode && model.getCurrentPlayer() == TicTacToeModel.PLAYER_O) {
            return;
        }

        if (!model.makeMove(row, col)) {
            resultLabel.setText("Essa casa ja foi escolhida. Tente outra.");
            return;
        }

        updateBoard();
        if (handleRoundEnd()) {
            return;
        }

        if (computerMode) {
            playComputerMove();
            return;
        }

        updateTurnLabel();
        resultLabel.setText("Agora e a vez do jogador " + model.getCurrentPlayer() + ".");
    }

    private void playComputerMove() {
        int[] move = model.findBestComputerMove();
        if (move == null) {
            handleRoundEnd();
            return;
        }

        model.makeMove(move[0], move[1]);
        updateBoard();

        if (!handleRoundEnd()) {
            updateTurnLabel();
            resultLabel.setText("O computador jogou. Sua vez.");
        }
    }

    private boolean handleRoundEnd() {
        if (!model.isGameOver()) {
            return false;
        }

        rounds++;

        if (model.isDraw()) {
            draws++;
            resultLabel.setText("Empate! Ninguem venceu esta rodada.");
        } else if (model.getWinner() == TicTacToeModel.PLAYER_X) {
            xScore++;
            resultLabel.setText(computerMode ? "Voce venceu o computador!" : "Jogador X venceu!");
        } else {
            oScore++;
            resultLabel.setText(computerMode ? "O computador venceu esta rodada." : "Jogador O venceu!");
        }

        updateScoreboard();
        updateTurnLabel();
        return true;
    }

    private void resetMatch() {
        xScore = 0;
        oScore = 0;
        draws = 0;
        rounds = 0;
        model.reset();
        updateModeButtons();
        updateBoard();
        updateScoreboard();
        updateTurnLabel();
        resultLabel.setText("Placar reiniciado. X comeca.");
    }

    private void updateModeButtons() {
        subtitleLabel.setText(computerMode
                ? "Modo atual: voce contra o computador"
                : "Modo atual: dois jogadores no mesmo teclado");

        computerModeButton.getStyleClass().remove("mode-button-selected");
        localModeButton.getStyleClass().remove("mode-button-selected");

        Button selectedButton = computerMode ? computerModeButton : localModeButton;
        if (!selectedButton.getStyleClass().contains("mode-button-selected")) {
            selectedButton.getStyleClass().add("mode-button-selected");
        }
    }

    private void updateBoard() {
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                Button button = cells[row][col];
                char symbol = model.getCell(row, col);

                button.setText(symbol == TicTacToeModel.EMPTY ? "" : String.valueOf(symbol));
                button.setDisable(model.isGameOver() || symbol != TicTacToeModel.EMPTY);
                button.getStyleClass().removeAll("tic-cell-x", "tic-cell-o");

                if (symbol == TicTacToeModel.PLAYER_X) {
                    button.getStyleClass().add("tic-cell-x");
                } else if (symbol == TicTacToeModel.PLAYER_O) {
                    button.getStyleClass().add("tic-cell-o");
                }
            }
        }
    }

    private void updateTurnLabel() {
        if (model.isGameOver()) {
            turnLabel.setText("Rodada encerrada");
            return;
        }

        if (computerMode && model.getCurrentPlayer() == TicTacToeModel.PLAYER_O) {
            turnLabel.setText("Vez do computador");
            return;
        }

        turnLabel.setText("Vez do jogador " + model.getCurrentPlayer());
    }

    private void updateScoreboard() {
        xScoreLabel.setText(String.valueOf(xScore));
        oScoreLabel.setText(String.valueOf(oScore));
        drawsLabel.setText(String.valueOf(draws));
        roundLabel.setText("Rodadas finalizadas: " + rounds);
    }
}
