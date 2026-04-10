package br.inatel.grupo2.features;
import java.util.Random;

public class Battleships {
    boolean[][] campo = new boolean[10][10];
    boolean[][] acertos = new boolean[10][10];

    int i;
    Random r = new Random();

    private int inicioX;
    private int inicioY;
    private int comprimento;
    private boolean vertical; // 0 - horizontal 1 - vertical

    // confirma se nao ha superposicao de navios e se o navio cabe no campo
    private boolean podeColocarNavio(int x, int y, int comprimento, boolean vertical) 
    {
        if (vertical) 
        {
            // primeiro verifica se o navio cabe no campo
            if (y + comprimento > 10) 
                return false;

            // se couber, verifica se ha colisao com algum outro navio ja colocado
            for (int i = 0; i < comprimento; i++) 
            {
                if (campo[x][y + i]) return false;
            }

        } 

        else
        {
            if (x + comprimento > 10)
                return false;
            for (int i = 0; i < comprimento; i++) 
            {
                if (campo[x + i][y]) return false;
            }
        }
        return true;
    }

    public void criarBattleship() 
    {
        boolean colocado = false;
        int tentativas = 0;
        // enquanto nao for colocado, vai tentando gerar posicoes aleatorias para o navio, verificando se ele pode ser colocado ali
        while (!colocado && tentativas < 100)
        {
            this.comprimento = r.nextInt(5) + 1; // 1 a 5
            this.inicioX = r.nextInt(10);
            this.inicioY = r.nextInt(10);
            this.vertical = r.nextBoolean();

            // testa colisoes e se o navio cabe no campo, se tudo certo, coloca o navio no campo
            if (podeColocarNavio(inicioX, inicioY, comprimento, vertical)) 
                {
                for (i = 0; i < comprimento; i++)
                {
                    if (vertical)
                    {
                        campo[inicioX][inicioY + i] = true;
                    }
                    else
                    {
                        campo[inicioX + i][inicioY] = true;
                    }
                }
                colocado = true; // se chegou aqui, o navio foi colocado com sucesso
            }
            tentativas++;
        }
        if (colocado) 
        {
            System.out.println("Navio criado!");
        } 
        else 
        {
            System.out.println("Falha na criação do navio.");
        }
    }

    // checa acertos e erros, e marca as tentativas no array de acertos
    public void checarAcerto(int linha, int coluna) {
        if (linha < 0 || linha >= 10 || coluna < 0 || coluna >= 10) {
            System.out.println("Coordenada fora do tabuleiro!");
            return;
        }
        if (acertos[linha][coluna]) {
            System.out.println("Já tentou essa posição!");
            return;
        }
        acertos[linha][coluna] = true;
        if (campo[linha][coluna]) {
            System.out.println("Acertou!");
            if (isSunk()) {
                System.out.println("Navio afundado!");
            }
        } else {
            System.out.println("Errou!");
        }
    }

    // Checks if the current ship has been sunk (only its cells)
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
}
