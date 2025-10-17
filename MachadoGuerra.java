import java.util.Random;

public class MachadoGuerra extends ArmaAbstrata {
    private Random rnd = new Random();

    public MachadoGuerra() {
        super("Machado de Guerra", 18, 5);
    }

    @Override
    public boolean podeUsar(Personagem usuario) {
        return super.podeUsar(usuario) && usuario.getForca() >= 15;
    }

    @Override
    public String atacar(Personagem atacante, Personagem alvo, Batalha batalha) {
        if (atacante.getMana() < custoMana) return atacante.getNome() + " não tem mana para usar " + nome + ".";
        atacante.gastarMana(custoMana);

        int dano = calcularDanoComCritico(atacante, danoBase);
        StringBuilder sb = new StringBuilder();
        sb.append(atacante.getNome()).append(" desfere ").append(nome).append(" em ").append(alvo.getNome())
          .append(", causando ").append(dano).append(" de dano. ");
        boolean acertou = batalha.aplicarDano(atacante, alvo, dano);
        if (!acertou) {
            sb.append("O alvo esquivou-se.");
            return sb.toString();
        }
        if (rnd.nextDouble() < 0.25) {
            alvo.adicionarEfeito(new EfeitoStatus(TipoEfeito.ATORDOADO, 1, 0));
            sb.append("Atordoou o inimigo!");
        }
        return sb.toString();
    }
}
