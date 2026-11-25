package ProjetoAnimais.excecao;

// Exceção personalizada: qualquer falha de acesso ao banco de dados (SQLException é encapsulada aqui) //

public class PersistenciaException extends Exception {

    private static final long serialVersionUID = 1L;

    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
