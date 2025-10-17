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

    // heurística para desprevenido: 50% de chance se não estiver ATORDOADO
    public boolean estaDesprevenido(Personagem alvo) {
        if (alvo.temEfeito(TipoEfeito.ATORDOADO)) return true;
        return rnd.nextDouble() < 0.5;
    }

    // Aplica dano considerando esquiva e reduções; retorna true se dano foi aplicado (não esquivou)
    public boolean aplicarDano(Personagem atacante, Personagem alvo, int dano) {
        if (alvo.tentaEsquiva()) return false;
        // soma modificadores de defesa reduzida
        int aumentoPercent = 0;
        for (EfeitoStatus e : alvo.efeitos) {
            if (e.getTipo() == TipoEfeito.DEFESA_REDUZIDA && !e.expirado()) {
                aumentoPercent += e.getPotencia(); // ex.: 20 significa +20% de dano recebido
            }
        }
        double danoAjustado = alvo.aplicarReducaoDano(dano);
        if (aumentoPercent > 0) danoAjustado *= (1.0 + aumentoPercent / 100.0);
        int danoFinal = (int)Math.round(danoAjustado);
        alvo.receberDano(danoFinal);
        return true;
    }

    // executa a batalha de forma interativa: o controle do Player deve chamar os métodos de escolha.
    // Aqui centralizamos um "passo" que processa todos os personagens na ordem: timeA então timeB.
    public void executarTurnoInterativo(PlayerController playerController) {
        System.out.println("\n--- Turno " + turno + " ---");
        // Processar time A
        for (Personagem p : new ArrayList<>(vivos(timeA))) {
            if (!p.estaVivo()) continue;
            String logEfeitos = p.processarEfeitosInicioTurno();
            if (!logEfeitos.isEmpty()) System.out.println(logEfeitos);
            if (!p.estaVivo()) { System.out.println(p.getNome() + " morreu por efeitos."); continue; }
            if (p.temEfeito(TipoEfeito.ATORDOADO)) { System.out.println(p.getNome() + " está atordoado e perde o turno."); continue; }

            // se personagem for controlado pelo jogador, pede ação ao PlayerController
            if (playerController.controla(p)) {
                playerController.acaoDoJogador(this, p);
            } else {
                // IA simplificada para aliados não-jogadores: ataca o primeiro inimigo vivo
                List<Personagem> inimigos = getOponentesDo(p);
                if (!inimigos.isEmpty()) {
                    Personagem alvo = inimigos.get(rnd.nextInt(inimigos.size()));
                    System.out.println(p.agir(this, alvo));
                }
            }
            p.fimDoTurno();
            if (terminou()) return;
        }

        // Processar time B (inimigos com IA)
        for (Personagem p : new ArrayList<>(vivos(timeB))) {
            if (!p.estaVivo()) continue;
            String logEfeitos = p.processarEfeitosInicioTurno();
            if (!logEfeitos.isEmpty()) System.out.println(logEfeitos);
            if (!p.estaVivo()) { System.out.println(p.getNome() + " morreu por efeitos."); continue; }
            if (p.temEfeito(TipoEfeito.ATORDOADO)) { System.out.println(p.getNome() + " está atordoado e perde o turno."); continue; }

            // IA inimiga: tenta usar arma se tiver, senão troca para uma arma válida (não implementa inventário)
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
