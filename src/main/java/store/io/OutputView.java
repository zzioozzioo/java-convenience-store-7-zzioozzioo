package store.io;

import java.util.List;
import store.domain.Product;
import store.domain.StoreHouse;
import store.dto.Receipt;
import store.io.parser.ProductListParser;
import store.io.parser.ReceiptParser;
import store.io.writer.Writer;

public class OutputView {

    private final Writer writer;
    private final ProductListParser productListParser;
    private final ReceiptParser receiptParser;

    public OutputView(Writer writer, ProductListParser productListParser, ReceiptParser receiptParser) {
        this.writer = writer;
        this.productListParser = productListParser;
        this.receiptParser = receiptParser;
    }

    public void printProductList(List<Product> products) {

        String productList = productListParser.parse(products);
        writer.write(productList);
    }

    public void printReceipt(Receipt receipt, StoreHouse storeHouse) {
        String result = receiptParser.parse(receipt, storeHouse);
        writer.write(result);
    }
}
