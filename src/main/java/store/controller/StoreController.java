package store.controller;

import java.util.List;
import store.config.IoConfig;
import store.domain.Choice;
import store.domain.MembershipManager;
import store.domain.Product;
import store.domain.PromotionManager;
import store.domain.Receipt;
import store.domain.StoreHouse;
import store.dto.PromotionInfo;
import store.dto.Purchase;
import store.io.InputView;
import store.io.OutputView;
import store.io.parser.ProductsFileLineParser;
import store.io.parser.PromotionsFileLineParser;
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
        StoreHouse storeHouse = new ProductsFileLineParser().readProductsFileInput();
        List<PromotionInfo> promotionInfos = new PromotionsFileLineParser().readPromotionsFileInput();

        PromotionManager promotionManager = new PromotionManager(promotionInfos, storeHouse, inputView);
        MembershipManager membershipManager = new MembershipManager(storeHouse);
        service = new StoreService(promotionManager, membershipManager);

        List<Product> allProduct = storeHouse.getProductList();
        processPurchase(allProduct, storeHouse, membershipManager);
    }

    private void processPurchase(List<Product> allProduct, StoreHouse storeHouse, MembershipManager membershipManager) {
        while (true) {
            outputView.printProductList(allProduct);
            Receipt receipt = getUserPurchase(storeHouse);
            applyMembershipDiscount(receipt);
            outputView.printReceipt(receipt, storeHouse, membershipManager);
            if (inputView.readAdditionalPurchaseChoice().equals(Choice.N)) {
                break;
            }
        }
    }

    private Receipt getUserPurchase(StoreHouse storeHouse) {
        while (true) {
            List<Purchase> purchaseList = inputView.readProductNameAndQuantity();
            try {
                return getReceipt(storeHouse, purchaseList);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private Receipt getReceipt(StoreHouse storeHouse, List<Purchase> purchaseList) {
        Receipt receipt = service.purchase(purchaseList, storeHouse);
        receipt.setPurchaseList(purchaseList);
        return receipt;
    }

    private void applyMembershipDiscount(Receipt receipt) {
        Choice membershipDiscountApplicationChoice = inputView.readMembershipDiscountApplicationChoice();
        if (membershipDiscountApplicationChoice.equals(Choice.Y)) {
            service.applyMembershipDiscount(receipt);
        }
    }

}
