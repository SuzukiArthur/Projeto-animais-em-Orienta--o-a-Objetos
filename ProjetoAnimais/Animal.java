package ProjetoAnimais;

/**
 * Classe base (ancestral) da hierarquia.
 * Demonstra Encapsulamento e Construtores (sobrecarga).
 * Todos os atributos são tipos primitivos ou String (sem relacionamentos entre
 * objetos).
 */
public class Animal {

    private String nome;
    private String especie;
    private int idade;
    private double peso;

    // Construtor padrão (sem parâmetros)
    public Animal() {
        this.nome = "Sem nome";
        this.especie = "Desconhecida";
        this.idade = 0;
        this.peso = 0.0;
    }

    // Construtor sobrecarregado (com todos os atributos)
    public Animal(String nome, String especie, int idade, double peso) {
        setNome(nome);
        setEspecie(especie);
        setIdade(idade);
        setPeso(peso);
    }

    // ----- Getters e Setters (com validações simples) -----

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = (nome == null || nome.trim().isEmpty()) ? "Sem nome" : nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = (especie == null || especie.trim().isEmpty()) ? "Desconhecida" : especie;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = (idade < 0) ? 0 : idade;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = (peso < 0) ? 0.0 : peso;
    }

    // Método para exibir informações no console
    public void exibirInformacoes() {
        System.out.println("Nome: " + nome +
                ", Espécie: " + especie +
                ", Idade: " + idade + " ano(s)" +
                ", Peso: " + peso + " kg");
    }

    // ----- Sobrecarga de método: calcularConsumoAlimentar -----

    // Versão 1: sem parâmetros (cálculo padrão: 2% do peso corporal por dia)
    public double calcularConsumoAlimentar() {
        return peso * 0.02;
    }

    // Versão 2: com um parâmetro (fator de atividade do animal)
    public double calcularConsumoAlimentar(double fatorAtividade) {
        return peso * 0.02 * fatorAtividade;
    }

    // Versão 3: com dois parâmetros (fator de atividade e fator de idade)
    public double calcularConsumoAlimentar(double fatorAtividade, double fatorIdade) {
        return peso * 0.02 * fatorAtividade * fatorIdade;
    }
}