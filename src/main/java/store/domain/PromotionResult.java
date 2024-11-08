package store.domain;

public class PromotionResult {

    private final String message;
    private final boolean requiresInput;
    private final int regularPriceQuantity;

    public PromotionResult(String message, boolean requiresInput, int regularPriceQuantity) {
        this.message = message;
        this.requiresInput = requiresInput;
        this.regularPriceQuantity = regularPriceQuantity;
    }

    public String getMessage() {
        return message;
    }

    public boolean getRequiresInput() {
        return requiresInput;
    }

    public int getRegularPriceQuantity() {
        return regularPriceQuantity;
    }
}
