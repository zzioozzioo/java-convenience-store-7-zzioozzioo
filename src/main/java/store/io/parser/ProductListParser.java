package store.io.parser;

import static store.constants.NumberConstants.ZERO;
import static store.constants.OutputMessages.CURRENCY_UNIT;
import static store.constants.OutputMessages.OUT_OF_STOCK;
import static store.constants.OutputMessages.QUANTITY_UNIT;
import static store.constants.OutputMessages.WELCOME_MESSAGE;
import static store.constants.StringConstants.DASH;
import static store.constants.StringConstants.EMPTY_STRING;
import static store.constants.StringConstants.NEW_LINE;
import static store.constants.StringConstants.NUMBER_FORMAT_WITH_COMMA;
import static store.constants.StringConstants.ONE_SPACE;

import java.util.List;
import store.domain.Product;
import store.domain.Promotion;

public class ProductListParser {

    public String parse(List<Product> products) {
        StringBuilder sb = new StringBuilder();

        appendWelcome(sb);
        products.forEach(product -> appendProduct(sb, product));
        sb.append(NEW_LINE);

        return sb.toString();
    }

    private void appendWelcome(StringBuilder sb) {
        sb.append(WELCOME_MESSAGE);
    }

    private void appendProduct(StringBuilder sb, Product product) {
        sb.append(DASH + ONE_SPACE).append(product.getName()).append(ONE_SPACE)
                .append(getFormattedPrice(product) + ONE_SPACE)
                .append(getRemainingQuantity(product) + ONE_SPACE)
                .append(getPromotionNameOrEmpty(product)).append(NEW_LINE);
    }

    private String getFormattedPrice(Product product) {
        return String.format(NUMBER_FORMAT_WITH_COMMA, product.getPrice()) + CURRENCY_UNIT;
    }

    private String getRemainingQuantity(Product product) {
        int quantity = product.getQuantity();
        if (quantity != ZERO) {
            return quantity + QUANTITY_UNIT;
        }
        return OUT_OF_STOCK;
    }

    private String getPromotionNameOrEmpty(Product product) {
        Promotion promotionName = product.getPromotionName();
        if (!promotionName.equals(Promotion.NULL)) {
            return promotionName.getPromotionName();
        }
        return EMPTY_STRING;
    }
}
