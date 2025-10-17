import java.util.Random;

public abstract class ArmaAbstrata implements Arma {
    protected String nome;
    protected int danoBase;
    protected int custoMana;
    protected Random rnd = new Random();

    public ArmaAbstrata(String nome, int danoBase, int custoMana) {
        this.nome = nome;
        this.danoBase = danoBase;
        this.custoMana = custoMana;
    }

    public String getNome() { return nome; }
    public int getDanoBase() { return danoBase; }
    public int getCustoMana() { return custoMana; }

    public boolean podeUsar(Personagem usuario) {
        return usuario.getMana() >= custoMana;
    }

    protected double chanceCritico(Personagem atacante) {
        return 0.10 + atacante.getDestreza() * 0.005;
    }

    protected int calcularDanoComCritico(Personagem atacante, int base) {
        double chance = chanceCritico(atacante);
        if (rnd.nextDouble() < chance) {
            double mult = 1.5 + rnd.nextDouble() * 0.5;
            return (int)Math.round(base * mult);
        }
        return base;
    }
}
