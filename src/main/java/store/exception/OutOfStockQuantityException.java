package store.exception;

public class OutOfStockQuantityException extends IllegalArgumentException {
    private static final String ERROR_MESSAGE = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    public OutOfStockQuantityException() {
        super(ERROR_MESSAGE);
    }
}
