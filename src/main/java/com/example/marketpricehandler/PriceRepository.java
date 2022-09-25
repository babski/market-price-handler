package com.example.marketpricehandler;

import java.util.Collection;
import java.util.Optional;

public interface PriceRepository {

    Optional<Price> getByInstrumentPair(String instrumentPair);

    Collection<Price> getAll();

    void update(Price price);
}
