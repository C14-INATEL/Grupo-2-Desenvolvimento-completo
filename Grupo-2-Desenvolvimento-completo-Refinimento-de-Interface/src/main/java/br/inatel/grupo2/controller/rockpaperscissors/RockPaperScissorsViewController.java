package br.inatel.grupo2.controller.rockpaperscissors;

import br.inatel.grupo2.app.GameHubApplication;
import br.inatel.grupo2.model.rockpaperscissors.RockPaperScissorsModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Locale;

public class RockPaperScissorsViewController {
    @FXML private Label playerChoiceLabel;
    @FXML private Label computerChoiceLabel;
    @FXML private Label resultLabel;
    @FXML private Label playerScoreLabel;
    @FXML private Label computerScoreLabel;
    @FXML private Label drawsLabel;
    @FXML private Label roundLabel;

    private final RockPaperScissorsModel model = new RockPaperScissorsModel();

    private int playerScore;
    private int computerScore;
    private int draws;
    private int rounds;

    @FXML
    public void initialize() {
        updateScoreboard();
        playerChoiceLabel.setText("Aguardando");
        computerChoiceLabel.setText("Aguardando");
        resultLabel.setText("Escolha uma jogada para comecar a partida.");
    }

    @FXML
    protected void onChooseRock() {
        playRound("pedra");
    }

    @FXML
    protected void onChoosePaper() {
        playRound("papel");
    }

    @FXML
    protected void onChooseScissors() {
        playRound("tesoura");
    }

    @FXML
    protected void onResetMatch() {
        playerScore = 0;
        computerScore = 0;
        draws = 0;
        rounds = 0;

        playerChoiceLabel.setText("Aguardando");
        computerChoiceLabel.setText("Aguardando");
        resultLabel.setText("Placar reiniciado. Escolha sua proxima jogada.");
        updateScoreboard();
    }

    @FXML
    protected void onBack() {
        GameHubApplication.showMenuScreen();
    }

    private void playRound(String playerChoice) {
        String computerChoice = model.getComputerChoice();
        String result = model.getResult(playerChoice, computerChoice);

        rounds++;
        playerChoiceLabel.setText(formatChoice(playerChoice));
        computerChoiceLabel.setText(formatChoice(computerChoice));

        switch (result) {
            case "Jogador" -> {
                playerScore++;
                resultLabel.setText("Voce venceu a rodada.");
            }
            case "Computador" -> {
                computerScore++;
                resultLabel.setText("O computador venceu a rodada.");
            }
            default -> {
                draws++;
                resultLabel.setText("A rodada terminou empatada.");
            }
        }

        updateScoreboard();
    }

    private void updateScoreboard() {
        playerScoreLabel.setText(String.valueOf(playerScore));
        computerScoreLabel.setText(String.valueOf(computerScore));
        drawsLabel.setText(String.valueOf(draws));
        roundLabel.setText("Rodadas jogadas: " + rounds);
    }

    private String formatChoice(String choice) {
        if (choice == null || choice.isBlank()) {
            return "Aguardando";
        }

        String normalized = choice.toLowerCase(Locale.ROOT);
        return Character.toUpperCase(normalized.charAt(0)) + normalized.substring(1);
    }
}
