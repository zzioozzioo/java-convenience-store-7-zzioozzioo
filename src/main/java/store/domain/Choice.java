package store.domain;

import static store.constants.ErrorMessages.YES_OR_NO;

public enum Choice {
    Y("Y"),
    N("N");

    private final String value;

    Choice(String value) {
        this.value = value;
    }

    public static Choice checkYesOrNo(String input) {
        for (Choice choice : Choice.values()) {
            if (choice.getValue().equals(input)) {
                return choice;
            }
        }
        throw new IllegalArgumentException(YES_OR_NO);
    }

    public String getValue() {
        return value;
    }
}
