package store.io;

import java.io.IOException;
import store.domain.Product;
import store.domain.StoreHouse;

public class InputView {
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
}
