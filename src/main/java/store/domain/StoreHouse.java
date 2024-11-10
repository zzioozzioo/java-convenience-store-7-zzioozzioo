package store.domain;

import java.util.ArrayList;
import java.util.List;
import store.exception.OutOfStockQuantityException;
import store.exception.ProductNotExistException;

public class StoreHouse {

    private final List<Product> productList = new ArrayList<>();


    public void buy(Product product, int quantity) {
        product.sell(quantity);
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

    public int checkValidStock(Product product, int purchaseQuantity) {
        if (product.getQuantity() < purchaseQuantity) {
            throw new OutOfStockQuantityException();
        }
        return product.getQuantity();
    }

    public boolean checkRegularPricePurchase(String productName) {
        int count = (int) productList.stream()
                .filter(product -> product.getName().equals(productName))
                .count();
        List<Product> products = findProductByName(productName);
        Promotion promotionName = products.getFirst().getPromotionName();

        return count == 1 && promotionName.equals(Promotion.NULL);
    }

    public void addProduct(Product product) {
        productList.add(product);
    }

    public List<Product> getProductList() {
        return productList;
    }
}

