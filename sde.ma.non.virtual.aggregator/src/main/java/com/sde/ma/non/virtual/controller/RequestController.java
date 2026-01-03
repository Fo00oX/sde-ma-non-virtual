package com.sde.ma.non.virtual.controller;

import com.sde.ma.non.virtual.model.ResponseModel;
import com.sde.ma.non.virtual.service.ConcurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.sde.ma.non.virtual.Constants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RequestController {

    private final ConcurrencyService concurrencyService;

    @GetMapping(REQUEST_PATH)
    public ResponseModel request(
            @RequestParam(value = REQUEST_PARAM_DELAY, defaultValue = "50") int delay,
            @RequestParam(value = REQUEST_PARAM_FAN_OUT) int fanOut,
            @RequestParam(value = REQUEST_PARAM_PAYLOAD) int payload
    ) {
        return concurrencyService.request(delay, fanOut, payload);
    }
}
