package store.domain;

import static store.constants.NumberConstants.MEMBERSHIP_DISCOUNT_LIMIT;
import static store.constants.NumberConstants.MEMBERSHIP_DISCOUNT_RATIO;
import static store.constants.NumberConstants.ZERO;

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

    public void applyDiscount(Map<Product, Integer> freebieProduct, List<Purchase> purchaseList) {
        if (freebieProduct.isEmpty()) {
            getRegularPriceAmount(purchaseList);
            return;
        }
        getRegularPriceAmountWhenHasFreebieProduct(freebieProduct, purchaseList);
    }

    private void getRegularPriceAmount(List<Purchase> purchaseList) {
        for (Purchase purchase : purchaseList) {
            calculateRegularPriceAmount(purchase);
        }
    }

    private void calculateRegularPriceAmount(Purchase purchase) {
        String productName = purchase.getProductName();
        int quantity = purchase.getQuantity();
        List<Product> products = storeHouse.findProductByName(productName);

        if (!products.isEmpty()) {
            long price = products.getFirst().getPrice();
            regularPriceAmount += (price * quantity);
        }
    }

    private void getRegularPriceAmountWhenHasFreebieProduct(Map<Product, Integer> freebieProduct,
                                                            List<Purchase> purchaseList) {
        for (Purchase purchase : purchaseList) {
            processNonFreebieProductPurchase(freebieProduct, purchase);
        }
    }

    private void processNonFreebieProductPurchase(Map<Product, Integer> freebieProduct, Purchase purchase) {
        String productName = purchase.getProductName();
        for (Product product : freebieProduct.keySet()) {
            if (!product.getName().equals(productName)) {
                calculateRegularPriceAmount(purchase);
            }
        }
    }

    public void calculateDiscountAmount() {
        discountAmount = (long) (regularPriceAmount * MEMBERSHIP_DISCOUNT_RATIO);
        if (discountAmount > MEMBERSHIP_DISCOUNT_LIMIT) {
            discountAmount = MEMBERSHIP_DISCOUNT_LIMIT;
        }
    }

    public void validateDiscountAmount(List<Purchase> purchaseList) {
        long totalPrice = ZERO;
        for (Purchase purchase : purchaseList) {
            List<Product> products = storeHouse.findProductByName(purchase.getProductName());
            totalPrice += products.getFirst().getPrice() * purchase.getQuantity();
        }
        if (totalPrice < discountAmount) {
            discountAmount = totalPrice;
        }
    }

    public long getDiscountAmount() {
        return discountAmount;
    }
}
