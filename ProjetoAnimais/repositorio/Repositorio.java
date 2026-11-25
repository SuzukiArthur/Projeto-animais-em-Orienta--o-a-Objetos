package ProjetoAnimais.repositorio;

import java.util.List;

import ProjetoAnimais.excecao.PersistenciaException;
import ProjetoAnimais.excecao.RecursoNaoEncontradoException;

// Interface genérica de CRUD (Aula 7: interfaces) //
// Quem implementa decide como persistir (aqui: JDBC com SQLite) //

public interface Repositorio<T> {

    // Create //
    int salvar(T entidade) throws PersistenciaException;

    // Read //
    T buscarPorId(int id) throws RecursoNaoEncontradoException, PersistenciaException;

    List<T> listarTodos() throws PersistenciaException;

    // Update //
    void atualizar(T entidade) throws RecursoNaoEncontradoException, PersistenciaException;

    // Delete //
    void remover(int id) throws RecursoNaoEncontradoException, PersistenciaException;
}
