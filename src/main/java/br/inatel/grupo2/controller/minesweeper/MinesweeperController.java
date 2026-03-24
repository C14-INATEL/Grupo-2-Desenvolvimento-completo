package br.inatel.grupo2.controller.minesweeper;

import br.inatel.grupo2.model.minesweeper.MinesweeperBoard;
import br.inatel.grupo2.model.minesweeper.MinesweeperCell;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class MinesweeperController {

    @FXML
    private GridPane grid;

    private MinesweeperBoard board;

    @FXML
    public void initialize() {
        startGame();
    }

    private void startGame() {
        board = new MinesweeperBoard();
        grid.getChildren().clear();
        drawBoard();
    }

    private void drawBoard() {
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {

                Button btn = new Button();
                btn.setPrefSize(40, 40);

                int row = i;
                int col = j;

                btn.setOnAction(e -> handleClick(btn, row, col));

                grid.add(btn, col, row);
            }
        }
    }

    private void handleClick(Button btn, int row, int col) {
        MinesweeperCell cell = board.getCell(row, col);

        if (cell.isRevealed()) return;

        cell.setRevealed(true);

        if (cell.isMine()) {
            btn.setText("💣");
            revealAllMines();
            System.out.println("Game Over!");
        } else {
            int count = cell.getAdjacentMines();
            btn.setText(count == 0 ? "" : String.valueOf(count));
        }
    }

    private void revealAllMines() {
        grid.getChildren().forEach(node -> {
            if (node instanceof Button btn) {
                Integer col = GridPane.getColumnIndex(btn);
                Integer row = GridPane.getRowIndex(btn);

                if (row == null || col == null) return;

                MinesweeperCell cell = board.getCell(row, col);
                if (cell.isMine()) {
                    btn.setText("💣");
                }
            }
        });
    }
}
