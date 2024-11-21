package store.io.parser;

import static store.constants.StringConstants.COMMA;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.domain.Product;
import store.domain.Promotion;
import store.io.reader.FileReader;
import store.io.reader.Reader;

public class ProductsFileLineParser {

    public final static String PRODUCTS_FILE_NAME = "src/main/resources/products.md";

    public List<Product> readProductsFileInput() {
        try {
            return readProductsFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Product> readProductsFile() throws IOException {
        Reader reader = new FileReader(PRODUCTS_FILE_NAME);
        reader.readLine();
        String line;
        List<Product> productList = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            Product product = parseLine(line);
            productList.add(product);
        }
        return productList;
    }

    public Product parseLine(String line) {
        String[] product = line.split(COMMA);
        return getProduct(product);
    }

    private Product getProduct(String[] product) {
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
