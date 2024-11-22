package store.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static store.domain.Promotion.NULL;
import static store.domain.Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.dto.BuyGetQuantity;
import store.dto.PromotionInfo;
import store.io.InputValidator;
import store.io.InputView;
import store.io.reader.MissionUtilsReader;
import store.io.writer.SystemWriter;
import store.testutil.ReaderFake;

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
    void 프로모션이_유효한지_확인한다() {
        //given
        PromotionManager manager = getPromotionManager();

        //when
        boolean result = manager.isValidPromotionApplicable(getProduct(), 2);

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
        BuyGetQuantity buyGetQuantity = PromotionManager.getBuyAndGetQuantity(promotionName);

        //then
        Assertions.assertThat(buyGetQuantity.getBuyQuantity()).isEqualTo(1);
        Assertions.assertThat(buyGetQuantity.getGetQuantity()).isEqualTo(1);
    }

    @Test
    void 프로모션_적용_테스트() {
        // given
        ReaderFake readerFake = new ReaderFake();
        readerFake.setInput("Y", "N");

        InputView inputView = new InputView(readerFake, new SystemWriter(), new InputValidator());
        StoreHouse storeHouse = new StoreHouse();

        Product product1 = new Product("콜라", 1000, 7, SPARKLING_BUY_TWO_GET_ONE_FREE);
        Product product2 = new Product("콜라", 1000, 10, NULL);
        storeHouse.addProduct(product1);
        storeHouse.addProduct(product2);

        List<PromotionInfo> promotionInfos = getPromotionInfos();
        PromotionManager manager = new PromotionManager(promotionInfos, storeHouse, inputView);
        manager.setPromotionInfo();

        // when
        Receipt receipt = manager.applyPromotion(product1, 2);
        Map<Product, Integer> freebieProduct = receipt.getFreebieProduct();
        Integer quantity = freebieProduct.get(product1);

        // then
        assertThat(quantity).isEqualTo(1);
    }

    private static List<PromotionInfo> getPromotionInfos() {
        return List.of(
                new PromotionInfo(SPARKLING_BUY_TWO_GET_ONE_FREE, 2, 1,
                        LocalDateTime.parse("2024-01-01T00:00:00"), LocalDateTime.parse("2024-12-31T23:59:59")),
                new PromotionInfo(Promotion.MD_RECOMMENDATION, 1, 1,
                        LocalDateTime.parse("2024-01-01T00:00:00"), LocalDateTime.parse("2024-12-31T23:59:59")),
                new PromotionInfo(Promotion.FLASH_SALE, 1, 1, LocalDateTime.parse("2024-11-01T00:00:00"),
                        LocalDateTime.parse("2024-11-30T23:59:59"))
        );
    }

    private PromotionManager getPromotionManager() {
        return new PromotionManager(getPromotionInfos(), new StoreHouse(), getInputView());
    }

    private Product getProduct() {
        Promotion.setPromotionPeriods(SPARKLING_BUY_TWO_GET_ONE_FREE,
                LocalDateTime.parse("2024-01-01T00:00:00"),
                LocalDateTime.parse("2024-12-31T23:59:59"));
        return new Product("콜라", 1000, 10, SPARKLING_BUY_TWO_GET_ONE_FREE);
    }

    private InputView getInputView() {
        return new InputView(new MissionUtilsReader(), new SystemWriter(),
                new InputValidator());
    }
}
