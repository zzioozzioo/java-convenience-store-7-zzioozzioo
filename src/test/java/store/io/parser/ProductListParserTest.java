package store.io.parser;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import store.domain.Product;
import store.domain.Promotion;

class ProductListParserTest {

    @Test
    void 상품_목록_파싱_테스트() {
        //given
        ProductListParser parser = new ProductListParser();
        List<Product> products = getProducts();

        //when
        String result = parser.parse(products);

        //then
        Assertions.assertThat(result).isEqualTo(
                """
                        안녕하세요. W편의점입니다.
                        현재 보유하고 있는 상품입니다.
                                                
                        - 콜라 1,000원 10개 탄산2+1
                        - 콜라 1,000원 10개\s
                        - 사이다 1,000원 8개 탄산2+1
                        - 사이다 1,000원 7개\s
                        - 오렌지주스 1,800원 9개 MD추천상품
                        - 오렌지주스 1,800원 재고 없음\s
                        - 탄산수 1,200원 3개 탄산2+1
                        - 탄산수 1,200원 재고 없음\s
                                                
                        """
        );
    }

    private List<Product> getProducts() {
        return List.of(
                new Product("콜라", 1000, 10, Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE),
                new Product("콜라", 1000, 10, Promotion.NULL),
                new Product("사이다", 1000, 8, Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE),
                new Product("사이다", 1000, 7, Promotion.NULL),
                new Product("오렌지주스", 1800, 9, Promotion.MD_RECOMMENDATION),
                new Product("오렌지주스", 1800, 0, Promotion.NULL),
                new Product("탄산수", 1200, 3, Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE),
                new Product("탄산수", 1200, 0, Promotion.NULL)
        );
    }

}