import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Cria personagens jogáveis (Time A)
        Guerreiro g = new Guerreiro("Thorin");
        Arqueiro a = new Arqueiro("Elandril");
        Mago m = new Mago("Meriana");

        // Cria inimigos (Time B)
        Paladino inimigo1 = new Paladino("Sir Malrick");
        Guerreiro inimigo2 = new Guerreiro("Brudok");

        // Equipa armas iniciais (tentativa, pode falhar caso requisitos não batam)
        try { g.equiparArma(new EspadaLonga()); } catch (Exception e) {}
        try { a.equiparArma(new ArcoElfico()); } catch (Exception e) {}
        try { m.equiparArma(new CajadoArcano()); } catch (Exception e) {}
        try { inimigo1.equiparArma(new AlabardaCristalina()); } catch (Exception e) {}
        try { inimigo2.equiparArma(new MachadoGuerra()); } catch (Exception e) {}

        List<Personagem> timeA = Arrays.asList(g, a, m);
        List<Personagem> timeB = Arrays.asList(inimigo1, inimigo2);

        Batalha batalha = new Batalha(timeA, timeB);

        // Player controla todos do time A neste exemplo
        PlayerController playerController = new PlayerController(sc, timeA);

        System.out.println("=== Batalha Iniciada (Modo Interativo) ===");
        System.out.println("Time do Jogador:");
        for (Personagem p : timeA) System.out.println(" - " + p.resumo());
        System.out.println("\nInimigos:");
        for (Personagem p : timeB) System.out.println(" - " + p.resumo());

        // loop principal
        while (!batalha.terminou()) {
            batalha.executarTurnoInterativo(playerController);
        }

        System.out.println("\n=== Batalha Encerrada ===");
        if (batalha.venceuTimeA()) {
            System.out.println("Parabéns! Time do jogador venceu!");
        } else if (batalha.venceuTimeB()) {
            System.out.println("Derrota. Os inimigos venceram.");
        } else {
            System.out.println("Batalha terminou sem vencedores claros.");
        }

        sc.close();
    }
}
