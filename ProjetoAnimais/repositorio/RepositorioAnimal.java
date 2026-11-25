package ProjetoAnimais.repositorio;

import java.util.List;

import ProjetoAnimais.Animal;
import ProjetoAnimais.excecao.PersistenciaException;

public interface RepositorioAnimal extends Repositorio<Animal> {

    // Consulta específica do relacionamento 1:N: todos os animais de um tutor //
    List<Animal> listarPorTutor(int tutorId) throws PersistenciaException;
}
