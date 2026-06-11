package com.gestaolivros.dao;

import com.gestaolivros.model.ItemPedido;
import com.gestaolivros.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemPedidoDAO {

    public void create(ItemPedido item) throws SQLException {
        String sql = "INSERT INTO Item_Pedido (fkPedido, fkLivro, quantidade, precoUnitario) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getFkPedido());
            stmt.setInt(2, item.getFkLivro());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getPrecoUnitario());
            stmt.executeUpdate();
            System.out.println("✔ Item adicionado ao pedido com sucesso!");
        }
    }

    public List<ItemPedido> readAll() throws SQLException {
        List<ItemPedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM Item_Pedido ORDER BY fkPedido, fkLivro";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    public void update(ItemPedido item) throws SQLException {
        String sql = "UPDATE Item_Pedido SET quantidade = ?, precoUnitario = ? WHERE fkPedido = ? AND fkLivro = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getQuantidade());
            stmt.setDouble(2, item.getPrecoUnitario());
            stmt.setInt(3, item.getFkPedido());
            stmt.setInt(4, item.getFkLivro());
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "✔ Item atualizado!" : "✘ Item não encontrado.");
        }
    }

    public void delete(int fkPedido, int fkLivro) throws SQLException {
        String sql = "DELETE FROM Item_Pedido WHERE fkPedido = ? AND fkLivro = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fkPedido);
            stmt.setInt(2, fkLivro);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "✔ Item deletado!" : "✘ Item não encontrado.");
        }
    }

    public List<ItemPedido> searchByPedido(int fkPedido) throws SQLException {
        List<ItemPedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM Item_Pedido WHERE fkPedido = ? ORDER BY fkLivro";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fkPedido);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    private ItemPedido mapRow(ResultSet rs) throws SQLException {
        return new ItemPedido(
            rs.getInt("fkPedido"),
            rs.getInt("fkLivro"),
            rs.getInt("quantidade"),
            rs.getDouble("precoUnitario")
        );
    }
}