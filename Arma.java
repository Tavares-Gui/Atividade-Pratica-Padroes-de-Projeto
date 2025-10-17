public interface Arma {
    String getNome();
    int getDanoBase();
    int getCustoMana();
    boolean podeUsar(Personagem usuario);
    String atacar(Personagem atacante, Personagem alvo, Batalha batalha);
}
