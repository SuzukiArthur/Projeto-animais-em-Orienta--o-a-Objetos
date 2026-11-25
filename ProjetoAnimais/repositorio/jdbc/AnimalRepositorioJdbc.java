package ProjetoAnimais.repositorio.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import ProjetoAnimais.Animal;
import ProjetoAnimais.Ave;
import ProjetoAnimais.Mamifero;
import ProjetoAnimais.TipoAnimal;
import ProjetoAnimais.excecao.PersistenciaException;
import ProjetoAnimais.excecao.RecursoNaoEncontradoException;
import ProjetoAnimais.repositorio.RepositorioAnimal;

// Persistência da hierarquia Animal (Mamifero / Ave) com JDBC puro //
// As três classes ficam numa única tabela: a coluna "tipo" diz qual subclasse criar ao ler //

public class AnimalRepositorioJdbc implements RepositorioAnimal {

    private static final String COLUNAS = "id, tutor_id, tipo, nome, especie, idade, peso, " +
            "numero_patas, producao_leite, envergadura_asas, capacidade_voo";

    @Override
    public int salvar(Animal animal) throws PersistenciaException {
        String sql = "INSERT INTO animal (tutor_id, tipo, nome, especie, idade, peso, " +
                "numero_patas, producao_leite, envergadura_asas, capacidade_voo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = ConexaoSQLite.abrir();
                PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preencher(ps, animal);
            ps.executeUpdate();

            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    int id = chaves.getInt(1);
                    animal.setId(id);
                    return id;
                }
            }
            throw new PersistenciaException("O banco não devolveu o ID do animal", null);
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao salvar animal", e);
        }
    }

    @Override
    public Animal buscarPorId(int id) throws RecursoNaoEncontradoException, PersistenciaException {
        String sql = "SELECT " + COLUNAS + " FROM animal WHERE id = ?";
        try (Connection c = ConexaoSQLite.abrir();
                PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new RecursoNaoEncontradoException("Nenhum animal com ID " + id);
                }
                return mapear(rs);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar animal", e);
        }
    }

    @Override
    public List<Animal> listarTodos() throws PersistenciaException {
        String sql = "SELECT " + COLUNAS + " FROM animal ORDER BY id";
        return executarListagem(sql, null);
    }

    @Override
    public List<Animal> listarPorTutor(int tutorId) throws PersistenciaException {
        String sql = "SELECT " + COLUNAS + " FROM animal WHERE tutor_id = ? ORDER BY id";
        return executarListagem(sql, tutorId);
    }

    @Override
    public void atualizar(Animal animal) throws RecursoNaoEncontradoException, PersistenciaException {
        String sql = "UPDATE animal SET tutor_id = ?, tipo = ?, nome = ?, especie = ?, idade = ?, peso = ?, " +
                "numero_patas = ?, producao_leite = ?, envergadura_asas = ?, capacidade_voo = ? " +
                "WHERE id = ?";
        try (Connection c = ConexaoSQLite.abrir();
                PreparedStatement ps = c.prepareStatement(sql)) {

            preencher(ps, animal);
            ps.setInt(11, animal.getId());
            if (ps.executeUpdate() == 0) {
                throw new RecursoNaoEncontradoException("Nenhum animal com ID " + animal.getId());
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar animal", e);
        }
    }

    @Override
    public void remover(int id) throws RecursoNaoEncontradoException, PersistenciaException {
        String sql = "DELETE FROM animal WHERE id = ?";
        try (Connection c = ConexaoSQLite.abrir();
                PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new RecursoNaoEncontradoException("Nenhum animal com ID " + id);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao remover animal", e);
        }
    }

    // ---------- auxiliares ----------

    private List<Animal> executarListagem(String sql, Integer tutorId) throws PersistenciaException {
        List<Animal> animais = new ArrayList<>();
        try (Connection c = ConexaoSQLite.abrir();
                PreparedStatement ps = c.prepareStatement(sql)) {

            if (tutorId != null) {
                ps.setInt(1, tutorId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    animais.add(mapear(rs));
                }
            }
            return animais;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar animais", e);
        }
    }

    // Preenche os parâmetros 1 a 10, na ordem das colunas do INSERT/UPDATE //
    private static void preencher(PreparedStatement ps, Animal animal) throws SQLException {
        ps.setInt(1, animal.getTutorId());
        ps.setString(2, animal.getTipo().name());
        ps.setString(3, animal.getNome());
        ps.setString(4, animal.getEspecie());
        ps.setInt(5, animal.getIdade());
        ps.setDouble(6, animal.getPeso());

        if (animal instanceof Mamifero m) {
            ps.setInt(7, m.getNumeroPatas());
            ps.setDouble(8, m.getProducaoLeiteDiaria());
            ps.setNull(9, Types.REAL);
            ps.setNull(10, Types.BOOLEAN);
        } else if (animal instanceof Ave v) {
            ps.setNull(7, Types.INTEGER);
            ps.setNull(8, Types.REAL);
            ps.setDouble(9, v.getEnvergaduraAsas());
            ps.setBoolean(10, v.isCapacidadeVoo());
        }
    }

    // Recria a subclasse correta a partir da linha lida //
    private static Animal mapear(ResultSet rs) throws SQLException {
        TipoAnimal tipo = TipoAnimal.valueOf(rs.getString("tipo"));
        Animal animal;
        if (tipo == TipoAnimal.MAMIFERO) {
            animal = new Mamifero(
                    rs.getString("nome"), rs.getString("especie"), rs.getInt("idade"), rs.getDouble("peso"),
                    rs.getInt("numero_patas"), rs.getDouble("producao_leite"));
        } else {
            animal = new Ave(
                    rs.getString("nome"), rs.getString("especie"), rs.getInt("idade"), rs.getDouble("peso"),
                    rs.getDouble("envergadura_asas"), rs.getBoolean("capacidade_voo"));
        }
        animal.setId(rs.getInt("id"));
        animal.setTutorId(rs.getInt("tutor_id"));
        return animal;
    }
}
