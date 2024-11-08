package store.domain;

import store.dto.Purchase;

public class MembershipManager {

    private final StoreHouse storeHouse;

    public MembershipManager(StoreHouse storeHouse) {
        this.storeHouse = storeHouse;
    }

    // TODO: 멤버십 할인 적용했을 때 음수가 되지 않도록 주의
    public long getDiscountAmount(Purchase purchase) {
        String productName = purchase.getProductName();
        int quantity = purchase.getQuantity();
        Product product = storeHouse.getProduct(productName);
        long beforeTotalPrice = product.getPrice() * quantity;
        long discountAmount = (long) (beforeTotalPrice * 0.3);
        if (discountAmount > 8000) {
            discountAmount = 8000;
        }
        return discountAmount;
    }
}
