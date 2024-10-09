package tables;

import addition.Regions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class FillingRegions {

    Regions regions = new Regions();
    ArrayList<String> regionsList = regions.getRegions();
    Connection connection;
    String insertSQL = "INSERT OR REPLACE INTO Regions (region) VALUES (?)";
    PreparedStatement statement;

    public FillingRegions() {
        fillTable();
    }

    private  void fillTable() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:reactorsDB.db");
            statement = connection.prepareStatement(insertSQL);

            for (String region : regionsList) {
                statement.setString(1, region);
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