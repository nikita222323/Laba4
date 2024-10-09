package tables;

import java.sql.*;
import java.util.List;

public class FillingReactrsFromPRIS {

    Connection connection;
    String insertSQL = "INSERT OR REPLACE INTO ReactorsFromPRIS (name, country, status, type, owner, operator, thermalCapacity, firstGridConnection, loadFactor, suspendedDate, permanentShutdownDate)    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    PreparedStatement statement;
    List<List<String>> dataList;

    public FillingReactrsFromPRIS(List<List<String>> dataList) {
        this.dataList = dataList;
        fillTable();
    }

    private void fillTable() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:reactorsDB.db");
            statement = connection.prepareStatement(insertSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (List<String> rowData : dataList) {
            try {
                statement.setString(1, rowData.get(0));
                statement.setString(2, rowData.get(1));
                statement.setString(3, rowData.get(2));
                statement.setString(4, rowData.get(3));
                statement.setString(5, rowData.get(4));
                statement.setString(6, rowData.get(5));

                Integer thermalCapacity = parseToInt(rowData.get(6));
                if (thermalCapacity != null) {
                    statement.setInt(7, thermalCapacity);
                } else {
                    statement.setInt(7, 85);
                }

                Integer firstGridConnection = parseToInt(rowData.get(7));
                if (firstGridConnection != null) {
                    statement.setInt(8, firstGridConnection);
                } else {
                    statement.setNull(8, Types.INTEGER);
                }

                Double loadFactor = parseToDouble(rowData.get(8));
                if (loadFactor != null) {
                    statement.setDouble(9, loadFactor);
                } else {
                    statement.setDouble(9, 90.0);
                }

                Integer suspendedDate = parseToInt(rowData.get(9));
                if (suspendedDate != null) {
                    statement.setInt(10, suspendedDate);
                } else {
                    statement.setNull(10, Types.INTEGER);
                }

                Integer permanentShutdownDate = parseToInt(rowData.get(10));
                if (permanentShutdownDate != null) {
                    statement.setInt(11, permanentShutdownDate);
                } else {
                    statement.setNull(11, Types.INTEGER);
                }

                statement.executeUpdate();
                System.out.println("Запись добавлена в таблицу ReactorPRIS");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Integer parseToInt(String value) {
        return value != null && !value.isEmpty() && !value.equalsIgnoreCase("N/A") ? Integer.parseInt(value) : null;
    }

    private Double parseToDouble(String value) {
        return value != null && !value.isEmpty() && !value.equalsIgnoreCase("N/A") ? Double.parseDouble(value) : null;
    }

}