import java.util.List;
import java.util.Scanner;

public class PlayerController {
    private Scanner sc;
    private List<Personagem> personagensControlados;

    public PlayerController(Scanner sc, List<Personagem> personagensControlados) {
        this.sc = sc;
        this.personagensControlados = personagensControlados;
    }

    public boolean controla(Personagem p) {
        return personagensControlados.contains(p);
    }

    public void acaoDoJogador(Batalha batalha, Personagem p) {
        if (!p.estaVivo()) return;
        System.out.println("\nÉ a vez de: " + p.resumo());
        System.out.println("Efeitos ativos: " + (p.efeitos.isEmpty() ? "Nenhum" : p.efeitos));
        boolean fimAcao = false;
        while (!fimAcao) {
            System.out.println("Escolha uma ação:");
            System.out.println("1) Atacar");
            System.out.println("2) Trocar arma");
            System.out.println("3) Ver status do time inimigo");
            System.out.println("4) Pular (passar turno)");
            System.out.print("> ");
            String opt = sc.nextLine().trim();
            switch (opt) {
                case "1":
                    List<Personagem> inimigos = batalha.getOponentesDo(p);
                    if (inimigos.isEmpty()) { System.out.println("Não há inimigos vivos."); fimAcao = true; break; }
                    System.out.println("Escolha o alvo:");
                    for (int i = 0; i < inimigos.size(); i++) {
                        System.out.println((i+1) + ") " + inimigos.get(i).resumo());
                    }
                    System.out.print("> ");
                    int idx = lerInteiro(sc, 1, inimigos.size()) - 1;
                    Personagem alvo = inimigos.get(idx);
                    System.out.println(p.agir(batalha, alvo));
                    fimAcao = true;
                    break;
                case "2":
                    System.out.println("Armas disponíveis para equipar:");
                    Arma[] todas = new Arma[] {
                        new EspadaLonga(), new MachadoGuerra(), new ArcoElfico(), new CajadoArcano(), new AdagaSombria(), new AlabardaCristalina()
                    };
                    for (int i = 0; i < todas.length; i++) {
                        System.out.println((i+1) + ") " + todas[i].getNome() + " (Dano " + todas[i].getDanoBase() + ", Mana " + todas[i].getCustoMana() + ")");
                    }
                    System.out.print("> ");
                    int escolha = lerInteiro(sc, 1, todas.length) - 1;
                    try {
                        p.equiparArma(todas[escolha]);
                        System.out.println(p.getNome() + " equipou " + todas[escolha].getNome());
                        fimAcao = true;
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Não foi possível equipar: " + ex.getMessage());
                    }
                    break;
                case "3":
                    List<Personagem> inimigos2 = batalha.getOponentesDo(p);
                    System.out.println("Inimigos vivos:");
                    for (Personagem ip : inimigos2) System.out.println(" - " + ip.resumo());
                    break;
                case "4":
                    System.out.println(p.getNome() + " passa o turno.");
                    fimAcao = true;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private int lerInteiro(Scanner sc, int min, int max) {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (v < min || v > max) throw new NumberFormatException();
                return v;
            } catch (NumberFormatException ex) {
                System.out.print("Entrada inválida. Digite um número entre " + min + " e " + max + ": ");
            }
        }
    }
}
