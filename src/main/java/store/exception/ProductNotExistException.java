package store.exception;

public class ProductNotExistException extends IllegalArgumentException {

    private final static String ERROR_MESSAGE = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";

    public ProductNotExistException() {
        super(ERROR_MESSAGE);
    }
}
