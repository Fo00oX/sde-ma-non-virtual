package com.sde.ma.non.virtual.model;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record ResponseModel(
        int fanOut,
        int success,
        int payloadBytes,
        long durationMs
) {
}
