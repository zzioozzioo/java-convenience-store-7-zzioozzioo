package store.exception;

import static store.constants.ErrorMessages.OUT_OF_STOCK_QUANTITY;

public class OutOfStockQuantityException extends IllegalArgumentException {

    public OutOfStockQuantityException() {
        super(OUT_OF_STOCK_QUANTITY);
    }
}
