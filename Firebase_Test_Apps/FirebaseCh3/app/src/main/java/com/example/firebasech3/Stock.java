package com.example.firebasech3;

public class Stock {
    public String stockTicker;
    public String stockName;
    public int stockQuantity;

    public Stock(){}
    public Stock(String ticker, String name, int quantity ){
        this.stockTicker = ticker;
        this.stockName = name;
        this.stockQuantity = quantity;
    }
}
