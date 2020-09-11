package model.repository;

import client.CacheManager;
import com.google.inject.Singleton;
import json.BuyShare;
import model.stock.Share;
import model.stock.Stock;
import model.stock.StockImpl;

import java.util.*;

@Singleton
public class RepositoryImpl implements Repository {

    private Map<String, Stock> stocks;

    public RepositoryImpl() {
        stocks = new HashMap<>();
    }

    @Override
    public Stock createStock(String ticker) {
        if (stocks.containsKey(ticker)) {
            throw new IllegalArgumentException("This stock has already been added to repository");
        }
        if (!CacheManager.validTicker(ticker)) {
            throw new IllegalArgumentException("This ticker is not valid");
        }
        stocks.put(ticker, new StockImpl(ticker));
        return stocks.get(ticker);


    }

    @Override
    public Stock deleteStock(String ticker) {
        if (stocks.containsKey(ticker)) {
            return stocks.remove(ticker);
        }
        throw new IllegalArgumentException("This ticker does not exist in the repository ");
    }

    @Override
    public Stock buyStock(BuyShare buyShare) {
        if (!stocks.containsKey(buyShare.getTicker())) {
            throw new IllegalArgumentException("This ticker does not exist in the repository");
        }
        try {
            CacheManager.stockValue(buyShare.getTicker(), buyShare.getDate());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        Stock stock = stocks.get(buyShare.getTicker());
        stock.addShare(buyShare.getShares().doubleValue(), buyShare.getDate());
        return stock;
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
        throw new IllegalArgumentException("This ticker does not exist in the repository");
    }

    @Override
    public List<Map<String, List<Map<String, String>>>> getStocksPrice() {
        List<Map<String, List<Map<String, String>>>> res = new ArrayList<>();
        for (Stock stock : stocks.values()) {
            Map<String, List<Map<String, String>>> map = new HashMap<>();
            res.add(map);
            List<Map<String, String>> list = new ArrayList<>();
            map.put(stock.getStockSymbol(), list);
            for (Share share : stock.getShares()) {
                Map<String, String> temp = new HashMap<>();
                temp.put("date", share.getDate());
                temp.put("share", String.valueOf(share.getShare()));
                temp.put("prices", CacheManager.stockValue(stock.getStockSymbol(), share.getDate()) + "$");
                list.add(temp);
            }
        }
        return res;
    }
}
