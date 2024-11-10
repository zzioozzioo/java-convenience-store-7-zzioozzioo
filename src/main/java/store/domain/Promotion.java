package store.domain;

import java.time.LocalDateTime;

public enum Promotion {

    NULL(""),
    SPARKLING_BUY_TWO_GET_ONE_FREE("탄산2+1"),
    MD_RECOMMENDATION("MD추천상품"),
    FLASH_SALE("반짝할인");

    private final String promotionName;
    private int buyQuantity;
    private int getQuantity;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    Promotion(String promotionName) {
        this.promotionName = promotionName;
    }

    static void setQuantity(Promotion promotionName, int buyQuantity, int getQuantity) {
        promotionName.buyQuantity = buyQuantity;
        promotionName.getQuantity = getQuantity;
    }

    static void setPromotionPeriods(Promotion promotionName, LocalDateTime startDateTime,
                                    LocalDateTime endDateTime) {
        promotionName.startDateTime = startDateTime;
        promotionName.endDateTime = endDateTime;
    }

    public static boolean isPromotionValid(Promotion promotionName) {
        LocalDateTime now = camp.nextstep.edu.missionutils.DateTimes.now();
        return !now.isBefore(promotionName.startDateTime) && !now.isAfter(promotionName.endDateTime);
    }

    public String getPromotionName() {
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
