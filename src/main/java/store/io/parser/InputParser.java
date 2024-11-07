package store.io.parser;

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
        List<String> products = Arrays.asList(input.split(",", -1));
        validateEmptyProduct(products);
        return products;
    }

    private void validateEmptyProduct(List<String> products) {
        if (products.contains("")) {
            throw new IllegalArgumentException("상품 구분자를 잘못 입력했습니다.");
        }
    }

    private Purchase parseProductAndQuantity(String product) {
        validateProductBrackets(product);

        String productAndQuantity = product.substring(1, product.length() - 1);
        String[] parts = productAndQuantity.split("-");

        validateHyphenSeparatedFormat(parts);

        return getValidProductAndQuantity(parts);
    }

    private void validateProductBrackets(String product) {
        if (!(product.startsWith("[") && product.endsWith("]"))) {
            throw new IllegalArgumentException("개별 상품은 대괄호로 묶어야 합니다.");
        }
    }

    private void validateHyphenSeparatedFormat(String[] parts) {
        // TODO: 에러 메시지 구체적으로 출력되도록 보완하기
        if (parts.length != 2) {
            throw new IllegalArgumentException("잘못된 입력 형식입니다.");
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
            throw new IllegalArgumentException("상품명을 입력해야 합니다.");
        }
    }

    private static int getValidQuantity(String inputQuantity) {
        int quantity;
        try {
            quantity = Integer.parseInt(inputQuantity);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("수량은 숫자로 입력해야 합니다.");
        }
        return quantity;
    }
}
