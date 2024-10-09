package reactor;

import java.sql.*;

public class Aggregating {

    Connection connection;
    String aggrCountry = "SELECT B.country, A.year, SUM(A.consumption) FROM Consumption AS A INNER JOIN ReactorsFromPRIS AS B ON A.reactor = B.name GROUP BY B.country, A.year";
    String aggrCompany = "SELECT B.operator, A.year, SUM(A.consumption) FROM Consumption AS A INNER JOIN ReactorsFromPRIS AS B ON A.reactor = B.name GROUP BY B.operator, A.year";
    String aggrRegion = "SELECT C.region, A.year, SUM(A.consumption) FROM Consumption AS A INNER JOIN ReactorsFromPRIS AS B ON A.reactor = B.name INNER JOIN Countries AS C ON B.country = C.country GROUP BY C.region, A.year";
    String insertSQLCountry = "INSERT OR REPLACE INTO ConsumptionCountry (country, year, consumption) VALUES (?, ?, ?)";
    String insertSQLCompany = "INSERT OR REPLACE INTO ConsumptionCompany (company, year, consumption) VALUES (?, ?, ?)";
    String insertSQLRegion = "INSERT OR REPLACE INTO ConsumptionRegion (region, year, consumption) VALUES (?, ?, ?)";

    PreparedStatement preparedStatementSelect;
    PreparedStatement preparedStatementInsert;

    ResultSet resultSet;

    public Aggregating() throws SQLException {
        fillTable(aggrCountry, insertSQLCountry);
        fillTable(aggrCompany, insertSQLCompany);
        fillTable(aggrRegion, insertSQLRegion);
    }

    public void fillTable(String select, String insert) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:reactorsDB.db");
            preparedStatementSelect = connection.prepareStatement(select);
            preparedStatementInsert = connection.prepareStatement(insert);
            resultSet = preparedStatementSelect.executeQuery();

            while (resultSet.next()) {
                preparedStatementInsert.setString(1, resultSet.getString(1));
                preparedStatementInsert.setString(2, resultSet.getString(2));
                preparedStatementInsert.setDouble(3, resultSet.getDouble(3));
                preparedStatementInsert.executeUpdate();
                System.out.println("Запись агрегации добавлена ");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatementSelect != null) preparedStatementSelect.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}