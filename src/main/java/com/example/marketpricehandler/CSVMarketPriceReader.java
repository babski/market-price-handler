package com.example.marketpricehandler;

import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class CSVMarketPriceReader {

    private static final Pattern DECIMAL_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");

    DateTimeFormatter formatter;

    CSVMarketPriceReader() {
        this.formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS");
    }

    @SneakyThrows
    public Stream<Price> readPriceFeed(String path) {
        if (!path.endsWith(".csv")) {
            throw new IllegalArgumentException(String.format("Provided file '%s' is not a CSV file.", path));
        }
        CSVReader reader = new CSVReaderBuilder(new FileReader(path)).build();
        return reader.readAll().stream()
                .peek(this::validateMessage)
                .map(values -> new Price(
                        Long.parseLong(values[0]),
                        values[1].trim(),
                        new BigDecimal(values[2].trim()),
                        new BigDecimal(values[3].trim()),
                        LocalDateTime.parse(values[4], formatter))
                );
    }

    private void validateMessage(String[] values) {
        if (values.length != 5) {
            throw new IllegalArgumentException(String.format("Incorrect number of fields in CSV row: %d.", values.length));
        }
        if (Arrays.stream(values).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Any field cannot be null.");
        }
        if (!StringUtils.isNumeric(values[0].trim())) {
            throw new IllegalArgumentException(String.format("Id field '%s' is not na number.", values[0]));
        }
        if (!DECIMAL_PATTERN.matcher(values[2].trim()).matches()) {
            throw new IllegalArgumentException(String.format("Bid field '%s' is not na number.", values[2]));
        }
        if (!DECIMAL_PATTERN.matcher(values[3].trim()).matches()) {
            throw new IllegalArgumentException(String.format("Ask field '%s' is not na number.", values[3]));
        }
    }

}