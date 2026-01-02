package com.sde.ma.non.virtual.service;

import com.sde.ma.non.virtual.model.RequestModel;
import com.sde.ma.non.virtual.model.ResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcurrencyService {

    public ResponseModel request(int delay, int fanOut, int payload) {
        return ResponseModel.builder()
                .message(delay + " " + fanOut + " " + payload)
                .build();
    }
}
