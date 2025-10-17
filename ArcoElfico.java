import java.util.List;

public class ArcoElfico extends ArmaAbstrata {

    public ArcoElfico() {
        super("Arco Élfico", 12, 15);
    }

    @Override
    public boolean podeUsar(Personagem usuario) {
        return super.podeUsar(usuario) && usuario.getDestreza() >= 8;
    }

    @Override
    public String atacar(Personagem atacante, Personagem alvoIgnorado, Batalha batalha) {
        if (atacante.getMana() < custoMana) return atacante.getNome() + " não tem mana para usar " + nome + ".";
        atacante.gastarMana(custoMana);

        StringBuilder sb = new StringBuilder();
        List<Personagem> inimigos = batalha.getOponentesDo(atacante);
        sb.append(atacante.getNome()).append(" usa ").append(nome).append(" -> Chuva de Flechas atingindo ")
          .append(inimigos.size()).append(" inimigo(s).\n");
        for (Personagem alvo : inimigos) {
            int dano = calcularDanoComCritico(atacante, danoBase);
            boolean acertou = batalha.aplicarDano(atacante, alvo, dano);
            sb.append(" - ").append(alvo.getNome()).append(" recebeu ").append(acertou ? dano + " de dano." : "esquiva.").append("\n");
        }
        return sb.toString();
    }
}
