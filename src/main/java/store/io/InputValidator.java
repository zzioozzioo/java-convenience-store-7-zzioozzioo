package store.io;

public class InputValidator {
    public void validateNonEmptyInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("값을 입력해야 합니다.");
        }
    }
}
