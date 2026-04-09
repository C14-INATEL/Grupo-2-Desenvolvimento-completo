package br.inatel.grupo2.model.rockpaperscissors;

import java.util.Random;

public class RockPaperScissorsModel {

    private String[] options = {"pedra", "papel", "tesoura"};
    private Random random = new Random();

    public String getComputerChoice() {
        return options[random.nextInt(3)];
    }

    public String getResult(String player, String computer) {
        if (player == null || computer == null) {
            throw new IllegalArgumentException("Valores não podem ser nulos");
        }

        player = player.toLowerCase().trim();
        computer = computer.toLowerCase().trim();

        if (!isValid(player) || !isValid(computer)) {
            throw new IllegalArgumentException("Opção inválida");
        }

        if (player.equals(computer)) return "Empate";

        if (
                (player.equals("pedra") && computer.equals("tesoura")) ||
                        (player.equals("papel") && computer.equals("pedra")) ||
                        (player.equals("tesoura") && computer.equals("papel"))
        ) return "Jogador";

        return "Computador";
    }

    private boolean isValid(String op) {
        return op.equals("pedra") || op.equals("papel") || op.equals("tesoura");
    }
}