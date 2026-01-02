package com.sde.ma.non.virtual;

import lombok.RequiredArgsConstructor;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class Constants {

    public static final String REQUEST_PATH = "/request";

    public static final String REQUEST_PARAM_DELAY = "delay";
    public static final String REQUEST_PARAM_FAN_OUT = "fanOut";
    public static final String REQUEST_PARAM_PAYLOAD = "payload";
}
