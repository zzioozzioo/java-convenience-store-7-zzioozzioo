package store.io.parser;

import static store.constants.NumberConstants.PRODUCT_NAME_INDEX;
import static store.constants.NumberConstants.QUANTITY_INDEX;
import static store.constants.NumberConstants.REQUIRED_TWO_PARTS_FORMAT;
import static store.constants.StringConstants.CLOSE_SQUARE_BRACKETS;
import static store.constants.StringConstants.COMMA;
import static store.constants.StringConstants.DASH;
import static store.constants.StringConstants.EMPTY_STRING;
import static store.constants.StringConstants.OPEN_SQUARE_BRACKETS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import store.dto.Purchase;
import store.exception.InvalidInputFormatException;

public class InputParser {

    public List<Purchase> parse(String input) {
        List<Purchase> purchaseList = new ArrayList<>();
        List<String> products = splitEachProduct(input);
        for (String product : products) {
            purchaseList.add(parseProductAndQuantity(product.trim()));
        }
        return purchaseList;
    }

    private List<String> splitEachProduct(String input) {
        List<String> products = Arrays.asList(input.split(COMMA, -1));
        validateEmptyProduct(products);
        return products;
    }

    private void validateEmptyProduct(List<String> products) {
        if (products.contains(EMPTY_STRING)) {
            throw new InvalidInputFormatException();
        }
    }

    private Purchase parseProductAndQuantity(String product) {
        validateProductBrackets(product);

        String productAndQuantity = product.substring(1, product.length() - 1);
        String[] parts = productAndQuantity.split(DASH);

        validateHyphenSeparatedFormat(parts);

        return getValidProductAndQuantity(parts);
    }

    private void validateProductBrackets(String product) {
        if (!(product.startsWith(OPEN_SQUARE_BRACKETS) && product.endsWith(CLOSE_SQUARE_BRACKETS))) {
            throw new InvalidInputFormatException();
        }
    }

    private void validateHyphenSeparatedFormat(String[] parts) {
        if (parts.length != REQUIRED_TWO_PARTS_FORMAT) {
            throw new InvalidInputFormatException();
        }
    }

    private Purchase getValidProductAndQuantity(String[] parts) {
        String productName = parts[PRODUCT_NAME_INDEX];
        validateEmptyProductName(productName);
        int quantity = getValidQuantity(parts[QUANTITY_INDEX]);
        return Purchase.of(productName, quantity);
    }

    private void validateEmptyProductName(String productName) {
        if (productName.isEmpty()) {
            throw new InvalidInputFormatException();
        }
    }

    private static int getValidQuantity(String inputQuantity) {
        int quantity;
        try {
            quantity = Integer.parseInt(inputQuantity);
        } catch (NumberFormatException e) {
            throw new InvalidInputFormatException();
        }
        return quantity;
    }
}
