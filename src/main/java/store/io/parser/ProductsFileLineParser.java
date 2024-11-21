package store.io.parser;

import static store.constants.StringConstants.COMMA;

import java.io.IOException;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.StoreHouse;
import store.io.reader.FileReader;
import store.io.reader.Reader;

public class ProductsFileLineParser {

    public final static String PRODUCTS_FILE_NAME = "src/main/resources/products.md";

    public StoreHouse readProductsFileInput() {
        try {
            return readProductsFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private StoreHouse readProductsFile() throws IOException {
        Reader reader = new FileReader(PRODUCTS_FILE_NAME);
        reader.readLine();
        String line;
        StoreHouse storeHouse = new StoreHouse();
        while ((line = reader.readLine()) != null) {
            Product product = parseLine(line);
            storeHouse.addProduct(product);
        }
        return storeHouse;
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
