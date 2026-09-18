package pims.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jbdc:mysql://localhost:3306/pims_db";
    private static final String USER = "root";
    private static final String PASSWORD = "PIMSql@26";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
