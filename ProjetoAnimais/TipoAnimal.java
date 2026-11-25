package ProjetoAnimais;

// Enumerador: tipos concretos da hierarquia de animais //
// O nome do enum é gravado na coluna "tipo" da tabela animal //

public enum TipoAnimal {

    MAMIFERO("Mamífero"),
    AVE("Ave");

    private final String descricao;

    TipoAnimal(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
