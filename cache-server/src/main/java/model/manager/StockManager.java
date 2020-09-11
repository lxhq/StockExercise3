package model.manager;

public interface StockManager {
    /**
     * Return true if the stockSymbol is cached in memory
     * @param stockSymbol Stock ticker, we want to check
     * @return True for in memory. False for not
     */
    boolean isCached(String stockSymbol);

    /**
     * Return the stock price with the given ticker and date.
     *
     * @param date the given date to get stock value
     * @return the stock value with given parameters
     */
    String searchStockValue(String tickerSymbol, String date);
}
