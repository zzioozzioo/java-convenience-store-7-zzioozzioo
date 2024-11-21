package store.service;

import java.util.List;
import java.util.Map;
import store.domain.MembershipManager;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.PromotionManager;
import store.domain.Receipt;
import store.domain.StoreHouse;
import store.dto.Purchase;

public class StoreService {

    private final PromotionManager promotionManager;
    private final MembershipManager membershipManager;

    public StoreService(PromotionManager promotionManager, MembershipManager membershipManager) {
        this.promotionManager = promotionManager;
        this.membershipManager = membershipManager;
    }

    public Receipt purchase(List<Purchase> purchaseList, StoreHouse storeHouse) {
        for (Purchase purchase : purchaseList) {
            storeHouse.validatePurchase(purchase);

            if (storeHouse.checkRegularPricePurchase(purchase.getProductName())) { // 일반 상품만을 구매한 경우
                purchaseGeneralProduct(purchase, storeHouse);
                return new Receipt();
            }
            return purchasePromotionProduct(purchase, storeHouse); // 프로모션 상품이 포함된 경우
        }
        return new Receipt();
    }


    private void purchaseGeneralProduct(Purchase purchase, StoreHouse storeHouse) {
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
        membershipManager.applyDiscount(freebieProduct, purchaseList);
        membershipManager.calculateDiscountAmount();
        membershipManager.validateDiscountAmount(purchaseList);
    }

}
