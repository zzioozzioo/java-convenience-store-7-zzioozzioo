package store.domain;

import static store.domain.Promotion.NULL;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class StoreHouseTest {

    @Test
    void 창고에_상품을_추가할_수_있다() {
        //given
        StoreHouse storeHouse = new StoreHouse();
        String rameon = "컵라면";
        long price = 1_700L;
        int quantity = 10;

        //when
        storeHouse.addProduct(new Product(rameon, price, quantity, NULL));
        int amount = storeHouse.findProductByName("컵라면").getFirst().getQuantity();

        //then
        Assertions.assertThat(amount).isEqualTo(10);
    }

    @Test
    void 창고는_상품_내역을_관리한다() {
        //given
        StoreHouse storeHouse = new StoreHouse();

        Product product = getProduct();
        storeHouse.addProduct(product);

        int beforeStock = storeHouse.findProductByName(product.getName()).getFirst().getQuantity();

        //when
        storeHouse.buy(product, 1);

        //then
        int afterStock = storeHouse.findProductByName(product.getName()).getFirst().getQuantity();
        Assertions.assertThat(afterStock).isEqualTo(beforeStock - 1);
    }

    @Test
    void 재고가_부족한_경우_예외가_발생한다() {
        //given
        StoreHouse storeHouse = new StoreHouse();
        Product product = getProduct();
        storeHouse.addProduct(product);

        //when & then
        Assertions.assertThatThrownBy(() -> storeHouse.buy(product, 12))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Product getProduct() {
        String coke = "콜라";
        long price = 1_000L;
        int quantity = 10;
        return new Product(coke, price, quantity, NULL);
    }

}
