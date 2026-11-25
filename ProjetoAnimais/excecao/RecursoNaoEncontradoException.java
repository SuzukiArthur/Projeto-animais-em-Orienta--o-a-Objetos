package ProjetoAnimais.excecao;

// Exceção personalizada: o ID informado não existe no banco //

public class RecursoNaoEncontradoException extends Exception {

    private static final long serialVersionUID = 1L;

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
