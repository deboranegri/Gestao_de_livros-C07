package com.gestaolivros.dao;

import com.gestaolivros.model.Cliente;
import com.gestaolivros.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void create(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Cliente (nome, email, dataNascimento, fkPerfilUsuario) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setDate(3, new java.sql.Date(cliente.getDataNascimento().getTime()));
            stmt.setInt(4, cliente.getFkPerfilUsuario());
            stmt.executeUpdate();
            System.out.println("✔ Cliente inserido com sucesso!");
        }
    }

    public List<Cliente> readAll() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente ORDER BY idCliente";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    public void update(Cliente cliente) throws SQLException {
        String sql = "UPDATE Cliente SET nome = ?, email = ?, dataNascimento = ?, fkPerfilUsuario = ? WHERE idCliente = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setDate(3, new java.sql.Date(cliente.getDataNascimento().getTime()));
            stmt.setInt(4, cliente.getFkPerfilUsuario());
            stmt.setInt(5, cliente.getIdCliente());
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "✔ Cliente atualizado!" : "✘ ID não encontrado.");
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE idCliente = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "✔ Cliente deletado!" : "✘ ID não encontrado.");
        }
    }

    public List<Cliente> searchByNome(String nome) throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente WHERE nome LIKE ? ORDER BY nome";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    public void listClientesComPerfil() throws SQLException {
        String sql = "SELECT c.idCliente, c.nome, c.email, p.userName, p.preferenciasDeLeitura "
                   + "FROM Cliente c "
                   + "JOIN Perfil_Usuario p ON c.fkPerfilUsuario = p.idPerfilUsuario "
                   + "ORDER BY c.idCliente";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n  ID  | Cliente               | Email                     | UserName            | Preferências");
            System.out.println("  ----|------------------------|---------------------------|---------------------|-------------------");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("  %-4d| %-22s | %-25s | %-19s | %s%n",
                    rs.getInt("idCliente"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("userName"),
                    rs.getString("preferenciasDeLeitura"));
            }
            if (!found) System.out.println("  (nenhum registro encontrado)");
        }
    }

    private Cliente mapRow(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("idCliente"),
            rs.getString("nome"),
            rs.getString("email"),
            rs.getDate("dataNascimento"),
            rs.getInt("fkPerfilUsuario")
        );
    }
}
