import java.util.Random;

public class Battleships
{
    boolean[][] campo = new boolean[10][10];

    int i;
    Random r = new Random(10);

    private int inicioX;
    private int inicioY;
    private int comprimento;
    private boolean vertical; // 0 - horizontal 1 - vertical

    public void criarBattleship()
    {
        this.inicioX = r.nextInt(4) + 1;
        this.inicioY = r.nextInt(4) + 1;
        this.comprimento = r.nextInt(5) + 1;
        this.vertical = r.nextBoolean();

        for(i = 0; i < comprimento; i++)
        {
            if(vertical)
            {
                campo[inicioX][inicioY + i] = true;
            }
            else
            {
                campo[inicioX + i][inicioY] = true;
            }
        }
        System.out.println("Battleship created!");
    }

    public void checarAcerto(int linha, int coluna)
    {
        if (campo[linha][coluna] == true)
        {
            System.out.println("Acertou!");
        }

        // criar checagem de afundamento

        else
        {
            System.out.println("Errou!");
        }
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

}
