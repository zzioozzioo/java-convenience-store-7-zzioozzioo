package store.exception;

import static store.constants.ErrorMessages.NOT_EXIST_PRODUCT;

public class ProductNotExistException extends IllegalArgumentException {

    public ProductNotExistException() {
        super(NOT_EXIST_PRODUCT);
    }
}
