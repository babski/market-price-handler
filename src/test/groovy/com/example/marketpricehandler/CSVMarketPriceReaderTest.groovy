package com.example.marketpricehandler

import spock.lang.Specification
import spock.lang.Subject

import java.math.RoundingMode
import java.time.LocalDateTime
import java.util.stream.Collectors

import static com.example.marketpricehandler.Price.MARGIN

class CSVMarketPriceReaderTest extends Specification {

    @Subject
    CSVMarketPriceReader csvMarketPriceReader = new CSVMarketPriceReader()

    def 'Prices with details are successfully read from CSV file'() {
        given: 'Source path to CSV file with 5 non-unique instrument pairs'
            def path = 'src/test/resources/testfeed.csv'

        when: 'Read data from a correct CSV file'
            def priceFeed = csvMarketPriceReader.readPriceFeed(path)

        then: 'Data from CSV file is transformed into price objects with margin adjusted quotations'
            def priceFeedList = priceFeed.collect(Collectors.toList())
            priceFeedList.size() == 5

            def price0 = priceFeedList.get(0)
            price0.getId() == 106
            price0.getInstrumentPair() == 'EUR/USD'
            price0.getBid() == withDiscount(new BigDecimal('1.1000'))
            price0.getAsk() == withPremium(new BigDecimal('1.2000'))
            price0.getTimestamp() == LocalDateTime.of(2020, 6, 1, 12, 1, 1, 1000000)

            def price1 = priceFeedList.get(1)
            price1.getId() == 107
            price1.getInstrumentPair() == 'EUR/JPY'
            price1.getBid() == withDiscount(new BigDecimal('119.60'))
            price1.getAsk() == withPremium(new BigDecimal('119.90'))
            price1.getTimestamp() == LocalDateTime.of(2020, 6, 1, 12, 1, 2, 2000000)

            def price2 = priceFeedList.get(2)
            price2.getId() == 108
            price2.getInstrumentPair() == 'GBP/USD'
            price2.getBid() == withDiscount(new BigDecimal('1.2500'))
            price2.getAsk() == withPremium(new BigDecimal('1.2560'))
            price2.getTimestamp() == LocalDateTime.of(2020, 6, 1, 12, 1, 2, 2000000)

            def price3 = priceFeedList.get(3)
            price3.getId() == 109
            price3.getInstrumentPair() == 'GBP/USD'
            price3.getBid() == withDiscount(new BigDecimal('1.2499'))
            price3.getAsk() == withPremium(new BigDecimal('1.2561'))
            price3.getTimestamp() == LocalDateTime.of(2020, 6, 1, 12, 1, 2, 100000000)

            def price4 = priceFeedList.get(4)
            price4.getId() == 110
            price4.getInstrumentPair() == 'EUR/JPY'
            price4.getBid() == withDiscount(new BigDecimal('119.61'))
            price4.getAsk() == withPremium(new BigDecimal('119.91'))
            price4.getTimestamp() == LocalDateTime.of(2020, 6, 1, 12, 1, 2, 110000000)
    }

    private BigDecimal withDiscount(BigDecimal bid) {
        bid.multiply(BigDecimal.ONE.subtract(MARGIN)).setScale(4, RoundingMode.HALF_UP)
    }

    private BigDecimal withPremium(BigDecimal ask) {
        ask.multiply(BigDecimal.ONE.add(MARGIN)).setScale(4, RoundingMode.HALF_UP)
    }
}
