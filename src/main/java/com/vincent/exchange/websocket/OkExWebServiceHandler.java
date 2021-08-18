package com.vincent.exchange.websocket;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class OkExWebServiceHandler {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * send request to subscribe the tickers channel
     *
     * @param webSocket
     * @param response
     */
    public void subscribeTickers(WebSocket webSocket, Response response) {
        webSocket.send("{\"op\":\"subscribe\",\"args\":[{\"channel\":\"tickers\",\"instId\":\"BTC-USDT\"}]}");
        webSocket.send("{\"op\":\"subscribe\",\"args\":[{\"channel\":\"tickers\",\"instId\":\"ETH-USDT\"}]}");
    }

    public void doReceiveMessage(WebSocket webSocket, String text) {
        JSON messageJson = JSONUtil.parse(text);
        Object messageData = JSONUtil.getByPath(messageJson, "$.data[0]");
        String channel = JSONUtil.getByPath(messageJson, "$.arg.channel", "").toUpperCase();
        String instrumentId = JSONUtil.getByPath(messageJson, "$.arg.instId", "").toUpperCase();
        Double timestamp = JSONUtil.getByPath(messageJson, "$.data[0].ts", Double.MIN_VALUE);
        if (messageData != null) {
            String key = String.format("%s:%s", channel, instrumentId);
            redisTemplate.opsForZSet().add(key, messageData, timestamp);
        }
    }

}
