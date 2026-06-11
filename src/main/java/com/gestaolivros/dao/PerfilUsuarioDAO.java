package com.gestaolivros.dao;

import com.gestaolivros.model.PerfilUsuario;
import com.gestaolivros.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerfilUsuarioDAO {

    public int create(PerfilUsuario perfil) throws SQLException {
        String sql = "INSERT INTO Perfil_Usuario (userName, preferenciasDeLeitura) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, perfil.getUserName());
            stmt.setString(2, perfil.getPreferenciasDeLeitura());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    System.out.println("✔ Perfil inserido com sucesso!");
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("✘ Falha ao obter ID do perfil inserido.");
    }

    public List<PerfilUsuario> readAll() throws SQLException {
        List<PerfilUsuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Perfil_Usuario ORDER BY idPerfilUsuario";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    public void update(PerfilUsuario perfil) throws SQLException {
        String sql = "UPDATE Perfil_Usuario SET userName = ?, preferenciasDeLeitura = ? WHERE idPerfilUsuario = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, perfil.getUserName());
            stmt.setString(2, perfil.getPreferenciasDeLeitura());
            stmt.setInt(3, perfil.getIdPerfilUsuario());
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "✔ Perfil atualizado!" : "✘ ID não encontrado.");
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Perfil_Usuario WHERE idPerfilUsuario = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "✔ Perfil deletado!" : "✘ ID não encontrado.");
        }
    }

    public List<PerfilUsuario> searchByUserName(String userName) throws SQLException {
        List<PerfilUsuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Perfil_Usuario WHERE userName LIKE ? ORDER BY userName";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + userName + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    private PerfilUsuario mapRow(ResultSet rs) throws SQLException {
        return new PerfilUsuario(
            rs.getInt("idPerfilUsuario"),
            rs.getString("userName"),
            rs.getString("preferenciasDeLeitura")
        );
    }
}
