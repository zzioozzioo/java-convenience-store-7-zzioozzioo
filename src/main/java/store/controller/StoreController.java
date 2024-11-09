package store.controller;

import java.util.List;
import store.config.IoConfig;
import store.config.ServiceConfig;
import store.domain.Choice;
import store.domain.MembershipManager;
import store.domain.Product;
import store.domain.PromotionManager;
import store.domain.StoreHouse;
import store.domain.StoreManager;
import store.dto.PromotionInfo;
import store.dto.Purchase;
import store.io.InputView;
import store.io.OutputView;
import store.service.StoreService;

public class StoreController {

    final static String PRODUCTS_FILE_NAME = "src/main/resources/products.md";
    final static String PROMOTIONS_FILE_NAME = "src/main/resources/promotions.md";

    private final InputView inputView;
    private final OutputView outputView;
    private final StoreService service;

    public StoreController(IoConfig ioConfig, ServiceConfig serviceConfig) {
        this.inputView = ioConfig.getInputView();
        this.outputView = ioConfig.getOutputView();
        this.service = serviceConfig.getService();
    }

    public void run() {
        try {
            storeOpen();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void storeOpen() {
        while (true) {

            // 파일 입출력
            StoreHouse storeHouse = inputView.readProductsFileInput(PRODUCTS_FILE_NAME);
            List<PromotionInfo> promotionInfos = inputView.readPromotionsFileInput(PROMOTIONS_FILE_NAME);

            // 매니저 세팅
            StoreManager storeManager = new StoreManager(storeHouse);
            PromotionManager promotionManager = new PromotionManager(promotionInfos, storeHouse);
            MembershipManager membershipManager = new MembershipManager(storeHouse);

            // 상품 세팅
            List<Product> allProduct = storeManager.getAllProduct();
            outputView.printProductList(allProduct);

            // 사용자 구매
            List<Purchase> purchaseList = userPurchase(storeHouse);

            // 멤버십 할인 여부
            inputView.readMembershipDiscountApplicationChoice();

            // 영수증 출력
            outputView.printReceipt(purchaseList, storeHouse);

            // 추가 구매 여부
            if (inputView.readAdditionalPurchaseChoice().equals(Choice.N)) {
                break;
            }
        }
    }

    private List<Purchase> userPurchase(StoreHouse storeHouse) {
        while (true) {
            try {
                List<Purchase> purchaseList = inputView.readProductNameAndQuantity();
                service.purchase(purchaseList, storeHouse);
                return purchaseList;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
