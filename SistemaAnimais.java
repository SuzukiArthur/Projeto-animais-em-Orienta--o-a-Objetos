import java.util.Scanner;
import java.util.ArrayList;

import ProjetoAnimais.Animal;
import ProjetoAnimais.Ave;
import ProjetoAnimais.Mamifero;

/**
 * Classe principal do sistema de gerenciamento de animais.
 * Usa Scanner para entrada de dados via console (cadastro interativo).
 * Demonstra: Encapsulamento, Construtores (sobrecarga), Herança e
 * Sobrecarga de métodos (polimorfismo básico).
 */
public class SistemaAnimais {

    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Animal> animais = new ArrayList<>();

    public static void main(String[] args) {

        // Pré-cadastro de alguns exemplos (demonstra os dois construtores)
        animais.add(new Mamifero()); // construtor padrão
        animais.add(new Ave()); // construtor padrão
        animais.add(new Mamifero("Rex", "Cachorro", 3, 28.5, 4, 0.0)); // sobrecarregado
        animais.add(new Mamifero("Mimosa", "Vaca", 5, 450.0, 4, 18.5)); // sobrecarregado
        animais.add(new Ave("Piu", "Canário", 1, 0.03, 0.18, true)); // sobrecarregado
        animais.add(new Ave("Peppa", "Avestruz", 4, 105.0, 2.0, false)); // sobrecarregado


        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    cadastrarMamifero();
                    break;
                case 2:
                    cadastrarAve();
                    break;
                case 3:
                    exibirTodos();
                    break;
                case 4:
                    calcularConsumoDeUmAnimal();
                    break;
                case 0:
                    System.out.println("Encerrando o sistema...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
            System.out.println();
        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n - Sistema de Gerenciamento de Animais -\n");
        System.out.println("1 - Cadastrar Mamífero");
        System.out.println("2 - Cadastrar Ave");
        System.out.println("3 - Exibir todos os animais cadastrados");
        System.out.println("4 - Calcular consumo alimentar de um animal");
        System.out.println("0 - Sair");
    }

    // ----- Cadastro via Scanner: cria Mamifero usando o construtor sobrecarregado
    // -----
    private static void cadastrarMamifero() {
        System.out.println("\n--- Cadastro de Mamífero ---");
        String nome = lerTexto("Nome: ");
        String especie = lerTexto("Espécie: ");
        int idade = lerInteiro("Idade (anos): ");
        double peso = lerDouble("Peso (kg): ");
        int patas = lerInteiro("Número de patas: ");
        double leite = lerDouble("Produção de leite diária (L, 0 se não produz): ");

        Mamifero novo = new Mamifero(nome, especie, idade, peso, patas, leite);
        animais.add(novo);
        System.out.println("Mamífero cadastrado com sucesso!");
    }

    // ----- Cadastro via Scanner: cria Ave usando o construtor sobrecarregado -----
    private static void cadastrarAve() {
        System.out.println("\n--- Cadastro de Ave ---");
        String nome = lerTexto("Nome: ");
        String especie = lerTexto("Espécie: ");
        int idade = lerInteiro("Idade (anos): ");
        double peso = lerDouble("Peso (kg): ");
        double envergadura = lerDouble("Envergadura das asas (m): ");
        boolean voa = lerBooleano("Tem capacidade de voo? (s/n): ");

        Ave nova = new Ave(nome, especie, idade, peso, envergadura, voa);
        animais.add(nova);
        System.out.println("Ave cadastrada com sucesso!");
    }

    // Demonstração de herança: percorre a lista tratando tudo como Animal (tipo
    // base)
    private static void exibirTodos() {
        System.out.println("\n--- Animais cadastrados (" + animais.size() + ") ---");
        for (int i = 0; i < animais.size(); i++) {
            System.out.println("[" + i + "]");
            animais.get(i).exibirInformacoes();
            System.out.println("-----");
        }
    }

    // Demonstração de sobrecarga de métodos: chama as 3 versões de
    // calcularConsumoAlimentar
    private static void calcularConsumoDeUmAnimal() {
        if (animais.isEmpty()) {
            System.out.println("Nenhum animal cadastrado ainda.");
            return;
        }
        exibirTodos();
        int indice = lerInteiro("Digite o índice do animal desejado: ");

        if (indice < 0 || indice >= animais.size()) {
            System.out.println("Índice inválido.");
            return;
        }

        Animal a = animais.get(indice);
        double fatorAtividade = lerDouble("Fator de atividade (ex.: 1.0 = normal, 1.2 = ativo): ");
        double fatorIdade = lerDouble("Fator de idade (ex.: 1.0 = adulto, 0.8 = idoso): ");

        System.out.println("\nResultados para " + a.getNome() + ":");
        System.out
                .println("  Versão sem parâmetros: " + String.format("%.3f", a.calcularConsumoAlimentar()) + " kg/dia");
        System.out.println("  Versão com 1 parâmetro: "
                + String.format("%.3f", a.calcularConsumoAlimentar(fatorAtividade)) + " kg/dia");
        System.out.println("  Versão com 2 parâmetros: "
                + String.format("%.3f", a.calcularConsumoAlimentar(fatorAtividade, fatorIdade)) + " kg/dia");
    }

    // ----- Métodos auxiliares de leitura via Scanner (com validação simples) -----

    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número inteiro.");
            }
        }
    }

    private static double lerDouble(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número (ex.: 12.5).");
            }
        }
    }

    private static boolean lerBooleano(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String resposta = scanner.nextLine().trim().toLowerCase();
            if (resposta.equals("s") || resposta.equals("sim"))
                return true;
            if (resposta.equals("n") || resposta.equals("nao") || resposta.equals("não"))
                return false;
            System.out.println("Resposta inválida. Digite 's' ou 'n'.");
        }
    }
}
