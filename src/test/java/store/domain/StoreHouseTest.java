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
        int amount = storeHouse.getProduct("컵라면").getQuantity();

        //then
        Assertions.assertThat(amount).isEqualTo(10);
    }

    @Test
    void 창고는_상품_내역을_관리한다() {
        //given
        StoreHouse storeHouse = new StoreHouse();

        String coke = "콜라";
        long price = 1_000L;
        int quantity = 10;
        storeHouse.addProduct(new Product(coke, price, quantity, NULL));

        int beforeStock = storeHouse.getProduct(coke).getQuantity();

        //when
        storeHouse.buy(coke, 1);

        //then
        int afterStock = storeHouse.getProduct(coke).getQuantity();
        Assertions.assertThat(afterStock).isEqualTo(beforeStock - 1);
    }

    @Test
    void 창고에_없는_물품을_구매하면_예외가_발생한다() {
        //given
        StoreHouse storeHouse = new StoreHouse();

        //when & then
        Assertions.assertThatThrownBy(() -> storeHouse.buy("라면", 2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 재고가_부족한_경우_예외가_발생한다() {
        //given
        StoreHouse storeHouse = new StoreHouse();
        String coke = "콜라";
        long price = 1_000L;
        int quantity = 1;
        storeHouse.addProduct(new Product(coke, price, quantity, NULL));

        //when & then
        Assertions.assertThatThrownBy(() -> storeHouse.buy(coke, 2))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
