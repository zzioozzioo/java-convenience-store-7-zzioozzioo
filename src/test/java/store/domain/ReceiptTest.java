package store.domain;

import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ReceiptTest {
    @Test
    void 증정품을_품목에_추가한다() {
        //given
        Receipt receipt = new Receipt();
        Product product = getProduct();
        int quantity = 3;

        //when
        receipt.addFreebieProduct(product, quantity);
        Map<Product, Integer> freebieProduct = receipt.getFreebieProduct();

        //then
        Assertions.assertThat(freebieProduct.size()).isEqualTo(1);
        Assertions.assertThat(freebieProduct.get(product)).isEqualTo(3);
    }

    private Product getProduct() {
        String name = "사이다";
        long price = 1_000L;
        int quantity = 10;
        Promotion promotionName = Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE;
        return new Product(name, price, quantity, promotionName);
    }

}