package tables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class FillingLoadFactor {

    Connection connection;
    String insertSQL = "INSERT OR REPLACE INTO LoadFactor(reactor, year, loadfactor)" + "VALUES(?, ?, ?)";
    PreparedStatement statement;
    HashMap<String, HashMap<Integer, Double>> mapForLoadMap;

    public FillingLoadFactor(HashMap<String, HashMap<Integer, Double>> mapForLoadMap) {
        this.mapForLoadMap = mapForLoadMap;
        fillTable();
    }

    public void fillTable() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:reactorsDB.db");
            statement = connection.prepareStatement(insertSQL);

            for (Map.Entry<String, HashMap<Integer, Double>> reactorEntry : mapForLoadMap.entrySet()) {
                String name = reactorEntry.getKey();
                Map<Integer, Double> loadFactors = reactorEntry.getValue();

                for (Map.Entry<Integer, Double> loadEntry : loadFactors.entrySet()) {
                    int year = loadEntry.getKey();
                    double value = loadEntry.getValue();

                    statement.setString(1, name);
                    statement.setInt(2, year);
                    statement.setDouble(3, value);
                    statement.executeUpdate();
                    System.out.println("Запись добавлена в таблицу Load Factor");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}