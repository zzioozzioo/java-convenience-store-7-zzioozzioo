package store.controller;

import static store.constants.InputMessages.PRODUCTS_FILE_NAME;
import static store.constants.InputMessages.PROMOTIONS_FILE_NAME;

import java.util.List;
import store.config.IoConfig;
import store.domain.Choice;
import store.domain.MembershipManager;
import store.domain.Product;
import store.domain.PromotionManager;
import store.domain.StoreHouse;
import store.domain.StoreManager;
import store.dto.PromotionInfo;
import store.dto.Purchase;
import store.dto.Receipt;
import store.io.InputView;
import store.io.OutputView;
import store.service.StoreService;

public class StoreController {


    private final InputView inputView;
    private final OutputView outputView;
    private StoreService service;

    public StoreController(IoConfig ioConfig) {
        this.inputView = ioConfig.getInputView();
        this.outputView = ioConfig.getOutputView();
    }

    public void run() {
        try {
            storeOpen();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void storeOpen() {
        // 파일 입출력
        StoreHouse storeHouse = inputView.readProductsFileInput(PRODUCTS_FILE_NAME);
        List<PromotionInfo> promotionInfos = inputView.readPromotionsFileInput(PROMOTIONS_FILE_NAME);

        // 매니저 세팅
        StoreManager storeManager = new StoreManager(storeHouse);
        PromotionManager promotionManager = new PromotionManager(promotionInfos, storeHouse);
        MembershipManager membershipManager = new MembershipManager(storeHouse);
        service = new StoreService(promotionManager, membershipManager);

        // 상품 세팅
        List<Product> allProduct = storeManager.getAllProduct();

        // 판매
        processPurchase(allProduct, storeHouse, membershipManager);
    }

    private void processPurchase(List<Product> allProduct, StoreHouse storeHouse, MembershipManager membershipManager) {
        while (true) {
            outputView.printProductList(allProduct);
            // 사용자 구매
            Receipt receipt = userPurchase(storeHouse);

            // 멤버십 할인 여부
            applyMembershipDiscount(receipt);

            // 영수증 출력
            outputView.printReceipt(receipt, storeHouse, membershipManager);

            // 추가 구매 여부
            if (inputView.readAdditionalPurchaseChoice().equals(Choice.N)) {
                break;
            }
        }
    }

    private Receipt userPurchase(StoreHouse storeHouse) {
        while (true) {
            try {
                List<Purchase> purchaseList = inputView.readProductNameAndQuantity();
                Receipt receipt = service.purchase(purchaseList, storeHouse);
                receipt.setPurchaseList(purchaseList);
                return receipt;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void applyMembershipDiscount(Receipt receipt) {
        Choice membershipDiscountApplicationChoice = inputView.readMembershipDiscountApplicationChoice();
        if (membershipDiscountApplicationChoice.equals(Choice.Y)) { // 멤버십 할인 적용
            service.applyMembershipDiscount(receipt);
        }
    }

}
