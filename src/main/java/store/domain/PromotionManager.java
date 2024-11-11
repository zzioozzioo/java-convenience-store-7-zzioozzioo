package store.domain;

import static store.constants.NumberConstants.FREEBIE_QUANTITY;

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
                    promotionInfo.getPromotionName(), promotionInfo.getBuyQuantity(), promotionInfo.getGetQuantity()
            );
            Promotion.setPromotionPeriods(
                    promotionInfo.getPromotionName(), promotionInfo.getStartDateTime(), promotionInfo.getEndDateTime()
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

    private Product findProductByPromotionName(Product product, Promotion promotionName) {
        List<Product> products = storeHouse.findProductByName(product.getName());

        return products.stream()
                .filter(prdt -> !prdt.getPromotionName().equals(promotionName))
                .findFirst()
                .orElse(null);
    }

    private Receipt getReceiptWhenPromotionPayment(Product product, int purchaseQuantity, Receipt receipt) {
        if (processFullPromotionPayment(receipt, product, purchaseQuantity)) {
            return receipt;
        }
        processPartialPromotionPayment(receipt, product, purchaseQuantity);
        return receipt;
    }

    private boolean processFullPromotionPayment(Receipt receipt, Product product, int purchaseQuantity) {

        if (purchaseQuantity <= product.getQuantity()) {
            return canAddOneFreebie(receipt, product, purchaseQuantity);
        }
        return false;
    }

    private boolean canAddOneFreebie(Receipt receipt, Product product, int purchaseQuantity) {
        BuyGetQuantity buyAndGetQuantity = getBuyAndGetQuantity(product.getPromotionName());
        int buyQuantity = buyAndGetQuantity.getBuyQuantity();
        int getQuantity = buyAndGetQuantity.getGetQuantity();
        int remainder = purchaseQuantity % (buyQuantity + getQuantity);
        if (remainder == FREEBIE_QUANTITY) {
            addOneFreebie(receipt, product, purchaseQuantity);
            receipt.addFreebieProduct(product, getPromotionAppliedQuantity(product) / (buyQuantity + getQuantity));
            return true;
        }
        return false;
    }

    private void addOneFreebie(Receipt receipt, Product product, int purchaseQuantity) {
        Choice freebieAdditionChoice = getInputView().readFreebieAdditionChoice(product.getName());
        int totalPurchaseQuantity = purchaseQuantity;
        if (freebieAdditionChoice.equals(Choice.Y)) {
            totalPurchaseQuantity += FREEBIE_QUANTITY;
            addFreebieFromRegularProduct(receipt, product, purchaseQuantity);
        }
        storeHouse.buy(product, totalPurchaseQuantity);
    }

    private void addFreebieFromRegularProduct(Receipt receipt, Product product, int purchaseQuantity) {
        BuyGetQuantity buyAndGetQuantity = getBuyAndGetQuantity(product.getPromotionName());
        int buyQuantity = buyAndGetQuantity.getBuyQuantity();
        int getQuantity = buyAndGetQuantity.getGetQuantity();
        int remainder = purchaseQuantity % (buyQuantity + getQuantity);
        if (purchaseQuantity == (product.getQuantity() - remainder)) { // 증정품 1개만 일반 재고 사용
            processRegularPricePayment(product, FREEBIE_QUANTITY);
            receipt.addFreebieProduct(product, FREEBIE_QUANTITY);
        }
    }

    private void processPartialPromotionPayment(Receipt receipt, Product product, int purchaseQuantity) {
        BuyGetQuantity buyAndGetQuantity = getBuyAndGetQuantity(product.getPromotionName());
        int buyQuantity = buyAndGetQuantity.getBuyQuantity();
        int getQuantity = buyAndGetQuantity.getGetQuantity();

        int promotionAppliedQuantity = getPromotionAppliedQuantity(product);

        // TODO: 음수로 나오는 에러 해결하기
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

    // TODO: 얘를 수정해야 할 듯
    private int getPromotionAppliedQuantity(Product product) {
        BuyGetQuantity buyAndGetQuantity = getBuyAndGetQuantity(product.getPromotionName());
        int bundle = buyAndGetQuantity.getBuyQuantity() + buyAndGetQuantity.getGetQuantity();
        int promotionStock = product.getQuantity();
        int count = promotionStock / bundle;

        return count * (buyAndGetQuantity.getBuyQuantity() + buyAndGetQuantity.getGetQuantity());
    }

    private Choice getRegularPriceApplicationChoice(Product product, int purchaseQuantity) {
        return getInputView().readRegularPricePaymentChoice(product.getName(), purchaseQuantity);
    }


    public static BuyGetQuantity getBuyAndGetQuantity(Promotion promotionName) {
        return BuyGetQuantity.of(promotionName.getBuyQuantity(), promotionName.getGetQuantity());
    }

    private InputView getInputView() {
        return new InputView(new MissionUtilsReader(), new SystemWriter(),
                new InputValidator());
    }

    public List<PromotionInfo> getPromotionInfos() {
        return promotionInfos;
    }
}
