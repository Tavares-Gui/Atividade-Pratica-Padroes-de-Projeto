import java.util.Random;

public class AdagaSombria extends ArmaAbstrata {
    private Random rnd = new Random();

    public AdagaSombria() {
        super("Adaga Sombria", 10, 10);
    }

    @Override
    public boolean podeUsar(Personagem usuario) {
        return super.podeUsar(usuario) && usuario.getDestreza() >= 12;
    }

    @Override
    public String atacar(Personagem atacante, Personagem alvo, Batalha batalha) {
        if (atacante.getMana() < custoMana) return atacante.getNome() + " não tem mana para usar " + nome + ".";
        atacante.gastarMana(custoMana);

        boolean desprevenido = batalha.estaDesprevenido(alvo);
        int dano;
        StringBuilder sb = new StringBuilder();
        if (desprevenido) {
            dano = calcularDanoComCritico(atacante, danoBase * 3);
            sb.append("Ataque Furtivo! ");
        } else {
            dano = calcularDanoComCritico(atacante, danoBase);
        }
        sb.append(atacante.getNome()).append(" ataca com ").append(nome).append(" em ").append(alvo.getNome())
          .append(", causando ").append(dano).append(" de dano. ");
        boolean acertou = batalha.aplicarDano(atacante, alvo, dano);
        if (!acertou) sb.append("O alvo esquivou-se.");
        return sb.toString();
    }
}
