package store.constants;

import static store.constants.StringConstants.NEW_LINE;
import static store.constants.StringConstants.ONE_SPACE;
import static store.constants.StringConstants.TAP;

public class OutputMessages {
    public static String WELCOME_MESSAGE = "안녕하세요. W편의점입니다." + NEW_LINE + "현재 보유하고 있는 상품입니다." + NEW_LINE.repeat(2);
    public static String PURCHASER_LIST_FORMAT =
            "===========W 편의점=============" + NEW_LINE + "상품명" + TAP.repeat(2) + "수량" + TAP + "금액" + NEW_LINE;
    public static String FREEBIE_LIST_FORMAT = "===========증" + TAP + "정=============" + NEW_LINE;
    public static String AMOUNT_INFORMATION_FORMAT = "==============================" + NEW_LINE;
    
    public static String TOTAL_PURCHASE_AMOUNT = "총구매액" + TAP.repeat(2);
    public static String PROMOTION_DISCOUNT = "행사할인" + TAP.repeat(3);
    public static String MEMBERSHIP_DISCOUNT = "멤버십할인" + TAP.repeat(3);
    public static String TOTAL_PRICE = "내실돈" + TAP.repeat(3) + ONE_SPACE;

    public static String CURRENCY_UNIT = "원";
    public static String QUANTITY_UNIT = "개";
    public static String OUT_OF_STOCK = "재고 없음";

}
