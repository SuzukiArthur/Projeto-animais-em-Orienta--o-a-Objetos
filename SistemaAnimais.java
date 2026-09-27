import java.util.Scanner;

import ProjetoAnimais.Animal;
import ProjetoAnimais.Ave;
import ProjetoAnimais.Mamifero;

public class SistemaAnimais {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Sistema de Gerenciamento de Animais ===\n");

        // ---- Instanciação com construtor padrão ----
        Mamifero mamiferoPadrao = new Mamifero();
        Ave avePadrao = new Ave();

        // ---- Instanciação com construtor sobrecarregado ----
        Mamifero mamifero1 = new Mamifero("Rex", "Cachorro", 3, 28.5, 4, 0.0);
        Mamifero mamifero2 = new Mamifero("Mimosa", "Vaca", 5, 450.0, 4, 18.5);

        Ave ave1 = new Ave("Piu", "Canário", 1, 0.03, 0.18, true);
        Ave ave2 = new Ave("Peppa", "Avestruz", 4, 105.0, 2.0, false);

        // ---- Demonstração de Herança: tratando subclasses como o tipo base ----
        Animal[] animais = { mamiferoPadrao, avePadrao, mamifero1, mamifero2, ave1, ave2 };

        // ---- Demonstração de Encapsulamento: uso de setters e getters ----
        mamifero1.setPeso(29.0);
        mamifero1.setNumeroPatas(4);
        ave2.setCapacidadeVoo(false);
        System.out.println("Peso atualizado de " + mamifero1.getNome() + ": " + mamifero1.getPeso() + " kg");
        System.out.println("Espécie de " + ave2.getNome() + ": " + ave2.getEspecie() + "\n");

        // ---- Exibição das informações completas de cada animal ----
        System.out.println("--- Informações dos animais cadastrados ---");
        for (Animal a : animais) {
            a.exibirInformacoes();
            System.out.println("-----");
        }

        // ---- Demonstração de Sobrecarga de Métodos ----
        System.out.println("\n--- Cálculo de consumo alimentar (sobrecarga) ---");
        for (Animal a : animais) {
            double consumoBase = a.calcularConsumoAlimentar();
            double consumoComAtividade = a.calcularConsumoAlimentar(1.2);
            double consumoComAtividadeEIdade = a.calcularConsumoAlimentar(1.2, 0.9);

            System.out.println(a.getNome() + ":");
            System.out.println("  Consumo padrão: " + String.format("%.3f", consumoBase) + " kg/dia");
            System.out.println(
                    "  Consumo c/ fator de atividade: " + String.format("%.3f", consumoComAtividade) + " kg/dia");
            System.out.println("  Consumo c/ fator de atividade e idade: "
                    + String.format("%.3f", consumoComAtividadeEIdade) + " kg/dia");
        }

        // Scanner incluído para leitura via console, conforme exigido pela
        // especificação
        System.out.println("\nPressione ENTER para encerrar o programa...");
        scanner.nextLine();
        scanner.close();
    }
}
