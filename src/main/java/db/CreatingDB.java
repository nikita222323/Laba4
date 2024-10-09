package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreatingDB {

    public Connection connect() {
        String url = "jdbc:sqlite:reactorsDB.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Соединение с SQLite установлено.");
        } catch (SQLException e) {
            System.out.println("Ошибка " + e.getMessage());
        }
        return conn;
    }
}
