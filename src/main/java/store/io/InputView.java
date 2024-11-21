package store.io;

import static store.constants.InputMessages.ADDITIONAL_PURCHASE_MESSAGE;
import static store.constants.InputMessages.FREEBIE_ADDITION_MESSAGE;
import static store.constants.InputMessages.INPUT_PRODUCT_NAME_AND_QUANTITY;
import static store.constants.InputMessages.MEMBERSHIP_DISCOUNT_CHOICE_MESSAGE;
import static store.constants.InputMessages.REGULAR_PRICE_BUY_MESSAGE;
import static store.constants.StringConstants.NEW_LINE;

import java.util.List;
import store.domain.Choice;
import store.dto.Purchase;
import store.io.parser.InputParser;
import store.io.reader.Reader;
import store.io.writer.Writer;

public class InputView {

    private final Reader reader;
    private final Writer writer;
    private final InputValidator validator;

    public InputView(Reader reader, Writer writer, InputValidator validator) {
        this.reader = reader;
        this.writer = writer;
        this.validator = validator;
    }


    public List<Purchase> readProductNameAndQuantity() {
        writer.write(INPUT_PRODUCT_NAME_AND_QUANTITY);
        String inputProductAndQuantity = reader.readLine();
        writer.write(NEW_LINE);
        validator.validateEmptyInput(inputProductAndQuantity);
        return new InputParser().parse(inputProductAndQuantity);
    }

    public Choice readFreebieAdditionChoice(String productName) {
        writer.write(String.format(FREEBIE_ADDITION_MESSAGE, productName));
        String input = reader.readLine();
        writer.write(NEW_LINE);
        validator.validateEmptyInput(input);
        return Choice.checkYesOrNo(input);
    }

    public Choice readRegularPricePaymentChoice(String productName, int quantity) {
        writer.write(String.format(REGULAR_PRICE_BUY_MESSAGE, productName, quantity));
        String input = reader.readLine();
        writer.write(NEW_LINE);
        validator.validateEmptyInput(input);
        return Choice.checkYesOrNo(input);
    }

    public Choice readMembershipDiscountApplicationChoice() {
        writer.write(MEMBERSHIP_DISCOUNT_CHOICE_MESSAGE);
        String input = reader.readLine();
        writer.write(NEW_LINE);
        validator.validateEmptyInput(input);
        return Choice.checkYesOrNo(input);
    }

    public Choice readAdditionalPurchaseChoice() {
        writer.write(ADDITIONAL_PURCHASE_MESSAGE);
        String input = reader.readLine();
        writer.write(NEW_LINE);
        validator.validateEmptyInput(input);
        return Choice.checkYesOrNo(input);
    }
}
