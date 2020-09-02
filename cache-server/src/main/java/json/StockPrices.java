package json;

import com.fasterxml.jackson.annotation.JsonSetter;

public class StockPrices {
    String open;
    String high;
    String low;
    String close;
    String volume;

    public StockPrices() {
    }

    public String getOpen() {
        return open;
    }

    @JsonSetter("1. open")
    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    @JsonSetter("2. high")
    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    @JsonSetter("3. low")
    public void setLow(String low) {
        this.low = low;
    }

    public String getClose() {
        return close;
    }

    @JsonSetter("4. close")
    public void setClose(String close) {
        this.close = close;
    }

    public String getVolume() {
        return volume;
    }

    @JsonSetter("5. volume")
    public void setVolume(String volume) {
        this.volume = volume;
    }
}
