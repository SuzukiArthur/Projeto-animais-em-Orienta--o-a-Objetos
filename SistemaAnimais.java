// Alice Miranda Martins //
// Arthur Souza Ribeiro //
// Arthur Yuji Mendes Suzuki //
// Felipe Souza de Jesus //
// Luiz Miguel Mazzega Lamas  //

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import ProjetoAnimais.Animal;
import ProjetoAnimais.Ave;
import ProjetoAnimais.Mamifero;
import ProjetoAnimais.TipoAnimal;
import ProjetoAnimais.Tutor;
import ProjetoAnimais.excecao.PersistenciaException;
import ProjetoAnimais.excecao.RecursoNaoEncontradoException;
import ProjetoAnimais.repositorio.RepositorioAnimal;
import ProjetoAnimais.repositorio.RepositorioTutor;
import ProjetoAnimais.repositorio.jdbc.AnimalRepositorioJdbc;
import ProjetoAnimais.repositorio.jdbc.ConexaoSQLite;
import ProjetoAnimais.repositorio.jdbc.TutorRepositorioJdbc;

public class SistemaAnimais {

    private static final Scanner scanner = new Scanner(System.in);
    private static final RepositorioTutor tutores = new TutorRepositorioJdbc();
    private static final RepositorioAnimal animais = new AnimalRepositorioJdbc();

    public static void main(String[] args) {

        try {
            ConexaoSQLite.criarTabelas();
        } catch (PersistenciaException e) {
            System.out.println("Não foi possível preparar o banco: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Detalhe: " + e.getCause().getMessage());
            }
            System.out.println("Verifique se o driver sqlite-jdbc está no classpath (veja o README.md).");
            return;
        }

        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1 -> cadastrarTutor();
                    case 2 -> listarTutores();
                    case 3 -> atualizarTutor();
                    case 4 -> removerTutor();
                    case 5 -> cadastrarAnimal();
                    case 6 -> listarAnimais();
                    case 7 -> atualizarAnimal();
                    case 8 -> removerAnimal();
                    case 9 -> calcularConsumoDeUmAnimal();
                    case 10 -> relatorio();
                    case 0 -> System.out.println("Encerrando o sistema...");
                    default -> System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (RecursoNaoEncontradoException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (PersistenciaException e) {
                System.out.println("Erro de persistência: " + e.getMessage());
                if (e.getCause() != null) {
                    System.out.println("Detalhe: " + e.getCause().getMessage());
                }
            }
            System.out.println();
        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n - Sistema de Gerenciamento de Animais -\n");
        System.out.println("1 - Cadastrar tutor");
        System.out.println("2 - Listar tutores");
        System.out.println("3 - Atualizar tutor");
        System.out.println("4 - Remover tutor");
        System.out.println("5 - Cadastrar animal (Mamífero ou Ave)");
        System.out.println("6 - Listar animais");
        System.out.println("7 - Atualizar animal");
        System.out.println("8 - Remover animal");
        System.out.println("9 - Calcular consumo alimentar de um animal");
        System.out.println("10 - Relatório");
        System.out.println("0 - Sair");
    }

    // ================= CRUD: Tutor =================

    private static void cadastrarTutor() throws PersistenciaException {
        System.out.println("\n Cadastro de Tutor ");
        String nome = lerTexto("Nome: ");
        String telefone = lerTexto("Telefone: ");

        int id = tutores.salvar(new Tutor(nome, telefone));
        System.out.println("Tutor cadastrado com sucesso! ID: " + id);
    }

    private static void listarTutores() throws PersistenciaException {
        List<Tutor> lista = tutores.listarTodos();
        System.out.println("\nTutores cadastrados (" + lista.size() + ")");
        for (Tutor t : lista) {
            t.exibirInformacoes();
        }
    }

    private static void atualizarTutor() throws RecursoNaoEncontradoException, PersistenciaException {
        listarTutores();
        int id = lerInteiro("ID do tutor a atualizar: ");

        Tutor t = tutores.buscarPorId(id);
        t.setNome(lerComPadrao("Nome", t.getNome()));
        t.setTelefone(lerComPadrao("Telefone", t.getTelefone()));

        tutores.atualizar(t);
        System.out.println("Tutor atualizado com sucesso!");
    }

    private static void removerTutor() throws RecursoNaoEncontradoException, PersistenciaException {
        listarTutores();
        int id = lerInteiro("ID do tutor a remover: ");

        Tutor t = tutores.buscarPorId(id);
        int qtdAnimais = animais.listarPorTutor(id).size();
        if (qtdAnimais > 0) {
            System.out.println("Atenção: " + qtdAnimais + " animal(is) deste tutor também serão removidos.");
        }
        if (!lerBooleano("Confirmar remoção de " + t.getNome() + "? (s/n): ")) {
            System.out.println("Operação cancelada.");
            return;
        }

        tutores.remover(id);
        System.out.println("Tutor removido com sucesso!");
    }

