package com.sde.ma.non.downstream.service;

import com.sde.ma.non.downstream.model.MockResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DownstreamService {

    public MockResponse process(int id, int delay, int payload) throws InterruptedException {

        Thread.sleep(Math.max(0, delay));

        String data = "x".repeat(Math.max(0, payload));

        return MockResponse.builder()
                .id(id)
                .delay(delay)
                .payload(payload)
                .timestamp(System.currentTimeMillis())
                .data(data)
                .build();
    }
}
