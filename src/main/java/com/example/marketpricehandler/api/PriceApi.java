package com.example.marketpricehandler.api;

import java.util.Collection;
import com.example.marketpricehandler.Price;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/prices")
@OpenAPIDefinition(info = @Info(title = "Price", description = "Price API", version = "1.0",
        contact = @Contact(name = "Marcel Babski", email = "dindane@o2.pl")))
interface PriceApi {

    @Operation(summary = "Get price by instrument pair",
            description = "Get price by instrument pair when provided exists.",
            parameters = {@Parameter(in = ParameterIn.PATH, name = "instrumentPair", example = "EUR-USD",
                    description = "Currency pairs divided by hyphen '-'")})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK - prices successfully fetched",
            content = @Content(schema = @Schema(implementation = Price.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.")})
    @GetMapping(value = "/{instrumentPair}", produces = MediaType.APPLICATION_JSON_VALUE)
    Price getPrice(@PathVariable String instrumentPair);

    @Operation(summary = "Get list of prices",
            description = "Get list of prices with latest quotations")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK - list of prices successfully fetched",
            content = @Content(schema = @Schema(implementation = Price.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    Collection<Price> getPrices();
}
