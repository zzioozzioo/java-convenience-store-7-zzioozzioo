package store.io.parser;

import store.domain.Product;
import store.domain.Promotion;

public class ProductsFileLineParser {

    private final String line;

    public ProductsFileLineParser(String line) {
        this.line = line;
    }

    public Product parseLine() {
        String[] product = line.split(",");
        return getProduct(product);
    }

    private Product getProduct(String[] product) {
        // TODO: 더 효율적인 로직 고민해 보기
        return new Product(product[0],
                convertPrice(product[1]),
                convertQuantity(product[2]),
                convertPromotion(product[3]));
    }

    private long convertPrice(String price) {
        return Long.parseLong(price);
    }

    private int convertQuantity(String quantity) {
        return Integer.parseInt(quantity);
    }

    private Promotion convertPromotion(String promotionName) {
        for (Promotion promotion : Promotion.values()) {
            if (promotion.getPromotionName().equals(promotionName)) {
                return promotion;
            }
        }
        return Promotion.NULL;
    }

}
