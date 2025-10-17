public class AlabardaCristalina extends ArmaAbstrata {

    public AlabardaCristalina() {
        super("Alabarda Cristalina", 14, 10);
    }

    @Override
    public boolean podeUsar(Personagem usuario) {
        return super.podeUsar(usuario) && usuario.getForca() >= 12;
    }

    @Override
    public String atacar(Personagem atacante, Personagem alvo, Batalha batalha) {
        if (atacante.getMana() < custoMana) return atacante.getNome() + " não tem mana para usar " + nome + ".";
        atacante.gastarMana(custoMana);

        int dano = calcularDanoComCritico(atacante, danoBase);
        StringBuilder sb = new StringBuilder();
        sb.append(atacante.getNome()).append(" golpeia ").append(alvo.getNome()).append(", causando ").append(dano).append(" de dano. ");
        boolean acertou = batalha.aplicarDano(atacante, alvo, dano);
        if (!acertou) {
            sb.append("O alvo esquivou-se.");
            return sb.toString();
        }
        // Aplica efeito de redução de defesa por 2 turnos (potência: 20 significa que o alvo receberá +20% de dano)
        alvo.adicionarEfeito(new EfeitoStatus(TipoEfeito.DEFESA_REDUZIDA, 2, 20));
        sb.append("Ressonância aplicada: defesa reduzida (-20%) por 2 turnos.");
        return sb.toString();
    }
}
