package store.io;


import static store.domain.Promotion.NULL;
import static store.domain.Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import store.domain.Product;

class FileLineParserTest {

    // TODO: 파일에서 잘못된 형식이 존재하는 경우도 테스트에 추가하기

    @Test
    void 파일_한_줄에서_상품_객체_가져오기() {
        //given
        String line = "콜라,1000,10,탄산2+1";
        FileLineParser fileLineParser = new FileLineParser(line);

        //when
        Product product = fileLineParser.parseLine();

        //then
        Assertions.assertThat(product.getName()).isEqualTo("콜라");
        Assertions.assertThat(product.getPrice()).isEqualTo(1_000L);
        Assertions.assertThat(product.getQuantity()).isEqualTo(10);
        Assertions.assertThat(product.getPromotionName()).isEqualTo(SPARKLING_BUY_TWO_GET_ONE_FREE);
    }

    @Test
    void 상품에서_프로모션_이름이_null인_경우() {
        //given
        String line = "비타민워터,1500,6,null";
        FileLineParser fileLineParser = new FileLineParser(line);

        //when
        Product product = fileLineParser.parseLine();

        //then
        Assertions.assertThat(product.getName()).isEqualTo("비타민워터");
        Assertions.assertThat(product.getPrice()).isEqualTo(1_500L);
        Assertions.assertThat(product.getQuantity()).isEqualTo(6);
        Assertions.assertThat(product.getPromotionName()).isEqualTo(NULL);
    }
}