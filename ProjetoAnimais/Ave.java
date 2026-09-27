package ProjetoAnimais;

/**
 * Subclasse 2 da hierarquia "Animal".
 * Demonstra Herança (extends) e sobrescrita de método.
 */
public class Ave extends Animal {

    private double envergaduraAsas; // em metros
    private boolean capacidadeVoo;

    // Construtor padrão - chama super()
    public Ave() {
        super();
        this.envergaduraAsas = 0.0;
        this.capacidadeVoo = false;
    }

    // Construtor sobrecarregado - chama super(...) e inicializa atributos extras
    public Ave(String nome, String especie, int idade, double peso,
            double envergaduraAsas, boolean capacidadeVoo) {
        super(nome, especie, idade, peso);
        setEnvergaduraAsas(envergaduraAsas);
        this.capacidadeVoo = capacidadeVoo;
    }

    public double getEnvergaduraAsas() {
        return envergaduraAsas;
    }

    public void setEnvergaduraAsas(double envergaduraAsas) {
        this.envergaduraAsas = (envergaduraAsas < 0) ? 0.0 : envergaduraAsas;
    }

    public boolean isCapacidadeVoo() {
        return capacidadeVoo;
    }

    public void setCapacidadeVoo(boolean capacidadeVoo) {
        this.capacidadeVoo = capacidadeVoo;
    }

    // Sobrescrita de exibirInformacoes(), reutilizando o método da classe base
    @Override
    public void exibirInformacoes() {
        super.exibirInformacoes();
        System.out.println("Envergadura das asas: " + envergaduraAsas + " m" +
                ", Capacidade de voo: " + (capacidadeVoo ? "Sim" : "Não"));
    }
}
