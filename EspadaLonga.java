import java.util.Random;

public class EspadaLonga extends ArmaAbstrata {
    private Random rnd = new Random();

    public EspadaLonga() {
        super("Espada Longa", 15, 0);
    }

    @Override
    public boolean podeUsar(Personagem usuario) {
        return super.podeUsar(usuario) && usuario.getForca() >= 10;
    }

    @Override
    public String atacar(Personagem atacante, Personagem alvo, Batalha batalha) {
        if (atacante.getMana() < custoMana) return atacante.getNome() + " não tem mana para usar " + nome + ".";
        atacante.gastarMana(custoMana);

        int danoBase = calcularDanoComCritico(atacante, danoBase());
        StringBuilder sb = new StringBuilder();
        sb.append(atacante.getNome()).append(" ataca com ").append(nome).append(" em ").append(alvo.getNome())
          .append(", causando ").append(danoBase).append(" de dano. ");

        boolean acertou = batalha.aplicarDano(atacante, alvo, danoBase);
        if (!acertou) {
            sb.append("O alvo esquivou-se.");
            return sb.toString();
        }
        if (rnd.nextDouble() < 0.30) {
            alvo.adicionarEfeito(new EfeitoStatus(TipoEfeito.SANGRAMENTO, 3, 5));
            sb.append("Causou SANGRAMENTO (5 por 3 turnos).");
        }
        return sb.toString();
    }

    private int danoBase() { return super.danoBase; }
}
