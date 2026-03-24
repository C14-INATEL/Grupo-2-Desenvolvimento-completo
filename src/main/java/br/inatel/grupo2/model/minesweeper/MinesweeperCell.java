package br.inatel.grupo2.model.minesweeper;

public class MinesweeperCell {
    private boolean isMine;
    private boolean isRevealed;
    private int adjacentMines;

    public MinesweeperCell() {
        this.isMine = false;
        this.isRevealed = false;
        this.adjacentMines = 0;
    }

    public boolean isMine() { return isMine; }
    public void setMine(boolean mine) { isMine = mine; }

    public boolean isRevealed() { return isRevealed; }
    public void setRevealed(boolean revealed) { isRevealed = revealed; }

    public int getAdjacentMines() { return adjacentMines; }
    public void setAdjacentMines(int count) { this.adjacentMines = count; }
}
