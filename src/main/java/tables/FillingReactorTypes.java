package tables;

import reactor.YamlReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class FillingReactorTypes {
    YamlReader yamlReader;
    Connection connection;
    Map<String, Double> reactorBurnupMap;

    String insertSQL = "INSERT OR REPLACE INTO ReactorsTypes (type, burnup) VALUES (?, ?)";

    PreparedStatement statement;

    public FillingReactorTypes(String path) {
        yamlReader = new YamlReader(path);
        reactorBurnupMap = yamlReader.getReactorBurnupMap();
        fillTable();
    }

    private  void fillTable(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:reactorsDB.db");
            statement = connection.prepareStatement(insertSQL);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Map.Entry<String, Double> entry : reactorBurnupMap.entrySet()) {
            try {
                statement.setString(1, entry.getKey());
                statement.setDouble(2, entry.getValue());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        addStatement("LWGR", 25);
        addStatement("GCR", 22);
        addStatement("HWDCR", 12);
        addStatement("HTGR", 100);
        addStatement("FBR", 150);
        addStatement("SGHWR", 8);
    }

    private void addStatement(String stringValue, double doubleValue) {
        try {
            statement.setString(1, stringValue);
            statement.setDouble(2, doubleValue);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}