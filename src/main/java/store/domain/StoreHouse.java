package store.domain;

import static store.constants.NumberConstants.SINGLE_PRODUCT_QUANTITY;
import static store.constants.NumberConstants.ZERO;

import java.util.List;
import store.dto.Purchase;
import store.exception.InvalidInputFormatException;
import store.exception.OutOfStockQuantityException;
import store.exception.ProductNotExistException;

public class StoreHouse {

    private final List<Product> productList;

    public StoreHouse(List<Product> productList) {
        this.productList = productList;
    }

    public void buy(Product product, int quantity) {
        product.sell(quantity);
    }

    public void validatePurchase(Purchase purchase) {
        validatePurchaseQuantity(purchase);
        validateSufficientStock(purchase);
    }

    private void validatePurchaseQuantity(Purchase purchase) {
        if (purchase.getQuantity() <= 0) {
            throw new InvalidInputFormatException();
        }
        findProductByName(purchase.getProductName());
    }

    private void validateSufficientStock(Purchase purchase) {
        String productName = purchase.getProductName();
        List<Product> products = findProductByName(productName);
        int sum = ZERO;
        for (Product product : products) {
            sum += product.getQuantity();
        }
        if (sum < purchase.getQuantity()) {
            throw new OutOfStockQuantityException();
        }
    }

    public List<Product> findProductByName(String productName) {
        List<Product> filteredProduct = productList.stream()
                .filter(product -> product.getName().equals(productName))
                .toList();

        if (filteredProduct.isEmpty()) {
            throw new ProductNotExistException();
        }

        return filteredProduct;
    }

    public boolean checkRegularPricePurchase(String productName) {
        int count = (int) productList.stream()
                .filter(product -> product.getName().equals(productName))
                .count();
        List<Product> products = findProductByName(productName);
        Promotion promotionName = products.getFirst().getPromotionName();

        return count == SINGLE_PRODUCT_QUANTITY && promotionName.equals(Promotion.NULL);
    }

    public List<Product> getProductList() {
        return productList;
    }
}

