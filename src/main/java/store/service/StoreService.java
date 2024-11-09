package store.service;

import java.util.List;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.PromotionManager;
import store.domain.StoreHouse;
import store.dto.Purchase;

public class StoreService {

    private final PromotionManager promotionManager;

    public StoreService(PromotionManager promotionManager) {
        this.promotionManager = promotionManager;
    }

    public void purchase(List<Purchase> purchaseList, StoreHouse storeHouse) {

        // TODO: 재고 변경된 값을 업데이트하려면 어떻게 해야 하지?
        //  그냥 purchaseList를 계속 갖고 다니면서 사용하고, 밖으로 내보내면 되나?
        for (Purchase purchase : purchaseList) {
            validatePurchase(purchase, storeHouse);
            // 일반 구매와 프로모션 구매를 나누기
            purchaseGeneralProduct(storeHouse, purchase);
            // 프로모션 구매
            purchasePromotionProduct(purchase, storeHouse);
        }
    }

    private void validatePurchase(Purchase purchase, StoreHouse storeHouse) {
        if (purchase.getQuantity() <= 0) {
            throw new IllegalArgumentException("구매 수량은 1 이상이어야 합니다.");
        }
        storeHouse.findProductByName(purchase.getProductName());
    }

    private static void purchaseGeneralProduct(StoreHouse storeHouse, Purchase purchase) {
        if (storeHouse.checkRegularPricePurchase(purchase.getProductName())) {
            List<Product> products = storeHouse.findProductByName(purchase.getProductName());
            storeHouse.buy(products.getFirst(), purchase.getQuantity());
        }
    }

    private void purchasePromotionProduct(Purchase purchase, StoreHouse storeHouse) {
        // TODO: 프로모션 구매인 경우 구현
        List<Product> products = storeHouse.findProductByName(purchase.getProductName());
        Product defaultProduct = null;
        Product promotionProduct = null;
        for (Product product : products) {
            if (product.getPromotionName().equals(Promotion.NULL)) {
                defaultProduct = product;
            }
            promotionProduct = product;
        }
        promotionManager.applyPromotion(promotionProduct, purchase.getQuantity());
    }


}
