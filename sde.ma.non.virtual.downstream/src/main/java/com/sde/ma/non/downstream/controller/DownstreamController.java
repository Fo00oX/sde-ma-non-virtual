package com.sde.ma.non.downstream.controller;

import com.sde.ma.non.downstream.DownstreamConstants;
import com.sde.ma.non.downstream.model.MockResponse;
import com.sde.ma.non.downstream.service.DownstreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DownstreamController {

    private final DownstreamService downstreamService;

    @GetMapping(DownstreamConstants.REQUEST_PATH)
    public MockResponse request(
            @PathVariable int id,
            @RequestParam(defaultValue = "120") int delay,
            @RequestParam(defaultValue = "2048") int payload
    ) throws InterruptedException {
        return downstreamService.process(id, delay, payload);
    }
}
