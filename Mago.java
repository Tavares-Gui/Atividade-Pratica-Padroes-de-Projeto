public class Mago extends Personagem {

    public Mago(String nome) {
        super(nome, 5, 7, 18, 70, 150);
    }

    @Override
    public void fimDoTurno() {
        // Regeneração de mana: +10 por turno
        restaurarMana(10);
    }
}