    // ================= CRUD: Animal =================

    private static void cadastrarAnimal() throws RecursoNaoEncontradoException, PersistenciaException {
        if (tutores.listarTodos().isEmpty()) {
            System.out.println("Cadastre ao menos um tutor antes de cadastrar animais.");
            return;
        }

        listarTutores();
        int tutorId = lerInteiro("ID do tutor responsável: ");
        tutores.buscarPorId(tutorId); // lança RecursoNaoEncontradoException se não existir

        TipoAnimal tipo = escolherTipo();
        if (tipo == null) {
            System.out.println("Tipo inválido.");
            return;
        }

        System.out.println("\n Cadastro de " + tipo.getDescricao() + " ");
        String nome = lerTexto("Nome: ");
        String especie = lerTexto("Espécie: ");
        int idade = lerInteiro("Idade (anos): ");
        double peso = lerDouble("Peso (kg): ");

        Animal novo;
        if (tipo == TipoAnimal.MAMIFERO) {
            int patas = lerInteiro("Número de patas: ");
            double leite = lerDouble("Produção de leite diária (L, 0 se não produz): ");
            novo = new Mamifero(nome, especie, idade, peso, patas, leite);
        } else {
            double envergadura = lerDouble("Envergadura das asas (m): ");
            boolean voa = lerBooleano("Tem capacidade de voo? (s/n): ");
            novo = new Ave(nome, especie, idade, peso, envergadura, voa);
        }
        novo.setTutorId(tutorId);

        int id = animais.salvar(novo);
        System.out.println(tipo.getDescricao() + " cadastrado com sucesso! ID: " + id);
    }

    private static void listarAnimais() throws PersistenciaException {
        exibirLista(animais.listarTodos());
    }

    private static void atualizarAnimal() throws RecursoNaoEncontradoException, PersistenciaException {
        exibirLista(animais.listarTodos());
        int id = lerInteiro("ID do animal a atualizar: ");

        Animal a = animais.buscarPorId(id);
        a.setNome(lerComPadrao("Nome", a.getNome()));
        a.setEspecie(lerComPadrao("Espécie", a.getEspecie()));
        a.setIdade(lerIntComPadrao("Idade (anos)", a.getIdade()));
        a.setPeso(lerDoubleComPadrao("Peso (kg)", a.getPeso()));

        if (a instanceof Mamifero m) {
            m.setNumeroPatas(lerIntComPadrao("Número de patas", m.getNumeroPatas()));
            m.setProducaoLeiteDiaria(lerDoubleComPadrao("Produção de leite diária (L)", m.getProducaoLeiteDiaria()));
        } else if (a instanceof Ave v) {
            v.setEnvergaduraAsas(lerDoubleComPadrao("Envergadura das asas (m)", v.getEnvergaduraAsas()));
            v.setCapacidadeVoo(lerBooleanoComPadrao("Tem capacidade de voo", v.isCapacidadeVoo()));
        }

        animais.atualizar(a);
        System.out.println("Animal atualizado com sucesso!");
    }

    private static void removerAnimal() throws RecursoNaoEncontradoException, PersistenciaException {
        exibirLista(animais.listarTodos());
        int id = lerInteiro("ID do animal a remover: ");

        Animal a = animais.buscarPorId(id);
        if (!lerBooleano("Confirmar remoção de " + a.getNome() + "? (s/n): ")) {
            System.out.println("Operação cancelada.");
            return;
        }

        animais.remover(id);
        System.out.println("Animal removido com sucesso!");
    }

    // Demonstração de herança: a lista contém Animal, mas cada item se comporta como Mamifero ou Ave //
    private static void exibirLista(List<Animal> lista) {
        System.out.println("\nAnimais cadastrados (" + lista.size() + ")");
        for (Animal a : lista) {
            System.out.println("[ID " + a.getId() + "] Tutor ID " + a.getTutorId() + " | " + a.getTipo().getDescricao());
            a.exibirInformacoes();
        }
    }

    // calcularConsumoAlimentar (sobrecarga de métodos) //
    private static void calcularConsumoDeUmAnimal() throws PersistenciaException, RecursoNaoEncontradoException {
        List<Animal> lista = animais.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum animal cadastrado ainda.");
            return;
        }
        exibirLista(lista);
        int id = lerInteiro("Digite o ID do animal desejado: ");

        Animal a = animais.buscarPorId(id);
        double fatorAtividade = lerDouble("Fator de atividade (ex: 1.0 = normal, 1.2 = ativo): ");
        double fatorIdade = lerDouble("Fator de idade (ex: 1.0 = adulto, 0.8 = idoso): ");

