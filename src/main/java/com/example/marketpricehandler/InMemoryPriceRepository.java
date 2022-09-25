package com.example.marketpricehandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class InMemoryPriceRepository implements PriceRepository {

    Map<String, Price> priceMap = new HashMap<>();

    @Override
    public Optional<Price> getByInstrumentPair(String instrumentPair) {
        return Optional.ofNullable(priceMap.get(instrumentPair));
    }

    @Override
    public Collection<Price> getAll() {
        return priceMap.values();
    }

    @Override
    public void update(Price price) {
        priceMap.put(price.getInstrumentPair(), price);
    }
}
