public class Paladino extends Personagem {
    public Paladino(String nome) {
        super(nome, 13, 10, 12, 110, 100);
    }

    @Override
    public double aplicarReducaoDano(double dano) {
        return dano * 0.9;
    }

    @Override
    public void fimDoTurno() {
        restaurarMana(5);
    }
}
