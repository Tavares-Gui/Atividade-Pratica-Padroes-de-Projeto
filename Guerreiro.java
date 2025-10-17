public class Guerreiro extends Personagem {
    public Guerreiro(String nome) {
        super(nome, 15, 8, 5, 120, 50);
    }

    @Override
    public double aplicarReducaoDano(double dano) {
        return dano * 0.8;
    }
}
