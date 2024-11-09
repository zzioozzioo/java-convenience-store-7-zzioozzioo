package store.io;

import static store.constants.InputMessages.ADDITIONAL_PURCHASE_MESSAGE;
import static store.constants.InputMessages.FREEBIE_ADDITION_MESSAGE;
import static store.constants.InputMessages.INPUT_PRODUCT_NAME_AND_QUANTITY;
import static store.constants.InputMessages.MEMBERSHIP_DISCOUNT_CHOICE_MESSAGE;
import static store.constants.InputMessages.REGULAR_PRICE_BUY_MESSAGE;
import static store.constants.StringConstants.NEW_LINE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.domain.Choice;
import store.domain.Product;
import store.domain.StoreHouse;
import store.dto.PromotionInfo;
import store.dto.Purchase;
import store.io.parser.InputParser;
import store.io.parser.ProductsFileLineParser;
import store.io.parser.PromotionsFileLineParser;
import store.io.reader.FileReader;
import store.io.reader.Reader;
import store.io.writer.Writer;

public class InputView {

    // TODO: 검증과 형변환이 동시에 일어나는 것에 대해 고민해 보기

    private final Reader reader;
    private final Writer writer;
    private final InputValidator validator;

    public InputView(Reader reader, Writer writer, InputValidator validator) {
        this.reader = reader;
        this.writer = writer;
        this.validator = validator;
    }

    public StoreHouse readProductsFileInput(String fileName) {
        try {
            return readProductsFile(fileName);
        } catch (IOException e) { // TODO: 어떤 exception인지 다시 체크하기
            throw new RuntimeException(e);
        }
    }

    private StoreHouse readProductsFile(String fileName) throws IOException {
        Reader reader = new FileReader(fileName);
        reader.readLine();
        String line;
        StoreHouse storeHouse = new StoreHouse();
        while ((line = reader.readLine()) != null) {
            Product product = new ProductsFileLineParser(line).parseLine();
            storeHouse.addProduct(product);
        }
        return storeHouse;
    }

    public List<PromotionInfo> readPromotionsFileInput(String fileName) {
        try {
            return readPromotionsFile(fileName);
        } catch (IOException e) { // TODO: 어떤 exception인지 다시 체크하기
            throw new RuntimeException(e);
        }
    }

    private List<PromotionInfo> readPromotionsFile(String fileName) throws IOException {
        Reader reader = new FileReader(fileName);
        reader.readLine();
        String line;
        List<PromotionInfo> promotionInfos = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            promotionInfos.add(new PromotionsFileLineParser(line).parseLine());
        }
        return promotionInfos;
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

    public Choice readFullPricePaymentChoice(String productName, int quantity) {
        while (true) {
            try {
                writer.write(String.format(REGULAR_PRICE_BUY_MESSAGE, productName, quantity));
                String input = reader.readLine();
                writer.write(NEW_LINE);
                validator.validateEmptyInput(input);
                return Choice.checkYesOrNo(input);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Choice readMembershipDiscountApplicationChoice() {
        while (true) {
            try {
                writer.write(MEMBERSHIP_DISCOUNT_CHOICE_MESSAGE);
                String input = reader.readLine();
                writer.write(NEW_LINE);
                validator.validateEmptyInput(input);
                return Choice.checkYesOrNo(input);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Choice readAdditionalPurchaseChoice() {
        while (true) {
            try {
                writer.write(ADDITIONAL_PURCHASE_MESSAGE);
                String input = reader.readLine();
                writer.write(NEW_LINE);
                validator.validateEmptyInput(input);
                return Choice.checkYesOrNo(input);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
