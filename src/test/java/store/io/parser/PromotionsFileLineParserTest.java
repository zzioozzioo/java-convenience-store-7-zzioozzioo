package store.io.parser;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import store.domain.Promotion;
import store.dto.PromotionInfo;

class PromotionsFileLineParserTest {

    @Test
    void 파일_한_줄에서_프로모션_정보_객체_가져오기() {
        //given
        String line = "탄산2+1,2,1,2024-01-01,2024-12-31";
        PromotionsFileLineParser parser = new PromotionsFileLineParser(line);

        //when
        PromotionInfo promotionInfo = parser.parseLine();

        //then
        Assertions.assertThat(promotionInfo.getPromotionName()).isEqualTo(Promotion.SPARKLING_BUY_TWO_GET_ONE_FREE);
        Assertions.assertThat(promotionInfo.getBuyQuantity()).isEqualTo(2);
        Assertions.assertThat(promotionInfo.getGetQuantity()).isEqualTo(1);
        Assertions.assertThat(promotionInfo.getStartDateTime()).isEqualTo("2024-01-01T00:00:00");
        Assertions.assertThat(promotionInfo.getEndDateTime()).isEqualTo("2024-12-31T23:59:59");
    }

}