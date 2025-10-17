public interface Arma {
    String getNome();
    int getDanoBase();
    int getCustoMana();
    boolean podeUsar(Personagem usuario);
    /**
     * Executa o ataque. Para armas de área, o parâmetro alvo pode ser ignorado.
     * Retorna um texto resumo do que aconteceu.
     */
    String atacar(Personagem atacante, Personagem alvo, Batalha batalha);
}
