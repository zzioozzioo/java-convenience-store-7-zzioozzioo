package store.domain;

public class Product {
    private String name;
    private long price;
    private int quantity;
    private Promotion promotionType;

    public Product(String name, long price, int quantity, Promotion promotionType) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotionType = promotionType;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void sell(int quantity) {
        if (quantity > this.quantity) {
            throw new IllegalArgumentException("상품의 재고가 부족합니다.");
        }
        this.quantity -= quantity;
    }
}
