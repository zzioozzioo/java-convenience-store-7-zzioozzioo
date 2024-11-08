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
        // TODO: 상품명 동일한 것 다 가져오되, 프로모션인지 아닌지 구분할 수 있는 로직 추가하기

        return productList.stream()
                .filter(product -> product.getName().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    }

    public void addProduct(Product product) {
        productList.add(product);
    }

    List<Product> getProductList() {
        return productList;
    }
}

