package store.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import store.dto.Purchase;

class MembershipManagerTest {

    @Test
    void 멤버십_할인_기능_테스트() {
        //given
        StoreHouse storeHouse = new StoreHouse();
        addProducts(storeHouse);
        MembershipManager manager = new MembershipManager(storeHouse);

        Map<Product, Integer> freebieProduct = getFreebieProduct();
        List<Purchase> purchaseList = getPurchaseList1();

        //when
        manager.applyDiscount(freebieProduct, purchaseList);
        manager.calculateDiscountAmount();
        manager.validateDiscountAmount(purchaseList);
        long discountAmount = manager.getDiscountAmount();

        //then
        Assertions.assertThat(discountAmount).isEqualTo(300L);
    }

    @Test
    void 멤버십_할인_한도_테스트() {
        //given
        StoreHouse storeHouse = new StoreHouse();
        addProducts(storeHouse);
        MembershipManager manager = new MembershipManager(storeHouse);

        Map<Product, Integer> freebieProduct = getFreebieProduct();
        List<Purchase> purchaseList = getPurchaseList2();

        //when
        manager.applyDiscount(freebieProduct, purchaseList);
        manager.calculateDiscountAmount();
        manager.validateDiscountAmount(purchaseList);

        long discountAmount = manager.getDiscountAmount();

        //then
        Assertions.assertThat(discountAmount).isEqualTo(8_000L);
    }

    private static void addProducts(StoreHouse storeHouse) {
        Product product1 = new Product("콜라", 1000, 10, Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE);
        Product product2 = new Product("콜라", 1000, 10, Promotion.NULL);
        Product product3 = new Product("물", 500, 10, Promotion.NULL);
        Product product4 = new Product("정식도시락", 6400, 8, Promotion.NULL);

        storeHouse.addProduct(product1);
        storeHouse.addProduct(product2);
        storeHouse.addProduct(product3);
        storeHouse.addProduct(product4);
    }

    private Map<Product, Integer> getFreebieProduct() {
        return Map.of(new Product("콜라", 1000, 10, Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE), 3);
    }

    private List<Purchase> getPurchaseList1() {
        List<Purchase> purchaseList = new ArrayList<>();
        Purchase purchase1 = Purchase.of("콜라", 3);
        Purchase purchase2 = Purchase.of("물", 2);
        purchaseList.add(purchase1);
        purchaseList.add(purchase2);
        return purchaseList;
    }

    private List<Purchase> getPurchaseList2() {
        List<Purchase> purchaseList = new ArrayList<>();
        Purchase purchase = Purchase.of("정식도시락", 8);
        purchaseList.add(purchase);
        return purchaseList;
    }

}