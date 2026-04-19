package br.inatel.grupo2.model.minesweeper;

public class DefaultMinesweeperBoardFactory implements MinesweeperBoardFactory {
    @Override
    public MinesweeperBoard createBoard() {
        return new MinesweeperBoard();
    }
}
