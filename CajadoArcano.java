public class CajadoArcano extends ArmaAbstrata {

    public CajadoArcano() {
        super("Cajado Arcano", 8, 25);
    }

    @Override
    public boolean podeUsar(Personagem usuario) {
        return super.podeUsar(usuario) && usuario.getInteligencia() >= 12;
    }

    @Override
    public String atacar(Personagem atacante, Personagem alvo, Batalha batalha) {
        if (atacante.getMana() < custoMana) return atacante.getNome() + " não tem mana para usar " + nome + ".";
        atacante.gastarMana(custoMana);

        int dano = calcularDanoComCritico(atacante, danoBase);
        StringBuilder sb = new StringBuilder();
        sb.append(atacante.getNome()).append(" conjura Bola de Fogo em ").append(alvo.getNome())
          .append(", causando ").append(dano).append(" de dano. ");
        boolean acertou = batalha.aplicarDano(atacante, alvo, dano);
        if (!acertou) {
            sb.append("O alvo esquivou-se.");
            return sb.toString();
        }
        alvo.adicionarEfeito(new EfeitoStatus(TipoEfeito.QUEIMADURA, 2, 10));
        sb.append("Aplicou QUEIMADURA (10 por 2 turnos).");
        return sb.toString();
    }
}
