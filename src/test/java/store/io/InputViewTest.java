package store.io;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import store.domain.Choice;
import store.testutil.ReaderFake;

public class InputViewTest {

    @BeforeEach
    void setUpStreams() {
        ByteArrayOutputStream outputMessage = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputMessage));
    }

    @AfterEach
    void restoresStreams() {
        System.setOut(System.out);
    }


    @Test
    void 상품_파일_입력_기능_테스트() {
        //given
        ReaderFake readerFake = new ReaderFake();
        InputView inputView = new InputView(readerFake, new InputValidator());
        String fileName = "src/main/resources/products.md";

        //when & then
        Assertions.assertThatCode(() -> inputView.readProductsFileInput(fileName))
                .doesNotThrowAnyException();
    }

    @Test
    void 프로모션_파일_입력_기능_테스트() {
        //given
        ReaderFake readerFake = new ReaderFake();
        InputView inputView = new InputView(readerFake, new InputValidator());
        String fileName = "src/main/resources/promotions.md";

        //when & then
        Assertions.assertThatCode(() -> inputView.readPromotionsFileInput(fileName))
                .doesNotThrowAnyException();
    }

    // TODO: Promotion 파일 테스트도 추가하기

    @Test
    void 상품명과_수량_입력_기능_테스트() {
        //given
        ReaderFake readerFake = new ReaderFake();
        InputView inputView = new InputView(readerFake, new InputValidator());

        readerFake.setInput("[사이다-2],[감자칩-1]");

        //when & then
        Assertions.assertThatCode(inputView::readProductNameAndQuantity)
                .doesNotThrowAnyException();

    }

    @ParameterizedTest
    @CsvSource(value = {"Y, Y", "N, N"})
    void 증정품_추가_여부_입력_기능_테스트(String input, Choice result) {
        //given
        ReaderFake readerFake = new ReaderFake();
        InputView inputView = new InputView(readerFake, new InputValidator());

        String productName = "콜라";
        readerFake.setInput(input);

        //when
        Choice choice = inputView.readFreebieAdditionChoice(productName);

        //then
        Assertions.assertThat(choice).isEqualTo(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"y", "n", "YY", "NN", "!", "예"})
    void 증정품_추가_여부_입력_예외_테스트(String input) {
        //given
        ReaderFake readerFake = new ReaderFake();
        InputView inputView = new InputView(readerFake, new InputValidator());

        String productName = "콜라";
        readerFake.setInput(input);

        //when & then
        Assertions.assertThatThrownBy(() -> inputView.readFreebieAdditionChoice(productName))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"Y, Y", "N, N"})
    void 프로모션_재고_부족_시_정가_구매_여부_입력_기능_테스트(String input, Choice result) {
        //given
        ReaderFake readerFake = new ReaderFake();
        InputView inputView = new InputView(readerFake, new InputValidator());

        String productName = "콜라";
        int quantity = 10;
        readerFake.setInput(input);

        //when
        Choice choice = inputView.readFullPricePaymentChoice(productName, quantity);

        //then
        Assertions.assertThat(choice).isEqualTo(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"y", "n", "YY", "NN", "!", "예"})
    void 프로모션_재고_부족_시_정가_구매_여부_입력_예외_테스트(String input) {
        //given
        ReaderFake readerFake = new ReaderFake();
        InputView inputView = new InputView(readerFake, new InputValidator());

        String productName = "콜라";
        int quantity = 10;
        readerFake.setInput(input);

        //when & then
        Assertions.assertThatThrownBy(() -> inputView.readFullPricePaymentChoice(productName, quantity))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"Y, Y", "N, N"})
    void 멤버십_할인_적용_여부_입력_기능_테스트(String input, Choice result) {
        //given
        ReaderFake readerFake = new ReaderFake();
        InputView inputView = new InputView(readerFake, new InputValidator());

        readerFake.setInput(input);

        //when
        Choice choice = inputView.readMembershipDiscountApplicationChoice();

        //then
        Assertions.assertThat(choice).isEqualTo(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"y", "n", "YY", "NN", "!", "예"})
    void 멤버십_할인_적용_여부_입력_예외_테스트(String input) {
        //given
        ReaderFake readerFake = new ReaderFake();
        InputView inputView = new InputView(readerFake, new InputValidator());

        readerFake.setInput(input);

        //when & then
        Assertions.assertThatThrownBy(inputView::readMembershipDiscountApplicationChoice)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"Y, Y", "N, N"})
    void 추가_구메_여부_입력_기능_테스트(String input, Choice result) {
        //given
        ReaderFake readerFake = new ReaderFake();
        InputView inputView = new InputView(readerFake, new InputValidator());

        readerFake.setInput(input);

        //when
        Choice choice = inputView.readAdditionalPurchaseChoice();

        //then
        Assertions.assertThat(choice).isEqualTo(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"y", "n", "YY", "NN", "!", "예"})
    void 추가_구메_여부_입력_예외_테스트(String input) {
        //given
        ReaderFake readerFake = new ReaderFake();
        InputView inputView = new InputView(readerFake, new InputValidator());

        readerFake.setInput(input);

        //when & then
        Assertions.assertThatThrownBy(inputView::readAdditionalPurchaseChoice)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
