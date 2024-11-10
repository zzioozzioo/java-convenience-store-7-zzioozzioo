package store.service;

import static store.constants.ErrorMessages.ZERO_QUANTITY;

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

        for (Purchase purchase : purchaseList) {
            validatePurchase(purchase, storeHouse);

            if (storeHouse.checkRegularPricePurchase(purchase.getProductName())) {
                purchaseGeneralProduct(storeHouse, purchase);
                return;
            }
            // 프로모션 구매
            purchasePromotionProduct(purchase, storeHouse);
        }
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

    private void purchasePromotionProduct(Purchase purchase, StoreHouse storeHouse) {
        // TODO: 프로모션 구매인 경우 구현
        List<Product> products = storeHouse.findProductByName(purchase.getProductName());
//        Product defaultProduct = null;
        Product promotionProduct = null;
        for (Product product : products) {
            if (!product.getPromotionName().equals(Promotion.NULL)) {
//                defaultProduct = product;
                promotionProduct = product;
            }
        }
        promotionManager.setPromotionInfo();
        promotionManager.applyPromotion(promotionProduct, purchase.getQuantity());
    }


}
