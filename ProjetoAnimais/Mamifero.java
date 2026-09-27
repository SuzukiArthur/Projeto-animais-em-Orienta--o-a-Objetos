package ProjetoAnimais;

/**
 * Subclasse 1 da hierarquia "Animal".
 * Demonstra Herança (extends) e sobrescrita de método.
 */
public class Mamifero extends Animal {

    private int numeroPatas;
    private double producaoLeiteDiaria; // em litros

    // Construtor padrão - chama super()
    public Mamifero() {
        super();
        this.numeroPatas = 4;
        this.producaoLeiteDiaria = 0.0;
    }

    // Construtor sobrecarregado - chama super(...) e inicializa atributos extras
    public Mamifero(String nome, String especie, int idade, double peso,
            int numeroPatas, double producaoLeiteDiaria) {
        super(nome, especie, idade, peso);
        setNumeroPatas(numeroPatas);
        setProducaoLeiteDiaria(producaoLeiteDiaria);
    }

    public int getNumeroPatas() {
        return numeroPatas;
    }

    public void setNumeroPatas(int numeroPatas) {
        this.numeroPatas = (numeroPatas < 0) ? 0 : numeroPatas;
    }

    public double getProducaoLeiteDiaria() {
        return producaoLeiteDiaria;
    }

    public void setProducaoLeiteDiaria(double producaoLeiteDiaria) {
        this.producaoLeiteDiaria = (producaoLeiteDiaria < 0) ? 0.0 : producaoLeiteDiaria;
    }

    // Sobrescrita de exibirInformacoes(), reutilizando o método da classe base
    @Override
    public void exibirInformacoes() {
        super.exibirInformacoes();
        System.out.println("Número de patas: " + numeroPatas +
                ", Produção de leite diária: " + producaoLeiteDiaria + " L");
    }
}