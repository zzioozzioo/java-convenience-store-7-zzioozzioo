package store.config;

import store.io.InputValidator;
import store.io.InputView;
import store.io.OutputView;
import store.io.parser.ProductListParser;
import store.io.parser.ReceiptParser;

public class IoConfig {

    private final InputView inputView;
    private final OutputView outputView;

    public IoConfig(Config config) {
        this.inputView = new InputView(
                config.getReader(),
                config.getWriter(),
                new InputValidator()
        );
        this.outputView = new OutputView(
                config.getWriter(),
                new ProductListParser(),
                new ReceiptParser()
        );
    }

    public InputView getInputView() {
        return inputView;
    }

    public OutputView getOutputView() {
        return outputView;
    }
}
