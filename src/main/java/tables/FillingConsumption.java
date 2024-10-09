package tables;

import java.sql.*;

public class FillingConsumption {
    Connection connection;
    PreparedStatement statement;
    PreparedStatement statementInsert;
    String insertSQL = "INSERT OR REPLACE INTO Consumption (reactor, year, consumption) VALUES (?, ?, ?)";
    String query = "SELECT A.name, A.type, A.thermalCapacity, B.burnup, C.loadfactor, C.year FROM ReactorsFromPRIS AS A INNER JOIN ReactorsTypes AS B ON A.type = B.type INNER JOIN LoadFactor AS C ON A.name = C.reactor";
    ResultSet resultSet;

    public FillingConsumption() {
        fillConsumptionTable();
    }

    private void fillConsumptionTable() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:reactorsDB.db");
            statement = connection.prepareStatement(query);
            statementInsert = connection.prepareStatement(insertSQL);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                statementInsert.setString(1, resultSet.getString(1));
                statementInsert.setString(2, resultSet.getString(6));
                statementInsert.setDouble(3, calculateConsumption(resultSet.getInt(3), resultSet.getInt(4), resultSet.getDouble(5)));
                statementInsert.executeUpdate();
                System.out.println("Запись добавлена в таблицу Consumption");
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

    private Double calculateConsumption(int thermalCapacity, int burnup, double loadFactor) {
        return (thermalCapacity*loadFactor/burnup);
    }
}