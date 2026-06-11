package com.gestaolivros.dao;

import com.gestaolivros.model.Livro;
import com.gestaolivros.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    public void create(Livro livro) throws SQLException {
        String sql = "INSERT INTO Livro (titulo, preco, estoque) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setDouble(2, livro.getPreco());
            stmt.setInt(3, livro.getEstoque());
            stmt.executeUpdate();
            System.out.println("✔ Livro inserido com sucesso!");
        }
    }

    public List<Livro> readAll() throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM Livro ORDER BY idLivro";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                livros.add(mapRow(rs));
            }
        }
        return livros;
    }

    public void update(Livro livro) throws SQLException {
        String sql = "UPDATE Livro SET titulo = ?, preco = ?, estoque = ? WHERE idLivro = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setDouble(2, livro.getPreco());
            stmt.setInt(3, livro.getEstoque());
            stmt.setInt(4, livro.getIdLivro());
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "✔ Livro atualizado!" : "✘ ID não encontrado.");
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 1. Deletar pagamentos vinculados aos pedidos que contêm este livro
                try (PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM Pagamento WHERE fkPedido IN " +
                        "(SELECT fkPedido FROM Item_Pedido WHERE fkLivro = ?)")) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                }
                // 2. Deletar os itens de pedido que referenciam este livro
                try (PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM Item_Pedido WHERE fkLivro = ?")) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                }
                // 3. Deletar o livro
                try (PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM Livro WHERE idLivro = ?")) {
                    stmt.setInt(1, id);
                    int rows = stmt.executeUpdate();
                    System.out.println(rows > 0 ? "✔ Livro deletado!" : "✘ ID não encontrado.");
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void adicionarEstoque(int id, int quantidade) throws SQLException {
        String sql = "UPDATE Livro SET estoque = estoque + ? WHERE idLivro = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantidade);
            stmt.setInt(2, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "✔ Estoque atualizado!" : "✘ ID não encontrado.");
        }
    }

    public List<Livro> searchByTitulo(String titulo) throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM Livro WHERE titulo LIKE ? ORDER BY titulo";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + titulo + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) livros.add(mapRow(rs));
            }
        }
        return livros;
    }

    private Livro mapRow(ResultSet rs) throws SQLException {
        return new Livro(
            rs.getInt("idLivro"),
            rs.getString("titulo"),
            rs.getDouble("preco"),
            rs.getInt("estoque")
        );
    }
}