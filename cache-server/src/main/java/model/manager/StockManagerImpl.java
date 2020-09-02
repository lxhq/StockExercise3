package model.manager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import json.StockInfo;
import json.StockPrices;

import javax.inject.Singleton;

@Singleton
public class StockManagerImpl implements StockManager{
    private Map<String, StockInfo> stockValues;

    public StockManagerImpl() {
        this.stockValues = new HashMap<>();
        read();
    }

    public boolean isCached(String stockSymbol) {
        return stockValues.containsKey(stockSymbol);
    }

    public void refresh(String stockSymbol) {
        StockInfo stockInfo = CheckingStock.fetchingData(stockSymbol);
        if (stockInfo != null) {
            store(stockSymbol);
            stockValues.put(stockSymbol, stockInfo);
        }
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
     * This method must be called first. Try to retrieve cached stock information from text file
     * Expect to use JDBC and retrieve data from a database later
     */
    private void read() {
        
    }

    /**
     * Convert current stock prices in memory to JSON String and store to cache
     * Expect to use JDBC and store to a database later
     * @param tickerSymbol
     */
    private void store(String tickerSymbol) {

    }
}