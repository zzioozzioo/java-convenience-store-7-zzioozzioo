package store.io.parser;

import static store.constants.StringConstants.NEW_LINE;

import java.util.List;
import java.util.Map;
import store.constants.StringConstants;
import store.domain.MembershipManager;
import store.domain.Product;
import store.domain.StoreHouse;
import store.dto.Purchase;
import store.dto.Receipt;

public class ReceiptParser {

    public String parse(Receipt receipt, StoreHouse storeHouse, MembershipManager membershipManager) {
        StringBuilder sb = new StringBuilder();
        appendPurchaseList(sb, receipt.getPurchaseList(), storeHouse);
        appendFreebieList(sb, receipt);
        appendAmountInfo(sb, receipt, storeHouse, membershipManager);
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

    private void appendFreebieList(StringBuilder sb, Receipt receipt) {
        sb.append("===========증\t정=============\n")
                .append(getFormattedFreebieList(receipt));
    }

    private String getFormattedFreebieList(Receipt receipt) {
        StringBuilder sb = new StringBuilder();
        Map<Product, Integer> freebieProduct = receipt.getFreebieProduct();
        for (Product product : freebieProduct.keySet()) {
            sb.append(product.getName())
                    .append("\t\t")
                    .append(freebieProduct.get(product))
                    .append(NEW_LINE);
        }
        return sb.toString();
    }

    private void appendAmountInfo(StringBuilder sb, Receipt receipt, StoreHouse storeHouse,
                                  MembershipManager membershipManager) {
        sb.append("==============================\n")
                .append("총구매액\t\t").append(getTotalPurchaseCount(receipt)).append("\t")
                .append(getTotalPurchaseAmount(receipt, storeHouse) + NEW_LINE)
                .append("행사할인\t\t\t")
                .append(StringConstants.DASH + getTotalPromotionDiscountAmount(receipt, storeHouse) + NEW_LINE)
                .append("멤버십할인\t\t\t")
                .append(StringConstants.DASH + getTotalMembershipDiscountAmount(membershipManager) + NEW_LINE)
                .append("내실돈\t\t\t ").append(getTotalPrice(receipt, storeHouse, membershipManager)).append("\n\n");
    }

    private String getTotalPrice(Receipt receipt, StoreHouse storeHouse, MembershipManager membershipManager) {
        long totalPurchaseAmount = Long.parseLong(getTotalPurchaseAmount(receipt, storeHouse).replace(",", ""));
        long totalPromotionDiscountAmount = Long.parseLong(
                getTotalPromotionDiscountAmount(receipt, storeHouse).replace(",", ""));
        long totalMembershipDiscountAmount = Long.parseLong(
                getTotalMembershipDiscountAmount(membershipManager).replace(",", ""));
        long result = totalPurchaseAmount - totalPromotionDiscountAmount - totalMembershipDiscountAmount;
        return String.format("%,d", result);
    }

    private String getTotalMembershipDiscountAmount(MembershipManager membershipManager) {
        return String.format("%,d", membershipManager.getDiscountAmount());
    }

    private int getTotalPurchaseCount(Receipt receipt) {
        List<Purchase> purchaseList = receipt.getPurchaseList();
        return purchaseList.stream()
                .mapToInt(Purchase::getQuantity)
                .sum();
    }

    private String getTotalPurchaseAmount(Receipt receipt, StoreHouse storeHouse) {
        // TODO: 스트림으로 변경 고민해 보기
        List<Purchase> purchaseList = receipt.getPurchaseList();
        long sum = 0;
        for (Purchase purchase : purchaseList) {
            List<Product> products = storeHouse.findProductByName(purchase.getProductName());
            sum += products.getFirst().getPrice() * purchase.getQuantity();
        }
        return String.format("%,d", sum);
    }

    private String getTotalPromotionDiscountAmount(Receipt receipt, StoreHouse storeHouse) {
        // TODO: 스트림으로 변경 고민해 보기
        Map<Product, Integer> freebieProduct = receipt.getFreebieProduct();
        long sum = 0;
        for (Product product : freebieProduct.keySet()) {
            List<Product> products = storeHouse.findProductByName(product.getName());
            sum += products.getFirst().getPrice() * freebieProduct.get(product);
        }
        return String.format("%,d", sum);
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
