package store.io.parser;

import static store.constants.NumberConstants.BUY_QUANTITY_INDEX;
import static store.constants.NumberConstants.END_LOCAL_DATE_TIME_HOUR;
import static store.constants.NumberConstants.END_LOCAL_DATE_TIME_INDEX;
import static store.constants.NumberConstants.END_LOCAL_DATE_TIME_MINUTE;
import static store.constants.NumberConstants.END_LOCAL_DATE_TIME_SECOND;
import static store.constants.NumberConstants.GET_QUANTITY_INDEX;
import static store.constants.NumberConstants.START_LOCAL_DATE_TIME_INDEX;
import static store.constants.StringConstants.COMMA;
import static store.constants.StringConstants.DATE_TIME_FORMATTER_PATTERN;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import store.domain.Promotion;
import store.dto.PromotionInfo;
import store.exception.InvalidInputFormatException;

public class PromotionsFileLineParser {

    private final String line;

    public PromotionsFileLineParser(String line) {
        this.line = line;
    }

    public PromotionInfo parseLine() {
        List<String> parts = Arrays.asList(line.split(COMMA));
        Promotion promotionName = getPromotion(parts);
        int buyQuantity = getValidQuantity(parts.get(BUY_QUANTITY_INDEX));
        int getQuantity = getValidQuantity(parts.get(GET_QUANTITY_INDEX));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_PATTERN);
        LocalDateTime startDateTime = getStartLocalDateTime(parts, formatter);
        LocalDateTime endDateTime = getEndLocalDateTime(parts, formatter);

        return PromotionInfo.of(promotionName, buyQuantity, getQuantity, startDateTime, endDateTime);
    }

    private static Promotion getPromotion(List<String> parts) {
        Promotion promotionName = Promotion.NULL;
        for (Promotion promotion : Promotion.values()) {
            if (promotion.getPromotionName().equals(parts.getFirst())) {
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
            throw new InvalidInputFormatException();
        }
        return quantity;
    }

    private static LocalDateTime getStartLocalDateTime(List<String> parts, DateTimeFormatter formatter) {
        LocalDate startDate = LocalDate.parse(parts.get(START_LOCAL_DATE_TIME_INDEX), formatter);
        return startDate.atStartOfDay();
    }

    private static LocalDateTime getEndLocalDateTime(List<String> parts, DateTimeFormatter formatter) {
        LocalDate endDate = LocalDate.parse(parts.get(END_LOCAL_DATE_TIME_INDEX), formatter);
        return endDate.atTime(END_LOCAL_DATE_TIME_HOUR, END_LOCAL_DATE_TIME_MINUTE, END_LOCAL_DATE_TIME_SECOND);
    }


}
