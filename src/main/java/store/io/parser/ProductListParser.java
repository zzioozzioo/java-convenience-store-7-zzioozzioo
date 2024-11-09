package store.io.parser;

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
        sb.append("\n");

        return sb.toString();
    }

    private void appendWelcome(StringBuilder sb) {
        sb.append("안녕하세요. W편의점입니다.")
                .append("\n")
                .append("현재 보유하고 있는 상품입니다.")
                .append("\n\n");
    }

    private void appendProduct(StringBuilder sb, Product product) {
        sb.append("-" + " ")
                .append(product.getName() + " ")
                .append(getFormattedPrice(product) + " ")
                .append(getRemainingQuantity(product) + " ")
                .append(getPromotionNameOrEmpty(product))
                .append("\n");
    }

    private String getFormattedPrice(Product product) {
        return String.format("%,d", product.getPrice()) + "원";
    }

    private String getRemainingQuantity(Product product) {
        int quantity = product.getQuantity();
        if (quantity != 0) {
            return quantity + "개";
        }
        return "재고 없음";
    }

    private String getPromotionNameOrEmpty(Product product) {
        Promotion promotionName = product.getPromotionName();
        if (!promotionName.equals(Promotion.NULL)) {
            return promotionName.getPromotionName();
        }
        return "";
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
