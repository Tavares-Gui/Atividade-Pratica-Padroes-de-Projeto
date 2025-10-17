public class Guerreiro extends Personagem {
    public Guerreiro(String nome) {
        super(nome, 15, 8, 5, 120, 50);
    }

    @Override
    public double aplicarReducaoDano(double dano) {
        // Pele Dura: reduz dano recebido em 20%
        return dano * 0.8;
    }
}
