package br.inatel.grupo2.model.tictactoe;

public class TicTacToeModel {
    public static final char EMPTY = ' ';
    public static final char PLAYER_X = 'X';
    public static final char PLAYER_O = 'O';

    private final char[][] board = new char[3][3];
    private char currentPlayer = PLAYER_X;
    private char winner = EMPTY;
    private boolean gameOver;

    public TicTacToeModel() {
        reset();
    }

    public void reset() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col] = EMPTY;
            }
        }
        currentPlayer = PLAYER_X;
        winner = EMPTY;
        gameOver = false;
    }

    public boolean makeMove(int row, int col) {
        if (!isInsideBoard(row, col) || gameOver || board[row][col] != EMPTY) {
            return false;
        }

        board[row][col] = currentPlayer;
        updateGameState(row, col);

        if (!gameOver) {
            switchPlayer();
        }

        return true;
    }

    public int[] findBestComputerMove() {
        int[] winningMove = findWinningMove(PLAYER_O);
        if (winningMove != null) {
            return winningMove;
        }

        int[] blockingMove = findWinningMove(PLAYER_X);
        if (blockingMove != null) {
            return blockingMove;
        }

        if (board[1][1] == EMPTY) {
            return new int[]{1, 1};
        }

        int[][] preferredMoves = {
                {0, 0}, {0, 2}, {2, 0}, {2, 2},
                {0, 1}, {1, 0}, {1, 2}, {2, 1}
        };

        for (int[] move : preferredMoves) {
            if (board[move[0]][move[1]] == EMPTY) {
                return move;
            }
        }

        return null;
    }

    public char getCell(int row, int col) {
        if (!isInsideBoard(row, col)) {
            throw new IllegalArgumentException("Posicao fora do tabuleiro");
        }
        return board[row][col];
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public char getWinner() {
        return winner;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isDraw() {
        return gameOver && winner == EMPTY;
    }

    private int[] findWinningMove(char player) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] != EMPTY) {
                    continue;
                }

                board[row][col] = player;
                boolean wins = hasWinner(row, col);
                board[row][col] = EMPTY;

                if (wins) {
                    return new int[]{row, col};
                }
            }
        }

        return null;
    }

    private void updateGameState(int row, int col) {
        if (hasWinner(row, col)) {
            winner = currentPlayer;
            gameOver = true;
            return;
        }

        if (isBoardFull()) {
            gameOver = true;
        }
    }

    private boolean hasWinner(int row, int col) {
        char symbol = board[row][col];
        if (symbol == EMPTY) {
            return false;
        }

        boolean fullRow = true;
        boolean fullCol = true;
        boolean mainDiagonal = row == col;
        boolean secondaryDiagonal = row + col == 2;

        for (int i = 0; i < 3; i++) {
            fullRow &= board[row][i] == symbol;
            fullCol &= board[i][col] == symbol;
            if (row == col) {
                mainDiagonal &= board[i][i] == symbol;
            }
            if (row + col == 2) {
                secondaryDiagonal &= board[i][2 - i] == symbol;
            }
        }

        return fullRow || fullCol || mainDiagonal || secondaryDiagonal;
    }

    private boolean isBoardFull() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isInsideBoard(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3;
    }

    private void switchPlayer() {
        currentPlayer = currentPlayer == PLAYER_X ? PLAYER_O : PLAYER_X;
    }
}
