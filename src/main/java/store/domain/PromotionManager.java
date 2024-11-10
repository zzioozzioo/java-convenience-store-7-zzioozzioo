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

        // 프로모션 재고가 있는 경우
        if (processFullPromotionPayment(receipt, product, purchaseQuantity)) {
            return receipt;
        }
        // 프로모션 재고가 부족한 경우
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

        // TODO: 일반 재고 + 프로모션 재고 합해서 구매 가능한지도 확인해야 함(하지 말까..)

        if (purchaseQuantity <= product.getQuantity()) { // 구매 수량과 프로모션 재고 비교
            int remainder = purchaseQuantity % (buyQuantity + getQuantity);
            if (remainder != 0 && remainder < buyQuantity) { // 증정품 하나 추가할 수 있는지 확인
                Choice freebieAdditionChoice = getInputView().readFreebieAdditionChoice(product.getName());
                int totalPurchaseQuantity = purchaseQuantity;
                totalPurchaseQuantity = getTotalPurchaseQuantity(product, freebieAdditionChoice, totalPurchaseQuantity);
                if (purchaseQuantity == (buyQuantity - remainder)) { // 증정품 하나를 일반 상품에서 소진
                    processRegularPricePayment(product, 1);
                    // TODO: 영수증에 증정 품목(Product, 개수) 추가
                    receipt.addFreebieProduct(product, 1);
                    return true;
                }
                // 프로모션 적용 후 구매하는 로직
                storeHouse.buy(product, totalPurchaseQuantity);
                // TODO: 영수증에 증정 품목 추가
                receipt.addFreebieProduct(product, getPromotionAppliedQuantity(product) / (buyQuantity + getQuantity));
                return true;
            }
        }
        return false;
    }

    private static int getTotalPurchaseQuantity(Product product, Choice freebieAdditionChoice,
                                                int totalPurchaseQuantity) {
        if (freebieAdditionChoice.equals(Choice.Y)) { // 추가하는 경우
            totalPurchaseQuantity += 1;
        }
        return totalPurchaseQuantity;
    }

    private void processPartialPromotionPayment(Receipt receipt, Product product, int purchaseQuantity) {
        BuyGetQuantity buyAndGetQuantity = getBuyAndGetQuantity(product.getPromotionName());
        int buyQuantity = buyAndGetQuantity.getBuyQuantity();
        int getQuantity = buyAndGetQuantity.getGetQuantity();

        int promotionAppliedQuantity = getPromotionAppliedQuantity(product);
        int regularPricePaymentQuantity = purchaseQuantity - promotionAppliedQuantity; // 정가 구매 수량

        Choice regularPricePaymentChoice = getRegularPriceApplicationChoice(product,
                regularPricePaymentQuantity); // 정가 구매 여부
        if (regularPricePaymentChoice.equals(
                Choice.Y)) { // 정가 구매 ㄱㅊ -> promotionAppliedQuantity는 프로모션 적용, 나머지는 정가로 일반 상품 구매
            Product regularProduct = findProductByPromotionName(product, Promotion.NULL);
            storeHouse.buy(regularProduct, regularPricePaymentQuantity); // 일단 정가로 일반 상품 구매 완료
        } // 정가 구매 ㄴㄴ -> promotionAppliedQuantity만 구매
        storeHouse.buy(product, promotionAppliedQuantity); // 구매 가능 프로모션 수량만 프로모션 상품 구매 완료
        // TODO: 영수증에 증정 상품 추가
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
        // 프로모션에 해당하는 buy, get 수량
        BuyGetQuantity buyAndGetQuantity = getBuyAndGetQuantity(product.getPromotionName());
        int bundle = buyAndGetQuantity.getBuyQuantity() + buyAndGetQuantity.getGetQuantity();
        int promotionStock = product.getQuantity(); // 프로모션 상품의 재고
        int count = promotionStock / bundle; // 프로모션을 적용할 수 있는 횟수

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
