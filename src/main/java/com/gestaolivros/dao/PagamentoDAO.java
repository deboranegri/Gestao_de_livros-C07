package com.gestaolivros.dao;

import com.gestaolivros.model.Pagamento;
import com.gestaolivros.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagamentoDAO {

    public void create(Pagamento pagamento) throws SQLException {
        String sql = "INSERT INTO Pagamento (dataPagamento, formaPagamento, valorTotal, fkPedido) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new java.sql.Timestamp(pagamento.getDataPagamento().getTime()));
            stmt.setString(2, pagamento.getFormaPagamento());
            stmt.setDouble(3, pagamento.getValorTotal());
            stmt.setInt(4, pagamento.getFkPedido());
            stmt.executeUpdate();
            System.out.println("✔ Pagamento registrado com sucesso!");
        }
    }

    public List<Pagamento> readAll() throws SQLException {
        List<Pagamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pagamento ORDER BY idPagamento";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    public void update(Pagamento pagamento) throws SQLException {
        String sql = "UPDATE Pagamento SET dataPagamento = ?, formaPagamento = ?, valorTotal = ?, fkPedido = ? WHERE idPagamento = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new java.sql.Timestamp(pagamento.getDataPagamento().getTime()));
            stmt.setString(2, pagamento.getFormaPagamento());
            stmt.setDouble(3, pagamento.getValorTotal());
            stmt.setInt(4, pagamento.getFkPedido());
            stmt.setInt(5, pagamento.getIdPagamento());
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "✔ Pagamento atualizado!" : "✘ ID não encontrado.");
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Pagamento WHERE idPagamento = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "✔ Pagamento deletado!" : "✘ ID não encontrado.");
        }
    }

    public List<Pagamento> searchByFormaPagamento(String forma) throws SQLException {
        List<Pagamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pagamento WHERE formaPagamento LIKE ? ORDER BY dataPagamento";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + forma + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    private Pagamento mapRow(ResultSet rs) throws SQLException {
        return new Pagamento(
            rs.getInt("idPagamento"),
            rs.getTimestamp("dataPagamento"),
            rs.getString("formaPagamento"),
            rs.getDouble("valorTotal"),
            rs.getInt("fkPedido")
        );
    }
}