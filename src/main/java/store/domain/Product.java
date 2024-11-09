package store.domain;

import store.exception.OutOfStockQuantityException;

public class Product {
    private final String name;
    private final long price;
    private int quantity;
    private final Promotion promotionName;

    public Product(String name, long price, int quantity, Promotion promotionName) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotionName = promotionName;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Promotion getPromotionName() {
        return promotionName;
    }

    public void sell(int quantity) {
        if (quantity > this.quantity) {
            throw new OutOfStockQuantityException();
        }
        this.quantity -= quantity;
    }
}
