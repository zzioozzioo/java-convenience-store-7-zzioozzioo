package store.domain;

import java.util.List;
import java.util.Map;
import store.dto.Purchase;

public class MembershipManager {

    private final StoreHouse storeHouse;
    private long regularPriceAmount;
    private long discountAmount;

    public MembershipManager(StoreHouse storeHouse) {
        this.storeHouse = storeHouse;
    }

    public void applyDiscount(Map<Product, Integer> freebieProduct, Purchase purchase) {
        String productName = purchase.getProductName();
        for (Product product : freebieProduct.keySet()) {
            calculateRegularPriceAmount(product, productName);
        }
        calculateDiscountAmount();
    }

    private void calculateRegularPriceAmount(Product product, String productName) {
        if (!product.getName().equals(productName)) {
            List<Product> products = storeHouse.findProductByName(productName);
            long price = products.getFirst().getPrice();
            regularPriceAmount += price;
        }
    }

    private void calculateDiscountAmount() {
        discountAmount = (long) (regularPriceAmount * 0.3);
        if (discountAmount > 8_000L) {
            discountAmount = 8_000L;
        }
    }

    public void validateDiscountAmount(List<Purchase> purchaseList) {
        long totalPrice = 0;
        for (Purchase purchase : purchaseList) {
            List<Product> products = storeHouse.findProductByName(purchase.getProductName());
            totalPrice += products.getFirst().getPrice();
        }
        if (totalPrice < discountAmount) {
            discountAmount = totalPrice;
        }
    }

    public long getDiscountAmount() {
        return discountAmount;
    }
}
