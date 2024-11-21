package store.io.parser;

import static store.constants.NumberConstants.PRODUCT_NAME_INDEX;
import static store.constants.NumberConstants.QUANTITY_INDEX;
import static store.constants.StringConstants.COMMA;
import static store.constants.StringConstants.DASH;
import static store.constants.StringConstants.EMPTY_STRING;
import static store.constants.StringConstants.ORDER_ONE_PRODUCT_FORMAT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import store.dto.Purchase;
import store.exception.InvalidInputException;
import store.exception.InvalidInputFormatException;

public class InputParser {

    public List<Purchase> parse(String input) {
        validateEmptyInput(input);
        List<String> products = splitEachProduct(input);
        validateInputFormat(products);
        return parseProducts(products);
    }

    private void validateEmptyInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidInputException();
        }
    }

    private List<String> splitEachProduct(String input) {
        return Arrays.asList(input.split(COMMA, -1));
    }

    private void validateInputFormat(List<String> products) {
        validateEmptyProduct(products);
        validateOrderFormat(products);
    }

    private void validateOrderFormat(List<String> products) {
        boolean isValidFormat = products.stream().allMatch(product -> product.matches(ORDER_ONE_PRODUCT_FORMAT));
        if (!isValidFormat) {
            throw new InvalidInputFormatException();
        }
    }

    private void validateEmptyProduct(List<String> products) {
        if (products.contains(EMPTY_STRING)) {
            throw new InvalidInputFormatException();
        }
    }

    private List<Purchase> parseProducts(List<String> products) {
        List<Purchase> purchaseList = new ArrayList<>();

        for (String product : products) {
            parseOneProductAndQuantity(product, purchaseList);
        }

        return purchaseList;
    }

    private void parseOneProductAndQuantity(String product, List<Purchase> purchaseList) {
        String productAndQuantityWithDash = product.substring(1, product.length() - 1);
        String[] productAndQuantity = productAndQuantityWithDash.split(DASH);

        String productName = productAndQuantity[PRODUCT_NAME_INDEX];
        int quantity = Integer.parseInt(productAndQuantity[QUANTITY_INDEX]);

        purchaseList.add(Purchase.of(productName, quantity));
    }
}