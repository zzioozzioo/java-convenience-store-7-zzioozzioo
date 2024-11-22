package store.io;

import store.exception.InvalidInputException;

public class InputValidator {
    public void validateEmptyInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidInputException();
        }
    }
}
