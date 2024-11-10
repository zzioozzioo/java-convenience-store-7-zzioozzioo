package store.domain;

import java.util.List;
import store.dto.BuyGetQuantity;
import store.dto.PromotionInfo;
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


    // TODO: 일반과 프로모션 상품이 있고 구매 수량이 프로모션의 getQuantity 이상이면
    //  프로모션 상품으로 처리
    //  ...아래는 프로모션 상품이 있다는 전제 하에 진행...
    public void applyPromotion(Product product, int purchaseQuantity) {

        if (!isValidPromotionApplicable(product, purchaseQuantity)) {
            processRegularPricePayment(product, purchaseQuantity, 0);
        }

        // 프로모션 재고가 있는 경우
        if (processFullPromotionPayment(product, purchaseQuantity)) {
            return;
        }
        // 프로모션 재고가 부족한 경우
        processPartialPromotionPayment(product, purchaseQuantity);
    }

    private boolean isValidPromotionApplicable(Product product, int purchaseQuantity) {
        return checkValidPromotion(product.getPromotionName()) && canApplyPromotion(product, purchaseQuantity);
    }

    public boolean checkValidPromotion(Promotion promotionName) {
        return Promotion.isPromotionValid(promotionName);
    }

    public boolean canApplyPromotion(Product product, int purchaseQuantity) {
        BuyGetQuantity buyAndGetQuantity = getBuyAndGetQuantity(product.getPromotionName());
        int buyQuantity = buyAndGetQuantity.getBuyQuantity();
        return purchaseQuantity >= buyQuantity;
    }

    private void processRegularPricePayment(Product product, int purchaseQuantity, int promotionAppliedQuantity) {

        Product generalProduct = findProductByPromotionName(product, Promotion.NULL);
        storeHouse.buy(generalProduct, purchaseQuantity - promotionAppliedQuantity);
    }

    private boolean processFullPromotionPayment(Product product, int purchaseQuantity) {
        BuyGetQuantity buyAndGetQuantity = getBuyAndGetQuantity(product.getPromotionName());
        int buyQuantity = buyAndGetQuantity.getBuyQuantity();
        int getQuantity = buyAndGetQuantity.getGetQuantity();

        // TODO: 일반 재고 + 프로모션 재고 합해서 구매 가능한지도 확인해야 함(하지 말까..)

        if (purchaseQuantity <= product.getQuantity()) { // 구매 수량과 프로모션 재고 비교
            int remainder = purchaseQuantity % (buyQuantity + getQuantity);
            if (remainder != 0 && remainder < buyQuantity) { // 증정품 하나 추가할 수 있는지 확인
                Choice freebieAdditionChoice = getInputView().readFreebieAdditionChoice(product.getName());
                int totalPurchaseQuantity = purchaseQuantity;
                totalPurchaseQuantity = getTotalPurchaseQuantity(freebieAdditionChoice, totalPurchaseQuantity);
                // 프로모션 적용 후 구매하는 로직
                storeHouse.buy(product, totalPurchaseQuantity);
                return true;
            }
        }
        return false;
    }

    private static int getTotalPurchaseQuantity(Choice freebieAdditionChoice, int totalPurchaseQuantity) {
        if (freebieAdditionChoice.equals(Choice.Y)) { // 추가하는 경우
            totalPurchaseQuantity += 1;
        }
        return totalPurchaseQuantity;
    }

    private void processPartialPromotionPayment(Product product, int purchaseQuantity) {
        int promotionAppliedQuantity = getAvailablePromotionQuantity(product); // 구매 가능 프로모션 수량
        int regularPricePaymentQuantity = purchaseQuantity - promotionAppliedQuantity; // 정가 구매 수량

        Choice regularPricePaymentChoice = getRegularPriceApplicationChoice(product,
                regularPricePaymentQuantity); // 정가 구매 여부
        if (regularPricePaymentChoice.equals(
                Choice.Y)) { // 정가 구매 ㄱㅊ -> promotionAppliedQuantity는 프로모션 적용, 나머지는 정가로 일반 상품 구매
            Product regularProduct = findProductByPromotionName(product, Promotion.NULL);
            storeHouse.buy(regularProduct, regularPricePaymentQuantity); // 일단 정가로 일반 상품 구매 완료
        } // 정가 구매 ㄴㄴ -> promotionAppliedQuantity만 구매
        storeHouse.buy(product, promotionAppliedQuantity); // 구매 가능 프로모션 수량만 프로모션 상품 구매 완료
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

    public BuyGetQuantity getBuyAndGetQuantity(Promotion promotionName) {
        return BuyGetQuantity.of(promotionName.getBuyQuantity(), promotionName.getGetQuantity());
    }

    private int getAvailablePromotionQuantity(Product product) {
        // 프로모션에 해당하는 buy, get 수량
        BuyGetQuantity buyAndGetQuantity = getBuyAndGetQuantity(product.getPromotionName());
        int bundle = buyAndGetQuantity.getBuyQuantity() + buyAndGetQuantity.getGetQuantity();
        int promotionStock = product.getQuantity(); // 프로모션 상품의 재고
        return promotionStock / bundle;
    }

    private InputView getInputView() {
        return new InputView(new MissionUtilsReader(), new SystemWriter(),
                new InputValidator());
    }

    public List<PromotionInfo> getPromotionInfos() {
        return promotionInfos;
    }
}
