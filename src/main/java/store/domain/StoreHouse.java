package store.domain;

import static store.constants.NumberConstants.SINGLE_PRODUCT_QUANTITY;

import java.util.ArrayList;
import java.util.List;
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

    public boolean checkRegularPricePurchase(String productName) {
        int count = (int) productList.stream()
                .filter(product -> product.getName().equals(productName))
                .count();
        List<Product> products = findProductByName(productName);
        Promotion promotionName = products.getFirst().getPromotionName();

        return count == SINGLE_PRODUCT_QUANTITY && promotionName.equals(Promotion.NULL);
    }

    public void addProduct(Product product) {
        productList.add(product);
    }

    public List<Product> getProductList() {
        return productList;
    }
}

