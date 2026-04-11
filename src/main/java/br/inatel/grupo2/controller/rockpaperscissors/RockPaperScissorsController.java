package br.inatel.grupo2.controller.rockpaperscissors;

import br.inatel.grupo2.model.rockpaperscissors.RockPaperScissorsModel;
import java.util.Scanner;

public class RockPaperScissorsController {

    private RockPaperScissorsModel model = new RockPaperScissorsModel();

    public void play() {

        Scanner scanner = new Scanner(System.in);
        boolean playing = true;

        System.out.println("=== Pedra, Papel e Tesoura ===");

        while (playing) {

            System.out.print("Escolha (pedra, papel, tesoura): ");
            String player = scanner.nextLine().toLowerCase();

            if (!player.equals("pedra") && !player.equals("papel") && !player.equals("tesoura")) {
                System.out.println("Opção inválida!");
                continue;
            }

            String computer = model.getComputerChoice();

            System.out.println("Você: " + player);
            System.out.println("Computador: " + computer);

            String result = model.getResult(player, computer);

            if (result.equals("Empate")) {
                System.out.println("Empate!");
            } else if (result.equals("Jogador")) {
                System.out.println("Você venceu!");
            } else {
                System.out.println("Você perdeu!");
            }

            System.out.print("Jogar novamente? (s/n): ");
            if (!scanner.nextLine().equalsIgnoreCase("s")) {
                playing = false;
            }

            System.out.println();
        }

        scanner.close();
    }
}
