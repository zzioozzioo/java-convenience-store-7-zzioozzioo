package store.io.parser;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import store.domain.MembershipManager;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Receipt;
import store.domain.StoreHouse;
import store.dto.Purchase;

class ReceiptParserTest {

    @Test
    void 영수증_파싱_테스트() {
        //given
        ReceiptParser parser = new ReceiptParser();
        Receipt receipt = getReceipt();
        StoreHouse storeHouse = new StoreHouse();
        storeHouse.addProduct(new Product("콜라", 1000, 10, Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE));
        storeHouse.addProduct(new Product("사이다", 1000, 10, Promotion.NULL));
        MembershipManager manager = new MembershipManager(storeHouse);

        //when
        String result = parser.parse(receipt, storeHouse, manager);

        //then
        Assertions.assertThat(result).isEqualTo(
                """
                        ===========W 편의점=============
                        상품명		수량	금액
                        콜라		3	3,000
                        사이다		1	1,000
                        ===========증	정=============
                        콜라		1
                        ==============================
                        총구매액		4	4,000
                        행사할인			-1,000
                        멤버십할인			-0
                        내실돈			 3,000
                                                
                        """
        );
    }

    private Receipt getReceipt() {
        List<Purchase> purchaseList = List.of(
                new Purchase("콜라", 3),
                new Purchase("사이다", 1)
        );
        Receipt receipt = new Receipt();
        receipt.setPurchaseList(purchaseList);
        receipt.addFreebieProduct(
                new Product("콜라", 1000, 10, Promotion.MD_RECOMMENDATION), 1
        );
        return receipt;
    }

}