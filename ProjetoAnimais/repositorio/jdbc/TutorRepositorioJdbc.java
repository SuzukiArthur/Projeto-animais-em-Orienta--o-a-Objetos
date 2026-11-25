package ProjetoAnimais.repositorio.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ProjetoAnimais.Tutor;
import ProjetoAnimais.excecao.PersistenciaException;
import ProjetoAnimais.excecao.RecursoNaoEncontradoException;
import ProjetoAnimais.repositorio.RepositorioTutor;

// Persistência de Tutor com JDBC puro (Aula 11) //

public class TutorRepositorioJdbc implements RepositorioTutor {

    @Override
    public int salvar(Tutor tutor) throws PersistenciaException {
        String sql = "INSERT INTO tutor (nome, telefone) VALUES (?, ?)";
        try (Connection c = ConexaoSQLite.abrir();
                PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, tutor.getNome());
            ps.setString(2, tutor.getTelefone());
            ps.executeUpdate();

            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    int id = chaves.getInt(1);
                    tutor.setId(id);
                    return id;
                }
            }
            throw new PersistenciaException("O banco não devolveu o ID do tutor", null);
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao salvar tutor", e);
        }
    }

    @Override
    public Tutor buscarPorId(int id) throws RecursoNaoEncontradoException, PersistenciaException {
        String sql = "SELECT id, nome, telefone FROM tutor WHERE id = ?";
        try (Connection c = ConexaoSQLite.abrir();
                PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new RecursoNaoEncontradoException("Nenhum tutor com ID " + id);
                }
                return mapear(rs);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar tutor", e);
        }
    }

    @Override
    public List<Tutor> listarTodos() throws PersistenciaException {
        String sql = "SELECT id, nome, telefone FROM tutor ORDER BY id";
        List<Tutor> tutores = new ArrayList<>();
        try (Connection c = ConexaoSQLite.abrir();
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                tutores.add(mapear(rs));
            }
            return tutores;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar tutores", e);
        }
    }

    @Override
    public void atualizar(Tutor tutor) throws RecursoNaoEncontradoException, PersistenciaException {
        String sql = "UPDATE tutor SET nome = ?, telefone = ? WHERE id = ?";
        try (Connection c = ConexaoSQLite.abrir();
                PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, tutor.getNome());
            ps.setString(2, tutor.getTelefone());
            ps.setInt(3, tutor.getId());
            if (ps.executeUpdate() == 0) {
                throw new RecursoNaoEncontradoException("Nenhum tutor com ID " + tutor.getId());
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar tutor", e);
        }
    }

    @Override
    public void remover(int id) throws RecursoNaoEncontradoException, PersistenciaException {
        // Os animais do tutor são removidos em cascata (ON DELETE CASCADE) //
        String sql = "DELETE FROM tutor WHERE id = ?";
        try (Connection c = ConexaoSQLite.abrir();
                PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new RecursoNaoEncontradoException("Nenhum tutor com ID " + id);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao remover tutor", e);
        }
    }

    private static Tutor mapear(ResultSet rs) throws SQLException {
        Tutor t = new Tutor(rs.getString("nome"), rs.getString("telefone"));
        t.setId(rs.getInt("id"));
        return t;
    }
}
