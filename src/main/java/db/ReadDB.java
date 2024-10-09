package db;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class ReadDB {
    private Connection connection;
    private List<String[]> dataCompany = new ArrayList<>();
    private List<String[]> dataCountry = new ArrayList<>();
    private List<String[]> dataRegion = new ArrayList<>();
    private String[] row;
    Statement statement;
    ResultSet resultSet;

    public ReadDB(String dbName) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.dataCompany = readCompanyConsumption();
        this.dataCountry = readCountryConsumption();
        this.dataRegion = readRegionConsumption();
    }

    public List<String[]> readCompanyConsumption() {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT company, consumption, year FROM ConsumptionCompany");

            while (resultSet.next()) {
                String company = resultSet.getString("company");
                double consumption = resultSet.getDouble("consumption");
                int year = resultSet.getInt("year");

                row = new String[]{company, String.valueOf(consumption), String.valueOf(year)};
                dataCompany.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataCompany;
    }

    public List<String[]> readCountryConsumption() {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT country, consumption, year FROM ConsumptionCountry");
            while (resultSet.next()) {
                row = new String[]{resultSet.getString("country"), String.valueOf(resultSet.getDouble("consumption")), String.valueOf(resultSet.getInt("year"))};
                dataCountry.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataCountry;
    }

    public List<String[]> readRegionConsumption() {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT region, consumption, year FROM ConsumptionRegion");

            while (resultSet.next()) {
                String region = resultSet.getString("region");
                double consumption = resultSet.getDouble("consumption");
                int year = resultSet.getInt("year");

                row = new String[]{region, String.valueOf(consumption), String.valueOf(year)};
                dataRegion.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataRegion;
    }

    public List<String[]> getDataRegion() {
        return dataRegion;
    }

    public List<String[]> getDataCompany() {
        return dataCompany;
    }

    public List<String[]> getDataCountry() {
        return dataCountry;
    }
}