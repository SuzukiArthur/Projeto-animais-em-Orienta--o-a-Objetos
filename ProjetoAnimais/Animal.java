package ProjetoAnimais;

public class Animal {
    private String nome;
    private double peso;
    private String especie;
    private int idade;

    public Animal(double peso, String nome, int idade, String especie) {
        this.peso = peso;
        this.nome = nome;
        this.idade = idade;
        this.especie = especie;
    }
}