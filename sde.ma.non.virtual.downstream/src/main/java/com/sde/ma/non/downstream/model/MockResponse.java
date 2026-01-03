package com.sde.ma.non.downstream.model;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record MockResponse(
        int id,
        int delay,
        int payload,
        long timestamp,
        String data
) {
}
