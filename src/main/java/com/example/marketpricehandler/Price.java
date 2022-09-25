package com.example.marketpricehandler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode
public class Price {

    private static final BigDecimal MARGIN = new BigDecimal("0.001");

    @JsonIgnore
    long id;
    @Schema(type = "string", example = "EUR/USD")
    String instrumentPair;
    @Schema(type = "number", example = "1.1000")
    BigDecimal bid;
    @Schema(type = "number", example = "1.2000")
    BigDecimal ask;
    @Schema(type = "string", example = "01-06-2020 12:01:01:001")
    LocalDateTime timestamp;

    Price(long id, String instrumentPair, BigDecimal bid, BigDecimal ask, LocalDateTime timestamp) {
        this.id = id;
        this.instrumentPair = instrumentPair;
        this.bid = bid.multiply(BigDecimal.ONE.subtract(MARGIN)).setScale(4, RoundingMode.HALF_UP);
        this.ask = ask.multiply(BigDecimal.ONE.add(MARGIN)).setScale(4, RoundingMode.HALF_UP);
        this.timestamp = timestamp;
    }

    public boolean isLessRecent(Price price) {
        return timestamp.isBefore(price.timestamp);
    }

}
