package store.constants;

import static store.constants.StringConstants.NEW_LINE;

public class InputMessages {
    public static final String INPUT_PRODUCT_NAME_AND_QUANTITY =
            "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])" + NEW_LINE;
    public static final String FREEBIE_ADDITION_MESSAGE = "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)" + NEW_LINE;
    public static final String REGULAR_PRICE_BUY_MESSAGE =
            "현재 %s %s개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)" + NEW_LINE;
    public static final String MEMBERSHIP_DISCOUNT_CHOICE_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)" + NEW_LINE;
    public static final String ADDITIONAL_PURCHASE_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)" + NEW_LINE;

    public final static String PRODUCTS_FILE_NAME = "src/main/resources/products.md";
    public final static String PROMOTIONS_FILE_NAME = "src/main/resources/promotions.md";

}
