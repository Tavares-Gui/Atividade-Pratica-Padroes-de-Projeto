import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Batalha {
    private List<Personagem> timeA;
    private List<Personagem> timeB;
    private Random rnd = new Random();
    private int turno = 1;

    public Batalha(List<Personagem> timeA, List<Personagem> timeB) {
        this.timeA = new ArrayList<>(timeA);
        this.timeB = new ArrayList<>(timeB);
    }

    public List<Personagem> getOponentesDo(Personagem p) {
        if (timeA.contains(p)) return vivos(timeB);
        return vivos(timeA);
    }

    private List<Personagem> vivos(List<Personagem> team) {
        List<Personagem> vivos = new ArrayList<>();
        for (Personagem p : team) if (p.estaVivo()) vivos.add(p);
        return vivos;
    }

    public boolean venceuTimeA() { return vivos(timeA).size() > 0 && vivos(timeB).isEmpty(); }
    public boolean venceuTimeB() { return vivos(timeB).size() > 0 && vivos(timeA).isEmpty(); }
    public boolean terminou() { return venceuTimeA() || venceuTimeB(); }

    public boolean estaDesprevenido(Personagem alvo) {
        if (alvo.temEfeito(TipoEfeito.ATORDOADO)) return true;
        return rnd.nextDouble() < 0.5;
    }

    public boolean aplicarDano(Personagem atacante, Personagem alvo, int dano) {
        if (alvo.tentaEsquiva()) return false;
        int aumentoPercent = 0;
        for (EfeitoStatus e : alvo.efeitos) {
            if (e.getTipo() == TipoEfeito.DEFESA_REDUZIDA && !e.expirado()) {
                aumentoPercent += e.getPotencia();
            }
        }
        double danoAjustado = alvo.aplicarReducaoDano(dano);
        if (aumentoPercent > 0) danoAjustado *= (1.0 + aumentoPercent / 100.0);
        int danoFinal = (int)Math.round(danoAjustado);
        alvo.receberDano(danoFinal);
        return true;
    }

    public void executarTurnoInterativo(PlayerController playerController) {
        System.out.println("\n--- Turno " + turno + " ---");
        for (Personagem p : new ArrayList<>(vivos(timeA))) {
            if (!p.estaVivo()) continue;
            String logEfeitos = p.processarEfeitosInicioTurno();
            if (!logEfeitos.isEmpty()) System.out.println(logEfeitos);
            if (!p.estaVivo()) { System.out.println(p.getNome() + " morreu por efeitos."); continue; }
            if (p.temEfeito(TipoEfeito.ATORDOADO)) { System.out.println(p.getNome() + " está atordoado e perde o turno."); continue; }

            if (playerController.controla(p)) {
                playerController.acaoDoJogador(this, p);
            } else {
                List<Personagem> inimigos = getOponentesDo(p);
                if (!inimigos.isEmpty()) {
                    Personagem alvo = inimigos.get(rnd.nextInt(inimigos.size()));
                    System.out.println(p.agir(this, alvo));
                }
            }
            p.fimDoTurno();
            if (terminou()) return;
        }

        for (Personagem p : new ArrayList<>(vivos(timeB))) {
            if (!p.estaVivo()) continue;
            String logEfeitos = p.processarEfeitosInicioTurno();
            if (!logEfeitos.isEmpty()) System.out.println(logEfeitos);
            if (!p.estaVivo()) { System.out.println(p.getNome() + " morreu por efeitos."); continue; }
            if (p.temEfeito(TipoEfeito.ATORDOADO)) { System.out.println(p.getNome() + " está atordoado e perde o turno."); continue; }

            List<Personagem> inimigos = getOponentesDo(p);
            if (!inimigos.isEmpty()) {
                Personagem alvo = inimigos.get(rnd.nextInt(inimigos.size()));
                System.out.println(p.agir(this, alvo));
            }
            p.fimDoTurno();
            if (terminou()) return;
        }

        turno++;
    }
}
