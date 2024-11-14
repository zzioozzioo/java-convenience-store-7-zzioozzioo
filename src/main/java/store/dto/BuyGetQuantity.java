package store.dto;

public class BuyGetQuantity {
    private final int buyQuantity;
    private final int getQuantity;

    public BuyGetQuantity(int buyQuantity, int getQuantity) {
        this.buyQuantity = buyQuantity;
        this.getQuantity = getQuantity;
    }

    public static BuyGetQuantity of(int buyQuantity, int getQuantity) {
        return new BuyGetQuantity(buyQuantity, getQuantity);
    }

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public int getGetQuantity() {
        return getQuantity;
    }
}
