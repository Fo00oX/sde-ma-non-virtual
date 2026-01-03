package com.sde.ma.non.virtual.downstream.model;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record ResponseModel(
        int id,
        int delay,
        int payload,
        long timestamp,
        String data
) {
}
