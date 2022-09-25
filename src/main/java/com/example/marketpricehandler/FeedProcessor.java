package com.example.marketpricehandler;

import java.util.Observable;
import java.util.Observer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
class FeedProcessor implements Observer {

    PriceRepository priceRepository;
    CSVMarketPriceReader marketPriceReader;

    @Override
    public void update(Observable o, Object arg) {
        processPriceFeed(arg.toString());
    }

    private void processPriceFeed(String source) {
        marketPriceReader.readPriceFeed(source)
                .forEach(price -> priceRepository.getByInstrumentPair(price.getInstrumentPair())
                        .ifPresentOrElse(existing -> updateIfMoreRecent(existing, price),
                                () -> priceRepository.update(price)));
        log.info("{} has been successfully processed.", source);
    }

    private void updateIfMoreRecent(Price existing, Price fromFeed) {
        if (existing.isLessRecent(fromFeed)) {
            priceRepository.update(fromFeed);
        }
    }
}
