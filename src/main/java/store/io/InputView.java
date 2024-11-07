package store.io;

import java.io.IOException;
import java.util.List;
import store.domain.Choice;
import store.domain.Product;
import store.domain.StoreHouse;
import store.dto.Purchase;
import store.io.parser.InputParser;
import store.io.parser.ProductsFileLineParser;
import store.io.parser.PromotionsFileLineParser;
import store.io.reader.FileReader;
import store.io.reader.Reader;

public class InputView {

    // TODO: 검증과 형변환이 동시에 일어나는 것에 대해 고민해 보기

    private final Reader reader;
    private final InputValidator validator;

    public InputView(Reader reader, InputValidator validator) {
        this.reader = reader;
        this.validator = validator;
    }

    public void readProductsFileInput(String fileName) {
        try {
            readProductsFile(fileName);
        } catch (IOException e) { // TODO: 어떤 exception인지 다시 체크하기
            throw new RuntimeException(e);
        }
    }

    private void readProductsFile(String fileName) throws IOException {
        Reader reader = new FileReader(fileName);
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            Product product = new ProductsFileLineParser(line).parseLine();
            new StoreHouse().addProduct(product);
        }
    }

    public void readPromotionsFileInput(String fileName) {
        try {
            readPromotionsFile(fileName);
        } catch (IOException e) { // TODO: 어떤 exception인지 다시 체크하기
            throw new RuntimeException(e);
        }
    }

    private void readPromotionsFile(String fileName) throws IOException {
        Reader reader = new FileReader(fileName);
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            new PromotionsFileLineParser(line).parseLine();
        }
    }

    public List<Purchase> readProductNameAndQuantity() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String inputProductAndQuantity = reader.readLine();
        validator.validateNonEmptyInput(inputProductAndQuantity);
        return new InputParser().parse(inputProductAndQuantity);
    }

    public Choice readFreebieAdditionChoice(String productName) {
        System.out.println("현재 " + productName + "은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
        String input = reader.readLine();
        validator.validateNonEmptyInput(input);
        return Choice.checkYesOrNo(input);
    }

    public Choice readFullPricePaymentChoice(String productName, int quantity) {
        System.out.println("현재 " + productName + " " + quantity + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
        String input = reader.readLine();
        validator.validateNonEmptyInput(input);
        return Choice.checkYesOrNo(input);
    }

    public Choice readMembershipDiscountApplicationChoice() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        String input = reader.readLine();
        validator.validateNonEmptyInput(input);
        return Choice.checkYesOrNo(input);
    }

    public Choice readAdditionalPurchaseChoice() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        String input = reader.readLine();
        validator.validateNonEmptyInput(input);
        return Choice.checkYesOrNo(input);
    }


}
