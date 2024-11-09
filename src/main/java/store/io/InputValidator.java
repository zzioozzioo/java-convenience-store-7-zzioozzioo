package store.io;

import static store.constants.ErrorMessages.EMPTY_INPUT;

public class InputValidator {
    public void validateEmptyInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(EMPTY_INPUT);
        }
    }
}
