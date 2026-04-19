package br.inatel.grupo2.controller.minesweeper;

import br.inatel.grupo2.model.minesweeper.DefaultMinesweeperBoardFactory;
import br.inatel.grupo2.model.minesweeper.MinesweeperBoard;
import br.inatel.grupo2.model.minesweeper.MinesweeperBoardFactory;
import br.inatel.grupo2.model.minesweeper.MinesweeperCell;
import br.inatel.grupo2.navigation.GameNavigator;
import br.inatel.grupo2.navigation.JavaFxGameNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class MinesweeperController {
    @FXML private Label statusLabel;
    @FXML private Label minesLabel;
    @FXML private Label revealedLabel;
    @FXML private Label resultLabel;
    @FXML private Button retryButton;
    @FXML private GridPane grid;

    private MinesweeperBoard board;
    private Button[][] buttons;
    private boolean gameOver;
    private GameNavigator navigator = new JavaFxGameNavigator();
    private MinesweeperBoardFactory boardFactory = new DefaultMinesweeperBoardFactory();

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

    private void startGame() {
        board = boardFactory.createBoard();
        buttons = new Button[board.getRows()][board.getCols()];
        gameOver = false;

        grid.getChildren().clear();
        drawBoard();
        hideRetryPrompt();
        updateStatus("Partida em andamento");
        resultLabel.setText("Clique em uma casa para comecar. Evite as minas.");
    }

    private void drawBoard() {
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                Button button = new Button();
                button.setMinSize(48, 48);
                button.setPrefSize(48, 48);
                button.getStyleClass().add("minesweeper-cell");

                int selectedRow = row;
                int selectedCol = col;

                button.setOnAction(e -> handleClick(selectedRow, selectedCol));

                buttons[row][col] = button;
                grid.add(button, col, row);
            }
        }
    }

    private void handleClick(int row, int col) {
        if (gameOver) return;

        MinesweeperCell cell = board.getCell(row, col);
        if (cell.isRevealed()) return;

        if (cell.isMine()) {
            cell.setRevealed(true);
            gameOver = true;
            revealAllMines();
            disableBoard();
            updateStatus("Fim de jogo");
            resultLabel.setText("Voce encontrou uma mina.");
            showRetryButton();
            return;
        }

        revealSafeArea(row, col);
        refreshBoard();

        if (hasWon()) {
            gameOver = true;
            disableBoard();
            updateStatus("Vitoria");
            resultLabel.setText("Voce venceu! Todas as casas seguras foram reveladas.");
            showRetryButton();
            return;
        }

        updateStatus("Partida em andamento");
        resultLabel.setText("Boa jogada. Continue procurando as casas seguras.");
    }

    private void revealAllMines() {
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                MinesweeperCell cell = board.getCell(row, col);
                if (cell.isMine()) {
                    cell.setRevealed(true);
                }
            }
        }
        refreshBoard();
    }

    private void revealSafeArea(int row, int col) {
        if (!isInsideBoard(row, col)) return;

        MinesweeperCell cell = board.getCell(row, col);
        if (cell.isRevealed() || cell.isMine()) return;

        cell.setRevealed(true);

        if (cell.getAdjacentMines() != 0) return;

        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int colOffset = -1; colOffset <= 1; colOffset++) {
                if (rowOffset != 0 || colOffset != 0) {
                    revealSafeArea(row + rowOffset, col + colOffset);
                }
            }
        }
    }

    private void refreshBoard() {
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                MinesweeperCell cell = board.getCell(row, col);
                Button button = buttons[row][col];

                button.getStyleClass().removeAll(
                        "minesweeper-cell-revealed",
                        "minesweeper-cell-mine",
                        "minesweeper-cell-number"
                );

                if (!cell.isRevealed()) {
                    button.setText("");
                    continue;
                }

                button.setDisable(true);
                button.getStyleClass().add("minesweeper-cell-revealed");

                if (cell.isMine()) {
                    button.setText("\uD83D\uDCA3");
                    button.getStyleClass().add("minesweeper-cell-mine");
                } else {
                    int count = cell.getAdjacentMines();
                    button.setText(count == 0 ? "" : String.valueOf(count));
                    if (count > 0) {
                        button.getStyleClass().add("minesweeper-cell-number");
                    }
                }
            }
        }
    }

    private boolean hasWon() {
        return countRevealedSafeCells() == board.getRows() * board.getCols() - board.getMines();
    }

    private int countRevealedSafeCells() {
        int revealed = 0;

        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                MinesweeperCell cell = board.getCell(row, col);
                if (cell.isRevealed() && !cell.isMine()) {
                    revealed++;
                }
            }
        }

        return revealed;
    }

    private void disableBoard() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setDisable(true);
            }
        }
    }

    private void updateStatus(String status) {
        int safeCells = board.getRows() * board.getCols() - board.getMines();

        statusLabel.setText(status);
        minesLabel.setText(String.valueOf(board.getMines()));
        revealedLabel.setText(countRevealedSafeCells() + "/" + safeCells);
    }

    private void showRetryButton() {
        retryButton.setManaged(true);
        retryButton.setVisible(true);
    }

    private void hideRetryPrompt() {
        retryButton.setVisible(false);
        retryButton.setManaged(false);
    }

    public void setNavigator(GameNavigator navigator) {
        this.navigator = navigator;
    }

    public void setBoardFactory(MinesweeperBoardFactory boardFactory) {
        this.boardFactory = boardFactory;
    }

    private boolean isInsideBoard(int row, int col) {
        return row >= 0 && row < board.getRows() && col >= 0 && col < board.getCols();
    }
}
