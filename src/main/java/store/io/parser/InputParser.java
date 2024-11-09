package store.io.parser;

import static store.constants.ErrorMessages.EMPTY_PRODUCT;
import static store.constants.ErrorMessages.EMPTY_PRODUCT_NAME;
import static store.constants.ErrorMessages.INVALID_INPUT_FORMAT;
import static store.constants.ErrorMessages.INVALID_PRODUCT_BRACKETS;
import static store.constants.ErrorMessages.NONNUMERICAL_QUANTITY;
import static store.constants.StringConstants.CLOSE_SQUARE_BRACKETS;
import static store.constants.StringConstants.COMMA;
import static store.constants.StringConstants.DASH;
import static store.constants.StringConstants.OPEN_SQUARE_BRACKETS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import store.dto.Purchase;

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
        if (products.contains("")) {
            throw new IllegalArgumentException(EMPTY_PRODUCT);
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
            throw new IllegalArgumentException(INVALID_PRODUCT_BRACKETS);
        }
    }

    private void validateHyphenSeparatedFormat(String[] parts) {
        // TODO: 에러 메시지 구체적으로 출력되도록 보완하기
        if (parts.length != 2) {
            throw new IllegalArgumentException(INVALID_INPUT_FORMAT);
        }
    }

    private Purchase getValidProductAndQuantity(String[] parts) {
        String productName = parts[0];
        validateEmptyProductName(productName);
        int quantity = getValidQuantity(parts[1]);
        return Purchase.of(productName, quantity);
    }

    private void validateEmptyProductName(String productName) {
        if (productName.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_PRODUCT_NAME);
        }
    }

    private static int getValidQuantity(String inputQuantity) {
        int quantity;
        try {
            quantity = Integer.parseInt(inputQuantity);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(NONNUMERICAL_QUANTITY);
        }
        return quantity;
    }
}
