package com.vincent.exchange.websocket;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class OkExSubscribeRunner implements CommandLineRunner {

    @Resource
    private OkExSubscribeClient okExSubscribeClient;

    @Override
    public void run(String... args) throws Exception {
        okExSubscribeClient.connect();
    }

}
