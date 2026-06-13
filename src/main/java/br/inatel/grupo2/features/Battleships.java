package br.inatel.grupo2.features;

import java.util.Random;

public class Battleships {
    public enum AttackResult {
        HIT,
        MISS,
        REPEATED,
        OUT_OF_BOUNDS
    }

    private static final int BOARD_SIZE = 10;
    private static final int[] DEFAULT_FLEET = {5, 4, 3, 3, 2};

    boolean[][] campo = new boolean[BOARD_SIZE][BOARD_SIZE];
    boolean[][] acertos = new boolean[BOARD_SIZE][BOARD_SIZE];

    int i;
    Random r;

    private int inicioX;
    private int inicioY;
    private int comprimento;
    private boolean vertical;

    public Battleships() {
        this(new Random());
    }

    public Battleships(Random random) {
        this.r = random;
    }

    public void criarFrotaPadrao() {
        reset();

        for (int tamanho : DEFAULT_FLEET) {
            colocarNavio(tamanho, false);
        }
    }

    private boolean podeColocarNavio(int x, int y, int comprimento, boolean vertical) {
        if (vertical) {
            if (y + comprimento > BOARD_SIZE) {
                return false;
            }

            for (int i = 0; i < comprimento; i++) {
                if (campo[x][y + i]) {
                    return false;
                }
            }
        } else {
            if (x + comprimento > BOARD_SIZE) {
                return false;
            }

            for (int i = 0; i < comprimento; i++) {
                if (campo[x + i][y]) {
                    return false;
                }
            }
        }

        return true;
    }

    public void criarBattleship() {
        colocarNavio(0, true);
    }

    private void colocarNavio(int tamanho, boolean tamanhoAleatorio) {
        boolean colocado = false;
        int tentativas = 0;

        while (!colocado && tentativas < 100) {
            this.comprimento = tamanhoAleatorio ? r.nextInt(5) + 1 : tamanho;
            this.inicioX = r.nextInt(BOARD_SIZE);
            this.inicioY = r.nextInt(BOARD_SIZE);
            this.vertical = r.nextBoolean();

            if (podeColocarNavio(inicioX, inicioY, comprimento, vertical)) {
                for (i = 0; i < comprimento; i++) {
                    if (vertical) {
                        campo[inicioX][inicioY + i] = true;
                    } else {
                        campo[inicioX + i][inicioY] = true;
                    }
                }
                colocado = true;
            }

            tentativas++;
        }

        if (colocado) {
            System.out.println("Navio criado!");
        } else {
            System.out.println("Falha na criaÃ§Ã£o do navio.");
        }
    }

    public void checarAcerto(int linha, int coluna) {
        AttackResult result = atacar(linha, coluna);

        switch (result) {
            case OUT_OF_BOUNDS -> System.out.println("Coordenada fora do tabuleiro!");
            case REPEATED -> System.out.println("JÃ¡ tentou essa posiÃ§Ã£o!");
            case HIT -> {
                System.out.println("Acertou!");
                if (isSunk()) {
                    System.out.println("Navio afundado!");
                }
            }
            case MISS -> System.out.println("Errou!");
        }
    }

    public AttackResult atacar(int linha, int coluna) {
        if (!isInsideBoard(linha, coluna)) {
            return AttackResult.OUT_OF_BOUNDS;
        }

        if (acertos[linha][coluna]) {
            return AttackResult.REPEATED;
        }

        acertos[linha][coluna] = true;
        return campo[linha][coluna] ? AttackResult.HIT : AttackResult.MISS;
    }

    public boolean isSunk() {
        for (int i = 0; i < comprimento; i++) {
            int x = inicioX;
            int y = inicioY;

            if (vertical) {
                y += i;
            } else {
                x += i;
            }

            if (!acertos[x][y]) {
                return false;
            }
        }

        return true;
    }

    public int getComprimento() {
        return comprimento;
    }

    public int getInicioX() {
        return inicioX;
    }

    public int getInicioY() {
        return inicioY;
    }

    public boolean isVertical() {
        return vertical;
    }

    public boolean[][] getCampo() {
        return campo;
    }

    public boolean[][] getAcertos() {
        return acertos;
    }

    public boolean hasShipAt(int linha, int coluna) {
        return isInsideBoard(linha, coluna) && campo[linha][coluna];
    }

    public boolean wasTried(int linha, int coluna) {
        return isInsideBoard(linha, coluna) && acertos[linha][coluna];
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }

    public int getTotalShipCells() {
        int total = 0;

        for (int linha = 0; linha < BOARD_SIZE; linha++) {
            for (int coluna = 0; coluna < BOARD_SIZE; coluna++) {
                if (campo[linha][coluna]) {
                    total++;
                }
            }
        }

        return total;
    }

    public int getTotalHits() {
        int total = 0;

        for (int linha = 0; linha < BOARD_SIZE; linha++) {
            for (int coluna = 0; coluna < BOARD_SIZE; coluna++) {
                if (campo[linha][coluna] && acertos[linha][coluna]) {
                    total++;
                }
            }
        }

        return total;
    }

    public int getAttempts() {
        int total = 0;

        for (int linha = 0; linha < BOARD_SIZE; linha++) {
            for (int coluna = 0; coluna < BOARD_SIZE; coluna++) {
                if (acertos[linha][coluna]) {
                    total++;
                }
            }
        }

        return total;
    }

    public boolean isFleetSunk() {
        int totalShipCells = getTotalShipCells();
        return totalShipCells > 0 && getTotalHits() == totalShipCells;
    }

    public void reset() {
        campo = new boolean[BOARD_SIZE][BOARD_SIZE];
        acertos = new boolean[BOARD_SIZE][BOARD_SIZE];
        inicioX = 0;
        inicioY = 0;
        comprimento = 0;
        vertical = false;
    }

    private boolean isInsideBoard(int linha, int coluna) {
        return linha >= 0 && linha < BOARD_SIZE && coluna >= 0 && coluna < BOARD_SIZE;
    }
}
