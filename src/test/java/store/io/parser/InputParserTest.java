package store.io.parser;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.dto.Purchase;

class InputParserTest {

    @Test
    void 입력된_상품과_수량을_구분자로_분리() {
        //given
        InputParser parser = new InputParser();
        String input = "[사이다-2],[감자칩-1]";

        //when
        List<Purchase> purchaseList = parser.parse(input);

        //then
        Assertions.assertThat(purchaseList.size()).isEqualTo(2);

        Assertions.assertThat(purchaseList.get(0).getProductName()).isEqualTo("사이다");
        Assertions.assertThat(purchaseList.get(0).getQuantity()).isEqualTo(2);

        Assertions.assertThat(purchaseList.get(1).getProductName()).isEqualTo("감자칩");
        Assertions.assertThat(purchaseList.get(1).getQuantity()).isEqualTo(1);
    }

    @ParameterizedTest
    @CsvSource({
            "'[사이다-@],[감자칩-1]'",
            "'[사이다:2],[감자칩-1]'",
            "'[사이다-2],,[감자칩-1]'",
            "'[사이다-2],[감자칩-1'",
            "'[사이다-2,[감자칩-1]'",
            "'사이다-2,감자칩-1'",
            "'[-]'",
            "''"
    })
    void 상품과_수량의_구분자가_잘못_입력된_경우(String input) {
        //given
        InputParser parser = new InputParser();

        //when & then
        Assertions.assertThatThrownBy(() -> parser.parse(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

}