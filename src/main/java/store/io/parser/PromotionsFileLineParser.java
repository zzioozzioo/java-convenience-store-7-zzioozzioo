package store.io.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import store.domain.Promotion;
import store.dto.PromotionInfo;

public class PromotionsFileLineParser {

    private final String line;

    public PromotionsFileLineParser(String line) {
        this.line = line;
    }

    // TODO: PromotionInfo 값 세팅을 여기서 해도 될지 고민해 보기 (밖으로 내보내서 해야 하나?)
    public PromotionInfo parseLine() {
        List<String> parts = Arrays.asList(line.split(","));
        Promotion promotionName = getPromotion(parts);
        int buyQuantity = getValidQuantity(parts.get(1));
        int getQuantity = getValidQuantity(parts.get(2));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime startDateTime = getStartLocalDateTime(parts, formatter);
        LocalDateTime endDateTime = getEndLocalDateTime(parts, formatter);

        return PromotionInfo.of(promotionName, buyQuantity, getQuantity, startDateTime, endDateTime);
    }

    private static Promotion getPromotion(List<String> parts) {
        Promotion promotionName = Promotion.NULL;
        for (Promotion promotion : Promotion.values()) {
            if (promotion.getPromotionName().equals(parts.get(0))) {
                promotionName = promotion;
            }
        }
        return promotionName;
    }

    private int getValidQuantity(String inputQuantity) {
        int quantity;
        try {
            quantity = Integer.parseInt(inputQuantity);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("숫자가 아닙니다.");
        }
        return quantity;
    }

    private static LocalDateTime getStartLocalDateTime(List<String> parts, DateTimeFormatter formatter) {
        LocalDate startDate = LocalDate.parse(parts.get(3), formatter);
        return startDate.atStartOfDay();
    }

    private static LocalDateTime getEndLocalDateTime(List<String> parts, DateTimeFormatter formatter) {
        LocalDate endDate = LocalDate.parse(parts.get(4), formatter);
        return endDate.atTime(23, 59, 59);
    }


}
