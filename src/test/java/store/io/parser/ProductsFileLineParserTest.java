package store.io.parser;


import static store.domain.Promotion.NULL;
import static store.domain.Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import store.domain.Product;

class ProductsFileLineParserTest {

    @Test
    void 파일_한_줄에서_상품_객체_가져오기() {
        //given
        String line = "콜라,1000,10,탄산2+1";
        ProductsFileLineParser productsFileLineParser = new ProductsFileLineParser(line);

        //when
        Product product = productsFileLineParser.parseLine();

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
        ProductsFileLineParser productsFileLineParser = new ProductsFileLineParser(line);

        //when
        Product product = productsFileLineParser.parseLine();

        //then
        Assertions.assertThat(product.getName()).isEqualTo("비타민워터");
        Assertions.assertThat(product.getPrice()).isEqualTo(1_500L);
        Assertions.assertThat(product.getQuantity()).isEqualTo(6);
        Assertions.assertThat(product.getPromotionName()).isEqualTo(NULL);
    }
}