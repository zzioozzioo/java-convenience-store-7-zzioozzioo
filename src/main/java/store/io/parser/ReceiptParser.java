package store.io.parser;

import static store.constants.NumberConstants.ZERO;
import static store.constants.OutputMessages.AMOUNT_INFORMATION_FORMAT;
import static store.constants.OutputMessages.FREEBIE_LIST_FORMAT;
import static store.constants.OutputMessages.MEMBERSHIP_DISCOUNT;
import static store.constants.OutputMessages.PROMOTION_DISCOUNT;
import static store.constants.OutputMessages.PURCHASER_LIST_FORMAT;
import static store.constants.OutputMessages.TOTAL_PRICE;
import static store.constants.OutputMessages.TOTAL_PURCHASE_AMOUNT;
import static store.constants.StringConstants.COMMA;
import static store.constants.StringConstants.EMPTY_STRING;
import static store.constants.StringConstants.NEW_LINE;
import static store.constants.StringConstants.NUMBER_FORMAT_WITH_COMMA;
import static store.constants.StringConstants.TAP;

import java.util.List;
import java.util.Map;
import store.constants.StringConstants;
import store.domain.MembershipManager;
import store.domain.Product;
import store.domain.Receipt;
import store.domain.StoreHouse;
import store.dto.Purchase;

public class ReceiptParser {

    public String parse(Receipt receipt, StoreHouse storeHouse, MembershipManager membershipManager) {
        StringBuilder sb = new StringBuilder();
        appendPurchaseList(sb, receipt, storeHouse);
        appendFreebieList(sb, receipt);
        appendAmountInfo(sb, receipt, storeHouse, membershipManager);
        return sb.toString();
    }

    private void appendPurchaseList(StringBuilder sb, Receipt receipt, StoreHouse storeHouse) {
        sb.append(PURCHASER_LIST_FORMAT)
                .append(getFormattedPurchaseList(receipt, storeHouse));
    }

    private String getFormattedPurchaseList(Receipt receipt, StoreHouse storeHouse) {
        StringBuilder sb = new StringBuilder();
        appendFormattedPurchaseList(receipt, storeHouse, sb);
        return sb.toString();
    }

    private static void appendFormattedPurchaseList(Receipt receipt, StoreHouse storeHouse, StringBuilder sb) {
        for (Purchase purchase : receipt.getPurchaseList()) {
            String productName = purchase.getProductName();
            int quantity = purchase.getQuantity();
            List<Product> products = storeHouse.findProductByName(productName);
            long price = products.getFirst().getPrice();
            sb.append(productName).append(TAP.repeat(2)).append(quantity).append(TAP)
                    .append(String.format(NUMBER_FORMAT_WITH_COMMA, price * quantity)).append(NEW_LINE);
        }
    }

    private void appendFreebieList(StringBuilder sb, Receipt receipt) {
        sb.append(FREEBIE_LIST_FORMAT)
                .append(getFormattedFreebieList(receipt));
    }

    private String getFormattedFreebieList(Receipt receipt) {
        StringBuilder sb = new StringBuilder();
        Map<Product, Integer> freebieProduct = receipt.getFreebieProduct();
        for (Product product : freebieProduct.keySet()) {
            sb.append(product.getName())
                    .append(TAP.repeat(2))
                    .append(freebieProduct.get(product))
                    .append(NEW_LINE);
        }
        return sb.toString();
    }

    private void appendAmountInfo(StringBuilder sb, Receipt receipt, StoreHouse storeHouse,
                                  MembershipManager membershipManager) {
        sb.append(AMOUNT_INFORMATION_FORMAT);
        appendDetailedAmountInfo(sb, receipt, storeHouse, membershipManager);

    }

    private void appendDetailedAmountInfo(StringBuilder sb, Receipt receipt, StoreHouse storeHouse,
                                          MembershipManager membershipManager) {
        sb.append(TOTAL_PURCHASE_AMOUNT).append(getTotalPurchaseCount(receipt)).append(TAP)
                .append(getTotalPurchaseAmount(receipt, storeHouse)).append(NEW_LINE)
                .append(PROMOTION_DISCOUNT).append(StringConstants.DASH)
                .append(getTotalPromotionDiscountAmount(receipt, storeHouse)).append(NEW_LINE)
                .append(MEMBERSHIP_DISCOUNT).append(StringConstants.DASH)
                .append(getTotalMembershipDiscountAmount(membershipManager)).append(NEW_LINE)
                .append(TOTAL_PRICE).append(getTotalPrice(receipt, storeHouse, membershipManager))
                .append(NEW_LINE.repeat(2));
    }

    private String getTotalMembershipDiscountAmount(MembershipManager membershipManager) {
        return String.format(NUMBER_FORMAT_WITH_COMMA, membershipManager.getDiscountAmount());
    }

    private int getTotalPurchaseCount(Receipt receipt) {
        List<Purchase> purchaseList = receipt.getPurchaseList();
        return purchaseList.stream()
                .mapToInt(Purchase::getQuantity)
                .sum();
    }

    private String getTotalPurchaseAmount(Receipt receipt, StoreHouse storeHouse) {
        List<Purchase> purchaseList = receipt.getPurchaseList();
        long sum = ZERO;
        for (Purchase purchase : purchaseList) {
            List<Product> products = storeHouse.findProductByName(purchase.getProductName());
            sum += products.getFirst().getPrice() * purchase.getQuantity();
        }
        return String.format(NUMBER_FORMAT_WITH_COMMA, sum);
    }

    private String getTotalPromotionDiscountAmount(Receipt receipt, StoreHouse storeHouse) {
        Map<Product, Integer> freebieProduct = receipt.getFreebieProduct();
        long sum = 0;
        for (Product product : freebieProduct.keySet()) {
            List<Product> products = storeHouse.findProductByName(product.getName());
            sum += products.getFirst().getPrice() * freebieProduct.get(product);
        }
        return String.format(NUMBER_FORMAT_WITH_COMMA, sum);
    }

    private String getTotalPrice(Receipt receipt, StoreHouse storeHouse, MembershipManager membershipManager) {
        long totalPurchaseAmount = Long.parseLong(
                getTotalPurchaseAmount(receipt, storeHouse).replace(COMMA, EMPTY_STRING));
        long totalPromotionDiscountAmount = Long.parseLong(
                getTotalPromotionDiscountAmount(receipt, storeHouse).replace(COMMA, EMPTY_STRING));
        long totalMembershipDiscountAmount = Long.parseLong(
                getTotalMembershipDiscountAmount(membershipManager).replace(COMMA, EMPTY_STRING));
        long result = totalPurchaseAmount - totalPromotionDiscountAmount - totalMembershipDiscountAmount;
        return String.format(NUMBER_FORMAT_WITH_COMMA, result);
    }
}
