package store.service;

import static store.constants.ErrorMessages.ZERO_QUANTITY;

import java.util.List;
import java.util.Map;
import store.domain.MembershipManager;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.PromotionManager;
import store.domain.StoreHouse;
import store.dto.Purchase;
import store.dto.Receipt;

public class StoreService {

    private final PromotionManager promotionManager;
    private final MembershipManager membershipManager;

    public StoreService(PromotionManager promotionManager, MembershipManager membershipManager) {
        this.promotionManager = promotionManager;
        this.membershipManager = membershipManager;
    }

    public Receipt purchase(List<Purchase> purchaseList, StoreHouse storeHouse) {
        for (Purchase purchase : purchaseList) {
            validatePurchase(purchase, storeHouse);

            if (storeHouse.checkRegularPricePurchase(purchase.getProductName())) {
                purchaseGeneralProduct(storeHouse, purchase);
                return new Receipt();
            }
            return purchasePromotionProduct(purchase, storeHouse);
        }
        return new Receipt();
    }

    private void validatePurchase(Purchase purchase, StoreHouse storeHouse) {
        if (purchase.getQuantity() <= 0) {
            throw new IllegalArgumentException(ZERO_QUANTITY);
        }
        storeHouse.findProductByName(purchase.getProductName());
    }

    private void purchaseGeneralProduct(StoreHouse storeHouse, Purchase purchase) {
        List<Product> products = storeHouse.findProductByName(purchase.getProductName());
        storeHouse.buy(products.getFirst(), purchase.getQuantity());
    }

    private Receipt purchasePromotionProduct(Purchase purchase, StoreHouse storeHouse) {
        List<Product> products = storeHouse.findProductByName(purchase.getProductName());
        Product promotionProduct = null;
        for (Product product : products) {
            if (!product.getPromotionName().equals(Promotion.NULL)) {
                promotionProduct = product;
            }
        }
        promotionManager.setPromotionInfo();
        return promotionManager.applyPromotion(promotionProduct, purchase.getQuantity());
    }

    public void applyMembershipDiscount(Receipt receipt) {
        Map<Product, Integer> freebieProduct = receipt.getFreebieProduct();
        List<Purchase> purchaseList = receipt.getPurchaseList();
        for (Purchase purchase : purchaseList) {
            membershipManager.applyDiscount(freebieProduct, purchase);
        }
        membershipManager.validateDiscountAmount(purchaseList);
    }

}
