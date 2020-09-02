package client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class CacheManager {
    static private Client client = ClientBuilder.newClient();
    static private String stockValidURI = "http://localhost:9091/stocks/StockValid/";
    static private String stockPriceURI = "http://localhost:9091/stocks/StockValue/";

    static public boolean validTicker(String ticker) {
        String uri = stockValidURI + ticker;
        WebTarget target = client.target(uri);
        return Boolean.valueOf(target.request(MediaType.TEXT_PLAIN).get(String.class));
    }

    static public double stockValue(String ticker, String date) {
        String uri = stockPriceURI + ticker + "/" + date;
        WebTarget target = client.target(uri);
        return Double.valueOf(target.request(MediaType.TEXT_PLAIN).get(String.class));
    }
}
