package tables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreatingTables {
    Connection connection;
    String queryReactorTypes = "CREATE TABLE IF NOT EXISTS ReactorsTypes (type TEXT PRIMARY KEY, burnup INT)";
    String queryRegions = "CREATE TABLE IF NOT EXISTS Regions (region TEXT PRIMARY KEY)";
    String queryCountries = "CREATE TABLE IF NOT EXISTS Countries (\n"
            + "    country TEXT PRIMARY KEY,\n"
            + "    region TEXT,\n"
            + "FOREIGN KEY (region) REFERENCES Regions(region)\n"
            + ");";
    String queryReactorsPRIS = "CREATE TABLE IF NOT EXISTS ReactorsFromPRIS (" +
            "name TEXT PRIMARY KEY, " +
            "country TEXT, " +
            "status TEXT, " +
            "type TEXT, " +
            "owner TEXT, " +
            "operator TEXT, " +
            "thermalCapacity INTEGER, " +
            "firstGridConnection INTEGER, " +
            "loadFactor REAL, " +
            "suspendedDate INTEGER, " +
            "permanentShutdownDate INTEGER, " +
            "FOREIGN KEY (type) REFERENCES ReactorsTypes(type))";
    String queryLoadFactor = "CREATE TABLE IF NOT EXISTS LoadFactor (\n"
            + " reactor TEXT,\n"
            + " year INTEGER,\n"
            + " loadfactor REAL,\n"
            + "FOREIGN KEY (reactor) REFERENCES ReactorsFromPRIS(name)\n"
            + ");";
    String queryConsumption = "CREATE TABLE IF NOT EXISTS Consumption (\n"
            + " reactor TEXT,\n"
            + " year INTEGER,\n"
            + " consumption REAL,\n"
            + "FOREIGN KEY (reactor) REFERENCES ReactorsFromPRIS(name)\n"
            + ");";
    String queryAggrCountry = "CREATE TABLE IF NOT EXISTS ConsumptionCountry (\n"
            + " country TEXT,\n"
            + " consumption REAL,\n"
            + " year INTEGER,\n"
            + "FOREIGN KEY (country) REFERENCES Countries(country)\n"
            + ");";
    String queryAggrCompany = "CREATE TABLE IF NOT EXISTS ConsumptionCompany (\n"
            + " company TEXT,\n"
            + " consumption REAL,\n"
            + " year INTEGER,\n"
            + "FOREIGN KEY (company) REFERENCES ReactorsFromPRIS(operator)\n"
            + ");";
    String queryAggrRegion = "CREATE TABLE IF NOT EXISTS ConsumptionRegion (\n"
            + " region TEXT,\n"
            + " consumption REAL,\n"
            + " year INTEGER,\n"
            + "FOREIGN KEY (region) REFERENCES Regions(region)\n"
            + ");";
    PreparedStatement statement;

    public CreatingTables() {
        {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:reactorsDB.db");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        creatingTable(queryReactorTypes);
        creatingTable(queryRegions);
        creatingTable(queryCountries);
        creatingTable(queryReactorsPRIS);
        creatingTable(queryLoadFactor);
        creatingTable(queryConsumption);
        creatingTable(queryAggrCountry);
        creatingTable(queryAggrCompany);
        creatingTable(queryAggrRegion);
    }

    private void creatingTable(String query) {
        try {
            statement = connection.prepareStatement(query);
            statement.executeUpdate();  // Выполняем создание таблицы
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}