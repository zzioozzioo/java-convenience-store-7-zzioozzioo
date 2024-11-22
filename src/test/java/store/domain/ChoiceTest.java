package store.domain;

import static store.domain.Choice.checkYesOrNo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ChoiceTest {
    @ParameterizedTest
    @ValueSource(strings = "n, y, NN, YY")
    void 해당되지_않는_입력이_들어오면_예외가_발생한다(String input) {
        Assertions.assertThatThrownBy(() -> checkYesOrNo(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

}