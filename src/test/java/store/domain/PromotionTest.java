package store.domain;

import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PromotionTest {

    @Test
    void 프로모션_상품_재고_세팅_테스트() {
        //given & when
        Promotion.setQuantity(Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE, 2, 1);
        int buyQuantity = Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE.getBuyQuantity();
        int getQuantity = Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE.getGetQuantity();

        //then
        Assertions.assertThat(buyQuantity).isEqualTo(2);
        Assertions.assertThat(getQuantity).isEqualTo(1);
    }

    @Test
    void 프로모션_상품_기간_세팅_테스트() {
        //given & when
        Promotion.setPromotionPeriods(Promotion.FLASH_SALE,
                LocalDateTime.of(2024, 11, 1, 0, 0, 0),
                LocalDateTime.of(2024, 11, 30, 23, 59, 59));
        LocalDateTime startDateTime = Promotion.FLASH_SALE.getStartDateTime();
        LocalDateTime endDateTime = Promotion.FLASH_SALE.getEndDateTime();

        //then
        Assertions.assertThat(startDateTime).isEqualTo(LocalDateTime.of(2024, 11, 1, 0, 0, 0));
        Assertions.assertThat(endDateTime).isEqualTo(LocalDateTime.of(2024, 11, 30, 23, 59, 59));
    }

    @Test
    void 프로모션_기간_유효_테스트() {
        //given
        Promotion.setPromotionPeriods(Promotion.MD_RECOMMENDATION,
                LocalDateTime.of(2024, 1, 1, 0, 0, 0),
                LocalDateTime.of(2024, 12, 31, 23, 59, 59));

        //when
        boolean promotionValid = Promotion.isPromotionValid(Promotion.MD_RECOMMENDATION);

        //then
        Assertions.assertThat(promotionValid).isEqualTo(true);
    }
}