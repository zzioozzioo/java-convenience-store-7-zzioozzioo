package store.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import store.dto.Purchase;

class MembershipManagerTest {

    @Test
    void 프로모션_미적용_금액의_30프로를_할인한다() {
        //given
        StoreHouse storeHouse = new StoreHouse();
        addProducts(storeHouse);

        MembershipManager manager = new MembershipManager(storeHouse);

        String productName = "콜라";
        int quantity = 3;
        Purchase purchase = Purchase.of(productName, quantity);

        //when
        long result = manager.getDiscountAmount(purchase);

        //then
        Assertions.assertThat(result).isEqualTo(900L);
    }

    private static void addProducts(StoreHouse storeHouse) {
        Product product1 = new Product("콜라", 1000, 10, Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE);
        Product product2 = new Product("콜라", 1000, 10, Promotion.NULL);
        storeHouse.addProduct(product1);
        storeHouse.addProduct(product2);
    }

}