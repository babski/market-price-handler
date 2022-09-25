package com.example.marketpricehandler

import spock.lang.Specification
import spock.lang.Subject

import java.util.stream.Collectors

class FeedProcessorTest extends Specification {

    def repository = new InMemoryPriceRepository()
    def priceReader = new CSVMarketPriceReader()

    @Subject
    FeedProcessor feedProcessor = new FeedProcessor(repository, priceReader)

    def 'Data from CSV file are correctly processed and only the latest quotations can be fetched from repository'() {
        given: 'Source path to CSV file with 5 non-unique instrument pairs'
            def source = 'src/test/resources/testfeed.csv'

        when: 'CSV file is processed'
            feedProcessor.update(null, source)

        then: 'Only 3 unique instrument pairs with latest quotations are available in repository'
            def prices = repository.getAll()
            prices.size() == 3
            prices.stream().map(price -> price.getInstrumentPair())
                    .collect(Collectors.toSet()).containsAll(['EUR/USD', 'EUR/JPY', 'GBP/USD'])
            repository.getByInstrumentPair('EUR/USD').get().getId() == 106
            repository.getByInstrumentPair('EUR/JPY').get().getId() == 110
            repository.getByInstrumentPair('GBP/USD').get().getId() == 109
    }

    def 'Incorrect data values in CSV line triggers validation failure'(String source) {

        when:
            feedProcessor.update(null, source)

        then:
            def e = thrown(IllegalArgumentException)
            e.message == details

        where:
            source                            | details
            'src/test/resources/testfeed.wtf' | "Provided file 'src/test/resources/testfeed.wtf' is not a CSV file."
            'src/test/resources/v00.csv'      | "Incorrect number of fields in CSV row: 6."
            'src/test/resources/v01.csv'      | "Id field '106a' is not na number."
            'src/test/resources/v03.csv'      | "Bid field ' 1.1000a' is not na number."
            'src/test/resources/v04.csv'      | "Ask field '1.2000a' is not na number."
    }
}