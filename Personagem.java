import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Personagem {
    protected String nome;
    protected int forca;
    protected int destreza;
    protected int inteligencia;
    protected int vidaMax;
    protected int vidaAtual;
    protected int manaMax;
    protected int manaAtual;
    protected List<EfeitoStatus> efeitos = new ArrayList<>();
    protected Arma armaEquipada;

    public Personagem(String nome, int forca, int destreza, int inteligencia, int vida, int mana) {
        this.nome = nome;
        this.forca = forca;
        this.destreza = destreza;
        this.inteligencia = inteligencia;
        this.vidaMax = vida;
        this.vidaAtual = vida;
        this.manaMax = mana;
        this.manaAtual = mana;
    }

    public String getNome() { return nome; }
    public int getForca() { return forca; }
    public int getDestreza() { return destreza; }
    public int getInteligencia() { return inteligencia; }
    public int getVidaAtual() { return vidaAtual; }
    public int getMana() { return manaAtual; }
    public boolean estaVivo() { return vidaAtual > 0; }

    public void equiparArma(Arma arma) {
        if (!arma.podeUsar(this)) throw new IllegalArgumentException(nome + " não pode equipar " + arma.getNome());
        this.armaEquipada = arma;
    }

    public void gastarMana(int q) { manaAtual = Math.max(0, manaAtual - q); }
    public void restaurarMana(int q) { manaAtual = Math.min(manaMax, manaAtual + q); }

    public void receberDano(int q) { vidaAtual = Math.max(0, vidaAtual - q); }
    public void curar(int q) { vidaAtual = Math.min(vidaMax, vidaAtual + q); }

    public void adicionarEfeito(EfeitoStatus e) {
        efeitos.add(e);
        System.out.println(nome + " recebeu efeito: " + e);
    }

    public boolean temEfeito(TipoEfeito t) {
        for (EfeitoStatus e : efeitos) if (e.getTipo() == t && !e.expirado()) return true;
        return false;
    }

    // Processa efeitos no início do turno; retorna log do que aconteceu
    public String processarEfeitosInicioTurno() {
        StringBuilder sb = new StringBuilder();
        Iterator<EfeitoStatus> it = efeitos.iterator();
        while (it.hasNext()) {
            EfeitoStatus e = it.next();
            if (e.expirado()) { it.remove(); continue; }
            if (e.getTipo() == TipoEfeito.SANGRAMENTO) {
                receberDano(e.getPotencia());
                sb.append(nome).append(" sofreu SANGRAMENTO: ").append(e.getPotencia()).append(" de dano. ");
            } else if (e.getTipo() == TipoEfeito.QUEIMADURA) {
                receberDano(e.getPotencia());
                sb.append(nome).append(" sofreu QUEIMADURA: ").append(e.getPotencia()).append(" de dano. ");
            } else if (e.getTipo() == TipoEfeito.ATORDOADO) {
                sb.append(nome).append(" está ATORDOADO e perde o turno. ");
            } else if (e.getTipo() == TipoEfeito.DEFESA_REDUZIDA) {
                sb.append(nome).append(" tem DEFESA REDUZIDA por ").append(e.getDuracao()).append(" turno(s). ");
            }
            e.reduzirDuracao();
            if (e.expirado()) it.remove();
        }
        return sb.toString();
    }

    // Hook para passivas, pode ser sobrescrito
    public double aplicarReducaoDano(double dano) {
        return dano; // por padrão sem redução
    }

    // Chance de esquiva (ex.: arqueiro)
    public boolean tentaEsquiva() { return false; }

    // Hook para fim de turno (ex.: regen do Mago)
    public void fimDoTurno() {}

    // Checa se tem arma equipada
    public boolean temArma() { return armaEquipada != null; }

    // Ação padrão: usa arma equipada
    public String agir(Batalha batalha, Personagem alvo) {
        if (!estaVivo()) return nome + " está morto e não pode agir.";
        if (temEfeito(TipoEfeito.ATORDOADO)) {
            return nome + " está atordoado e perde o turno.";
        }
        if (armaEquipada == null) return nome + " não tem arma equipada.";
        return armaEquipada.atacar(this, alvo, batalha);
    }

    // resumo para interface
    public String resumo() {
        return String.format("%s - HP: %d/%d | Mana: %d/%d | Força: %d Destreza: %d Inteligência: %d",
                nome, vidaAtual, vidaMax, manaAtual, manaMax, forca, destreza, inteligencia);
    }
}
