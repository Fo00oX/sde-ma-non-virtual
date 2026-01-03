package com.sde.ma.non.virtual.downstream.service;

import com.sde.ma.non.virtual.downstream.model.ResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DownstreamService {

    public ResponseModel process(int id, int delay, int payload) throws InterruptedException {

        Thread.sleep(Math.max(0, delay));

        String data = "x".repeat(Math.max(0, payload));

        return ResponseModel.builder()
                .id(id)
                .delay(delay)
                .payload(payload)
                .timestamp(System.currentTimeMillis())
                .data(data)
                .build();
    }
}
