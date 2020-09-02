package json;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Map;

public class StockInfo {
    private MetaData metaData;
    private Map<String, StockPrices> timeSeries;

    public StockInfo() {
    }

    public Map<String, StockPrices> getTimeSeries() {
        return timeSeries;
    }

    @JsonSetter("Time Series (Daily)")
    public void setTimeSeries(Map<String, StockPrices> timeSeries) {
        this.timeSeries = timeSeries;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    @JsonSetter("Meta Data")
    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

}
