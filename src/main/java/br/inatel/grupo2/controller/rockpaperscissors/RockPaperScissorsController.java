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

            String player;
            while (true) {
                System.out.print("Escolha (pedra, papel, tesoura): ");
                player = scanner.nextLine().trim().toLowerCase();

                if (player.isEmpty()) {
                    continue;
                }

                if (!player.equals("pedra") &&
                        !player.equals("papel") &&
                        !player.equals("tesoura")) {

                    System.out.println("Opção inválida!");
                    continue;
                }

                break;
            }

            String computer = model.getComputerChoice();

            System.out.println("Você: " + player);
            System.out.println("Computador: " + computer);

            String result = model.getResult(player, computer);

            switch (result) {
                case "Empate":
                    System.out.println("Empate!");
                    break;
                case "Jogador":
                    System.out.println("Você venceu!");
                    break;
                default:
                    System.out.println("Você perdeu!");
            }

            while (true) {
                System.out.print("Jogar novamente? (s/n): ");
                String resposta = scanner.nextLine().trim().toLowerCase();

                if (resposta.equals("s")) {
                    break;
                } else if (resposta.equals("n")) {
                    playing = false;
                    break;
                } else {
                    System.out.println("Digite apenas 's' ou 'n'");
                }
            }

            System.out.println();
        }

        scanner.close();
        System.out.println("Jogo encerrado.");
    }
}