package store.domain;

public class Product {
    private String name;
    private long price;
    private int quantity;
    private Promotion promotionName;

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
            throw new IllegalArgumentException("상품의 재고가 부족합니다.");
        }
        this.quantity -= quantity;
    }
}
