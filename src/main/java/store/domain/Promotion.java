package store.domain;

public enum Promotion {

    // TODO: SPARKLING 이름 고민해 보기
    NULL(""),
    SPARKLING_BUY_TWO_GET_ONE_FREE("탄산2+1"),
    MD_RECOMMODATION("MD추천상품"),
    FLASH_SALE("반짝할인");

    public String getPromotionName() {
        return promotionName;
    }

    private final String promotionName;

    Promotion(String promotionName) {
        this.promotionName = promotionName;
    }
}
