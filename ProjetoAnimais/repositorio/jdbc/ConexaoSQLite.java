package ProjetoAnimais.repositorio.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import ProjetoAnimais.excecao.PersistenciaException;

// Abre conexões JDBC com o banco SQLite (arquivo animais.db na pasta de execução) //
// Requer o driver sqlite-jdbc no classpath //

public final class ConexaoSQLite {

    private static final String URL = "jdbc:sqlite:animais.db";

    private ConexaoSQLite() {
    }

    public static Connection abrir() throws SQLException {
        Connection conexao = DriverManager.getConnection(URL);
        // SQLite só respeita chaves estrangeiras quando o PRAGMA está ligado em cada conexão //
        try (Statement st = conexao.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
        return conexao;
    }

    // Cria as tabelas se ainda não existirem (não é uma migration: apenas garante o esquema inicial) //
    public static void criarTabelas() throws PersistenciaException {
        String tabelaTutor = "CREATE TABLE IF NOT EXISTS tutor (" +
                " id       INTEGER PRIMARY KEY AUTOINCREMENT," +
                " nome     TEXT    NOT NULL," +
                " telefone TEXT" +
                ")";

        String tabelaAnimal = "CREATE TABLE IF NOT EXISTS animal (" +
                " id               INTEGER PRIMARY KEY AUTOINCREMENT," +
                " tutor_id         INTEGER NOT NULL REFERENCES tutor(id) ON DELETE CASCADE," +
                " tipo             TEXT    NOT NULL," +
                " nome             TEXT    NOT NULL," +
                " especie          TEXT    NOT NULL," +
                " idade            INTEGER NOT NULL," +
                " peso             REAL    NOT NULL," +
                " numero_patas     INTEGER," +
                " producao_leite   REAL," +
                " envergadura_asas REAL," +
                " capacidade_voo   INTEGER" +
                ")";

        try (Connection conexao = abrir(); Statement st = conexao.createStatement()) {
            st.execute(tabelaTutor);
            st.execute(tabelaAnimal);
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao criar as tabelas do banco", e);
        }
    }
}
