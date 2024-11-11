package store.io.parser;

import static store.constants.NumberConstants.ZERO;
import static store.constants.OutputMessages.CURRENCY_UNIT;
import static store.constants.OutputMessages.OUT_OF_STOCK;
import static store.constants.OutputMessages.QUANTITY_UNIT;
import static store.constants.OutputMessages.WELCOME_MESSAGE;
import static store.constants.StringConstants.DASH;
import static store.constants.StringConstants.EMPTY_STRING;
import static store.constants.StringConstants.NEW_LINE;
import static store.constants.StringConstants.NUMBER_FORMAT_WITH_COMMA;
import static store.constants.StringConstants.ONE_SPACE;

import java.util.List;
import store.domain.Product;
import store.domain.Promotion;

public class ProductListParser {

    // TODO: 재고가 0인 상품은 '재고 없음'으로 출력
    // TODO: 프로모션이 없는 상품은 빈 문자열로 출력

    public String parse(List<Product> products) {
        StringBuilder sb = new StringBuilder();

        appendWelcome(sb);
        products.forEach(product -> appendProduct(sb, product));
        sb.append(NEW_LINE);

        return sb.toString();
    }

    private void appendWelcome(StringBuilder sb) {
        sb.append(WELCOME_MESSAGE);
    }

    private void appendProduct(StringBuilder sb, Product product) {
        sb.append(DASH + ONE_SPACE).append(product.getName()).append(ONE_SPACE)
                .append(getFormattedPrice(product) + ONE_SPACE)
                .append(getRemainingQuantity(product) + ONE_SPACE)
                .append(getPromotionNameOrEmpty(product)).append(NEW_LINE);
    }

    private String getFormattedPrice(Product product) {
        return String.format(NUMBER_FORMAT_WITH_COMMA, product.getPrice()) + CURRENCY_UNIT;
    }

    private String getRemainingQuantity(Product product) {
        int quantity = product.getQuantity();
        if (quantity != ZERO) {
            return quantity + QUANTITY_UNIT;
        }
        return OUT_OF_STOCK;
    }

    private String getPromotionNameOrEmpty(Product product) {
        Promotion promotionName = product.getPromotionName();
        if (!promotionName.equals(Promotion.NULL)) {
            return promotionName.getPromotionName();
        }
        return EMPTY_STRING;
    }

    //  출력 형식
    //- 콜라 1,000원 10개 탄산2+1
    //- 콜라 1,000원 10개
    //- 사이다 1,000원 8개 탄산2+1
    //- 사이다 1,000원 7개
    //- 오렌지주스 1,800원 9개 MD추천상품
    //- 오렌지주스 1,800원 재고 없음
    //- 탄산수 1,200원 5개 탄산2+1
    //- 탄산수 1,200원 재고 없음
    //- 물 500원 10개
    //- 비타민워터 1,500원 6개
    //- 감자칩 1,500원 5개 반짝할인
    //- 감자칩 1,500원 5개
    //- 초코바 1,200원 5개 MD추천상품
    //- 초코바 1,200원 5개
    //- 에너지바 2,000원 5개
    //- 정식도시락 6,400원 8개
    //- 컵라면 1,700원 1개 MD추천상품
    //- 컵라면 1,700원 10개
}
