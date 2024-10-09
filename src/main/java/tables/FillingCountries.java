package tables;

import addition.Regions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class FillingCountries {
    Regions regions = new Regions();
    Map<String, String> countries = regions.getCountries();
    Connection connection;
    String insertSQL = "INSERT OR REPLACE INTO Countries (country, region) VALUES (?, ?)";
    PreparedStatement statement;

    public FillingCountries() {
        fillTable();
    }

    private void fillTable() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:reactorsDB.db");
            statement = connection.prepareStatement(insertSQL);

            for (Map.Entry<String, String> entry : countries.entrySet()) {
                statement.setString(1, entry.getKey());
                statement.setString(2, entry.getValue());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}