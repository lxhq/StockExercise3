package model.repository;

import client.CacheManager;
import com.google.inject.Singleton;
import json.BuyShare;
import model.stock.Stock;
import model.stock.StockImpl;

import java.time.LocalDate;
import java.util.*;

@Singleton
public class RepositoryImpl implements Repository {

    private Map<String, Stock> stocks;

    public RepositoryImpl() {
        stocks = new HashMap<>();
    }

    @Override
    public Stock createStock(String ticker) {
        if (!stocks.containsKey(ticker) && CacheManager.validTicker(ticker)) {
            stocks.put(ticker, new StockImpl(ticker));
            return stocks.get(ticker);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Stock deleteStock(String ticker) {
        if (stocks.containsKey(ticker)) {
            return stocks.remove(ticker);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Stock buyStock(BuyShare buyShare) {
        if (stocks.containsKey(buyShare.getTicker())) {
            stocks.get(buyShare.getTicker())
                    .addShare(buyShare.getShares().doubleValue(),
                            LocalDate.parse(buyShare.getDate()));
            return stocks.get(buyShare.getTicker());
        }
        throw new IllegalArgumentException();
    }

    @Override
    public List<Stock> getAllStocks() {
        return new ArrayList<>(stocks.values());
    }

    @Override
    public Stock getStock(String ticker) {
        if (stocks.containsKey(ticker)) {
            return stocks.get(ticker);
        }
        throw new IllegalArgumentException();
    }
}
