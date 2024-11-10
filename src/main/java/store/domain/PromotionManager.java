package store.domain;

import java.util.List;
import store.dto.BuyGetQuantity;
import store.dto.PromotionInfo;
import store.dto.Receipt;
import store.io.InputValidator;
import store.io.InputView;
import store.io.reader.MissionUtilsReader;
import store.io.writer.SystemWriter;

public class PromotionManager {

    private final List<PromotionInfo> promotionInfos;
    private final StoreHouse storeHouse;

    public PromotionManager(List<PromotionInfo> promotionInfos, StoreHouse storeHouse) {
        this.promotionInfos = promotionInfos;
        this.storeHouse = storeHouse;
    }

    public void setPromotionInfo() {
        promotionInfos.forEach(promotionInfo -> {
            Promotion.setQuantity(
                    promotionInfo.getPromotionName(), promotionInfo.getBuyQuantity(),
                    promotionInfo.getGetQuantity()
            );
            Promotion.setPromotionPeriods(
                    promotionInfo.getPromotionName(), promotionInfo.getStartDateTime(),
                    promotionInfo.getEndDateTime()
            );
        });
    }

    public Receipt applyPromotion(Product product, int purchaseQuantity) {
        Receipt receipt = new Receipt();
        if (!isValidPromotionApplicable(product, purchaseQuantity)) {
            processRegularPricePayment(product, purchaseQuantity);
            return receipt;
        }
        return getReceiptWhenPromotionPayment(product, purchaseQuantity, receipt);
    }

    private Receipt getReceiptWhenPromotionPayment(Product product, int purchaseQuantity, Receipt receipt) {
        if (processFullPromotionPayment(receipt, product, purchaseQuantity)) {
            return receipt;
        }
        processPartialPromotionPayment(receipt, product, purchaseQuantity);
        return receipt;
    }

    private boolean isValidPromotionApplicable(Product product, int purchaseQuantity) {
        return validPromotionPeriod(product.getPromotionName()) &&
                canApplyPromotion(product, purchaseQuantity) &&
                validPromotionProductStock(product);
    }

    public boolean validPromotionPeriod(Promotion promotionName) {
        return Promotion.isPromotionValid(promotionName);
    }

    public boolean canApplyPromotion(Product product, int purchaseQuantity) {
        BuyGetQuantity buyAndGetQuantity = getBuyAndGetQuantity(product.getPromotionName());
        int buyQuantity = buyAndGetQuantity.getBuyQuantity();
        return purchaseQuantity >= buyQuantity;
    }

    private boolean validPromotionProductStock(Product product) {
        BuyGetQuantity buyAndGetQuantity = getBuyAndGetQuantity(product.getPromotionName());
        int buyQuantity = buyAndGetQuantity.getBuyQuantity();
        return buyQuantity <= product.getQuantity();
    }

    private void processRegularPricePayment(Product product, int purchaseQuantity) {

        Product generalProduct = findProductByPromotionName(product, Promotion.NULL);
        storeHouse.buy(generalProduct, purchaseQuantity);
    }

    private boolean processFullPromotionPayment(Receipt receipt, Product product, int purchaseQuantity) {
        BuyGetQuantity buyAndGetQuantity = getBuyAndGetQuantity(product.getPromotionName());
        int buyQuantity = buyAndGetQuantity.getBuyQuantity();
        int getQuantity = buyAndGetQuantity.getGetQuantity();

        if (purchaseQuantity <= product.getQuantity()) {
            int remainder = purchaseQuantity % (buyQuantity + getQuantity);
            if (remainder != 0 && remainder < buyQuantity) {
                Choice freebieAdditionChoice = getInputView().readFreebieAdditionChoice(product.getName());
                int totalPurchaseQuantity = purchaseQuantity;
                totalPurchaseQuantity = getTotalPurchaseQuantity(freebieAdditionChoice, totalPurchaseQuantity);
                if (purchaseQuantity == (buyQuantity - remainder)) {
                    processRegularPricePayment(product, 1);
                    receipt.addFreebieProduct(product, 1);
                    return true;
                }
                storeHouse.buy(product, totalPurchaseQuantity);
                receipt.addFreebieProduct(product, getPromotionAppliedQuantity(product) / (buyQuantity + getQuantity));
                return true;
            }
        }
        return false;
    }

    private static int getTotalPurchaseQuantity(Choice freebieAdditionChoice, int totalPurchaseQuantity) {
        if (freebieAdditionChoice.equals(Choice.Y)) {
            totalPurchaseQuantity += 1;
        }
        return totalPurchaseQuantity;
    }

    private void processPartialPromotionPayment(Receipt receipt, Product product, int purchaseQuantity) {
        BuyGetQuantity buyAndGetQuantity = getBuyAndGetQuantity(product.getPromotionName());
        int buyQuantity = buyAndGetQuantity.getBuyQuantity();
        int getQuantity = buyAndGetQuantity.getGetQuantity();

        int promotionAppliedQuantity = getPromotionAppliedQuantity(product);
        int regularPricePaymentQuantity = purchaseQuantity - promotionAppliedQuantity;

        Choice regularPricePaymentChoice = getRegularPriceApplicationChoice(product,
                regularPricePaymentQuantity);
        if (regularPricePaymentChoice.equals(
                Choice.Y)) {
            Product regularProduct = findProductByPromotionName(product, Promotion.NULL);
            storeHouse.buy(regularProduct, regularPricePaymentQuantity);
        }
        storeHouse.buy(product, promotionAppliedQuantity);
        receipt.addFreebieProduct(product, promotionAppliedQuantity / (buyQuantity + getQuantity));
    }

    private Choice getRegularPriceApplicationChoice(Product product, int purchaseQuantity) {
        return getInputView().readRegularPricePaymentChoice(product.getName(), purchaseQuantity);
    }


    private Product findProductByPromotionName(Product product, Promotion promotionName) {
        List<Product> products = storeHouse.findProductByName(product.getName());

        return products.stream()
                .filter(prdt -> !prdt.getPromotionName().equals(promotionName))
                .findFirst()
                .orElse(null);
    }

    public static BuyGetQuantity getBuyAndGetQuantity(Promotion promotionName) {
        return BuyGetQuantity.of(promotionName.getBuyQuantity(), promotionName.getGetQuantity());
    }

    private static int getPromotionAppliedQuantity(Product product) {
        BuyGetQuantity buyAndGetQuantity = getBuyAndGetQuantity(product.getPromotionName());
        int bundle = buyAndGetQuantity.getBuyQuantity() + buyAndGetQuantity.getGetQuantity();
        int promotionStock = product.getQuantity();
        int count = promotionStock / bundle;

        return count * (buyAndGetQuantity.getBuyQuantity() + buyAndGetQuantity.getGetQuantity());
    }

    private InputView getInputView() {
        return new InputView(new MissionUtilsReader(), new SystemWriter(),
                new InputValidator());
    }

    public List<PromotionInfo> getPromotionInfos() {
        return promotionInfos;
    }
}
