package br.inatel.grupo2.model.rockpaperscissors;

import java.util.Random;

public class RockPaperScissorsModel {

    private String[] options = {"pedra", "papel", "tesoura"};
    private Random random = new Random();

    public String getComputerChoice() {
        return options[random.nextInt(3)];
    }

    public String getResult(String player, String computer) {

        if (player.equals(computer)) {
            return "Empate";
        }

        if (
                (player.equals("pedra") && computer.equals("tesoura")) ||
                        (player.equals("papel") && computer.equals("pedra")) ||
                        (player.equals("tesoura") && computer.equals("papel"))
        ) {
            return "Jogador";
        }

        return "Computador";
    }
}
