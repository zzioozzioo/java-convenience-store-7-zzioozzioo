package store.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        Map<Product, Integer> freebieProduct = getFreebieProduct();
        List<Purchase> purchaseList = getPurchaseList();

        //when
        manager.applyDiscount(freebieProduct, purchaseList);
        manager.calculateDiscountAmount();
        long discountAmount = manager.getDiscountAmount();

        //then
        Assertions.assertThat(discountAmount).isEqualTo(300L);
    }

    private static void addProducts(StoreHouse storeHouse) {
        Product product1 = new Product("콜라", 1000, 10, Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE);
        Product product2 = new Product("콜라", 1000, 10, Promotion.NULL);
        Product product3 = new Product("물", 500, 10, Promotion.NULL);

        storeHouse.addProduct(product1);
        storeHouse.addProduct(product2);
        storeHouse.addProduct(product3);
    }

    private Map<Product, Integer> getFreebieProduct() {
        return Map.of(new Product("콜라", 1000, 10, Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE), 3);
    }

    private List<Purchase> getPurchaseList() {
        List<Purchase> purchaseList = new ArrayList<>();
        Purchase purchase1 = Purchase.of("콜라", 3);
        Purchase purchase2 = Purchase.of("물", 2);
        purchaseList.add(purchase1);
        purchaseList.add(purchase2);
        return purchaseList;
    }

}