package store.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {
    @Test
    void 판매_기능_테스트() {
        //given
        Product product = getProduct();

        //when
        int beforeQuantity = product.getQuantity();
        product.sell(3);
        int afterQuantity = product.getQuantity();

        //then
        Assertions.assertThat(afterQuantity).isEqualTo(beforeQuantity - 3);
    }

    @ParameterizedTest
    @ValueSource(ints = {11, 15, 20, 100})
    void 판매_예외_테스트(int quantity) {
        //given
        Product product = getProduct();

        //when & then
        Assertions.assertThatThrownBy(() -> product.sell(quantity))
                .isInstanceOf(IllegalArgumentException.class);

    }

    private Product getProduct() {
        String name = "사이다";
        long price = 1_000L;
        int quantity = 10;
        Promotion promotionName = Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE;
        return new Product(name, price, quantity, promotionName);
    }

}