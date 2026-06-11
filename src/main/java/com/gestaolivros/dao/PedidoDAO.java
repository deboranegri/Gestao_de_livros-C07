package com.gestaolivros.dao;

import com.gestaolivros.model.Pedido;
import com.gestaolivros.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public void create(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO Pedido (dataPedido, fkCliente) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new java.sql.Timestamp(pedido.getDataPedido().getTime()));
            stmt.setInt(2, pedido.getFkCliente());
            stmt.executeUpdate();
            System.out.println("✔ Pedido inserido com sucesso!");
        }
    }

    public List<Pedido> readAll() throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pedido ORDER BY idPedido";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    public void update(Pedido pedido) throws SQLException {
        String sql = "UPDATE Pedido SET dataPedido = ?, fkCliente = ? WHERE idPedido = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new java.sql.Timestamp(pedido.getDataPedido().getTime()));
            stmt.setInt(2, pedido.getFkCliente());
            stmt.setInt(3, pedido.getIdPedido());
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "✔ Pedido atualizado!" : "✘ ID não encontrado.");
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Pedido WHERE idPedido = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "✔ Pedido deletado!" : "✘ ID não encontrado.");
        }
    }

    public List<Pedido> searchById(int id) throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pedido WHERE idPedido = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    public void listPedidosComClientes() throws SQLException {
        String sql = "SELECT p.idPedido, p.dataPedido, c.idCliente, c.nome "
                   + "FROM Pedido p "
                   + "JOIN Cliente c ON p.fkCliente = c.idCliente "
                   + "ORDER BY p.idPedido";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n  ID Pedido | Data do Pedido          | ID Cliente | Nome do Cliente");
            System.out.println("  ----------|-------------------------|------------|---------------------");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("  %-10d| %-23s | %-10d | %s%n",
                    rs.getInt("idPedido"),
                    rs.getTimestamp("dataPedido"),
                    rs.getInt("idCliente"),
                    rs.getString("nome"));
            }
            if (!found) System.out.println("  (nenhum registro encontrado)");
        }
    }

    public void listRelatorioVendas() throws SQLException {
        String sql = "SELECT * FROM vw_relatorio_vendas ORDER BY idPedido";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n  Pedido | Cliente              | Livro                          | Qtd | Preço Unit | Subtotal   | Data");
            System.out.println("  -------|----------------------|--------------------------------|-----|------------|------------|--------------------");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("  %-7d| %-20s | %-30s | %-3d | R$ %7.2f | R$ %8.2f | %s%n",
                    rs.getInt("idPedido"),
                    rs.getString("nomeCliente"),
                    rs.getString("tituloLivro"),
                    rs.getInt("quantidade"),
                    rs.getDouble("precoUnitario"),
                    rs.getDouble("subtotal"),
                    rs.getTimestamp("dataPedido"));
            }
            if (!found) System.out.println("  (nenhum registro encontrado — adicione itens ao pedido via MySQL)");
        }
    }

    private Pedido mapRow(ResultSet rs) throws SQLException {
        return new Pedido(
            rs.getInt("idPedido"),
            rs.getTimestamp("dataPedido"),
            rs.getInt("fkCliente")
        );
    }
}
