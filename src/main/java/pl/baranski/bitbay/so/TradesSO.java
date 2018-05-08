package pl.baranski.bitbay;

import lombok.Data;

@Data
public class TradesSO {
    private long date;
    private double price;
    private double amount;
    private int tid;
}
