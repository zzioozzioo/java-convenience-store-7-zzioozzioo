package store.io.parser;

import java.util.List;
import store.domain.Product;
import store.domain.StoreHouse;
import store.dto.Purchase;

public class ReceiptParser {

    public String parse(List<Purchase> purchaseList, StoreHouse storeHouse) {
        StringBuilder sb = new StringBuilder();
        appendPurchaseList(sb, purchaseList, storeHouse);
        appendGetFreeList(sb);
        appendAmountInfo(sb);
        return sb.toString();
    }

    private void appendPurchaseList(StringBuilder sb, List<Purchase> purchaseList, StoreHouse storeHouse) {
        sb.append("===========W 편의점=============" + "\n")
                .append("상품명\t\t수량\t금액" + " \n")
                .append(getFormattedPurchaseList(purchaseList, storeHouse));
    }

    private String getFormattedPurchaseList(List<Purchase> purchaseList, StoreHouse storeHouse) {
        StringBuilder sb = new StringBuilder();
        for (Purchase purchase : purchaseList) {
            String productName = purchase.getProductName();
            int quantity = purchase.getQuantity();
            List<Product> products = storeHouse.findProductByName(productName);
            long price = products.getFirst().getPrice();

            sb.append(productName).append("\t\t").append(quantity).append("\t")
                    .append(String.format("%,d", price * quantity)).append("\n");
        }
        return sb.toString();
    }

    private void appendGetFreeList(StringBuilder sb) {
        // TODO: 증정 상품 저장하는 객체 만들고 나서 구현하기
    }

    private void appendAmountInfo(StringBuilder sb) {

    }

    //출력 형식
    //===========W 편의점=============
    //상품명		수량	금액
    //콜라		3 	3,000
    //에너지바 		5 	10,000
    //===========증	정=============
    //콜라		1
    //==============================
    //총구매액		8	13,000
    //행사할인			-1,000
    //멤버십할인			-3,000
    //내실돈			 9,000
}
