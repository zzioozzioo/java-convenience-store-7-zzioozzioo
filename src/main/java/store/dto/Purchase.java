package store.dto;

public class Purchase {
    private final String productName;
    private final int quantity;

    public Purchase(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public static Purchase of(String productName, int quantity) {
        return new Purchase(productName, quantity);
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }
}
