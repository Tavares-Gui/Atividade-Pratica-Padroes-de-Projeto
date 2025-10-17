public class EfeitoStatus {
    private TipoEfeito tipo;
    private int duracao; // em turnos
    private int potencia; // dano por turno ou intensidade

    public EfeitoStatus(TipoEfeito tipo, int duracao, int potencia) {
        this.tipo = tipo;
        this.duracao = duracao;
        this.potencia = potencia;
    }

    public TipoEfeito getTipo() { return tipo; }
    public int getDuracao() { return duracao; }
    public int getPotencia() { return potencia; }

    public void reduzirDuracao() { duracao = Math.max(0, duracao - 1); }
    public boolean expirado() { return duracao <= 0; }

    @Override
    public String toString() {
        return tipo + " (potência: " + potencia + ", turnos restantes: " + duracao + ")";
    }
}
