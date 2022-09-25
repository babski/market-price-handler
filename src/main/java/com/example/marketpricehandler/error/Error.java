package com.example.marketpricehandler.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
public class Error {

    @Schema(type = "integer", example = "404")
    int status;

    @Schema(type = "string", example = "NOT FOUND")
    String message;

    @Schema(type = "string", example = "Instrument pair EUR/PLN not found.")
    String details;
}
