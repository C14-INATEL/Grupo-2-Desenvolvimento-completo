package br.inatel.grupo2.navigation;

public interface GameNavigator {
    void showMenuScreen();

    void showGameDetailScreen(String gameName, String icon, String description);

    void showRockPaperScissorsScreen();

    void showMinesweeperScreen();

    void showTicTacToeScreen();
}
