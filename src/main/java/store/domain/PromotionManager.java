package store.domain;

import java.util.List;
import store.dto.BuyGetQuantity;
import store.dto.PromotionInfo;

public class PromotionManager {

    private final List<PromotionInfo> promotionInfos;
    private final StoreHouse storeHouse;

    public PromotionManager(List<PromotionInfo> promotionInfos, StoreHouse storeHouse) {
        this.promotionInfos = promotionInfos;
        this.storeHouse = storeHouse;
    }

    public void setPromotionInfo() {
        promotionInfos.forEach(promotionInfo -> {
            Promotion.setQuantity(
                    promotionInfo.getPromotionName(), promotionInfo.getBuyQuantity(),
                    promotionInfo.getGetQuantity()
            );
            Promotion.setPromotionPeriods(
                    promotionInfo.getPromotionName(), promotionInfo.getStartDateTime(),
                    promotionInfo.getEndDateTime()
            );
        });
    }

    public boolean checkValidPromotion(Promotion promotionName) {
        return Promotion.isPromotionValid(promotionName);
    }

    // TODO: 일반과 프로모션 상품이 있고 구매 수량이 프로모션의 getQuantity 이상이면
    //  프로모션 상품으로 처리
    //  ...아래는 프로모션 상품이 있다는 전제 하에 진행...

    public PromotionResult applyPromotion(Product product, int purchaseQuantity) {

        if (!checkValidPromotion(product.getPromotionName())) {
            return new PromotionResult("", false, 0);
        }

        if (!canApplyPromotion(product, purchaseQuantity)) {
            return new PromotionResult("", false, 0);
        }

        // 프로모션에 해당하는 buy, get 수량
        BuyGetQuantity buyAndGetQuantity = getBuyAndGetQuantity(product.getPromotionName());
        int buyQuantity = buyAndGetQuantity.getBuyQuantity();
        int getQuantity = buyAndGetQuantity.getGetQuantity();

        int promotionStock = product.getQuantity(); // 프로모션 재고

        int totalPromotionQuantity = (purchaseQuantity / buyQuantity) * buyQuantity;
        int promotionAppliedQuantity = Math.min(totalPromotionQuantity, promotionStock);

        if (promotionAppliedQuantity < purchaseQuantity) {
            int regularPriceQuantity = purchaseQuantity - promotionAppliedQuantity;
            return new PromotionResult("현재 " + product.getName() + " " + regularPriceQuantity
                    + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)", true, regularPriceQuantity);
        }

        int remainder = purchaseQuantity % (buyQuantity + getQuantity);
        if (remainder != 0 && remainder < buyQuantity) {
            return new PromotionResult(
                    "현재 " + product.getName() + "은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)", true, 0
            );
        }

        storeHouse.buy(product, purchaseQuantity);

        return new PromotionResult("", false, 0);
    }

    public boolean canApplyPromotion(Product product, int purchaseQuantity) {
        BuyGetQuantity buyAndGetQuantity = getBuyAndGetQuantity(product.getPromotionName());
        int buyQuantity = buyAndGetQuantity.getBuyQuantity();
        return purchaseQuantity >= buyQuantity;
    }

    public BuyGetQuantity getBuyAndGetQuantity(Promotion promotionName) {
        return BuyGetQuantity.of(promotionName.getBuyQuantity(), promotionName.getGetQuantity());
    }


    public List<PromotionInfo> getPromotionInfos() {
        return promotionInfos;
    }
}
