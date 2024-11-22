package store.dto;

import java.time.LocalDateTime;
import store.domain.Promotion;

public class PromotionInfo {

    private final Promotion promotionName;
    private final int buyQuantity;
    private final int getQuantity;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public PromotionInfo(Promotion promotionName, int buyQuantity, int getQuantity,
                         LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.promotionName = promotionName;
        this.buyQuantity = buyQuantity;
        this.getQuantity = getQuantity;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public static PromotionInfo of(Promotion promotionName, int buyQuantity, int getQuantity,
                                   LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return new PromotionInfo(promotionName, buyQuantity, getQuantity, startDateTime, endDateTime);
    }

    public Promotion getPromotionName() {
        return promotionName;
    }

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public int getGetQuantity() {
        return getQuantity;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }
}
