package com.vincent.exchange.websocket;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class OkExWebServiceHandler {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * send request to subscribe the tickers channel
     *
     * @param webSocket
     * @param response
     */
    public void subscribeTickers(WebSocket webSocket, Response response) {
        webSocket.send("{\"op\":\"subscribe\",\"args\":[{\"channel\":\"tickers\",\"instId\":\"BTC-USDT\"}]}");
        webSocket.send("{\"op\":\"subscribe\",\"args\":[{\"channel\":\"tickers\",\"instId\":\"ETH-USDT\"}]}");
        webSocket.send("{\"op\":\"subscribe\",\"args\":[{\"channel\":\"candle1m\",\"instId\":\"BTC-USDT\"}]}");
        webSocket.send("{\"op\":\"subscribe\",\"args\":[{\"channel\":\"candle1m\",\"instId\":\"ETH-USDT\"}]}");
        webSocket.send("{\"op\":\"subscribe\",\"args\":[{\"channel\":\"trades\",\"instId\":\"BTC-USDT\"}]}");
        webSocket.send("{\"op\":\"subscribe\",\"args\":[{\"channel\":\"trades\",\"instId\":\"ETH-USDT\"}]}");
    }

    /**
     * send the received message to redis stream channel
     *
     * @param webSocket
     * @param text
     */
    public void doReceiveMessage(WebSocket webSocket, String text) {
        JSON messageJson = JSONUtil.parse(text);
        Object messageData = JSONUtil.getByPath(messageJson, "$.data[0]");
        String channel = JSONUtil.getByPath(messageJson, "$.arg.channel", "").toUpperCase();
        String instrumentId = JSONUtil.getByPath(messageJson, "$.arg.instId", "").toUpperCase();
        if (messageData != null) {
            String key = String.format("OKEX-SUBSCRIBE-%s-%s", channel, instrumentId);
            ObjectRecord<String, Object> objectRecord = StreamRecords.newRecord().ofObject(messageData).withStreamKey(key);
            stringRedisTemplate.opsForStream().add(objectRecord);
        }
    }

}
