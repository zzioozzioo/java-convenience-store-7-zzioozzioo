package store.domain;

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
        throw new IllegalArgumentException("Y와 N 중에 입력해야 합니다.");
    }
    
    public String getValue() {
        return value;
    }
}
