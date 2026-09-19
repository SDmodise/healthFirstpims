package pims.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/pims_db";
    private static final String USER = "root";
    private static final String PASSWORD = System.getenv("PIMS_DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        if (PASSWORD == null || PASSWORD.isEmpty()) {
            throw new SQLException("PIMS_DB_PASSWORD environment variable is not set.");
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}