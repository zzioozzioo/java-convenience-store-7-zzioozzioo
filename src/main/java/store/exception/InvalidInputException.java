package store.exception;

import static store.constants.ErrorMessages.INVALID_INPUT;

public class InvalidInputException extends IllegalArgumentException {
    public InvalidInputException() {
        super(INVALID_INPUT);
    }
}
