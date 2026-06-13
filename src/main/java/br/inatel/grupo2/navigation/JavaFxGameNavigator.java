package br.inatel.grupo2.navigation;

import br.inatel.grupo2.app.GameHubApplication;

public class JavaFxGameNavigator implements GameNavigator {
    @Override
    public void showMenuScreen() {
        GameHubApplication.showMenuScreen();
    }

    @Override
    public void showGameDetailScreen(String gameName, String icon, String description) {
        GameHubApplication.showGameDetailScreen(gameName, icon, description);
    }

    @Override
    public void showRockPaperScissorsScreen() {
        GameHubApplication.showRockPaperScissorsScreen();
    }

    @Override
    public void showMinesweeperScreen() {
        GameHubApplication.showMinesweeperScreen();
    }

    @Override
    public void showTicTacToeScreen() {
        GameHubApplication.showTicTacToeScreen();
    }

    @Override
    public void showBattleshipsScreen() {
        GameHubApplication.showBattleshipsScreen();
    }
}
