package com.example.marketpricehandler;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static com.example.marketpricehandler.MarketPriceHandlerApplication.PRICE_FEED_SOURCE;
import static com.example.marketpricehandler.PriceFeedBootstrap.createPriceFeedFile;

@SpringBootApplication
public class MarketPriceHandlerApplication {

    static String PRICE_FEED_SOURCE;

    @SneakyThrows
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(MarketPriceHandlerApplication.class, args);
        PriceRepository priceRepository = run.getBean(PriceRepository.class);
        FeedProcessor feedProcessor = new FeedProcessor(priceRepository, new CSVMarketPriceReader());
        MarketPriceListener marketPriceListener = new MarketPriceListener(PRICE_FEED_SOURCE);
        marketPriceListener.addObserver(feedProcessor);
        createPriceFeedFile();
        marketPriceListener.listen();
    }

    @Value("${price-feed-source}")
    void setPriceFeedSource(String name) {
        PRICE_FEED_SOURCE = name;
    }

}

class PriceFeedBootstrap {
    public static void createPriceFeedFile() throws IOException {
        List<String[]> priceFeedLines = List.of(
                new String[]{"106", "EUR/USD", " 1.1000", "1.2000", "01-06-2020 12:01:01:001"},
                new String[]{"107", " EUR/JPY", " 119.60", "119.90", "01-06-2020 12:01:02:002"},
                new String[]{"108", " GBP/USD", " 1.2500", "1.2560", "01-06-2020 12:01:02:002"},
                new String[]{"109", " GBP/USD", " 1.2499", "1.2561", "01-06-2020 12:01:02:100"},
                new String[]{"110", " EUR/JPY", " 119.61", "119.91", "01-06-2020 12:01:02:110"});

        File csvOutputFile = new File(PRICE_FEED_SOURCE + "price_feed_01.csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            priceFeedLines.stream()
                    .map(line -> String.join(",", line))
                    .forEach(pw::println);
        }
    }
}
