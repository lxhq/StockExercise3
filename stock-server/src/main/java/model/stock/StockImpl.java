package model.stock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StockImpl implements Stock {

    private String stockSymbol;
    private List<Share> shares;
    /**
     * Constructs an object of Stock with its given parameter
     *
     * @param stockSymbol the given ticker symbol to create a stock object
     */
    public StockImpl(String stockSymbol) {
        this.shares = new ArrayList<>();
        this.stockSymbol = stockSymbol;
    }

    @Override
    public String getStockSymbol() {
        return this.stockSymbol;
    }

    @Override
    public List<Share> getShares() {
        List<Share> res = new ArrayList<>();
        for (Share share : shares) {
            res.add(new Share(share.getShare(), share.getDate()));
        }
        return res;
    }

    @Override
    public void addShare(double share, LocalDate date) {
        this.shares.add(new Share(share, date.toString()));
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Stock Ticker: " + this.stockSymbol + "\n");
        sb.append("Shares:\n");
        for (Share share : shares) {
            sb.append("Buy " + share.getShare() + " shares at " + share.getDate() + "\n");
        }
        return sb.toString();
    }
}