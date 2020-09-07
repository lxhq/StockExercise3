package model.manager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import database.DBManager;
import json.StockInfo;
import json.StockPrices;

import javax.inject.Singleton;

@Singleton
public class StockManagerImpl implements StockManager{
    private Map<String, StockInfo> stockValues;
    private DBManager dbManager;

    public StockManagerImpl() {
        this.stockValues = new HashMap<>();
        this.dbManager = new DBManager();
    }

    public boolean isCached(String stockSymbol) {
        if (stockValues.containsKey(stockSymbol)) {
            return true;
        }
        refresh(stockSymbol);
        return stockValues.containsKey(stockSymbol);
    }

    public String searchStockValue(String tickerSymbol, LocalDate date) {
        if (stockValues.containsKey(tickerSymbol)) {
            Map<String, StockPrices> map = stockValues.get(tickerSymbol).getTimeSeries();
            if (map.containsKey(date.toString())) {
                return map.get(date.toString()).getClose();
            }
        }
        return "";
    }

    /**
     * Fetch Stock prices for the given ticker from the Internet and cache them into a local file
     * @param stockSymbol the stock ticker
     */
    private void refresh(String stockSymbol) {
        read(stockSymbol);
        if (stockValues.containsKey(stockSymbol)) return;
        StockInfo stockInfo = CheckingStock.fetchingData(stockSymbol);
        if (stockInfo != null) {
            store(stockInfo);
            stockValues.put(stockSymbol, stockInfo);
        }
    }

    /**
     * This method must be called first. Try to retrieve cached stock information from text file
     * Expect to use JDBC and retrieve data from a database later
     */
    private void read(String stockSymbol) {
        StockInfo stockInfo = dbManager.getStock(stockSymbol);
        if (stockInfo != null) {
            stockValues.put(stockSymbol, stockInfo);
        }
    }

    /**
     * Convert current stock prices in memory to JSON String and store to cache
     * Expect to use JDBC and store to a database later
     * @param stockInfo
     */
    private void store(StockInfo stockInfo) {
        dbManager.storeStock(stockInfo);
    }
}