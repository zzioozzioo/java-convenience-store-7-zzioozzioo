package store.io;

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

    static final String INPUT_PRODUCT_NAME_AND_QUANTITY = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])\n";
    static final String FREEBIE_ADDITION_MESSAGE = "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)\n";
    static final String REGULAR_PRICE_BUY_MESSAGE = "현재 $s %s개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)\n";
    static final String MEMBERSHIP_DISCOUNT_CHOICE_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)\n";
    static final String ADDITIONAL_PURCHASE_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)\n";

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
        writer.write("\n");
        validator.validateNonEmptyInput(inputProductAndQuantity);
        return new InputParser().parse(inputProductAndQuantity);
    }

    public Choice readFreebieAdditionChoice(String productName) {
        writer.write(String.format(FREEBIE_ADDITION_MESSAGE, productName));
        String input = reader.readLine();
        writer.write("\n");
        validator.validateNonEmptyInput(input);
        return Choice.checkYesOrNo(input);
    }

    public Choice readFullPricePaymentChoice(String productName, int quantity) {
        while (true) {
            try {
                writer.write(String.format(REGULAR_PRICE_BUY_MESSAGE, productName, quantity));
                String input = reader.readLine();
                writer.write("\n");
                validator.validateNonEmptyInput(input);
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
                writer.write("\n");
                validator.validateNonEmptyInput(input);
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
                writer.write("\n");
                validator.validateNonEmptyInput(input);
                return Choice.checkYesOrNo(input);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
