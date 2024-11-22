package store.exception;

import static store.constants.ErrorMessages.INVALID_INPUT_FORMAT;

public class InvalidInputFormatException extends IllegalArgumentException {
    public InvalidInputFormatException() {
        super(INVALID_INPUT_FORMAT);
    }
}
