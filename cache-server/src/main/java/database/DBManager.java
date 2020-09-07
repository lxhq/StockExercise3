package database;

import json.MetaData;
import json.StockInfo;
import json.StockPrices;
import oracle.jdbc.OracleDriver;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBManager {
    private Connection connection;

    public DBManager() {
        initConn();
    }

    /**
     * Get all information for input stock symbol from database if existed
     * Return null if there is no such data in database
     * @param stockSymbol stock symbol
     * @return A StockInfo or null that contains all information
     */
    public StockInfo getStock(String stockSymbol) {
        try {
            PreparedStatement statement1 = this.connection.prepareStatement(
                    "SELECT * FROM Stock_MetaData WHERE SYMBOL=?"
            );
            statement1.setString(1, stockSymbol);
            ResultSet rs1 = statement1.executeQuery();
            StockInfo stockInfo = new StockInfo();
            MetaData metaData = new MetaData();
            boolean isExisted = false;
            while (rs1.next()) {
                isExisted = true;
                metaData.setInformation(rs1.getString("INFORMATION"));
                metaData.setSymbol(rs1.getString("SYMBOL"));
                metaData.setLastRefreshed(rs1.getString("LASTREFRESHED"));
                metaData.setOutputSize(rs1.getString("OUTPUTSIZE"));
                metaData.setTimeZone(rs1.getString("TIMEZONE"));
            }
            if (!isExisted) return null;
            stockInfo.setMetaData(metaData);
            PreparedStatement statement2 = this.connection.prepareStatement(
                    "SELECT * FROM Stock_Price WHERE SYMBOL=?"
            );
            statement2.setString(1, stockSymbol);
            ResultSet rs2 = statement2.executeQuery();
            Map<String, StockPrices> map = new HashMap<>();
            while (rs2.next()) {
                StockPrices stockPrices = new StockPrices();
                String key = rs2.getString("STOCK_DATE");
                stockPrices.setOpen(rs2.getString("OPEN"));
                stockPrices.setLow(rs2.getString("LOW"));
                stockPrices.setHigh(rs2.getString("HIGH"));
                stockPrices.setClose(rs2.getString("CLOSE"));
                stockPrices.setVolume(rs2.getString("VOLUME"));
                map.put(key, stockPrices);
            }
            stockInfo.setTimeSeries(map);
            System.out.println("Read from database");
            return stockInfo;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Store the input stock information into database
     * @param stockInfo stock information that needed to stored into database
     */
    public void storeStock(StockInfo stockInfo) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "INSERT INTO Stock_MetaData" +
                            "(INFORMATION," +
                            "SYMBOL," +
                            "LASTREFRESHED," +
                            "OUTPUTSIZE," +
                            "TIMEZONE) VALUES (" +
                            "?, ?, ?, ?, ?)");
            MetaData metaData = stockInfo.getMetaData();
            preparedStatement.setString(1, metaData.getInformation());
            preparedStatement.setString(2, metaData.getSymbol());
            preparedStatement.setString(3, metaData.getLastRefreshed());
            preparedStatement.setString(4, metaData.getOutputSize());
            preparedStatement.setString(5, metaData.getTimeZone());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Store meta data failed");
        }

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "INSERT INTO Stock_Price" +
                            "(SYMBOL," +
                            "STOCK_DATE," +
                            "OPEN," +
                            "HIGH," +
                            "LOW," +
                            "CLOSE," +
                            "VOLUME) VALUES (" +
                            "?, ?, ?, ?, ?, ?, ?)");
            Map<String, StockPrices> timeSeries = stockInfo.getTimeSeries();
            for (String key : timeSeries.keySet()) {
                StockPrices stockPrices = timeSeries.get(key);
                preparedStatement.setString(1, stockInfo.getMetaData().getSymbol());
                preparedStatement.setString(2, key);
                preparedStatement.setString(3, stockPrices.getOpen());
                preparedStatement.setString(4, stockPrices.getHigh());
                preparedStatement.setString(5, stockPrices.getLow());
                preparedStatement.setString(6, stockPrices.getClose());
                preparedStatement.setString(7, stockPrices.getVolume());
                preparedStatement.execute();
            }
            System.out.println("Stored to database");
        } catch (SQLException e) {
            System.out.println("Store Stock Price failed");
        }
    }

    /**
     * Initialize the connection to database and create the table if not existed
     */
    private void initConn() {
        try {
            Driver driver = new OracleDriver();
            DriverManager.deregisterDriver(driver);
            this.connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE",
                    "stockExercise",
                    "stock"
            );
            createTable();
        } catch (SQLException e) {
            System.out.println("Table existed");
        }
    }

    /**
     * Try to create the table we used to store data in database
     * @throws SQLException
     */
    private void createTable() throws SQLException{
        Statement statement = this.connection.createStatement();
        statement.execute(
                "CREATE TABLE Stock_MetaData (" +
                        "INFORMATION VARCHAR2(50)," +
                        "SYMBOL VARCHAR2(10) PRIMARY KEY," +
                        "LASTREFRESHED VARCHAR2(30)," +
                        "OUTPUTSIZE VARCHAR2(30)," +
                        "TIMEZONE VARCHAR2(30))");
        statement.execute(
                "CREATE TABLE Stock_Price (" +
                        "SYMBOL VARCHAR2(10)," +
                        "STOCK_DATE VARCHAR2(15)," +
                        "OPEN VARCHAR2(15)," +
                        "HIGH VARCHAR2(15)," +
                        "LOW VARCHAR2(15)," +
                        "CLOSE VARCHAR2(15)," +
                        "VOLUME VARCHAR2(15)," +
                        "PRIMARY KEY (SYMBOL, STOCK_DATE))");
    }
}
