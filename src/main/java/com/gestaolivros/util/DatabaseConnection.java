package com.gestaolivros.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    private static final String URL      = "jdbc:mysql://localhost:3306/SistemaGestaoDeLivros"
                                         + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER     = "root";
    private static final String PASSWORD = "dFXQLpqp2w&";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
