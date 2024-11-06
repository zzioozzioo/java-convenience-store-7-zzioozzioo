package store.domain;

import java.util.ArrayList;
import java.util.List;

public class StoreHouse {

    private final List<Product> productList = new ArrayList<>();

    public void buy(String productName, int quantity) {
        Product targetProduct = getProduct(productName);
        targetProduct.sell(quantity);
    }

    public Product getProduct(String productName) {
        return productList.stream()
                .filter(product -> product.getName().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    }

    public void addProduct(String productName, long price, int quantity, Promotion promotionType) {
        Product product = new Product(productName, price, quantity, promotionType);
        productList.add(product);
    }
}

