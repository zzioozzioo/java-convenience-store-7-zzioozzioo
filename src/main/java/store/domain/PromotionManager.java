package store.domain;

import java.time.LocalDateTime;
import java.util.List;
import store.dto.BuyGetQuantity;
import store.dto.PromotionInfo;

public class PromotionManager {

    private final List<PromotionInfo> promotionInfos;

    public PromotionManager(List<PromotionInfo> promotionInfos) {
        this.promotionInfos = promotionInfos;
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

    public boolean checkValidPromotion(Promotion promotionName, LocalDateTime now) {
        return Promotion.isPromotionValid(promotionName, now);
    }

    public BuyGetQuantity getBuyAndGetQuantity(Promotion promotionName) {
        return BuyGetQuantity.of(promotionName.getBuyQuantity(), promotionName.getGetQuantity());
    }
    // TODO: 일반과 프로모션 상품이 있고 구매 수량이 프로모션의 getQuantity 이상이면

    //  프로모션 상품으로 처리

    public List<PromotionInfo> getPromotionInfos() {
        return promotionInfos;
    }
}
