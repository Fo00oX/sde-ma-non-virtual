package com.sde.ma.non.virtual.downstream.controller;

import com.sde.ma.non.virtual.downstream.DownstreamConstants;
import com.sde.ma.non.virtual.downstream.model.ResponseModel;
import com.sde.ma.non.virtual.downstream.service.DownstreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DownstreamController {

    private final DownstreamService service;

    @GetMapping(DownstreamConstants.REQUEST_PATH)
    public ResponseModel request(
            @PathVariable int id,
            @RequestParam(defaultValue = "120") int delay,
            @RequestParam(defaultValue = "2048") int payload
    ) throws InterruptedException {
        return service.process(id, delay, payload);
    }
}
