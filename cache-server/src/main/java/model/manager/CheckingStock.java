package model.manager;

import json.StockInfo;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * This is a helper class to get some values about stock.
 */
public final class CheckingStock {

    static private Client client = ClientBuilder.newClient();
    static private String uri = "https://www.alphavantage.co/query";
    static private String apiKey = "537ORK8WW95GYYS2";
    /**
     * Fetching the data with given tickerSymbol.
     *
     * @param tickerSymbol the given parameter
     * @return the stock information
     */
    public static StockInfo fetchingData(String tickerSymbol) {

        WebTarget target = client.target(uri);
        try {
            StockInfo stockInfo = target.queryParam("function", "TIME_SERIES_DAILY")
                    .queryParam("outputsize", "full")
                    .queryParam("symbol", tickerSymbol)
                    .queryParam("apikey", apiKey)
                    .request(MediaType.APPLICATION_JSON)
                    .get(StockInfo.class);
            return stockInfo;
        } catch (Exception e) {
            return null;
        }
    }
}
