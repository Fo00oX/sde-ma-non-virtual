package com.sde.ma.non.virtual.model;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record RequestModel(
        int delay, // per-call delay in ms
        int fanOut, // number of parallel downstream calls per request
        int payloadBytes // JSON size
) {
}
