import java.util.Random;

public class Arqueiro extends Personagem {
    private Random rnd = new Random();

    public Arqueiro(String nome) {
        super(nome, 8, 15, 7, 90, 80);
    }

    @Override
    public boolean tentaEsquiva() {
        // Esquiva: 25% de chance de evitar um ataque
        return rnd.nextDouble() < 0.25;
    }
}