        System.out.println("\nResultados para " + a.getNome() + ":");
        System.out.println("  Versão sem parâmetros: " + String.format("%.3f", a.calcularConsumoAlimentar()) + " kg/dia");
        System.out.println("  Versão com 1 parâmetro: "
                + String.format("%.3f", a.calcularConsumoAlimentar(fatorAtividade)) + " kg/dia");
        System.out.println("  Versão com 2 parâmetros: "
                + String.format("%.3f", a.calcularConsumoAlimentar(fatorAtividade, fatorIdade)) + " kg/dia");
    }

    // ================= Relatório (Collections) =================

    private static void relatorio() throws PersistenciaException {
        List<Animal> todosAnimais = animais.listarTodos();
        List<Tutor> todosTutores = tutores.listarTodos();

        Map<TipoAnimal, Integer> porTipo = new EnumMap<>(TipoAnimal.class);
        Map<String, Integer> porEspecie = new TreeMap<>();
        Set<String> especiesDistintas = new TreeSet<>();
        Set<Integer> tutoresComAnimais = new HashSet<>();
        double consumoTotal = 0.0;

        for (Animal a : todosAnimais) {
            porTipo.merge(a.getTipo(), 1, Integer::sum);
            porEspecie.merge(a.getEspecie(), 1, Integer::sum);
            especiesDistintas.add(a.getEspecie());
            tutoresComAnimais.add(a.getTutorId());
            consumoTotal += a.calcularConsumoAlimentar();
        }

        List<String> tutoresSemAnimais = new ArrayList<>();
        for (Tutor t : todosTutores) {
            if (!tutoresComAnimais.contains(t.getId())) {
                tutoresSemAnimais.add(t.getNome());
            }
        }

        System.out.println("\nRelatório");
        System.out.println("Total de tutores: " + todosTutores.size());
        System.out.println("Total de animais: " + todosAnimais.size());
        for (TipoAnimal tipo : TipoAnimal.values()) {
            System.out.println("  " + tipo.getDescricao() + "s: " + porTipo.getOrDefault(tipo, 0));
        }
        System.out.println("Espécies distintas (" + especiesDistintas.size() + "): " + especiesDistintas);
        System.out.println("Quantidade por espécie: " + porEspecie);
        System.out.println("Consumo alimentar total estimado: " + String.format("%.3f", consumoTotal) + " kg/dia");
        System.out.println("Tutores sem animais: " + (tutoresSemAnimais.isEmpty() ? "nenhum" : tutoresSemAnimais));
        System.out.println("\nJustificativa: o sistema persiste duas entidades ligadas em 1:N (um tutor possui vários animais). "
                + "A hierarquia Animal, Mamífero e Ave é gravada em uma única tabela, com a coluna 'tipo' "
                + "indicando a subclasse. O acesso ao banco é feito com JDBC puro, por meio de interfaces de repositório.");
    }

    // ================= Auxiliares de entrada =================

    private static TipoAnimal escolherTipo() {
        TipoAnimal[] tipos = TipoAnimal.values();
        System.out.println("Tipos disponíveis:");
        for (int i = 0; i < tipos.length; i++) {
            System.out.println((i + 1) + " - " + tipos[i].getDescricao());
        }
        int op = lerInteiro("Escolha o tipo: ");
        if (op < 1 || op > tipos.length) {
            return null;
        }
        return tipos[op - 1];
    }

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

    // Na atualização: Enter mantém o valor atual //

    private static String lerComPadrao(String rotulo, String atual) {
        System.out.print(rotulo + " [" + atual + "]: ");
        String entrada = scanner.nextLine().trim();
        return entrada.isEmpty() ? atual : entrada;
    }

    private static int lerIntComPadrao(String rotulo, int atual) {
        while (true) {
            try {
                return Integer.parseInt(lerComPadrao(rotulo, String.valueOf(atual)));
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número inteiro.");
            }
        }
    }

    private static double lerDoubleComPadrao(String rotulo, double atual) {
        while (true) {
            try {
                return Double.parseDouble(lerComPadrao(rotulo, String.valueOf(atual)).replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número (ex.: 12.5).");
            }
        }
    }

    private static boolean lerBooleanoComPadrao(String rotulo, boolean atual) {
        while (true) {
            String resposta = lerComPadrao(rotulo + " (s/n)", atual ? "s" : "n").toLowerCase();
            if (resposta.equals("s") || resposta.equals("sim"))
                return true;
            if (resposta.equals("n") || resposta.equals("nao") || resposta.equals("não"))
                return false;
            System.out.println("Resposta inválida. Digite 's' ou 'n'.");
        }
    }
}
