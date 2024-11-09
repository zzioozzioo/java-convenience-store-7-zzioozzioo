package store.domain;

import java.util.ArrayList;
import java.util.List;
import store.exception.ProductNotExistException;

public class StoreHouse {

    private final List<Product> productList = new ArrayList<>();

    public void buy(Product product, int quantity) {
        product.sell(quantity);
    }

    public List<Product> findProductByName(String productName) {
        // TODO: 상품명 동일한 것 다 가져오되, 프로모션인지 아닌지 구분할 수 있는 로직 추가하기

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

        return count == 1 && promotionName.equals(Promotion.NULL);
    }

    public void addProduct(Product product) {
        productList.add(product);
    }

    List<Product> getProductList() {
        return productList;
    }
}

