package store.domain;

import static store.domain.Promotion.NULL;
import static store.domain.Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE;

import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.dto.BuyGetQuantity;
import store.dto.PromotionInfo;

class PromotionManagerTest {

    @ParameterizedTest
    @CsvSource(value = {
            "0, SPARKLING_BUY_TWO_GET_ONE_FREE, 2, 1, 2024-01-01T00:00:00, 2024-12-31T23:59:59",
            "1, MD_RECOMMENDATION, 1, 1, 2024-01-01T00:00:00, 2024-12-31T23:59:59",
            "2, FLASH_SALE, 1, 1, 2024-11-01T00:00:00, 2024-11-30T23:59:59"
    })
    void 프로모션_정보를_관리한다(int index, Promotion promotionName, int buyQuantity, int getQuantity,
                       LocalDateTime startDateTime, LocalDateTime endDateTime) {
        //given
        PromotionManager manager = getPromotionManager();

        //when
        manager.setPromotionInfo();

        //then
        Assertions.assertThat(manager.getPromotionInfos().size()).isEqualTo(3);
        Assertions.assertThat(manager.getPromotionInfos().get(index).getPromotionName())
                .isEqualTo(promotionName);
        Assertions.assertThat(manager.getPromotionInfos().get(index).getBuyQuantity())
                .isEqualTo(buyQuantity);
        Assertions.assertThat(manager.getPromotionInfos().get(index).getGetQuantity())
                .isEqualTo(getQuantity);
        Assertions.assertThat(manager.getPromotionInfos().get(index).getStartDateTime())
                .isEqualTo(startDateTime);
        Assertions.assertThat(manager.getPromotionInfos().get(index).getEndDateTime())
                .isEqualTo(endDateTime);
    }

    @Test
    void 오늘_날짜가_프로모션_기간_내에_포함된_경우_프로모션을_적용한다() {
        //given
        PromotionManager manager = getPromotionManager();
        manager.setPromotionInfo();

        Promotion promotionName = Promotion.MD_RECOMMENDATION;

        //when
        boolean result = manager.checkValidPromotion(promotionName);

        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void 구매_수량과_증정_수량을_반환한다() {
        //given
        PromotionManager manager = getPromotionManager();
        manager.setPromotionInfo();

        Promotion promotionName = Promotion.FLASH_SALE;

        //when
        BuyGetQuantity buyGetQuantity = manager.getBuyAndGetQuantity(promotionName);

        //then
        Assertions.assertThat(buyGetQuantity.getBuyQuantity()).isEqualTo(1);
        Assertions.assertThat(buyGetQuantity.getGetQuantity()).isEqualTo(1);
    }

    @Test
    void 프로모션_적용_테스트() {
        //given
        PromotionManager manager = getPromotionManager();
        manager.setPromotionInfo();

        StoreHouse storeHouse = new StoreHouse();
        Product product1 = new Product("콜라", 1000, 7, SPARKLING_BUY_TWO_GET_ONE_FREE);
        Product product2 = new Product("콜라", 1000, 10, NULL);
        storeHouse.addProduct(product1);
        storeHouse.addProduct(product2);

        //when
        PromotionResult actualResult = manager.applyPromotion(
                new Product("콜라", 1000, 7, SPARKLING_BUY_TWO_GET_ONE_FREE), 10);
        PromotionResult expectedResult = new PromotionResult("현재 콜라 4개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)", true,
                4);

        //then
        Assertions.assertThat(actualResult.getMessage()).isEqualTo(expectedResult.getMessage());
        Assertions.assertThat(actualResult.getRequiresInput()).isEqualTo(expectedResult.getRequiresInput());
        Assertions.assertThat(actualResult.getRegularPriceQuantity())
                .isEqualTo(expectedResult.getRegularPriceQuantity());
    }

    private PromotionManager getPromotionManager() {
        List<PromotionInfo> promotionInfos = List.of(
                new PromotionInfo(SPARKLING_BUY_TWO_GET_ONE_FREE, 2, 1,
                        LocalDateTime.parse("2024-01-01T00:00:00"), LocalDateTime.parse("2024-12-31T23:59:59")),
                new PromotionInfo(Promotion.MD_RECOMMENDATION, 1, 1,
                        LocalDateTime.parse("2024-01-01T00:00:00"), LocalDateTime.parse("2024-12-31T23:59:59")),
                new PromotionInfo(Promotion.FLASH_SALE, 1, 1, LocalDateTime.parse("2024-11-01T00:00:00"),
                        LocalDateTime.parse("2024-11-30T23:59:59"))
        );
        return new PromotionManager(promotionInfos, new StoreHouse());
    }
}
