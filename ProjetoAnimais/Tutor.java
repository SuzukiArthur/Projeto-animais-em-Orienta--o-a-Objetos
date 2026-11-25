package ProjetoAnimais;

// Entidade "1" do relacionamento 1:N: um tutor pode possuir vários animais //

public class Tutor {

    private int id;
    private String nome;
    private String telefone;

    public Tutor() {
        this.nome = "Sem nome";
        this.telefone = "";
    }

    public Tutor(String nome, String telefone) {
        setNome(nome);
        setTelefone(telefone);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = (nome == null || nome.trim().isEmpty()) ? "Sem nome" : nome.trim();
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = (telefone == null) ? "" : telefone.trim();
    }

    public void exibirInformacoes() {
        System.out.println("[ID " + id + "] Tutor: " + nome + ", Telefone: " + (telefone.isEmpty() ? "não informado" : telefone));
    }
}
