package store.io;

import java.io.IOException;
import store.domain.Product;
import store.domain.StoreHouse;
import store.io.file.FileReader;
import store.io.parser.FileLineParser;
import store.io.parser.InputParser;

public class InputView {

    private final MissionUtilsReader reader;
    private final InputValidator validator;

    public InputView(MissionUtilsReader reader, InputValidator validator) {
        this.reader = reader;
        this.validator = validator;
    }

    public void readFileInput() {
        try {
            read();
        } catch (IOException e) { // TODO: 어떤 exception인지 다시 체크하기
            throw new RuntimeException(e);
        }
    }

    private void read() throws IOException {
        while (true) {
            String line = new FileReader().getReader().readLine();
            if (line == null) {
                break;
            }
            Product product = new FileLineParser(line).parseLine();
            new StoreHouse().addProduct(product);
        }
    }

    public void readProductNameAndQuantity() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String inputProductAndQuantity = reader.readLine();
        validator.validateInput(inputProductAndQuantity); // 값 존재 여부 판별
        new InputParser().parse(inputProductAndQuantity);
    }


}
