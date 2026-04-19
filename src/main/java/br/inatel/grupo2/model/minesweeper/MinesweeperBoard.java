package br.inatel.grupo2.model.minesweeper;

import java.util.Random;

public class MinesweeperBoard {
    private final int rows = 8;
    private final int cols = 8;
    private final int mines = 10;

    private MinesweeperCell[][] board;

    public MinesweeperBoard() {
        board = new MinesweeperCell[rows][cols];
        initBoard();
        placeMines();
        calculateNumbers();
    }

    private void initBoard() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                board[i][j] = new MinesweeperCell();
    }

    private void placeMines() {
        Random rand = new Random();
        int count = 0;

        while (count < mines) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);

            if (!board[r][c].isMine()) {
                board[r][c].setMine(true);
                count++;
            }
        }
    }

    private void calculateNumbers() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                if (board[i][j].isMine()) continue;

                int count = 0;

                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {

                        int ni = i + x;
                        int nj = j + y;

                        if (ni >= 0 && nj >= 0 && ni < rows && nj < cols) {
                            if (board[ni][nj].isMine()) count++;
                        }
                    }
                }

                board[i][j].setAdjacentMines(count);
            }
        }
    }

    public MinesweeperCell getCell(int row, int col) {
        return board[row][col];
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public int getMines() { return mines; }
}
