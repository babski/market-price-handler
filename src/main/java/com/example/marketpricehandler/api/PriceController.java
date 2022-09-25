package com.example.marketpricehandler.api;

import java.util.Collection;
import com.example.marketpricehandler.Price;
import com.example.marketpricehandler.PriceRepository;
import com.example.marketpricehandler.error.InstrumentPairNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
class PriceController implements PriceApi {

    PriceRepository priceRepository;

    @Override
    public Price getPrice(String instrumentPair) {
        String pair = instrumentPair.replaceAll("-", "/");
        return priceRepository.getByInstrumentPair(pair)
                .orElseThrow(() -> new InstrumentPairNotFoundException(pair));
    }

    @Override
    public Collection<Price> getPrices() {
        return priceRepository.getAll();
    }
}
